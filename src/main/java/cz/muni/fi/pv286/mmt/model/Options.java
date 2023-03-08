package cz.muni.fi.pv286.mmt.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

public class Options {
    private IOFormat inputFormat;
    private Optional<FromToOption> inputFromToOption = Optional.empty();
    private IOFormat outputFormat;
    private Optional<FromToOption> outputFromToOption = Optional.empty();
    private InputStream inputFile = System.in;
    private OutputStream outputFile = System.out;
    private char delimiter = '\n';
    private Optional<BracketType> bracketType = Optional.empty();
    private boolean wasFromOptionSet = false;
    private boolean wasToOptionSet = false;
    private boolean wasBracketSet = false;

    public IOFormat getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(IOFormat inputFormat) {
        if (inputFormat == IOFormat.Int && !wasFromOptionSet) {
            inputFromToOption = Optional.of(FromToOption.Big);
        }
        if(inputFormat == IOFormat.Bits && !wasFromOptionSet) {
            inputFromToOption = Optional.of(FromToOption.Left);
        }
        this.inputFormat = inputFormat;
    }

    public Optional<FromToOption> getInputFromToOption() {
        return inputFromToOption;
    }

    public void setBracketOption(BracketType bracketType) {
        this.bracketType = Optional.of(bracketType);
        wasBracketSet = true;
    }

    public void setInputFromToOption(FromToOption inputFromToOption) {
        this.inputFromToOption = Optional.of(inputFromToOption);
        wasFromOptionSet = true;
    }

    public IOFormat getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(IOFormat outputFormat) {
        if (outputFormat == IOFormat.Int && !wasToOptionSet) {
            outputFromToOption = Optional.of(FromToOption.Big);
        }
        if(outputFormat == IOFormat.Array && !wasToOptionSet) {
            outputFromToOption = Optional.of(FromToOption.Hex);
        }
        if(outputFormat == IOFormat.Array && !wasBracketSet) {
            bracketType = Optional.of(BracketType.CurlyBracket);
        }

        this.outputFormat = outputFormat;
    }

    public Optional<FromToOption> getOutputFromToOption() {
        return outputFromToOption;
    }

    public void setOutputFromToOption(FromToOption outputFromToOption) {
        if (outputFromToOption == FromToOption.CurlyBracket ){
            setBracketOption(BracketType.CurlyBracket);
        } else if (outputFromToOption == FromToOption.SquareBracket) {
            setBracketOption(BracketType.SquareBracket);
        } else if (outputFromToOption == FromToOption.RegularBracket) {
            setBracketOption(BracketType.RegularBracket);
        } else {
            this.outputFromToOption = Optional.of(outputFromToOption);
        }

    }

    public InputStream getInputFile() {
        return inputFile;
    }

    public void setInputFile(InputStream inputFile) {
        this.inputFile = inputFile;
    }

    public OutputStream getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(OutputStream outputFile) {
        this.outputFile = outputFile;
    }

    public char getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    public Optional<BracketType> getBracketType() {
        return bracketType;
    }

    private void setBracketType(Optional<BracketType> bracketType) {
        this.bracketType = bracketType;
    }

    private boolean wasFromOptionSet() {
        return wasFromOptionSet;
    }

    private boolean wasToOptionSet() {
        return wasToOptionSet;
    }

    private boolean wasBracketSet() {
        return wasBracketSet;
    }
}
