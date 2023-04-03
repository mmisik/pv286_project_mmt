package cz.muni.fi.pv286.mmt.arguments;

import static cz.muni.fi.pv286.mmt.model.Messages.CANT_READ_FILE;
import static cz.muni.fi.pv286.mmt.model.Messages.CANT_WRITE_FILE;
import static cz.muni.fi.pv286.mmt.model.Messages.FILE_IS_DIRECTORY;
import static cz.muni.fi.pv286.mmt.model.Messages.FILE_SET_DUPLICITY;
import static cz.muni.fi.pv286.mmt.model.Messages.FORMAT_SET_DUPLICITY;
import static cz.muni.fi.pv286.mmt.model.Messages.MESSAGE_INVALID_ARGUMENTS;
import static cz.muni.fi.pv286.mmt.model.Messages.UNKNOWN_ARGUMENT;
import static cz.muni.fi.pv286.mmt.model.Messages.UNKNOWN_FORMAT;
import static cz.muni.fi.pv286.mmt.model.Messages.UNKNOWN_OPTION;

import cz.muni.fi.pv286.mmt.exceptions.BadArgumentsException;
import cz.muni.fi.pv286.mmt.exceptions.HelpInvokedException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidInputException;
import cz.muni.fi.pv286.mmt.model.ArgMatch;
import cz.muni.fi.pv286.mmt.model.FromOrTo;
import cz.muni.fi.pv286.mmt.model.FromToOption;
import cz.muni.fi.pv286.mmt.model.IoFormat;
import cz.muni.fi.pv286.mmt.model.Options;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Arguments parser.
 */
public class ArgParser {

    private final String[] args;
    private boolean hasFromFormat = false;
    private boolean hasToFormat = false;
    private boolean hasInputFile = false;
    private boolean hasOutputFile = false;
    private boolean hasDelimiter = false;

    public ArgParser(String[] args) {
        this.args = args;
    }

    private static IoFormat matchFormat(String format) throws BadArgumentsException {
        if (Patterns.bits.matcher(format).matches()) {
            return IoFormat.Bits;
        }
        if (Patterns.hex.matcher(format).matches()) {
            return IoFormat.Hex;
        }
        if (Patterns.integer.matcher(format).matches()) {
            return IoFormat.Int;
        }
        if (Patterns.bytes.matcher(format).matches()) {
            return IoFormat.Bytes;
        }
        if (Patterns.array.matcher(format).matches()) {
            return IoFormat.Array;
        }
        throw new BadArgumentsException(UNKNOWN_FORMAT);
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
        throw new BadArgumentsException(UNKNOWN_OPTION);
    }

    private static String getNextOption(String[] options, int index) throws BadArgumentsException {
        if (index + 1 >= options.length) {
            throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
        }
        return options[index + 1];
    }

