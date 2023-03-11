package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.model.FromToOption;
import cz.muni.fi.pv286.mmt.model.Options;
import org.junit.jupiter.api.Test;

public class BitParserTest extends ParserTest{
    @Test
    public void roundTripTest() {
        assertRoundTrip(BitParser.class, "0".getBytes(), new byte[] {(byte) 0});
        assertRoundTrip(BitParser.class, "00".getBytes(), new byte[] {(byte) 0});
        assertRoundTrip(BitParser.class, "01".getBytes(), new byte[] {(byte) 1});
        assertRoundTrip(BitParser.class, "11".getBytes(), new byte[] {(byte) 3});
        assertRoundTrip(BitParser.class, "1".getBytes(), new byte[] {(byte) 1});
        assertRoundTrip(BitParser.class, "100 1111  0100 1011".getBytes(), new byte[] {(byte) 79, (byte) 75});
    }

    @Test
    public void interleavedSpacesTest() {
        assertRoundTrip(BitParser.class, "10 0 1 111 100 0 10".getBytes(), new byte[] {(byte) 19, (byte) 226});
        assertRoundTrip(BitParser.class, "1  10 00 1 1001".getBytes(), new byte[] {(byte) 3, (byte) 25});
        assertRoundTrip(BitParser.class, "1 00 00 01 1 1 100001".getBytes(), new byte[] {(byte) 65, (byte) 225});
        assertRoundTrip(BitParser.class, "1 00000111 100001 1".getBytes(), new byte[] {(byte) 131, (byte) 195});
        assertRoundTrip(BitParser.class, "1 1 0 1 0 1 1 1 0 1 0 0 0 1 1 1 0 1".getBytes(), new byte[] {(byte) 3, (byte) 93, (byte) 29});
    }

    @Test
    public void fromBitsToBytes() {
        assertConversion(BitParser.class, ByteParser.class, "100 1111  0100 1011".getBytes(), "OK".getBytes());
    }

    @Test
    public void fromBitsToHexTest() {
        Options options = new Options();
        options.setInputFromToOption(FromToOption.Right);

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
        assertRoundTrip(BitParser.class, "0".getBytes(), new byte[] {(byte) 0});
    }

    @Test
    public void paddingLeftTest() {
        assertRoundTrip(BitParser.class, "1".getBytes(), new byte[] {1});
        assertRoundTrip(BitParser.class, "10".getBytes(), new byte[] {2});
        assertRoundTrip(BitParser.class, "11".getBytes(), new byte[] {3});
    }

    @Test
    public void paddingRightTest() {
        Options options = new Options();
        options.setInputFromToOption(FromToOption.Right);

        assertConversion(options, BitParser.class, BitParser.class, "0".getBytes(), new byte[] {0});
        assertConversion(options, BitParser.class, BitParser.class, "1".getBytes(), new byte[] {(byte) 128});
        assertConversion(options, BitParser.class, BitParser.class, "11".getBytes(), new byte[] {(byte) 192});
    }

}
