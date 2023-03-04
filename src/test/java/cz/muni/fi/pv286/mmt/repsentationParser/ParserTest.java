package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.model.Options;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private <From extends RepresentationParser, To extends RepresentationParser>
    void assertConversion(Options options, From fromParser, To toParser, byte[] input, byte[] output) {

        var inputStream = new ByteArrayInputStream(input);
        options.setInputFile(inputStream);

        var outputStream = new ByteArrayOutputStream();
        options.setOutputFile(outputStream);

        try {
            byte[] bytes = fromParser.parseFrom();
            toParser.parseTo(bytes);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertArrayEquals(output, outputStream.toByteArray());
    }

    protected <From extends RepresentationParser, To extends RepresentationParser>
    void assertConversion(Class<From> fromClass, Class<To> toClass, byte[] input, byte[] output) {
        Options options = new Options();

        try {
            From fromInstance = fromClass.getConstructor(Options.class).newInstance(options);
            To toInstance = toClass.getConstructor(Options.class).newInstance(options);

            assertConversion(options, fromInstance, toInstance, input, output);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    protected <T extends RepresentationParser>
    void assertRoundTrip(Class<T> parser, byte[] input) {
        assertConversion(parser, parser, input, input);
    }
}
