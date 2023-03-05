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
    public void fromBitsToBytes() {
        assertConversion(BitParser.class, ByteParser.class, "100 1111  0100 1011".getBytes(), "OK".getBytes());
    }

    @Test
    public void fromBytesToHexTest() {
        Options options = new Options();
        options.setInputFromToOption(FromToOption.RIGHT);

        assertConversion(options, BitParser.class, HexParser.class, "100111101001011".getBytes(), "9e96".getBytes());

        assertConversion(options, BitParser.class, HexParser.class, "1010111010000011".getBytes(), "ae83".getBytes());

        assertConversion(options, BitParser.class, HexParser.class, "10101010".getBytes(), "aa".getBytes());
        assertConversion(options, BitParser.class, HexParser.class, "11001100".getBytes(), "cc".getBytes());
        assertConversion(options, BitParser.class, HexParser.class, "11110000".getBytes(), "f0".getBytes());
        assertConversion(options, BitParser.class, HexParser.class, "00001111".getBytes(), "0f".getBytes());
        assertConversion(options, BitParser.class, HexParser.class, "10010110".getBytes(), "96".getBytes());
        assertConversion(options, BitParser.class, HexParser.class, "01101101".getBytes(), "6d".getBytes());
        assertConversion(options, BitParser.class, HexParser.class, "00110011".getBytes(), "33".getBytes());
        assertConversion(options, BitParser.class, HexParser.class, "11000011".getBytes(), "c3".getBytes());
        assertConversion(options, BitParser.class, HexParser.class, "01010101".getBytes(), "55".getBytes());
        assertConversion(options, BitParser.class, HexParser.class, "10100101".getBytes(), "a5".getBytes());


    }

}
