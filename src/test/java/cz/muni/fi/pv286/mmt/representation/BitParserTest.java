package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.model.FromToOption;
import cz.muni.fi.pv286.mmt.model.Options;
import org.junit.jupiter.api.Test;

public class BitParserTest extends ParserTest{
    @Test
    public void roundTripTest() {
        assertRoundTrip(BitParser.class, "0".getBytes(), "00000000".getBytes());
        assertRoundTrip(BitParser.class, "00".getBytes(), "00000000".getBytes());
        assertRoundTrip(BitParser.class, "01".getBytes(), "00000001".getBytes());
        assertRoundTrip(BitParser.class, "11".getBytes(), "00000011".getBytes());
        assertRoundTrip(BitParser.class, "1".getBytes(), "00000001".getBytes());
        assertRoundTrip(BitParser.class, "100 1111  0100 1011".getBytes(), "0100111101001011".getBytes());
    }

    @Test
    public void interleavedSpacesTest() {
        assertRoundTrip(BitParser.class, "10 0 1 111 100 0 10".getBytes(), "0001001111100010".getBytes());
        assertRoundTrip(BitParser.class, "1  10 00 1 1001".getBytes(), "0000001100011001".getBytes());
        assertRoundTrip(BitParser.class, "1 00 00 01 1 1 100001".getBytes(), "0100000111100001".getBytes());
        assertRoundTrip(BitParser.class, "1 00000111 100001 1".getBytes(), "1000001111000011".getBytes());
        assertRoundTrip(BitParser.class, "1 1 0 1 0 1 1 1 0 1 0 0 0 1 1 1 0 1".getBytes(), "000000110101110100011101".getBytes());
    }

    @Test
    public void fromBitsToBytes() {
        assertConversion(BitParser.class, ByteParser.class, "100 1111  0100 1011".getBytes(), "OK".getBytes());
    }
    @Test
    public void fromBytesToBits() {
        assertConversion(ByteParser.class, BitParser.class, "OK".getBytes(), "0100111101001011".getBytes());
    }

    @Test
    public void fromBitsToHexTest() {
        Options options = new Options();
        options.setInputFromToOption(FromToOption.RIGHT);

        assertConversion(options, BitParser.class, HexParser.class, "100111101001011".getBytes(), "9e96".getBytes());
        assertConversion(options, BitParser.class, HexParser.class, "1010111010000011".getBytes(), "ae83".getBytes());
    }

    @Test
    public void invalidInputTest() {
        assertRoundTripThrows(BitParser.class, "hello".getBytes());
        assertRoundTripThrows(BitParser.class, "10A".getBytes());
    }

    @Test
    public void noInputTest() {
        assertRoundTripThrows(BitParser.class, "".getBytes());
    }

    @Test
    public void smallestInputTest() {
        assertRoundTrip(BitParser.class, "0".getBytes(), "00000000".getBytes());
    }

    @Test
    public void paddingLeftTest() {
        assertRoundTrip(BitParser.class, "1".getBytes(), "00000001".getBytes());
        assertRoundTrip(BitParser.class, "10".getBytes(), "00000010".getBytes());
        assertRoundTrip(BitParser.class, "11".getBytes(), "00000011".getBytes());
    }

    @Test
    public void paddingRightTest() {
        Options options = new Options();
        options.setInputFromToOption(FromToOption.RIGHT);

        assertConversion(options, BitParser.class, BitParser.class, "0".getBytes(), "00000000".getBytes());
        assertConversion(options, BitParser.class, BitParser.class, "1".getBytes(), "10000000".getBytes());
        assertConversion(options, BitParser.class, BitParser.class, "11".getBytes(), "11000000".getBytes());
    }

}
