package cz.muni.fi.pv286.mmt.representation.fuzzing;

import cz.muni.fi.pv286.mmt.model.Options;
import cz.muni.fi.pv286.mmt.representation.RepresentationParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserFuzzTest {

    private <From extends RepresentationParser, To extends RepresentationParser>
    void assertConversion(Options options, From fromParser, To toParser, byte[] input, byte[] output) throws IOException {

        var inputStream = new ByteArrayInputStream(input);
        options.setInputFile(inputStream);

        var outputStream = new ByteArrayOutputStream();
        options.setOutputFile(outputStream);

//        try {
            byte[] bytes = fromParser.parseFrom();
            toParser.parseTo(bytes);
//        } catch (Exception e) {
//            fail(e.getMessage());
//        }

//        System.out.println("Expected:" + Arrays.toString(output) + " Got: " + Arrays.toString(outputStream.toByteArray()));
        assertArrayEquals(output, outputStream.toByteArray());
    }

    protected <From extends RepresentationParser, To extends RepresentationParser>
    void assertConversion(Class<From> fromClass, Class<To> toClass, byte[] input, byte[] output) throws IOException {
        Options options = new Options();

        From fromInstance = null;
        To toInstance = null;

        try {
            fromInstance = fromClass.getConstructor(Options.class).newInstance(options);
            toInstance = toClass.getConstructor(Options.class).newInstance(options);
        } catch (Exception e) {
            // ignore
        }

        if (fromInstance == null || toInstance == null)
            fail("Could not create instances of parsers.");

        assertConversion(options, fromInstance, toInstance, input, output);
    }

    protected <From extends RepresentationParser, To extends RepresentationParser>
    void assertConversion(Options options, Class<From> fromClass, Class<To> toClass, byte[] input, byte[] output) throws IOException {
        From fromInstance = null;
        To toInstance = null;

        try {
            fromInstance = fromClass.getConstructor(Options.class).newInstance(options);
            toInstance = toClass.getConstructor(Options.class).newInstance(options);
        } catch (Exception e) {
            // ignore
        }

        if (fromInstance == null || toInstance == null)
            fail("Could not create instances of parsers.");

        assertConversion(options, fromInstance, toInstance, input, output);
    }

    protected <T extends RepresentationParser>
    void assertRoundTrip(Class<T> parser, byte[] input) throws IOException {
        assertConversion(parser, parser, input, input);
    }

    protected <T extends RepresentationParser>
    void assertRoundTrip(Class<T> parser, byte[] input, byte[] output) throws IOException {
        assertConversion(parser, parser, input, output);
    }
}
