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
import cz.muni.fi.pv286.mmt.model.IoOption;
import cz.muni.fi.pv286.mmt.model.Options;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
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
        this.args = args == null ? null : Arrays.copyOf(args, args.length);
    }

    private static IoFormat matchFormat(String format) throws BadArgumentsException {
        if (Patterns.bits.matcher(format).matches()) {
            return IoFormat.BITS;
        }
        if (Patterns.hex.matcher(format).matches()) {
            return IoFormat.HEX;
        }
        if (Patterns.integer.matcher(format).matches()) {
            return IoFormat.INT;
        }
        if (Patterns.bytes.matcher(format).matches()) {
            return IoFormat.BYTES;
        }
        if (Patterns.array.matcher(format).matches()) {
            return IoFormat.ARRAY;
        }
        throw new BadArgumentsException(UNKNOWN_FORMAT);
    }

    private static FromToOption matchOption(String option) throws BadArgumentsException {
        if (Patterns.big.matcher(option).matches()) {
            return FromToOption.BIG;
        }
        if (Patterns.little.matcher(option).matches()) {
            return FromToOption.LITTLE;
        }
        if (Patterns.left.matcher(option).matches()) {
            return FromToOption.LEFT;
        }
        if (Patterns.right.matcher(option).matches()) {
            return FromToOption.RIGHT;
        }
        if (Patterns.curlyBracket.matcher(option).matches()) {
            return FromToOption.CURLY_BRACKET;
        }
        if (Patterns.squareBracket.matcher(option).matches()) {
            return FromToOption.SQUARE_BRACKET;
        }
        if (Patterns.regularBracket.matcher(option).matches()) {
            return FromToOption.REGULAR_BRACKET;
        }
        if (Patterns.hexOption.matcher(option).matches()) {
            return FromToOption.HEX;
        }
        if (Patterns.decimalOption.matcher(option).matches()) {
            return FromToOption.DECIMAL;
        }
        if (Patterns.charOption.matcher(option).matches()) {
            return FromToOption.CHARACTER;
        }
        if (Patterns.binaryOption.matcher(option).matches()) {
            return FromToOption.BINARY;
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
                && (options.getInputFormat() != IoFormat.INT
                && options.getInputFormat() != IoFormat.BITS)) {
            throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
        }
    }

    private static void checkHasOutputFromToOptionAndIsRightFormat(Options options)
            throws BadArgumentsException {
        if (options.getOutputFromToOption().isPresent()
                && (options.getOutputFormat() != IoFormat.INT
                && options.getOutputFormat() != IoFormat.ARRAY)) {
            throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
        }
    }

    private static void checkIsArrayIfHasBracketFormat(Options options)
            throws BadArgumentsException {
        if (options.getBracketType().isPresent() && options.getOutputFormat() != IoFormat.ARRAY) {
            throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
        }
    }

    private static void checkExistingIntInputFromToOptionIsBigOrLittle(Options options)
            throws BadArgumentsException {
        if (options.getInputFormat() != IoFormat.INT) {
            return;
        }
        Optional<IoOption> fromToOption = options.getInputFromToOption();

        if (fromToOption.isPresent()) {
            IoOption option = fromToOption.get();
            if (option != IoOption.BIG && option != IoOption.LITTLE) {
                throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
            }
        }
    }

    private static void checkExistingIntOutputFromToOptionIsBigOrLittle(Options options)
            throws BadArgumentsException {
        if (options.getOutputFormat() != IoFormat.INT) {
            return;
        }
        Optional<IoOption> fromToOption = options.getOutputFromToOption();
        if (fromToOption.isPresent()) {
            IoOption option = fromToOption.get();
            if (option != IoOption.BIG && option != IoOption.LITTLE) {
                throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
            }
        }
    }

    private static void checkExistingBitsInputFromToOptionIsLeftOfRight(Options options)
            throws BadArgumentsException {
        if (options.getInputFormat() != IoFormat.BITS) {
            return;
        }

        Optional<IoOption> fromToOption = options.getInputFromToOption();

        if (fromToOption.isPresent()) {
            IoOption option = fromToOption.get();
            if (option != IoOption.LEFT && option != IoOption.RIGHT) {
                throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
            }
        }
    }

    private static void checkExistingArrayOutputFromToOptionsHexToBinary(Options options)
            throws BadArgumentsException {
        if (options.getOutputFormat() != IoFormat.ARRAY) {
            return;
        }
        Optional<IoOption> fromToOption = options.getOutputFromToOption();
        if (fromToOption.isPresent()) {
            IoOption option = fromToOption.get();
            if (option != IoOption.HEX
                    && option != IoOption.BINARY
                    && option != IoOption.DECIMAL
                    && option != IoOption.CHARACTER) {
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
                case SHORT_FROM_FORMAT -> {
                    IoFormat format = matchFormat(getNextOption(args, i));
                    setFormat(options, format, FromOrTo.FROM);
                    skipNext = true;
                }
                case LONG_FROM_FORMAT -> {
                    IoFormat format = matchFormat(extractOption(arg));
                    setFormat(options, format, FromOrTo.FROM);
                }
                case FROM_OPTION -> {
                    FromToOption fromToOption = matchOption(extractOption(arg));
                    setOption(options, fromToOption, FromOrTo.FROM);
                }
                case SHORT_TO_FORMAT -> {
                    IoFormat format = matchFormat(getNextOption(args, i));
                    setFormat(options, format, FromOrTo.TO);
                    skipNext = true;
                }
                case LONG_TO_FORMAT -> {
                    IoFormat format = matchFormat(extractOption(arg));
                    setFormat(options, format, FromOrTo.TO);
                }
                case TO_OPTION -> {
                    FromToOption fromToOption = matchOption(extractOption(arg));
                    setOption(options, fromToOption, FromOrTo.TO);
                }
                case SHORT_FROM_FILE -> {
                    String path = getNextOption(args, i);
                    setFile(options, path, FromOrTo.FROM);
                    skipNext = true;

                }
                case LONG_FROM_FILE -> {
                    String path = extractOption(arg);
                    setFile(options, path, FromOrTo.FROM);
                }
                case SHORT_TO_FILE -> {
                    String path = getNextOption(args, i);
                    setFile(options, path, FromOrTo.TO);
                    skipNext = true;

                }
                case LONG_TO_FILE -> {
                    String path = extractOption(arg);
                    setFile(options, path, FromOrTo.TO);

                }
                case SHORT_DELIMITER -> {
                    String delimiter = getNextOption(args, i);
                    setDelimiter(options, delimiter);
                    skipNext = true;

                }
                case LONG_DELIMITER -> {
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
            return ArgMatch.SHORT_FROM_FORMAT;
        }
        if (Patterns.longFromFormat.matcher(str).matches()) {
            return ArgMatch.LONG_FROM_FORMAT;
        }
        if (Patterns.fromOption.matcher(str).matches()) {
            return ArgMatch.FROM_OPTION;
        }
        if (Patterns.shortToFormat.matcher(str).matches()) {
            return ArgMatch.SHORT_TO_FORMAT;
        }
        if (Patterns.longToFormat.matcher(str).matches()) {
            return ArgMatch.LONG_TO_FORMAT;
        }
        if (Patterns.toOption.matcher(str).matches()) {
            return ArgMatch.TO_OPTION;
        }
        if (Patterns.shortInputFile.matcher(str).matches()) {
            return ArgMatch.SHORT_FROM_FILE;
        }
        if (Patterns.longInputFile.matcher(str).matches()) {
            return ArgMatch.LONG_FROM_FILE;
        }
        if (Patterns.shortOutputFile.matcher(str).matches()) {
            return ArgMatch.SHORT_TO_FILE;
        }
        if (Patterns.longOutputFile.matcher(str).matches()) {
            return ArgMatch.LONG_TO_FILE;
        }
        if (Patterns.shortDelimiter.matcher(str).matches()) {
            return ArgMatch.SHORT_DELIMITER;
        }
        if (Patterns.longDelimiter.matcher(str).matches()) {
            return ArgMatch.LONG_DELIMITER;
        }
        if (Patterns.shortHelp.matcher(str).matches()) {
            return ArgMatch.HELP;
        }
        if (Patterns.longHelp.matcher(str).matches()) {
            return ArgMatch.HELP;
        }
        throw new BadArgumentsException(MESSAGE_INVALID_ARGUMENTS);
    }

    private void setOption(Options options, FromToOption fromToOption, FromOrTo fromOrTo)
            throws BadArgumentsException {
        try {
            if (FromOrTo.FROM == fromOrTo) {
                options.setInputFromToOption(fromToOption);
            } else if (FromOrTo.TO == fromOrTo) {
                options.setOutputFromToOption(fromToOption);
            } else {
                throw new BadArgumentsException(UNKNOWN_OPTION);
            }
        } catch (IllegalArgumentException e) {
            throw new BadArgumentsException(UNKNOWN_OPTION);
        }

    }

    private void setFormat(Options options, IoFormat format, FromOrTo fromOrTo)
            throws BadArgumentsException {
        if (this.hasFromFormat && (fromOrTo == FromOrTo.FROM)
                || this.hasToFormat && (fromOrTo == FromOrTo.TO)) {
            throw new BadArgumentsException(FORMAT_SET_DUPLICITY);
        }
        if (FromOrTo.FROM == fromOrTo) {
            this.hasFromFormat = true;
            options.setInputFormat(format);
        } else if (FromOrTo.TO == fromOrTo) {
            this.hasToFormat = true;
            options.setOutputFormat(format);
        }
    }

    private void setFile(Options options, String path, FromOrTo fromOrTo)
            throws BadArgumentsException, FileNotFoundException, InvalidInputException {
        if (this.hasInputFile && (fromOrTo == FromOrTo.FROM)
                || this.hasOutputFile && (fromOrTo == FromOrTo.TO)) {
            throw new BadArgumentsException(FILE_SET_DUPLICITY);
        }
        File file = new File(path);
        if (file.isDirectory()) {
            throw new BadArgumentsException(FILE_IS_DIRECTORY);
        }
        if (!file.canWrite() && fromOrTo == FromOrTo.TO) {
            throw new InvalidInputException(CANT_WRITE_FILE);
        }
        if (!file.canRead() && fromOrTo == FromOrTo.FROM) {
            throw new InvalidInputException(CANT_READ_FILE);
        }
        if (FromOrTo.FROM == fromOrTo) {
            this.hasInputFile = true;
            options.setInputFile(new FileInputStream(file));
        } else if (FromOrTo.TO == fromOrTo) {
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
        options.setDelimiter(delimiter);
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
        static Pattern curlyBracket = Pattern.compile("^[{}]");
        static Pattern squareBracket = Pattern.compile("^[\\[\\]]");
        static Pattern regularBracket = Pattern.compile("^[()]");
        static Pattern hexOption = Pattern.compile("^0x$");
        static Pattern decimalOption = Pattern.compile("^0$");
        static Pattern charOption = Pattern.compile("^a$");
        static Pattern binaryOption = Pattern.compile("^0b$");

        private Patterns() {
        }

    }
}
