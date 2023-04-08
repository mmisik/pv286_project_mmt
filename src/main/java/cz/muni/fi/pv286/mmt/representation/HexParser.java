package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.exceptions.InvalidHexCharacterException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidHexCountException;
import cz.muni.fi.pv286.mmt.model.Options;
import cz.muni.fi.pv286.mmt.model.ResultTree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Hex parser.
 */
public class HexParser extends RepresentationParser {

    public HexParser(Options options) {
        super(options);
    }

    private boolean isHex(char c) {
        return Character.digit(c, 16) != -1;
    }

    static byte getByteFromHex(char a, char b) {
        int first = Character.digit(a, 16);
        int second = Character.digit(b, 16);

        return (byte) ((first << 4) + second);
    }

    static String getHexFromByte(byte b) {
        return String.format("%02x", b);
    }

    @Override
    public byte[] parseTo(byte[] bytes) throws IOException {
        StringBuilder hexString = new StringBuilder();
        for(byte b: bytes) {
            hexString.append(getHexFromByte(b));
        }
        return hexString.toString().getBytes();

    }

    @Override
    public byte[] parseFrom(byte[] bytes) throws IOException {
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
