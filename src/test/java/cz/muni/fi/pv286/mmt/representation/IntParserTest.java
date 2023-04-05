package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.model.FromToOption;
import cz.muni.fi.pv286.mmt.model.Options;
import org.junit.jupiter.api.Test;

class IntParserTest extends ParserTest {

    @Test
    public void roundTripTest() {
        assertRoundTrip(IntParser.class, "1234567890".getBytes());
        assertRoundTrip(IntParser.class, "1".getBytes());
        assertRoundTrip(IntParser.class, "123".getBytes());
        assertRoundTrip(IntParser.class, "5331".getBytes());
        assertRoundTrip(IntParser.class, "14234242".getBytes());
    }

    @Test
    public void fromIntToHexTest() {
        assertConversion(IntParser.class, HexParser.class, "1234567890".getBytes(), "499602d2".getBytes());
    }

    @Test
    public void fromIntToByteTest() {
        assertConversion(IntParser.class, ByteParser.class, "1234567890".getBytes(), new byte[] { 73, -106, 2, -46 });
    }

    @Test
    public void unsignedIntegerTest() {
        assertRoundTrip(IntParser.class, "2147483647".getBytes()); // 32 bit signed max
        assertRoundTrip(IntParser.class, "4294967295".getBytes()); // 32 bit unsigned max
    }

    @Test
    public void integerOverflowTest() {
        assertRoundTripThrows(IntParser.class, "4294967296".getBytes());
        assertRoundTripThrows(IntParser.class, "12345678901234567890".getBytes());
        assertRoundTripThrows(IntParser.class, "123456789012345678901234567890".getBytes());
        assertRoundTripThrows(IntParser.class, "12345678901234567890123456789012345678901234567890".getBytes());
        assertRoundTripThrows(IntParser.class, "123456789012345678901234567890123456789012345678901234567890".getBytes());
    }

    @Test
    public void integerInvalidCharactersTest() {
        assertRoundTripThrows(IntParser.class, "a".getBytes());
        assertRoundTripThrows(IntParser.class, "1123a".getBytes());
        assertRoundTripThrows(IntParser.class, "-231".getBytes());
        assertRoundTripThrows(IntParser.class, "12313f".getBytes());
        assertRoundTripThrows(IntParser.class, "abcdefgh".getBytes());
        assertRoundTripThrows(IntParser.class, "0.13213".getBytes());
        assertRoundTripThrows(IntParser.class, "1,13213".getBytes());
        assertRoundTripThrows(IntParser.class, "+13213".getBytes());
    }

    @Test
    public void maxFourBytesTest() {
        // input must be of maximum 4 bytes, otherwise it cannot be parsed as an 32 bit integer
        assertConversionThrows(ByteParser.class, IntParser.class, "test\n".getBytes(), "".getBytes());
        assertConversion(ByteParser.class, IntParser.class, "test".getBytes(), "1952805748".getBytes());
    }

    @Test
    public void padZeroesTest() {
        assertConversion(ByteParser.class, IntParser.class, "te".getBytes(), "29797".getBytes());
    }

    @Test
    public void endianityTest() {
        Options options = new Options();
        options.setInputFromToOption(FromToOption.LITTLE);
        assertConversion(options, IntParser.class, HexParser.class, "1234567890".getBytes(), "d2029649".getBytes());

        options = new Options();
        options.setOutputFromToOption(FromToOption.LITTLE);
        assertConversion(options, HexParser.class, IntParser.class, "d2029649".getBytes(), "1234567890".getBytes());
    }

}