    private static String extractOption(String option)
            throws IllegalArgumentException, IndexOutOfBoundsException {
        Matcher m = Patterns.extractor.matcher(option);
        if (m.find()) {
            return m.group(1);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static void checkHasInputFormat(Options options)
            throws BadArgumentsException {
        if (options.getInputFormat() == null || options.getOutputFormat() == null) {
            throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
        }
    }

    private static void checkHasInputFromToOptionAndIsRightFormat(Options options)
            throws BadArgumentsException {
        if (options.getInputFromToOption().isPresent()
                && (options.getInputFormat() != IoFormat.Int
                && options.getInputFormat() != IoFormat.Bits)) {
            throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
        }
    }

    private static void checkHasOutputFromToOptionAndIsRightFormat(Options options)
            throws BadArgumentsException {
        if (options.getOutputFromToOption().isPresent()
                && (options.getOutputFormat() != IoFormat.Int
                && options.getOutputFormat() != IoFormat.Array)) {
            throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
        }
    }

    private static void checkIsArrayIfHasBracketFormat(Options options)
            throws BadArgumentsException {
        if (options.getBracketType().isPresent() && options.getOutputFormat() != IoFormat.Array) {
            throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
        }
    }

    private static void checkExistingIntInputFromToOptionIsBigOrLittle(Options options)
            throws BadArgumentsException {
        if (options.getInputFormat() != IoFormat.Int) {
            return;
        }
        Optional<FromToOption> fromToOption = options.getInputFromToOption();

        if (fromToOption.isPresent()) {
            FromToOption option = fromToOption.get();
            if (option != FromToOption.Big && option != FromToOption.Little) {
                throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
            }
        }
    }

    private static void checkExistingIntOutputFromToOptionIsBigOrLittle(Options options)
            throws BadArgumentsException {
        if (options.getOutputFormat() != IoFormat.Int) {
            return;
        }
        Optional<FromToOption> fromToOption = options.getOutputFromToOption();
        if (fromToOption.isPresent()) {
            FromToOption option = fromToOption.get();
            if (option != FromToOption.Big && option != FromToOption.Little) {
                throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
            }
        }
    }

    private static void checkExistingBitsInputFromToOptionIsLeftOfRight(Options options)
            throws BadArgumentsException {
        if (options.getInputFormat() != IoFormat.Bits) {
            return;
        }

        Optional<FromToOption> fromToOption = options.getInputFromToOption();

        if (fromToOption.isPresent()) {
            FromToOption option = fromToOption.get();
            if (option != FromToOption.Left && option != FromToOption.Right) {
                throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
            }
        }
    }

    private static void checkExistingArrayOutputFromToOptionsHexToBinary(Options options)
            throws BadArgumentsException {
        if (options.getOutputFormat() != IoFormat.Array) {
            return;
        }
        Optional<FromToOption> fromToOption = options.getOutputFromToOption();
        if (fromToOption.isPresent()) {
            FromToOption option = fromToOption.get();
            if (option != FromToOption.Hex
                    && option != FromToOption.Binary
                    && option != FromToOption.Decimal
                    && option != FromToOption.Character) {
                throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
            }
        }

    }

    private static void validateOptions(Options options)
            throws BadArgumentsException {
        checkHasInputFormat(options);
        checkHasInputFromToOptionAndIsRightFormat(options);
        checkHasOutputFromToOptionAndIsRightFormat(options);
        checkIsArrayIfHasBracketFormat(options);
        checkExistingIntInputFromToOptionIsBigOrLittle(options);
        checkExistingIntOutputFromToOptionIsBigOrLittle(options);
        checkExistingBitsInputFromToOptionIsLeftOfRight(options);
        checkExistingArrayOutputFromToOptionsHexToBinary(options);
    }

    /**
     * Prints help.
     */
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

    private void cleanFlags() {
        hasFromFormat = false;
        hasToFormat = false;
        hasInputFile = false;
        hasOutputFile = false;
        hasDelimiter = false;
    }

    /**
     * Parses input arguments using Regex.
     */
    public Options parse()
            throws FileNotFoundException,
            BadArgumentsException,
            InvalidInputException,
            HelpInvokedException {
        Options options = new Options();
        boolean skipNext = false;
        cleanFlags();
        checkForHelp(args);
        for (int i = 0; i < args.length; i++) {
            if (skipNext) {
                skipNext = false;
                continue;
            }
            String arg = args[i];
            ArgMatch argMatch = getArgMatch(arg);
            switch (argMatch) {
                case ShortFromFormat -> {
                    IoFormat format = matchFormat(getNextOption(args, i));
                    setFormat(options, format, FromOrTo.From);
                    skipNext = true;
                }
                case LongFromFormat -> {
                    IoFormat format = matchFormat(extractOption(arg));
                    setFormat(options, format, FromOrTo.From);
                }
                case FromOption -> {
                    FromToOption fromToOption = matchOption(extractOption(arg));
                    setOption(options, fromToOption, FromOrTo.From);
                }
                case ShortToFormat -> {
                    IoFormat format = matchFormat(getNextOption(args, i));
                    setFormat(options, format, FromOrTo.To);
                    skipNext = true;
                }
                case LongToFormat -> {
                    IoFormat format = matchFormat(extractOption(arg));
                    setFormat(options, format, FromOrTo.To);
                }
                case ToOption -> {
                    FromToOption fromToOption = matchOption(extractOption(arg));
                    setOption(options, fromToOption, FromOrTo.To);
                }
                case ShortFromFile -> {
                    String path = getNextOption(args, i);
                    setFile(options, path, FromOrTo.From);
                    skipNext = true;

                }
                case LongFromFile -> {
                    String path = extractOption(arg);
                    setFile(options, path, FromOrTo.From);
                }
                case ShortToFile -> {
                    String path = getNextOption(args, i);
                    setFile(options, path, FromOrTo.To);
                    skipNext = true;

                }
                case LongToFile -> {
                    String path = extractOption(arg);
                    setFile(options, path, FromOrTo.To);

                }
                case ShortDelimiter -> {
                    String delimiter = getNextOption(args, i);
                    setDelimiter(options, delimiter);
                    skipNext = true;

                }
                case LongDelimiter -> {
                    String delimiter = extractOption(arg);
                    setDelimiter(options, delimiter);
                }
                default -> throw new BadArgumentsException(UNKNOWN_ARGUMENT);
            }
        }
        validateOptions(options);

        return options;
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
        throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
    }

    private void setOption(Options options, FromToOption fromToOption, FromOrTo fromOrTo)
            throws BadArgumentsException {
        if (FromOrTo.From == fromOrTo) {
            options.setInputFromToOption(fromToOption);
        } else if (FromOrTo.To == fromOrTo) {
            options.setOutputFromToOption(fromToOption);
        } else {
            throw new BadArgumentsException(UNKNOWN_OPTION);
        }
    }

    private void setFormat(Options options, IoFormat format, FromOrTo fromOrTo)
            throws BadArgumentsException {
        if (this.hasFromFormat && (fromOrTo == FromOrTo.From)
                || this.hasToFormat && (fromOrTo == FromOrTo.To)) {
            throw new BadArgumentsException(FORMAT_SET_DUPLICITY);
        }
        if (FromOrTo.From == fromOrTo) {
            this.hasFromFormat = true;
            options.setInputFormat(format);
        } else if (FromOrTo.To == fromOrTo) {
            this.hasToFormat = true;
            options.setOutputFormat(format);
        }
    }

    private void setFile(Options options, String path, FromOrTo fromOrTo)
            throws BadArgumentsException, FileNotFoundException, InvalidInputException {
        if (this.hasInputFile && (fromOrTo == FromOrTo.From)
                || this.hasOutputFile && (fromOrTo == FromOrTo.To)) {
            throw new BadArgumentsException(FILE_SET_DUPLICITY);
        }
        File file = new File(path);
        if (file.isDirectory()) {
            throw new BadArgumentsException(FILE_IS_DIRECTORY);
        }
        if (!file.canWrite() && fromOrTo == FromOrTo.To) {
            throw new InvalidInputException(CANT_WRITE_FILE);
        }
        if (!file.canRead() && fromOrTo == FromOrTo.From) {
            throw new InvalidInputException(CANT_READ_FILE);
        }
        if (FromOrTo.From == fromOrTo) {
            this.hasInputFile = true;
            options.setInputFile(new FileInputStream(file));
        } else if (FromOrTo.To == fromOrTo) {
            this.hasOutputFile = true;
            options.setOutputFile(new FileOutputStream(file));
        }
    }

    private void setDelimiter(Options options, String delimiter)
            throws BadArgumentsException {
        if (this.hasDelimiter) {
            throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
        }
        this.hasDelimiter = true;
        options.setDelimiter(delimiter.charAt(0));
    }

    private void checkForHelp(String[] args)
            throws HelpInvokedException {
        for (String arg : args) {
            if (Patterns.shortHelp.matcher(arg).matches()
                    || Patterns.longHelp.matcher(arg).matches()) {
                throw new HelpInvokedException();
            }
        }
    }

    /**
     * Contains Regex patterns.
     */
    public static final class Patterns {
        static Pattern shortFromFormat = Pattern.compile("^-f$");
        static Pattern longFromFormat = Pattern.compile("^--from=\\w+$");
        static Pattern fromOption = Pattern.compile("^--from-options=\\w+$");
        static Pattern shortToFormat = Pattern.compile("^-t$");
        static Pattern longToFormat = Pattern.compile("^--to=\\w+$");
        static Pattern toOption = Pattern.compile("^--to-options=.*$");
        static Pattern shortInputFile = Pattern.compile("^-i$");
        static Pattern longInputFile = Pattern.compile("^--input=.*$");
        static Pattern shortOutputFile = Pattern.compile("^-o$");
        static Pattern longOutputFile = Pattern.compile("^--output=.*$");
        static Pattern shortDelimiter = Pattern.compile("^-d$");
        static Pattern longDelimiter = Pattern.compile("^--delimiter=.");
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

        private Patterns() {
        }

    }
}
