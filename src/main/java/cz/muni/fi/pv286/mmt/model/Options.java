package cz.muni.fi.pv286.mmt.model;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;

public class Options {
    private IOFormat inputFormat;
    private Optional<FromToOption> inputFromToOption;
    private IOFormat outputFormat;
    private Optional<FromToOption> outputFromToOption;
    private InputStream inputFile = System.in;
    private OutputStream outputFile = System.out;
    private char delimiter = '\n';

    public IOFormat getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(IOFormat inputFormat) {
        this.inputFormat = inputFormat;
    }

    public Optional<FromToOption> getInputFromToOption() {
        return inputFromToOption;
    }

    public void setInputFromToOption(Optional<FromToOption> inputFromToOption) {
        this.inputFromToOption = inputFromToOption;
    }

    public IOFormat getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(IOFormat outputFormat) {
        this.outputFormat = outputFormat;
    }

    public Optional<FromToOption> getOutputFromToOption() {
        return outputFromToOption;
    }

    public void setOutputFromToOption(Optional<FromToOption> outputFromToOption) {
        this.outputFromToOption = outputFromToOption;
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
}
