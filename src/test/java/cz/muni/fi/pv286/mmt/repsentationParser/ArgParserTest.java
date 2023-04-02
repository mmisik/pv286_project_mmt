package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.arg_parser.ArgParser;
import cz.muni.fi.pv286.mmt.exceptions.BadArgumentsException;
import cz.muni.fi.pv286.mmt.exceptions.HelpInvokedException;
import cz.muni.fi.pv286.mmt.model.BracketType;
import cz.muni.fi.pv286.mmt.model.FromToOption;
import cz.muni.fi.pv286.mmt.model.IOFormat;
import cz.muni.fi.pv286.mmt.model.Options;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ArgParserTest {
    @Test
    void shortBytesTest() {
        String[] args = "-f bytes -t bytes".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(IOFormat.Bytes, options.getInputFormat());
            assertEquals(IOFormat.Bytes, options.getOutputFormat());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void longBytesTest() {
        String[] args = "--from=bytes --to=bytes".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(IOFormat.Bytes, options.getInputFormat());
            assertEquals(IOFormat.Bytes, options.getOutputFormat());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    void hexInputIntOutputTestMixed() {
        String[] args = "-f hex --to=int".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(IOFormat.Hex, options.getInputFormat());
            assertEquals(IOFormat.Int, options.getOutputFormat());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void bitsInputArrayOutputTestMixed() {
        String[] args = "--from=bits -t array".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(IOFormat.Bits, options.getInputFormat());
            assertEquals(IOFormat.Array, options.getOutputFormat());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testArrayOptions() {
        String[] args = "--from=array --to=array --to-options=0 --to-options=\"{\"".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(IOFormat.Array, options.getInputFormat());
            assertEquals(IOFormat.Array, options.getOutputFormat());
            assertEquals(FromToOption.Decimal, options.getOutputFromToOption().orElseThrow());
            assertEquals(BracketType.CurlyBracket, options.getBracketType().orElseThrow());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void defaultArrayOptionsTest() {
        String[] args = "--from=array --to=array".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(IOFormat.Array, options.getInputFormat());
            assertEquals(IOFormat.Array, options.getOutputFormat());
            assertEquals(FromToOption.Hex, options.getOutputFromToOption().orElseThrow());
            assertEquals(BracketType.CurlyBracket, options.getBracketType().orElseThrow());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void intDefaultOptionsTest() {
        String[] args = "--from=int --to=int".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(IOFormat.Int, options.getInputFormat());
            assertEquals(IOFormat.Int, options.getOutputFormat());
            assertEquals(FromToOption.Big, options.getInputFromToOption().orElseThrow());
            assertEquals(FromToOption.Big, options.getOutputFromToOption().orElseThrow());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void intOptionsTest() {
        String[] args = "--from=int --to=int --to-options=little --from-options=little".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(IOFormat.Int, options.getInputFormat());
            assertEquals(IOFormat.Int, options.getOutputFormat());
            assertEquals(FromToOption.Little, options.getInputFromToOption().orElseThrow());
            assertEquals(FromToOption.Little, options.getOutputFromToOption().orElseThrow());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void bitsDefaultOptionsTest() {
        String[] args = "--from=bits --to=bits".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(IOFormat.Bits, options.getInputFormat());
            assertEquals(IOFormat.Bits, options.getOutputFormat());
            assertTrue(options.getOutputFromToOption().isEmpty());
            assertEquals(FromToOption.Left, options.getInputFromToOption().orElseThrow());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @ParameterizedTest
    @MethodSource("badArgumentsProvider")
    void failOnBadArgumentsTest(String[] args) {
        ArgParser argParser = new ArgParser(args);
        assertThrows(BadArgumentsException.class, argParser::parse);
    }

    private static Stream<Arguments> badArgumentsProvider() {
        return Stream.of(
                Arguments.of((Object) new String[]{"-t", "bytes"}),
                Arguments.of((Object) new String[]{"-f", "bytes"}),
                Arguments.of((Object) new String[]{"-f", "bytes", "-t", "bytes", "--from-options=big"})
        );
    }

    @Test
    void invokeHelpShortTest() {
        String[] args = "--from=bits --to=bits --help".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        assertThrows(HelpInvokedException.class, argParser::parse);
    }

    @Test
    void invokeHelpLongTest() {
        String[] args = "-h".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        assertThrows(HelpInvokedException.class, argParser::parse);
    }

    @Test
    void failNoInputFormatGivenTest() {
        String[] args = "-t bytes".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        assertThrows(BadArgumentsException.class, argParser::parse);
    }

    @Test
    void failNoOutputFormatGivenTest() {
        String[] args = "-f bytes".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        assertThrows(BadArgumentsException.class, argParser::parse);
    }
    @Test
    void delimiterTest() {
        String[] args = "--from=bits --to=bits --delimiter=,".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals( ',', options.getDelimiter());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void inputFileTest() {
        File file = getTestFile();

        String[] args = ("--from=bits --to=bits --input="+ file.getAbsolutePath()).split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            argParser.parse();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        file.deleteOnExit();
    }

    @Test
    void outputFileTest() {
        File file = getTestFile();

        String[] args = ("--from=bits --to=bits --output=" + file.getAbsolutePath()).split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            argParser.parse();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        file.deleteOnExit();
    }

    private File getTestFile() {
        try {
            return File.createTempFile("panbyte-test", ".txt");
        } catch (IOException e) {
            fail(e.getMessage());
        }

        throw new RuntimeException("Could not create test file");
    }
}
