package cz.muni.fi.pv286.mmt.argParser;

import cz.muni.fi.pv286.mmt.exceptions.BadArgumentsException;
import cz.muni.fi.pv286.mmt.model.ArgMatch;
import cz.muni.fi.pv286.mmt.model.Options;

import java.util.regex.Matcher;
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
        static Pattern longFromFormatExtract = Pattern.compile("^--from=(.?)$");
        static Pattern fromOption = Pattern.compile("^--from-options=\\w+$");
        static Pattern longFromOptionExtract = Pattern.compile("^--from-options=(.?)$");
        static Pattern shortToFormat = Pattern.compile("^-t$");
        static Pattern longToFormat = Pattern.compile("^--to=\\w+$");
        static Pattern longToFormatExtract = Pattern.compile("^--to=(.?)$");
        static Pattern toOption = Pattern.compile("^--to-options=\\w+$");
        static Pattern longToOptionExtract = Pattern.compile("^--to-options=(.?)$");
        static Pattern shortInputFile = Pattern.compile("^-i$");
        static Pattern longInputFile = Pattern.compile("^--input=\\w+$");
        static Pattern longInputFileExtract = Pattern.compile("^--input=(.?)$");
        static Pattern shortOutputFile = Pattern.compile("^-o$");
        static Pattern longOutputFile = Pattern.compile("^--output=\\w+$");
        static Pattern longOutputFileExtract = Pattern.compile("^--output=(.?)$");
        static Pattern shortDelimiter = Pattern.compile("^-d$");
        static Pattern longDelimiter = Pattern.compile("^--delimiter=.");
        static Pattern longDelimiterExtract = Pattern.compile("^--delimiter=(.)$");
        static Pattern shortHelp = Pattern.compile("^-h$");
        static Pattern longHelp = Pattern.compile("^--help$");


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
        System.exit(0);
    }

    public ArgParser(String[] args) {
        this.args = args;
    }
    private Options parse(String[] args) throws BadArgumentsException {
        Options options = new Options();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            ArgMatch argMatch = getArgMatch(arg);
            switch (argMatch) {
                case Help -> {
                    PrintHelp();
                }
                case ShortFromFormat -> {
                    this.hasFromFormat = true;
                    if (i + 1 >= args.length) {
                        throw new BadArgumentsException();
                    }



                }
                case LongFromFormat -> {
                    this.hasFromFormat = true;

                }
                case FromOption -> {

                }
                case ShortToFormat -> {

                }
                case LongToFormat -> {

                }
                case ToOption -> {

                }
                case ShortFromFile -> {

                }
                case LongFromFile -> {

                }
                case ShortToFile -> {

                }
                case LongToFile -> {

                }
                case ShortDelimiter -> {

                }
                case LongDelimiter -> {

                }
                default -> {
                    throw new BadArgumentsException();
                }
            }
        }
    }

    private static ArgMatch getArgMatch(String str) throws BadArgumentsException {
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

    private static String getNextOption(String[] options, int index) {

    }
}
