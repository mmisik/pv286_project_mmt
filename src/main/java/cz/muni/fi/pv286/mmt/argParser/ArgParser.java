package cz.muni.fi.pv286.mmt.argParser;

import cz.muni.fi.pv286.mmt.exceptions.BadArgumentsException;
import cz.muni.fi.pv286.mmt.exceptions.HelpInvokedException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidInputException;
import cz.muni.fi.pv286.mmt.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArgParser {

    private boolean hasFromFormat = false;
    private boolean hasToFormat = false;
    private boolean hasInputFile = false;
    private boolean hasOutputFile = false;
    private boolean hasDelimiter = false;

    private String[] args;

    public static final class Patterns {
        static Pattern shortFromFormat = Pattern.compile("^-f$");
        static Pattern longFromFormat = Pattern.compile("^--from=\\w+$");
        //static Pattern longFromFormatExtract = Pattern.compile("^--from=(.?)$");
        static Pattern fromOption = Pattern.compile("^--from-options=\\w+$");
        //static Pattern longFromOptionExtract = Pattern.compile("^--from-options=(.?)$");
        static Pattern shortToFormat = Pattern.compile("^-t$");
        static Pattern longToFormat = Pattern.compile("^--to=\\w+$");
        //static Pattern longToFormatExtract = Pattern.compile("^--to=(.?)$");
        static Pattern toOption = Pattern.compile("^--to-options=.*$");
        //static Pattern longToOptionExtract = Pattern.compile("^--to-options=(.?)$");
        static Pattern shortInputFile = Pattern.compile("^-i$");
        static Pattern longInputFile = Pattern.compile("^--input=.*$");
        //static Pattern longInputFileExtract = Pattern.compile("^--input=(.?)$");
        static Pattern shortOutputFile = Pattern.compile("^-o$");
        static Pattern longOutputFile = Pattern.compile("^--output=.*$");
        //static Pattern longOutputFileExtract = Pattern.compile("^--output=(.?)$");
        static Pattern shortDelimiter = Pattern.compile("^-d$");
        static Pattern longDelimiter = Pattern.compile("^--delimiter=.");
        //static Pattern longDelimiterExtract = Pattern.compile("^--delimiter=(.)$");
        static Pattern shortHelp = Pattern.compile("^-h$");
        static Pattern longHelp = Pattern.compile("^--help$");
        static Pattern bytes = Pattern.compile("^bytes$");
        static Pattern hex = Pattern.compile("^hex$");
        static Pattern integer = Pattern.compile("^int$");
        static Pattern bits = Pattern.compile("^bits$");
        static Pattern array = Pattern.compile("^array$");
        static Pattern big = Pattern.compile("^big$");
        static Pattern little = Pattern.compile("^little$");
        static Pattern left = Pattern.compile("^left$");
        static Pattern right = Pattern.compile("^right$");
        static Pattern extractor = Pattern.compile("-{1,2}\\w+=(.*)");
        static Pattern curlyBracket = Pattern.compile("^\"[{}]\"$");
        static Pattern squareBracket = Pattern.compile("^\"[\\[\\]]\"$");
        static Pattern regularBracket = Pattern.compile("^\"[()]\"$");
        static Pattern hexOption = Pattern.compile("^0x$");
        static Pattern decimalOption = Pattern.compile("^0$");
        static Pattern charOption = Pattern.compile("^a$");
        static Pattern binaryOption = Pattern.compile("^0b$");

    }

    public void printHelp() {
        System.out.println("""
                ./panbyte [ARGS...]
                ARGS:
                    -f FORMAT --from=FORMAT Set input data format
                    --from-options=OPTIONS Set input options
                    -t FORMAT --to=FORMAT Set output data format
                    --to-options=OPTIONS Set output options
                    -i FILE --input=FILE Set input file (default stdin)
                    -o FILE --output=FILE Set output file (default stdout)
                    -d DELIMITER --delimiter=DELIMITER Record delimiter (default newline)
                    -h --help Print help
                FORMATS:
                    bytes Raw bytes
                    hex Hex-encoded string
                    int Integer
                    bits 0,1-represented bits
                    array Byte array
                """);
    }

    public ArgParser(String[] args) {
        this.args = args;
    }

    private void cleanFlags() {
        hasFromFormat = false;
        hasToFormat = false;
        hasInputFile = false;
        hasOutputFile = false;
        hasDelimiter = false;
    }

    public Options parse() throws FileNotFoundException, BadArgumentsException, InvalidInputException, HelpInvokedException {
        Options options = new Options();
        cleanFlags();
        checkForHelp(args);
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            ArgMatch argMatch = getArgMatch(arg);
            switch (argMatch) {
                case ShortFromFormat -> {
                    IOFormat format = matchFormat(getNextOption(args, i));
                    setFormat(options, format, FromOrTo.From);
                    i++;
                }
                case LongFromFormat -> {
                    IOFormat format = matchFormat(extractOption(arg));
                    setFormat(options, format, FromOrTo.From);
                }
                case FromOption -> {
                    FromToOption fromToOption = matchOption(extractOption(arg));
                    setOption(options, fromToOption, FromOrTo.From);
                }
                case ShortToFormat -> {
                    IOFormat format = matchFormat(getNextOption(args, i));
                    setFormat(options, format, FromOrTo.To);
                    i++;
                }
                case LongToFormat -> {
                    IOFormat format = matchFormat(extractOption(arg));
                    setFormat(options, format, FromOrTo.To);
                }
                case ToOption -> {
                    FromToOption fromToOption = matchOption(extractOption(arg));
                    setOption(options, fromToOption, FromOrTo.To);
                }
                case ShortFromFile -> {
                    String path = getNextOption(args, i);
                    setFile(options, path, FromOrTo.From);
                    i++;

                }
                case LongFromFile -> {
                    String path = extractOption(arg);
                    setFile(options, path, FromOrTo.From);
                }
                case ShortToFile -> {
                    String path = getNextOption(args, i);
                    setFile(options, path, FromOrTo.To);
                    i++;

                }
                case LongToFile -> {
                    String path = extractOption(arg);
                    setFile(options, path, FromOrTo.To);

                }
                case ShortDelimiter -> {
                    String delimiter = getNextOption(args, i);
                    setDelimiter(options, delimiter);
                    i++;

                }
                case LongDelimiter -> {
                    String delimiter = extractOption(arg);
                    setDelimiter(options, delimiter);
                }
                default -> throw new BadArgumentsException("Unknown argument");
            }
        }
        validateOptions(options);

        return options;
    }

    private static IOFormat matchFormat(String format) throws BadArgumentsException {
        if (Patterns.bits.matcher(format).matches()) {
            return IOFormat.Bits;
        }
        if (Patterns.hex.matcher(format).matches()) {
            return IOFormat.Hex;
        }
        if (Patterns.integer.matcher(format).matches()) {
            return IOFormat.Int;
        }
        if (Patterns.bytes.matcher(format).matches()) {
            return IOFormat.Bytes;
        }
        if (Patterns.array.matcher(format).matches()) {
            return IOFormat.Array;
        }
        throw new BadArgumentsException("Invalid format");
    }

    private ArgMatch getArgMatch(String str) throws BadArgumentsException {
        if (Patterns.shortFromFormat.matcher(str).matches()) {
            return ArgMatch.ShortFromFormat;
        }
        if (Patterns.longFromFormat.matcher(str).matches()) {
            return ArgMatch.LongFromFormat;
        }
        if (Patterns.fromOption.matcher(str).matches()) {
            return ArgMatch.FromOption;
        }
        if (Patterns.shortToFormat.matcher(str).matches()) {
            return ArgMatch.ShortToFormat;
        }
        if (Patterns.longToFormat.matcher(str).matches()) {
            return ArgMatch.LongToFormat;
        }
        if (Patterns.toOption.matcher(str).matches()) {
            return ArgMatch.ToOption;
        }
        if (Patterns.shortInputFile.matcher(str).matches()) {
            return ArgMatch.ShortFromFile;
        }
        if (Patterns.longInputFile.matcher(str).matches()) {
            return ArgMatch.LongFromFile;
        }
        if (Patterns.shortOutputFile.matcher(str).matches()) {
            return ArgMatch.ShortToFile;
        }
        if (Patterns.longOutputFile.matcher(str).matches()) {
            return ArgMatch.LongToFile;
        }
        if (Patterns.shortDelimiter.matcher(str).matches()) {
            return ArgMatch.ShortDelimiter;
        }
        if (Patterns.longDelimiter.matcher(str).matches()) {
            return ArgMatch.LongDelimiter;
        }
        if (Patterns.shortHelp.matcher(str).matches()) {
            return ArgMatch.Help;
        }
        if (Patterns.longHelp.matcher(str).matches()) {
            return ArgMatch.Help;
        }
        throw new BadArgumentsException("Invalid argument provided");
    }

    private static FromToOption matchOption(String option) throws BadArgumentsException {
        if (Patterns.big.matcher(option).matches()) {
            return FromToOption.Big;
        }
        if (Patterns.little.matcher(option).matches()) {
            return FromToOption.Little;
        }
        if (Patterns.left.matcher(option).matches()) {
            return FromToOption.Left;
        }
        if (Patterns.right.matcher(option).matches()) {
            return FromToOption.Right;
        }
        if (Patterns.curlyBracket.matcher(option).matches()) {
            return FromToOption.CurlyBracket;
        }
        if (Patterns.squareBracket.matcher(option).matches()) {
            return FromToOption.SquareBracket;
        }
        if (Patterns.regularBracket.matcher(option).matches()) {
            return FromToOption.RegularBracket;
        }
        if (Patterns.hexOption.matcher(option).matches()) {
            return FromToOption.Hex;
        }
        if (Patterns.decimalOption.matcher(option).matches()) {
            return FromToOption.Decimal;
        }
        if (Patterns.charOption.matcher(option).matches()) {
            return FromToOption.Character;
        }
        if (Patterns.binaryOption.matcher(option).matches()) {
            return FromToOption.Binary;
        }
        throw new BadArgumentsException("Unknown option provided");
    }


    private static String getNextOption(String[] options, int index) throws BadArgumentsException {
        if (index + 1 >= options.length) {
            throw new BadArgumentsException("Not enough arguments provided");
        }
        return options[index + 1];
    }

    private static String extractOption(String option) throws IllegalArgumentException, IndexOutOfBoundsException {
        Matcher m = Patterns.extractor.matcher(option);
        if (m.find()) {
            return m.group(1);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void setOption(Options options, FromToOption fromToOption, FromOrTo fromOrTo) throws BadArgumentsException {
        if (FromOrTo.From == fromOrTo) {
            options.setInputFromToOption(fromToOption);
        } else if (FromOrTo.To == fromOrTo) {
            options.setOutputFromToOption(fromToOption);
        }
    }

    private void setFormat(Options options, IOFormat format, FromOrTo fromOrTo) throws BadArgumentsException {
        if (this.hasFromFormat && (fromOrTo == FromOrTo.From) || this.hasToFormat && (fromOrTo == FromOrTo.To)) {
            throw new BadArgumentsException("Format set duplicity");
        }
        if (FromOrTo.From == fromOrTo) {
            this.hasFromFormat = true;
            options.setInputFormat(format);
        } else if (FromOrTo.To == fromOrTo) {
            this.hasToFormat = true;
            options.setOutputFormat(format);
        }
    }


    private void setFile(Options options, String path, FromOrTo fromOrTo) throws BadArgumentsException, FileNotFoundException, InvalidInputException {
        if (this.hasInputFile && (fromOrTo == FromOrTo.From) || this.hasOutputFile && (fromOrTo == FromOrTo.To)) {
            throw new BadArgumentsException("Already has file");
        }
        File file = new File(path);
        if (file.isDirectory()) {
            throw new BadArgumentsException("File is directory");
        }
        if (!file.canWrite() && fromOrTo == FromOrTo.To) {
            throw new InvalidInputException("Can't write in file");
        }
        if (!file.canRead() && fromOrTo == FromOrTo.From) {
            throw new InvalidInputException("Can't read from file");
        }
        if (FromOrTo.From == fromOrTo) {
            this.hasInputFile = true;
            options.setInputFile(new FileInputStream(file));
        } else if (FromOrTo.To == fromOrTo) {
            this.hasOutputFile = true;
            options.setOutputFile(new FileOutputStream(file));
        }
    }

    private void setDelimiter(Options options, String delimiter) throws BadArgumentsException {
        if (this.hasDelimiter) {
            throw new BadArgumentsException("Invalid program arguments");
        }
        this.hasDelimiter = true;
        options.setDelimiter(delimiter.charAt(0)); //TODO maybe set delimiter to string
    }

    private void checkForHelp(String[] args) throws HelpInvokedException {
        for (String arg : args) {
            if (Patterns.shortHelp.matcher(arg).matches() || Patterns.longHelp.matcher(arg).matches()) {
                throw new HelpInvokedException();
            }
        }
    }

    private static void validateOptions(Options options) throws BadArgumentsException {
        if (options.getInputFormat() == null || options.getOutputFormat() == null) {
            throw new BadArgumentsException("Invalid program arguments");
        }
        if (options.getInputFromToOption().isPresent() &&
                (options.getInputFormat() != IOFormat.Int
                        && options.getInputFormat() != IOFormat.Bits
                )) {
            throw new BadArgumentsException("Invalid program arguments");
        }
        if (options.getOutputFromToOption().isPresent() &&
                (options.getOutputFormat() != IOFormat.Int
                        && options.getOutputFormat() != IOFormat.Array
                )) {
            throw new BadArgumentsException("Invalid program arguments");
        }
        if (options.getBracketType().isPresent() && options.getOutputFormat() != IOFormat.Array) {
            throw new BadArgumentsException("Invalid program arguments");
        }

        if (options.getInputFromToOption().isPresent() &&
                (options.getInputFormat() == IOFormat.Int
                        && (options.getInputFromToOption().get().ordinal() < FromToOption.Big.ordinal()
                        || options.getInputFromToOption().get().ordinal() > FromToOption.Little.ordinal()))) {
            throw new BadArgumentsException("Invalid program arguments");
        }
        if (options.getInputFromToOption().isPresent() &&
                (options.getInputFormat() == IOFormat.Int
                        && (options.getInputFromToOption().get().ordinal() < FromToOption.Big.ordinal()
                        || options.getInputFromToOption().get().ordinal() > FromToOption.Little.ordinal()))) {
            throw new BadArgumentsException("Invalid program arguments");
        }
        if (options.getInputFromToOption().isPresent() &&
                (options.getInputFormat() == IOFormat.Bits
                        && (options.getInputFromToOption().get().ordinal() < FromToOption.Left.ordinal()
                        || options.getInputFromToOption().get().ordinal() > FromToOption.Right.ordinal()))) {
            throw new BadArgumentsException("Invalid program arguments");
        }

        if (options.getOutputFromToOption().isPresent() &&
                (options.getOutputFormat() == IOFormat.Array
                        && (options.getOutputFromToOption().get().ordinal() < FromToOption.Hex.ordinal()
                        || options.getOutputFromToOption().get().ordinal() > FromToOption.Binary.ordinal()))) {
            throw new BadArgumentsException("Invalid program arguments");
        }
    }
}
