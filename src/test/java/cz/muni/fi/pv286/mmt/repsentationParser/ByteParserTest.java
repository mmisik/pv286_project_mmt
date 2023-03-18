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

    @Test
    public void fromBytesToIntTest() {
        assertConversion(ByteParser.class, IntParser.class, "test".getBytes(), "1952805748".getBytes());
    }
    @Test
    public void fromBytesToBitsTest() {
        assertConversion(ByteParser.class, BitParser.class, "OK".getBytes(), "0100111101001011".getBytes());    }
}