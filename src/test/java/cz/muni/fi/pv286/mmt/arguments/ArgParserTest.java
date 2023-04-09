package cz.muni.fi.pv286.mmt.arguments;

import cz.muni.fi.pv286.mmt.exceptions.BadArgumentsException;
import cz.muni.fi.pv286.mmt.exceptions.HelpInvokedException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidInputException;
import cz.muni.fi.pv286.mmt.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.io.FileNotFoundException;
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
            assertEquals(IoFormat.BYTES, options.getInputFormat());
            assertEquals(IoFormat.BYTES, options.getOutputFormat());
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
            assertEquals(IoFormat.BYTES, options.getInputFormat());
            assertEquals(IoFormat.BYTES, options.getOutputFormat());
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
            assertEquals(IoFormat.HEX, options.getInputFormat());
            assertEquals(IoFormat.INT, options.getOutputFormat());
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
            assertEquals(IoFormat.BITS, options.getInputFormat());
            assertEquals(IoFormat.ARRAY, options.getOutputFormat());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void testArrayOptions() {
        String[] args = "--from=array --to=array --to-options=0 --to-options={".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(IoFormat.ARRAY, options.getInputFormat());
            assertEquals(IoFormat.ARRAY, options.getOutputFormat());
            assertEquals(IoOption.DECIMAL, options.getOutputFromToOption().orElseThrow());
            assertEquals(BracketType.CURLY_BRACKET, options.getBracketType().orElseThrow());
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
            assertEquals(IoFormat.ARRAY, options.getInputFormat());
            assertEquals(IoFormat.ARRAY, options.getOutputFormat());
            assertEquals(IoOption.HEX, options.getOutputFromToOption().orElseThrow());
            assertEquals(BracketType.CURLY_BRACKET, options.getBracketType().orElseThrow());
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
            assertEquals(IoFormat.INT, options.getInputFormat());
            assertEquals(IoFormat.INT, options.getOutputFormat());
            assertEquals(IoOption.BIG, options.getInputFromToOption().orElseThrow());
            assertEquals(IoOption.BIG, options.getOutputFromToOption().orElseThrow());
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
            assertEquals(IoFormat.INT, options.getInputFormat());
            assertEquals(IoFormat.INT, options.getOutputFormat());
            assertEquals(IoOption.LITTLE, options.getInputFromToOption().orElseThrow());
            assertEquals(IoOption.LITTLE, options.getOutputFromToOption().orElseThrow());
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
            assertEquals(IoFormat.BITS, options.getInputFormat());
            assertEquals(IoFormat.BITS, options.getOutputFormat());
            assertTrue(options.getOutputFromToOption().isEmpty());
            assertEquals(IoOption.LEFT, options.getInputFromToOption().orElseThrow());
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
            assertEquals( ",", options.getDelimiter());
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

    @Test
    void arraySetFormatAndBracketTypeTest() {
        String[] args = "--from=array --to=array --to-options=0 --to-options={".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(IoFormat.ARRAY, options.getInputFormat());
            assertEquals(IoFormat.ARRAY, options.getOutputFormat());
            assertEquals(IoOption.DECIMAL, options.getOutputFromToOption().orElseThrow());
            assertEquals(BracketType.CURLY_BRACKET, options.getBracketType().orElseThrow());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void arraySetBracketTypeTwiceTest() {
        String[] args = "--from=array --to=array --to-options=[ --to-options={".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(IoFormat.ARRAY, options.getInputFormat());
            assertEquals(IoFormat.ARRAY, options.getOutputFormat());
            assertEquals(IoOption.HEX, options.getOutputFromToOption().orElseThrow());
            assertEquals(BracketType.CURLY_BRACKET, options.getBracketType().orElseThrow());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    void arraySetFormatTwiceTest() {
        String[] args = "--from=array --to=array --to-options=0 --to-options=0x".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(IoFormat.ARRAY, options.getInputFormat());
            assertEquals(IoFormat.ARRAY, options.getOutputFormat());
            assertEquals(IoOption.HEX, options.getOutputFromToOption().orElseThrow());
            assertEquals(BracketType.CURLY_BRACKET, options.getBracketType().orElseThrow());
        } catch (Exception e) {
            fail(e.getMessage());
        }


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
