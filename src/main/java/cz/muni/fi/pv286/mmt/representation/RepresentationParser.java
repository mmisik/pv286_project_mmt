package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.model.Options;
import cz.muni.fi.pv286.mmt.model.ResultTree;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class for representation parsers.
 */
public abstract class RepresentationParser {

    protected final Options options;

    public RepresentationParser(Options options) {
        this.options = options;
    }

    // TODO: make this streamable
    //  or allocate in blocks to avoid allocating a big chunk of memory at once
    protected List<ResultTree> getInput() throws IOException {
        InputStream inputStream = options.getInputFile();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int readByte;

        final int maxFileSize = 1073741824; // 1 GB

        List<ResultTree> representations = new ArrayList<>();

        while (true) {
            readByte = inputStream.read();

            if (readByte == -1) { // break on EOF
                representations.add(new ResultTree(outputStream.toByteArray()));
                outputStream.reset();
                break;
            }

            if (outputStream.size() > maxFileSize) {
                throw new IOException("Input is too big.");
            }

            outputStream.write(readByte);

            if (foundDelimiter(outputStream)) {
                var bytes = stripDelimiter(outputStream.toByteArray());
                representations.add(new ResultTree(bytes));
                outputStream.reset();
            }
        }

        return representations;
    }

    protected boolean foundDelimiter(ByteArrayOutputStream stream) {

        if (!options.wasDelimiterSet()) {
            return false;
        }

        byte[] bytes = stream.toByteArray();
        byte[] delimiterBytes = options.getDelimiter().getBytes(StandardCharsets.UTF_8);

        int delimiterCounter = 0;
        for (int i = bytes.length - delimiterBytes.length; i < bytes.length; i++) {
            if (bytes[i] != delimiterBytes[delimiterCounter++]) {
                return false;
            }
        }

        return true;
    }

    protected byte[] stripDelimiter(byte[] bytes) {
        byte[] delimiterBytes = options.getDelimiter().getBytes(StandardCharsets.UTF_8);

        int len = bytes.length - delimiterBytes.length;
        byte[] strippedBytes = new byte[len];

        System.arraycopy(bytes, 0, strippedBytes, 0, len);

        return strippedBytes;
    }

    protected abstract byte[] parseTo(byte[] bytes) throws IOException;

    protected abstract byte[] parseFrom(byte[] bytes) throws IOException;

    /**
     * Provides common functionality of reading and writing input and output in parsers.
     *
     * @param parseTo The parser used for conversion.
     * @throws IOException If an error occurs while reading or writing data.
     */
    public void parse(RepresentationParser parseTo) throws IOException {
        var results = getInput();

        for (var result : results) {
            byte[] representation = result.getValue();
            byte[] universal = this.parseFrom(representation);
            byte[] parsed = parseTo.parseTo(universal);

            options.getOutputFile().write(parsed);

            // write delimiter to output only if:
            // - the delimiter was set
            // - and there is more than one element in the list
            // - and the current element is not the last
            if (options.wasDelimiterSet() && results.size() > 1 && result != results.get(results.size() - 1)) {
                options.getOutputFile().write(options.getDelimiter().getBytes(StandardCharsets.UTF_8));
            }
        }
    }
}
