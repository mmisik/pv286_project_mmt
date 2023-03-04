package cz.muni.fi.pv286.mmt.repsentationParser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HexParserTest extends ParserTest {

    @Test
    public void roundTripTest() {
        assertRoundTrip(HexParser.class, "74657374".getBytes());
        assertRoundTrip(HexParser.class, "7465".getBytes());
        assertRoundTrip(HexParser.class, "74".getBytes());
        assertRoundTrip(HexParser.class, "aa".getBytes());
        assertRoundTrip(HexParser.class, "bb".getBytes());
    }

    @Test
    public void roundTripUppercaseLettersTest() {
        assertRoundTripFails(HexParser.class, "AA".getBytes());
        assertRoundTripFails(HexParser.class, "BB".getBytes());

        // note: uppercase letters cannot round trip, because the output is always lowercase
        // this behaviour is not defined in the specification
    }

    @Test
    public void invalidHexLengthTest() {
        assertRoundTripThrows(HexParser.class, "7465737".getBytes());
        assertRoundTripThrows(HexParser.class, "a".getBytes());
        assertRoundTripThrows(HexParser.class, "aaa".getBytes());
        assertRoundTripThrows(HexParser.class, "aaaaa".getBytes());
    }

    @Test
    public void invalidHexCharactersTest() {
        assertRoundTripThrows(HexParser.class, "test".getBytes());
        assertRoundTripThrows(HexParser.class, "7465737g".getBytes());
        assertRoundTripThrows(HexParser.class, "abcdefgh".getBytes());
        assertRoundTripThrows(HexParser.class, "1234556x".getBytes());
        assertRoundTripThrows(HexParser.class, "x1234567".getBytes());
    }

    @Test
    public void fromHexToBytesTest() {
        assertConversion(HexParser.class, ByteParser.class, "74657374".getBytes(), "test".getBytes());
    }
}