package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.model.Options;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public abstract class RepresentationParser {

    protected final Options options;

    public RepresentationParser(Options options) {
        this.options = options;
    }

    // TODO: make this streamable / allocate in blocks to avoid allocating a big chunk of memory at once
    protected byte[] getInput() throws IOException {
        InputStream inputStream = options.getInputFile();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int readByte;

        final int maxFileSize = 1073741824; // 1 GB

        while (true) {
            readByte = inputStream.read();

            if (readByte == -1) { // break on EOF
                break;
            }

            if (outputStream.size() > maxFileSize) {
                throw new IOException("Input is too big.");
            }

            outputStream.write(readByte);
        }

        return outputStream.toByteArray();
    }

    public abstract void parseTo(byte[] bytes) throws IOException;

    public abstract byte[] parseFrom() throws IOException;
}
