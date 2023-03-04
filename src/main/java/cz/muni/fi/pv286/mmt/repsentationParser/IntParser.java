package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.model.Options;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class IntParser extends RepresentationParser {

    private enum Endian {
        LITTLE,
        BIG
    }

    public IntParser(Options options) {
        super(options);
    }

    private boolean isNumber(char c) {
        return Character.digit(c, 10) != -1;
    }

    private byte[] getBytesFromInteger(int integer, Endian endian) {
        if (endian == Endian.BIG) {
            return new byte[] {
                    (byte) (integer >> 24),
                    (byte) (integer >> 16),
                    (byte) (integer >> 8),
                    (byte) integer
            };
        } else {
            return new byte[] {
                    (byte) integer,
                    (byte) (integer >> 8),
                    (byte) (integer >> 16),
                    (byte) (integer >> 24)
            };
        }
    }

    private byte[] reverseByteArray(byte[] array) {
        byte[] reversed = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            reversed[i] = array[array.length - i - 1];
        }
        return reversed;
    }

    @Override
    public void parseTo(byte[] bytes) throws IOException {

        if (bytes.length > 4) {
            throw new IOException("Integer is not valid.");
        }

        byte[] bytesToWrite = new byte[4];

        int pad = 4 - bytes.length;
        int i;
        for (i = 0; i < pad; i++) {
            bytesToWrite[i] = 0;
        }

        for (byte b : bytes) {
            bytesToWrite[i] = b;
            i++;
        }

        // TODO: little/big endian
        // bytesToWrite = reverseByteArray(bytesToWrite);

        // Java doesn't have unsigned types, so we need to apply a mask to every byte
        long integer = ((long) (bytesToWrite[0] & 0xff) << 24) + ((bytesToWrite[1] & 0xff) << 16) + ((bytesToWrite[2] & 0xff) << 8) + (bytesToWrite[3] & 0xff);
        byte[] integerAsString = String.valueOf(integer).getBytes();

        options.getOutputFile().write(integerAsString);
    }

    @Override
    public byte[] parseFrom() throws IOException {
        byte[] bytes = getInput();

        final InputStream input = new ByteArrayInputStream(bytes);

        int characterCount = 0;

        final StringBuilder integerBuilder = new StringBuilder();

        while (true) {
            int readByte = input.read();

            if (readByte == -1) {
                break;
            }

            if (!isNumber((char) readByte)) {
                throw new IOException("Invalid number characters encountered.");
            }

            characterCount++;

            // 10 characters is the maximum number of digits in an integer
            if (characterCount > 10) {
                throw new IOException("Integer of this length would overflow.");
            }

            integerBuilder.append((char)readByte);
        }

        int integer;

        try {
            integer = Integer.parseUnsignedInt(integerBuilder.toString());
        } catch (NumberFormatException e) {
            throw new IOException("Integer is not valid.");
        }

        byte[] integerAsBytes = getBytesFromInteger(integer, Endian.BIG);

        // TODO: little/big endian

        return integerAsBytes;
    }
}
