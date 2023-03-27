package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.argParser.ArgParser;
import cz.muni.fi.pv286.mmt.exceptions.BadArgumentsException;
import cz.muni.fi.pv286.mmt.exceptions.HelpInvokedException;
import cz.muni.fi.pv286.mmt.model.BracketType;
import cz.muni.fi.pv286.mmt.model.FromToOption;
import cz.muni.fi.pv286.mmt.model.IOFormat;
import cz.muni.fi.pv286.mmt.model.Options;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class ArgParserTest {
    @Test
    public void shortBytesTest() {
        String[] args = "-f bytes -t bytes".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(options.getInputFormat(), IOFormat.Bytes);
            assertEquals(options.getOutputFormat(), IOFormat.Bytes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void longBytesTest() {
        String[] args = "--from=bytes --to=bytes".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(options.getInputFormat(), IOFormat.Bytes);
            assertEquals(options.getOutputFormat(), IOFormat.Bytes);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    @Test
    public void hexInputIntOutputTestMixed() {
        String[] args = "-f hex --to=int".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(options.getInputFormat(), IOFormat.Hex);
            assertEquals(options.getOutputFormat(), IOFormat.Int);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void bitsInputArrayOutputTestMixed() {
        String[] args = "--from=bits -t array".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(options.getInputFormat(), IOFormat.Bits);
            assertEquals(options.getOutputFormat(), IOFormat.Array);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testArrayOptions() {
        String[] args = "--from=array --to=array --to-options=0 --to-options=\"{\"".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(options.getInputFormat(), IOFormat.Array);
            assertEquals(options.getOutputFormat(), IOFormat.Array);
            assertEquals(options.getOutputFromToOption().get(), FromToOption.Decimal);
            assertEquals(options.getBracketType().get(), BracketType.CurlyBracket);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void defaultArrayOptionsTest() {
        String[] args = "--from=array --to=array".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(options.getInputFormat(), IOFormat.Array);
            assertEquals(options.getOutputFormat(), IOFormat.Array);
            assertEquals(options.getOutputFromToOption().get(), FromToOption.Hex);
            assertEquals(options.getBracketType().get(), BracketType.CurlyBracket);
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
            assertEquals(options.getInputFormat(), IOFormat.Int);
            assertEquals(options.getOutputFormat(), IOFormat.Int);
            assertEquals(options.getInputFromToOption().get(), FromToOption.Big);
            assertEquals(options.getOutputFromToOption().get(), FromToOption.Big);
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
            assertEquals(options.getInputFormat(), IOFormat.Int);
            assertEquals(options.getOutputFormat(), IOFormat.Int);
            assertEquals(options.getInputFromToOption().get(), FromToOption.Little);
            assertEquals(options.getOutputFromToOption().get(), FromToOption.Little);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void bitsDefaultOptionsTest() {
        String[] args = "--from=bits --to=bits".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            Options options = argParser.parse();
            assertEquals(options.getInputFormat(), IOFormat.Bits);
            assertEquals(options.getOutputFormat(), IOFormat.Bits);
            assertTrue(options.getOutputFromToOption().isEmpty());
            assertEquals(options.getInputFromToOption().get(), FromToOption.Left);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }


    @Test
    public void failDoubleSetTest() {
        String[] args = "-f bytes -t bytes --from-options=big".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        assertThrows(BadArgumentsException.class, () -> argParser.parse());
    }

    @Test
    public void invokeHelpShortTest() {
        String[] args = "--from=bits --to=bits --help".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        assertThrows(HelpInvokedException.class, () -> argParser.parse());
    }

    @Test
    public void invokeHelpLongTest() {
        String[] args = "-h".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        assertThrows(HelpInvokedException.class, () -> argParser.parse());
    }

    @Test
    public void failNoInputFormatGivenTest() {
        String[] args = "-t bytes".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        assertThrows(BadArgumentsException.class, () -> argParser.parse());
    }

    @Test
    public void failNoOutputFormatGivenTest() {
        String[] args = "-f bytes".split("\\s+");
        ArgParser argParser = new ArgParser(args);
        assertThrows(BadArgumentsException.class, () -> argParser.parse());
    }
    @Test
    public void delimiterTest() {
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
    public void inputFileTest() {
        File file = getTestFile();
        try {
            file.createNewFile();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        String[] args = ("--from=bits --to=bits --input="+ file.getAbsolutePath()).split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            argParser.parse();
        } catch (Exception e) {
            fail(e.getMessage());
        }
        file.delete();
    }

    @Test
    public void outputFileTest() {
        File file = getTestFile();
        try {
            file.createNewFile();
        } catch (Exception e) {
            fail(e.getMessage());
        }

        String[] args = ("--from=bits --to=bits --output=" + file.getAbsolutePath()).split("\\s+");
        ArgParser argParser = new ArgParser(args);
        try {
            argParser.parse();
        } catch (Exception e) {
            fail(e.getMessage());
        }
        file.delete();
    }

    private File getTestFile() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        String tmpdir = System.getProperty("java.io.tmpdir");
        return new File(tmpdir + "/" + generatedString + ".txt");
    }
}
