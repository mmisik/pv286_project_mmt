package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.exceptions.InvalidHexCharacterException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidHexCountException;
import cz.muni.fi.pv286.mmt.model.Options;

import java.io.*;

public class HexParser extends RepresentationParser {

    public HexParser(Options options) {
        super(options);
    }

    private boolean isHex(char c) {
        return Character.digit(c, 16) != -1;
    }

    private byte getByteFromHex(char a, char b) {
        int first = Character.digit(a, 16);
        int second = Character.digit(b, 16);

        return (byte) ((first << 4) + second);
    }

    private String getHexFromByte(byte b) {
        return String.format("%02x", b);
    }

    @Override
    public void parseTo(byte[] bytes) throws IOException {
        final OutputStream output = options.getOutputFile();
        final InputStream input = new ByteArrayInputStream(bytes);

        while (true) {
            int readByte = input.read();

            if (readByte == -1) {
                break;
            }

            String hexString = getHexFromByte((byte) readByte);

            output.write(hexString.getBytes());
        }
    }

    @Override
    public byte[] parseFrom() throws IOException {

        byte[] bytes = getInput();

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final InputStream input = new ByteArrayInputStream(bytes);

        while (true) {
            int readByte1 = input.read();

            // ignore empty spaces
            while (readByte1 == ' ') {
                readByte1 = input.read();
            }

            if (readByte1 == -1) {
                break;
            }

            int readByte2 = input.read();

            // ignore empty spaces
            while (readByte2 == ' ') {
                readByte2 = input.read();
            }

            if (readByte2 == -1) {
                throw new InvalidHexCountException();
            }

            if (!isHex((char) readByte1) || !isHex((char) readByte2)) {
                throw new InvalidHexCharacterException();
            }

            byte hexByte = getByteFromHex((char) readByte1, (char) readByte2);

            output.write(hexByte);
        }

        return output.toByteArray();
    }
}
