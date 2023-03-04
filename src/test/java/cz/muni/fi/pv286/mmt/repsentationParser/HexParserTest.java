package cz.muni.fi.pv286.mmt.repsentationParser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HexParserTest extends ParserTest {

    @Test
    public void roundTripTest() {
        assertRoundTrip(HexParser.class, "74657374".getBytes());
    }

    @Test
    public void fromHexToBytesTest() {
        assertConversion(HexParser.class, ByteParser.class, "74657374".getBytes(), "test".getBytes());
    }
}