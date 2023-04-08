package cz.muni.fi.pv286.mmt;

import static java.lang.System.exit;

import cz.muni.fi.pv286.mmt.arguments.ArgParser;
import cz.muni.fi.pv286.mmt.exceptions.BadArgumentsException;
import cz.muni.fi.pv286.mmt.exceptions.HelpInvokedException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidInputException;
import cz.muni.fi.pv286.mmt.model.IoFormat;
import cz.muni.fi.pv286.mmt.model.Options;
//import cz.muni.fi.pv286.mmt.representation.ArrayParser;
import cz.muni.fi.pv286.mmt.representation.*;

import java.io.FileNotFoundException;


/**
 * Main class of the application.
 */
public class App {
    private static
        RepresentationParser getRepresentationParser(Options options, IoFormat ioFormat) {
        switch (ioFormat) {
            case BYTES -> {
                return new ByteParser(options);
            }
            case HEX -> {
                return new HexParser(options);
            }
            case INT -> {
                return new IntParser(options);
            }
            case BITS -> {
                return new BitParser(options);
            }
            case ARRAY -> {
                return new ArrayParser(options);
            }
            default -> {
                throw new IllegalArgumentException("Invalid IOFormat");
            }
        }
    }

    /**
     * Main method of the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArgParser parser = new ArgParser(args);

        if (args.length == 0) {
            parser.printHelp();
            exit(0);
        }

        try {
            Options options = parser.parse();
            RepresentationParser inParser =
                    getRepresentationParser(options, options.getInputFormat());
            RepresentationParser outParser =
                    getRepresentationParser(options, options.getOutputFormat());

            inParser.parse(outParser);
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
            exit(1);
        } catch (BadArgumentsException e) {
            System.err.println(e.getMessage());
            parser.printHelp();
            exit(2);
        } catch (InvalidInputException e) {
            System.err.println("Invalid input " + e.getMessage());
            exit(3);
        } catch (HelpInvokedException e) {
            parser.printHelp();
            exit(0);
        } catch (Exception e) {
            System.err.println("Unexpected error");
            exit(4);
        }
    }
}
