package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.model.BracketType;
import cz.muni.fi.pv286.mmt.model.FromToOption;
import cz.muni.fi.pv286.mmt.model.IoFormat;
import cz.muni.fi.pv286.mmt.model.Options;
import org.junit.jupiter.api.Test;

public class ArrayParserTest extends ParserTest {

    @Test
    public void roundTripTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.Array);
        options.setOutputFormat(IoFormat.Array);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "{0x01, 0x02}".getBytes(), "{0x01, 0x02}".getBytes());
        assertConversion(options, ArrayParser.class, ArrayParser.class, "{0x01, 2, 0b11, '\\x04'}".getBytes(), "{0x01, 0x02, 0x03, 0x04}".getBytes());
    }

    @Test
    public void fromHexToArrayTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.Hex);

        assertConversion(HexParser.class, ArrayParser.class, "01020304".getBytes(), "{0x01, 0x02, 0x03, 0x04}".getBytes());
    }

    @Test
    public void fromArrayToHexTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.Array);
        options.setOutputFormat(IoFormat.Hex);

        assertConversion(options, ArrayParser.class, HexParser.class, "{0x01, 0x02, 0x03, 0x04}".getBytes(), "01020304".getBytes());
    }


    @Test
    public void fromBitsToArrayTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.Bits);
        options.setInputFromToOption(FromToOption.Left);
        options.setOutputFromToOption(FromToOption.Hex);

        assertConversion(options, BitParser.class, ArrayParser.class, "00000001000000100000001100000100".getBytes(), "{0x01, 0x02, 0x03, 0x04}".getBytes());
    }

    @Test
    public void fromArrayToBitsTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.Array);
        options.setOutputFormat(IoFormat.Bits);
        options.setInputFromToOption(FromToOption.Left);
        options.setOutputFromToOption(FromToOption.Hex);

        assertConversion(options, ArrayParser.class, BitParser.class, "{0x01, 0x02, 0x03, 0x04}".getBytes(), "00000001000000100000001100000100".getBytes());
    }

    @Test
    public void invalidInputTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.Array);
        options.setOutputFormat(IoFormat.Array);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "{hello}".getBytes(), "{}".getBytes());
        assertConversion(options, ArrayParser.class, ArrayParser.class, "{10A}".getBytes(), "{}".getBytes());
    }

    @Test
    public void noInputTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.Array);
        options.setOutputFormat(IoFormat.Array);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "{}".getBytes(), "{}".getBytes());
    }

    @Test
    public void nestedInputTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.Array);
        options.setOutputFormat(IoFormat.Array);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "{{0x01, (2), [3, 0b100, 0x05],'\\x06'}}".getBytes(), "{{0x01, {0x02}, {0x03, 0x04, 0x05}, 0x06}}".getBytes());
    }

    @Test
    public void customBracketTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.Array);
        options.setOutputFormat(IoFormat.Array);
        options.setBracketOption(BracketType.SquareBracket);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "[0x01, 0x02, 0x03, 0x04]".getBytes(), "[0x01, 0x02, 0x03, 0x04]".getBytes());

        options.setBracketOption(BracketType.RegularBracket);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "(0x01, 0x02, 0x03, 0x04)".getBytes(), "(0x01, 0x02, 0x03, 0x04)".getBytes());
    }

    @Test
    public void customOutputFormatTest() {
        Options options = new Options();
        options.setInputFormat(IoFormat.Array);
        options.setOutputFormat(IoFormat.Array);
        options.setOutputFromToOption(FromToOption.Decimal);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "{0x01, 0x02, 0x03, 0x04}".getBytes(), "{1, 2, 3, 4}".getBytes());

        options.setOutputFromToOption(FromToOption.Character);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "{0x41, 0x42, 0x43, 0x44}".getBytes(), "{'A', 'B', 'C', 'D'}".getBytes());

        options.setOutputFromToOption(FromToOption.Binary);

        assertConversion(options, ArrayParser.class, ArrayParser.class, "{0x01, 0x02, 0x03, 0x04}".getBytes(), "{0b1, 0b10, 0b11, 0b100}".getBytes());
    }
}
