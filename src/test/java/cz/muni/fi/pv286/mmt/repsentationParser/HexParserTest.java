package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.model.Options;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class HexParserTest {

    @Test
    public void roundTripTest() {
        Options options = new Options();

        // 74657374 = test
        byte[] testCase = "74657374".getBytes();

        var input = new ByteArrayInputStream(testCase);
        options.setInputFile(input);

        var output = new ByteArrayOutputStream();
        options.setOutputFile(output);

        HexParser hexParser = new HexParser(options);

        try {
            byte[] bytes = hexParser.parseFrom();
            hexParser.parseTo(bytes);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertArrayEquals(testCase, output.toByteArray());
    }

    @Test
    public void fromHexToBytesTest() {
        Options options = new Options();

        // 74657374 = test
        byte[] testCase = "74657374".getBytes();
        byte[] testOutput = "test".getBytes();

        var input = new ByteArrayInputStream(testCase);
        options.setInputFile(input);

        var output = new ByteArrayOutputStream();
        options.setOutputFile(output);

        HexParser hexParser = new HexParser(options);

        ByteParser byteParser = new ByteParser(options);

        try {
            byte[] bytes = hexParser.parseFrom();
            byteParser.parseTo(bytes);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertArrayEquals(testOutput, output.toByteArray());
    }

}