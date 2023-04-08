package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.model.Options;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private <From extends RepresentationParser, To extends RepresentationParser>
    void assertConversionThrows(Options options, From fromParser, To toParser, byte[] input, byte[] output) {

        var inputStream = new ByteArrayInputStream(input);
        options.setInputFile(inputStream);

        var outputStream = new ByteArrayOutputStream();
        options.setOutputFile(outputStream);

        try {
            fromParser.parse(toParser);
        } catch (Exception e) {
            assertTrue(true, e.getMessage());
            return;
        }

        fail("Expected exception was not thrown.");
    }

    protected <From extends RepresentationParser, To extends RepresentationParser>
    void assertConversionThrows(Class<From> fromClass, Class<To> toClass, byte[] input, byte[] output) {
        Options options = new Options();

        try {
            From fromInstance = fromClass.getConstructor(Options.class).newInstance(options);
            To toInstance = toClass.getConstructor(Options.class).newInstance(options);

            assertConversionThrows(options, fromInstance, toInstance, input, output);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    protected <T extends RepresentationParser>
    void assertRoundTripThrows(Class<T> parser, byte[] input) {
        assertConversionThrows(parser, parser, input, input);
    }

    protected <T extends RepresentationParser>
    void assertRoundTripThrows(Class<T> parser, byte[] input, byte[] output) {
        assertConversionThrows(parser, parser, input, output);
    }

    private <From extends RepresentationParser, To extends RepresentationParser>
    void assertConversion(Options options, From fromParser, To toParser, byte[] input, byte[] output) {

        var inputStream = new ByteArrayInputStream(input);
        options.setInputFile(inputStream);

        var outputStream = new ByteArrayOutputStream();
        options.setOutputFile(outputStream);

        try {
            fromParser.parse(toParser);
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

    protected <From extends RepresentationParser, To extends RepresentationParser>
    void assertConversion(Options options, Class<From> fromClass, Class<To> toClass, byte[] input, byte[] output) {

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

    protected <T extends RepresentationParser>
    void assertRoundTrip(Class<T> parser, byte[] input, byte[] output) {
        assertConversion(parser, parser, input, output);
    }

    protected <T extends RepresentationParser>
    void assertRoundTrip(Options options, Class<T> parser, byte[] input, byte[] output) {
        assertConversion(options, parser, parser, input, output);
    }

    private <From extends RepresentationParser, To extends RepresentationParser>
    void assertConversionFails(Options options, From fromParser, To toParser, byte[] input, byte[] output) {

        var inputStream = new ByteArrayInputStream(input);
        options.setInputFile(inputStream);

        var outputStream = new ByteArrayOutputStream();
        options.setOutputFile(outputStream);

        try {
            fromParser.parse(toParser);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertFalse(Arrays.equals(output, outputStream.toByteArray()));
    }

    protected <From extends RepresentationParser, To extends RepresentationParser>
    void assertConversionFails(Class<From> fromClass, Class<To> toClass, byte[] input, byte[] output) {
        Options options = new Options();

        try {
            From fromInstance = fromClass.getConstructor(Options.class).newInstance(options);
            To toInstance = toClass.getConstructor(Options.class).newInstance(options);

            assertConversionFails(options, fromInstance, toInstance, input, output);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    protected <T extends RepresentationParser>
    void assertRoundTripFails(Class<T> parser, byte[] input) {
        assertConversionFails(parser, parser, input, input);
    }

    protected <T extends RepresentationParser>
    void assertRoundTripFails(Class<T> parser, byte[] input, byte[] output) {
        assertConversionFails(parser, parser, input, output);
    }
}
