package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.exceptions.InvalidBitCharacterException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidBitInputException;
import cz.muni.fi.pv286.mmt.model.FromToOption;
import cz.muni.fi.pv286.mmt.model.Options;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

public class BitParser extends RepresentationParser {

    public BitParser(Options options) {
        super(options);
    }

    public static byte[] padOutput(Optional<FromToOption> inputFromToOption, byte[] bits) {
        int remaining = bits.length % 8;
        byte padByte = 48;

        if (remaining == 0) {
            return bits;
        }

        byte[] paddedBits = new byte[bits.length + (8 - remaining)];

        FromToOption direction;

        try {
            direction = inputFromToOption.orElse(FromToOption.Left);
        }
        catch (NullPointerException e) {
            direction = FromToOption.Left;
        }


        if (direction == FromToOption.Left) {
            Arrays.fill(paddedBits, 0, 8 - remaining, padByte);
            System.arraycopy(bits, 0, paddedBits, 8 - remaining, bits.length);
        } else {
            int padStart = bits.length;
            int padEnd = paddedBits.length;
            if (padEnd > padStart) {
                Arrays.fill(paddedBits, padStart, padEnd, padByte);
            }
            System.arraycopy(bits, 0, paddedBits, 0, bits.length);
        }

        return paddedBits;
    }

    private static byte[] encodeToByteArray(byte[] bits) {
        byte[] results = new byte[(bits.length + 7) / 8];
        int byteValue = 0;
        int index;
        for (index = 0; index < bits.length; index++) {

            byteValue = (byteValue << 1) | (bits[index] & 0x1);

            if (index % 8 == 7) {
                results[index / 8] = (byte) byteValue;
                byteValue = 0;
            }
        }

        if (index % 8 != 0) {
            results[index / 8] = (byte) (byteValue << (8 - (index % 8)));
        }

        return results;
    }

    public static byte[] binaryStringToBinaryBytes(String binary) {
        int len = binary.length();
        byte[] bytes = new byte[len / 8];
        for (int i = 0; i < len; i += 8) {
            bytes[i / 8] = (byte) Integer.parseInt(binary.substring(i, i + 8), 2);
        }
        return bytes;
    }


    public static String bytesToString(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }


    @Override
    public void parseTo(byte[] bytes) throws IOException {
        final OutputStream output = options.getOutputFile();
        final InputStream input = new ByteArrayInputStream(bytes);
        StringBuilder binaryString = new StringBuilder();


        while (true) {
            int inputByte = input.read();

            if (inputByte == -1) {
                break;
            }
            String binary = String.format("%8s", Integer.toBinaryString(inputByte & 0xFF)).replace(' ', '0');
            binaryString.append(binary);

        }

        output.write(binaryString.toString().getBytes());
    }

    @Override
    public byte[] parseFrom() throws IOException {
        byte[] bytes = getInput();

        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final InputStream input = new ByteArrayInputStream(bytes);

        while (true) {
            int inputBit = input.read();

            if (inputBit == -1)
                break;

            if (inputBit != 48 && inputBit != 49) { // 48 = '0', 49 = '1'
                if (inputBit == 32) // 32 = ' '
                    continue;

                throw new InvalidBitCharacterException();
            }

            output.write(inputBit);
        }

        if (output.size() == 0) {
            throw new InvalidBitInputException();
        }

        byte[] padded = padOutput(options.getInputFromToOption(), output.toByteArray());
        String str = new String(padded, StandardCharsets.UTF_8);

        return encodeToByteArray(padded);
    }
}
