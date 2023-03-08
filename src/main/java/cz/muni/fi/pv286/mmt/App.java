package cz.muni.fi.pv286.mmt;

import cz.muni.fi.pv286.mmt.argParser.ArgParser;
import cz.muni.fi.pv286.mmt.exceptions.BadArgumentsException;
import cz.muni.fi.pv286.mmt.exceptions.HelpInvokedException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidInputException;
import cz.muni.fi.pv286.mmt.model.IOFormat;
import cz.muni.fi.pv286.mmt.model.Options;
import cz.muni.fi.pv286.mmt.repsentationParser.*;

import java.io.FileNotFoundException;

import static java.lang.System.exit;

public class App 
{
    private static RepresentationParser getRepresentationParser(Options options, IOFormat ioFormat) {
        switch (ioFormat) {
            case Bytes -> {
                return new ByteParser(options);
            }
            case Hex -> {
                return new HexParser(options);
            }
            case Int -> {
                return new IntParser(options);
            }
            case Bits -> {
                return new BitParser(options);
            }
            case Array -> {
                return new ArrayParser(options);
            }
        }

        throw new IllegalArgumentException("Invalid IOFormat");
    }

    public static void main(String[] args)
    {
        ArgParser parser = new ArgParser(args);

        if (args.length == 0) {
            parser.printHelp();
            exit(0);
        }

        try {
            Options options = parser.parse();
            RepresentationParser inParser = getRepresentationParser(options, options.getInputFormat());
            RepresentationParser outParser = getRepresentationParser(options, options.getOutputFormat());

            var input = inParser.parseFrom();
            outParser.parseTo(input);
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
            e.printStackTrace();
            exit(4);
        }
    }
}
