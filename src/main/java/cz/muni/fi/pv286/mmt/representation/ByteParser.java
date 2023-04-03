package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.model.Options;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Byte parser.
 */
public class ByteParser extends RepresentationParser {

    public ByteParser(Options options) {
        super(options);
    }

    @Override
    public void parseTo(byte[] bytes) throws IOException {
        final OutputStream output = options.getOutputFile();
        final InputStream input = new ByteArrayInputStream(bytes);
        int readByte;

        while (true) {
            readByte = input.read();

            if (readByte == -1) {
                break;
            }

            output.write(readByte);
            output.flush();
        }
    }

    @Override
    public byte[] parseFrom() throws IOException {
        // no additional processing is necessary
        return getInput();
    }
}
