package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.model.FromToOption;
import cz.muni.fi.pv286.mmt.model.Options;

import java.io.*;
import java.util.Arrays;
import java.util.Optional;

public class BitParser extends RepresentationParser {

    public BitParser(Options options) {
        super(options);
    }

    private byte[] padOutput(byte[] bits) {
        int remaining = bits.length % 8;
        byte padByte = 48;

        if (remaining == 0) {
            return bits;
        }

        byte[] paddedBits = new byte[bits.length + (8 - remaining)];

        Optional<FromToOption> inputFromToOption = this.options.getInputFromToOption();
        FromToOption direction;

        try {
            direction = inputFromToOption.orElse(FromToOption.Left);
        }
        catch(NullPointerException e) {
            direction = FromToOption.Left;
        }


        if (direction == FromToOption.Left) {
            Arrays.fill(paddedBits, 0, 8 - remaining, padByte);
            System.arraycopy(bits, 0, paddedBits, 8 - remaining, bits.length);
        } else {
            Arrays.fill(paddedBits, bits.length, paddedBits.length, padByte);
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

    @Override
    public void parseTo(byte[] bytes) throws IOException {
        final OutputStream output = options.getOutputFile();
        final InputStream input = new ByteArrayInputStream(bytes);

        while (true) {
            int inputBit = input.read();

            if (inputBit == -1) {
                break;
            }

            output.write(inputBit);
        }
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

            if (inputBit != 48 && inputBit != 49) {
                if (inputBit == 32)
                    continue;

                throw new IOException("Invalid character found");
            }

            output.write(inputBit);
        }

        if (output.size() == 0){
            throw new IOException("No input found");
        }

        return encodeToByteArray(padOutput(output.toByteArray()));
    }
}
