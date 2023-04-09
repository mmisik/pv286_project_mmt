package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.model.Options;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class RepresentationParserTest {

    @Test
    public void delimiterTest() {
        assertRoundTripDelimiter(Optional.empty(), "12\n12\n12");
        assertRoundTripDelimiter(Optional.of(";"), "12;12;12");
    }

    private void assertRoundTripDelimiter(Optional<String> delimiter, String data) {
        assertRoundTripDelimiter(delimiter, data, data);
    }

    private void assertRoundTripDelimiter(Optional<String> delimiter, String data, String expected) {
        var options = new Options();
        delimiter.ifPresent(options::setDelimiter);

        var parser = new ByteParser(options);

        var inputData = data.getBytes();
        var inputStream = new ByteArrayInputStream(inputData);
        options.setInputFile(inputStream);

        var outputStream = new ByteArrayOutputStream();
        options.setOutputFile(outputStream);

        try {
            parser.parse(parser);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        assertArrayEquals(expected.getBytes(), outputStream.toByteArray());
    }
}
