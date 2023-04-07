package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.model.IoFormat;
import cz.muni.fi.pv286.mmt.model.Options;
import cz.muni.fi.pv286.mmt.model.ResultTree;

import javax.xml.transform.Result;
import java.nio.ByteBuffer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Abstract class for representation parsers.
 * Provides common functionality of reading and writing input and output in parsers.
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
                representations.add(new ResultTree(outputStream.toByteArray()));
                outputStream.reset();
            }
        }

        return representations;
    }

    protected boolean foundDelimiter(ByteArrayOutputStream stream) {
        if (!options.wasDelimiterSet()) {
            return false;
        }

        ByteBuffer wrappedBuffer = ByteBuffer.wrap(stream.toByteArray());
        byte[] delimiterBytes = options.getDelimiter().getBytes();

        byte[] lastBytes = Stream
                .generate(wrappedBuffer::get)
                .limit(delimiterBytes.length)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        list -> {
                            byte[] result = new byte[list.size()];
                            for (int i = 0; i < list.size(); i++) {
                                result[i] = list.get(i);
                            }
                            return result;
                        }
                ));

        return Arrays.equals(delimiterBytes, lastBytes);
    }

    protected abstract byte[] parseTo(byte[] bytes) throws IOException;

    protected abstract byte[] parseFrom(byte[] bytes) throws IOException;

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
                options.getOutputFile().write(options.getDelimiter().getBytes());
            }
        }
    }
}
