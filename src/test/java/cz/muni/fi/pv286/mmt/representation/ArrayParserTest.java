package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.model.*;
import org.junit.jupiter.api.Test;

public class ArrayParserTest extends ParserTest {

    @Test
    public void roundTripTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.ARRAY);
        options.setOutputFormat(IoFormat.ARRAY);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "{0x01, 0x02}".getBytes(), "{0x1, 0x2}".getBytes());
        assertConversion(options, ArrayParser.class, ArrayParser.class, "{0x01, 2, 0b11, '\\x04'}".getBytes(), "{0x1, 0x2, 0x3, 0x4}".getBytes());
    }

    @Test
    public void fromHexToArrayTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.HEX);
        options.setOutputFormat(IoFormat.ARRAY);

        assertConversion(options, HexParser.class, ArrayParser.class, "01020304".getBytes(), "{0x1, 0x2, 0x3, 0x4}".getBytes());
    }

    @Test
    public void fromArrayToHexTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.ARRAY);
        options.setOutputFormat(IoFormat.HEX);

        assertConversion(options, ArrayParser.class, HexParser.class, "{0x01, 0x02, 0x03, 0x04}".getBytes(), "01020304".getBytes());
    }


    @Test
    public void fromBitsToArrayTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.BITS);
        options.setInputFromToOption(FromToOption.LEFT);
        options.setOutputFormat(IoFormat.ARRAY);
        options.setOutputFromToOption(FromToOption.HEX);

        assertConversion(options, BitParser.class, ArrayParser.class, "00000001000000100000001100000100".getBytes(), "{0x1, 0x2, 0x3, 0x4}".getBytes());
    }

    @Test
    public void fromArrayToBitsTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.ARRAY);
        options.setOutputFormat(IoFormat.BITS);
        options.setInputFromToOption(FromToOption.LEFT);
        options.setOutputFromToOption(FromToOption.HEX);

        assertConversion(options, ArrayParser.class, BitParser.class, "{0x01, 0x02, 0x03, 0x04}".getBytes(), "00000001000000100000001100000100".getBytes());
    }

    @Test
    public void invalidInputTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.ARRAY);
        options.setOutputFormat(IoFormat.ARRAY);

        assertConversionThrows(options, ArrayParser.class, ArrayParser.class, "{hello}".getBytes(), "{}".getBytes());
        assertConversionThrows(options, ArrayParser.class, ArrayParser.class, "{10A}".getBytes(), "{}".getBytes());
    }

    @Test
    public void noInputTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.ARRAY);
        options.setOutputFormat(IoFormat.ARRAY);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "{}".getBytes(), "{}".getBytes());
        assertConversion(options, ArrayParser.class, ArrayParser.class, "([], {})".getBytes(), "{{}, {}}".getBytes());
    }

    @Test
    public void nestedInputTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.ARRAY);
        options.setOutputFormat(IoFormat.ARRAY);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "{{0x01, (2), [3, 0b100, 0x05],'\\x06'}}".getBytes(), "{{0x1, {0x2}, {0x3, 0x4, 0x5}, 0x6}}".getBytes());
    }

    @Test
    public void customBracketTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.ARRAY);
        options.setOutputFormat(IoFormat.ARRAY);
        options.setBracketOption(BracketType.SQUARE_BRACKET);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "[0x01, 0x02, 0x03, 0x04]".getBytes(), "[0x1, 0x2, 0x3, 0x4]".getBytes());

        options.setBracketOption(BracketType.REGULAR_BRACKET);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "(0x01, 0x02, 0x03, 0x04)".getBytes(), "(0x1, 0x2, 0x3, 0x4)".getBytes());
    }

    @Test
    public void customOutputFormatTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.ARRAY);
        options.setOutputFormat(IoFormat.ARRAY);
        options.setOutputFromToOption(FromToOption.DECIMAL);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "{0x01, 0x02, 0x03, 0x04}".getBytes(), "{1, 2, 3, 4}".getBytes());

        options.setOutputFromToOption(FromToOption.CHARACTER);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "{0x41, 0x42, 0x43, 0x44}".getBytes(), "{'\\x41', '\\x42', '\\x43', '\\x44'}".getBytes());

        options.setOutputFromToOption(FromToOption.BINARY);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "{0x01, 0x02, 0x03, 0x04}".getBytes(), "{0b1, 0b10, 0b11, 0b100}".getBytes());
    }
}
