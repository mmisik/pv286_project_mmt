package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.model.Options;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

class ByteParserTest {

    @Test
    public void basicTest() {
        Options options = new Options();

        byte[] testCase = "test".getBytes();

        var input = new ByteArrayInputStream(testCase);
        options.setInputFile(input);

        var output = new ByteArrayOutputStream();
        options.setOutputFile(output);

        ByteParser byteParser = new ByteParser(options);

        try {
            byte[] bytes = byteParser.parseFrom();
            byteParser.parseTo(bytes);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertArrayEquals(testCase, output.toByteArray());
    }
}