package cz.muni.fi.pv286.mmt.repsentationParser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ByteParserTest extends ParserTest {

    @Test
    public void roundTripTest() {
        assertRoundTrip(ByteParser.class, "test".getBytes());
    }

    @Test
    public void fromBytesToHexTest() {
        assertConversion(ByteParser.class, HexParser.class, "test".getBytes(), "74657374".getBytes());
    }
}