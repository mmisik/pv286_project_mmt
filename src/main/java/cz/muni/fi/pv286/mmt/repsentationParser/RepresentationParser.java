package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.model.Options;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class RepresentationParser {

    protected final Options options;

    public RepresentationParser(Options options) {
        this.options = options;
    }

    // TODO: make this streamable / allocate in blocks to avoid allocating a big chunk of memory at once
    protected byte[] getInput() throws IOException {
        var input = options.getInputFile();
        var reader = new ByteArrayOutputStream();
        int readByte;

        do {
            readByte = input.read();
            reader.write(readByte);

            final int maxFileSize = 10 ^ 9; // 1 GB
            if (reader.size() > maxFileSize) {
                throw new IOException("Input is too big.");
            }
        } while (readByte != -1); // EOF

        return reader.toByteArray();
    }

    public abstract void parseTo(byte[] bytes) throws IOException;

    public abstract byte[] parseFrom() throws IOException;
}
