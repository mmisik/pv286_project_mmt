package cz.muni.fi.pv286.mmt.argParser;

import cz.muni.fi.pv286.mmt.exceptions.BadArgumentsException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidInputException;
import cz.muni.fi.pv286.mmt.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Optional;
import java.util.regex.Pattern;

public class ArgParser {

    private boolean hasFromFormat = false;
    private boolean hasToFormat = false;
    private boolean hasFromOption = false;
    private boolean hasToOption = false;
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
        static Pattern toOption = Pattern.compile("^--to-options=\\w+$");
        //static Pattern longToOptionExtract = Pattern.compile("^--to-options=(.?)$");
        static Pattern shortInputFile = Pattern.compile("^-i$");
        static Pattern longInputFile = Pattern.compile("^--input=\\w+$");
        //static Pattern longInputFileExtract = Pattern.compile("^--input=(.?)$");
        static Pattern shortOutputFile = Pattern.compile("^-o$");
        static Pattern longOutputFile = Pattern.compile("^--output=\\w+$");
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
        static Pattern extractor = Pattern.compile("-{1,2}\\w+=(.?)");
        static Pattern curlyBracket = Pattern.compile("^\"[{}]\"$");
        static Pattern squareBracket = Pattern.compile("^\"[\\[\\]]\"$");
        static Pattern regularBracket = Pattern.compile("^\"[()]\"$");
    }

    public static void PrintHelp() {
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
        hasFromOption = false;
        hasToOption = false;
        hasInputFile = false;
        hasOutputFile = false;
        hasDelimiter = false;
    }

    private Options parse() throws FileNotFoundException {
        Options options = new Options();
        cleanFlags();
        checkForHelp(args);
        try {
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
                    default -> throw new BadArgumentsException();
                }
            }
            validateOptions(options);
        } catch (BadArgumentsException e) {
            PrintHelp();
            System.exit(1);
        } catch (InvalidInputException e) {
            PrintHelp();
            System.exit(2);
        }
        return options;
    }

    private static IOFormat matchFormat(String format) throws InvalidInputException {
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
        throw new InvalidInputException();
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
        throw new BadArgumentsException();
    }

    private static FromToOption matchOption(String option) throws InvalidInputException {
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
        throw new InvalidInputException();
    }


    private static String getNextOption(String[] options, int index) throws InvalidInputException {
        if (index + 1 >= options.length) {
            throw new InvalidInputException();
        }
        return options[index + 1];
    }

    private static String extractOption(String option) throws IllegalArgumentException, IndexOutOfBoundsException {
        return Patterns.extractor.matcher(option).group(0);
    }

    private void setOption(Options options, FromToOption fromToOption, FromOrTo fromOrTo) throws BadArgumentsException {
        if (this.hasFromOption && (fromOrTo == FromOrTo.From) || this.hasToOption && (fromOrTo == FromOrTo.To)) {
            throw new BadArgumentsException();
        }
        if (FromOrTo.From == fromOrTo) {
            this.hasFromOption = true;
            options.setInputFromToOption(Optional.of(fromToOption));
        } else if (FromOrTo.To == fromOrTo) {
            this.hasToOption = true;
            options.setOutputFromToOption(Optional.of(fromToOption));
        }
    }

    private void setFormat(Options options, IOFormat format, FromOrTo fromOrTo) throws BadArgumentsException {
        if (this.hasFromFormat && (fromOrTo == FromOrTo.From) || this.hasToFormat && (fromOrTo == FromOrTo.To)) {
            throw new BadArgumentsException();
        }
        if (FromOrTo.From == fromOrTo) {
            this.hasFromFormat = true;
            options.setInputFormat(format);
        } else if (FromOrTo.To == fromOrTo) {
            this.hasToFormat = true;
            options.setOutputFormat(format);
        }
        setDefaults(options, format, fromOrTo);
    }

    private void setDefaults(Options options, IOFormat format, FromOrTo fromOrTo) {
        if (fromOrTo == FromOrTo.From && hasFromFormat) {
            return;
        }
        if (fromOrTo == FromOrTo.To && hasToFormat) {
            return;
        }
        if (format == IOFormat.Int) {
            if (fromOrTo == FromOrTo.From) {
                options.setInputFromToOption(Optional.of(FromToOption.Big));
            } else if (fromOrTo == FromOrTo.To) {
                options.setOutputFromToOption(Optional.of(FromToOption.Big));
            }
        }
        if (format == IOFormat.Bits) {
            if (fromOrTo == FromOrTo.From) {
                options.setInputFromToOption(Optional.of(FromToOption.Left));
            } else if (fromOrTo == FromOrTo.To) {
                options.setOutputFromToOption(Optional.of(FromToOption.Left));
            }
        }
        if (format == IOFormat.Array) {
            if (fromOrTo == FromOrTo.From) {
                options.setInputFromToOption(Optional.of(FromToOption.CurlyBracket));
            } else if (fromOrTo == FromOrTo.To) {
                options.setOutputFromToOption(Optional.of(FromToOption.CurlyBracket));
            }
        }
    }

    private void setFile(Options options, String path, FromOrTo fromOrTo) throws BadArgumentsException, FileNotFoundException, InvalidInputException {
        if (this.hasInputFile && (fromOrTo == FromOrTo.From) || this.hasOutputFile && (fromOrTo == FromOrTo.To)) {
            throw new BadArgumentsException();
        }
        File file = new File(path);
        if (file.isDirectory()) {
            throw new BadArgumentsException();
        }
        if (!file.canWrite() && fromOrTo == FromOrTo.To) {
            throw new InvalidInputException();
        }
        if (!file.canRead() && fromOrTo == FromOrTo.From) {
            throw new InvalidInputException();
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
            throw new BadArgumentsException();
        }
        this.hasDelimiter = true;
        options.setDelimiter(delimiter.charAt(0)); //TODO maybe set delimiter to string
    }

    private void checkForHelp(String[] args) {
        for (String arg : args) {
            if (Patterns.shortHelp.matcher(arg).matches() || Patterns.longHelp.matcher(arg).matches()) {
                PrintHelp();
                System.exit(0);
            }
        }
    }

    private static void validateOptions(Options options) throws BadArgumentsException {
        if (options.getInputFormat() == null || options.getOutputFormat() == null) {
            throw new BadArgumentsException();
        }
        if (options.getInputFromToOption().isPresent() &&
                (options.getInputFormat() != IOFormat.Int
                        || options.getInputFormat() != IOFormat.Bits
                        || options.getInputFormat() != IOFormat.Array
                )) {
            throw new BadArgumentsException();
        }
        if (options.getOutputFromToOption().isPresent() &&
                (options.getOutputFormat() != IOFormat.Int
                        || options.getOutputFormat() != IOFormat.Bits
                        || options.getOutputFormat() != IOFormat.Array
                )) {
            throw new BadArgumentsException();
        }
        if (options.getInputFromToOption().isPresent() &&
                (options.getInputFormat() == IOFormat.Int
                        && (options.getInputFromToOption().get() != FromToOption.Big
                        || options.getInputFromToOption().get() != FromToOption.Little))) {
            throw new BadArgumentsException();
        }
        if (options.getOutputFromToOption().isPresent() &&
                (options.getOutputFormat() == IOFormat.Int
                        && (options.getOutputFromToOption().get() != FromToOption.Big
                        || options.getOutputFromToOption().get() != FromToOption.Little))) {
            throw new BadArgumentsException();
        }
        if (options.getInputFromToOption().isPresent() &&
                (options.getInputFormat() == IOFormat.Bits
                        && (options.getInputFromToOption().get() != FromToOption.Left
                        || options.getInputFromToOption().get() != FromToOption.Right))) {
            throw new BadArgumentsException();
        }
        if (options.getOutputFromToOption().isPresent() &&
                (options.getOutputFormat() == IOFormat.Bits
                        && (options.getOutputFromToOption().get() != FromToOption.Left
                        || options.getOutputFromToOption().get() != FromToOption.Right))) {
            throw new BadArgumentsException();
        }
        if (options.getInputFromToOption().isPresent() &&
                (options.getInputFormat() == IOFormat.Array
                        && (options.getInputFromToOption().get() != FromToOption.CurlyBracket
                        || options.getInputFromToOption().get() != FromToOption.SquareBracket
                        || options.getInputFromToOption().get() != FromToOption.RegularBracket))) {
            throw new BadArgumentsException();
        }
        if (options.getOutputFromToOption().isPresent() &&
                (options.getOutputFormat() == IOFormat.Array
                        && (options.getOutputFromToOption().get() != FromToOption.CurlyBracket
                        || options.getOutputFromToOption().get() != FromToOption.SquareBracket
                        || options.getOutputFromToOption().get() != FromToOption.RegularBracket))) {
            throw new BadArgumentsException();
        }
    }
}
