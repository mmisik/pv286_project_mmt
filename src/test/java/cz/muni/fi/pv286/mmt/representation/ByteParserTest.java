package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.model.Options;
import org.junit.jupiter.api.Test;

class ByteParserTest extends ParserTest {

    @Test
    public void roundTripTest() {
        assertRoundTrip(ByteParser.class, "test".getBytes());
    }

    @Test
    public void fromBytesToHexTest() {
        assertConversion(ByteParser.class, HexParser.class, "test".getBytes(), "74657374".getBytes());
    }

    @Test
    public void fromBytesToIntTest() {
        assertConversion(ByteParser.class, IntParser.class, "test".getBytes(), "1952805748".getBytes());
    }

    @Test
    public void fromBytesToBitsTest() {
        assertConversion(ByteParser.class, BitParser.class, "OK".getBytes(), "0100111101001011".getBytes());    }

    @Test
    public void delimiterTest() {

        // implicit delimiter - do nothing
        assertRoundTrip(ByteParser.class, "1\n1".getBytes(), "1\n1".getBytes());

        var options = new Options();
        options.setDelimiter(";");

        // explicit delimiter
        assertRoundTrip(options, ByteParser.class, "\n;\n".getBytes(), "\n;\n".getBytes());
    }
}