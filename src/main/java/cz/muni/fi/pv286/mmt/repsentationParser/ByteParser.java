package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.model.Options;

import java.io.*;

public class ByteParser extends RepresentationParser {

    public ByteParser(Options options) {
        super(options);
    }

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
        }
    }

    public byte[] parseFrom() throws IOException {
        // no additional processing is necessary
        return getInput();
    }
}
