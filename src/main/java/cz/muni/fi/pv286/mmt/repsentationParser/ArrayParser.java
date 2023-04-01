package cz.muni.fi.pv286.mmt.repsentationParser;

import cz.muni.fi.pv286.mmt.model.BracketType;
import cz.muni.fi.pv286.mmt.model.FromToOption;
import cz.muni.fi.pv286.mmt.model.Options;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArrayParser extends RepresentationParser {

    public ArrayParser(Options options) {
        super(options);
    }

    private String getOpeningBracket(BracketType bracketType) {
        if (bracketType == BracketType.SquareBracket){
            return "[";
        }

        if (bracketType == BracketType.RegularBracket){
            return "(";
        }

        return "{";
    }


    private String getClosingBracket(BracketType bracketType) {
        if (bracketType == BracketType.SquareBracket){
            return "]";
        }

        if (bracketType == BracketType.RegularBracket){
            return ")";
        }

        return "}";
    }

    private String formatOutput(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        FromToOption outputOption = options.getOutputFromToOption().orElse(FromToOption.Hex);
        BracketType bracketType = options.getBracketType().orElse(BracketType.CurlyBracket);

        sb.append(getOpeningBracket(bracketType));

        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            if (i != 0) {
                sb.append(", ");
            }

            if (outputOption == FromToOption.Hex) {
                sb.append(String.format("0x%02x", b));
            } else if (outputOption == FromToOption.Decimal) {
                sb.append(b & 0xff);
            } else if (outputOption == FromToOption.Character) {
                sb.append(String.format("'%s'", (char) b));
            } else if (outputOption == FromToOption.Binary) {
                sb.append("0b").append(Integer.toBinaryString(b & 0xff).replaceFirst("^0+(?!$)", ""));
            }
        }

        sb.append(getClosingBracket(bracketType));
        return sb.toString();
    }

    private byte[] parseArray(String input) throws IOException {
        Pattern valuePattern = Pattern.compile("((0b[01]+)|(0x[0-9a-fA-F]+)|(\\\\x[0-9a-fA-F]{2})|(\\b[0-9]+\\b))");
        Matcher valueMatcher = valuePattern.matcher(input);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        boolean matched = false;

        while (valueMatcher.find()) {
            matched = true;
            String value = valueMatcher.group(1);

            if (value.startsWith("0x")) {
                byteArrayOutputStream.write(Integer.parseInt(value.substring(2), 16));
            } else if (value.startsWith("0b")) {
                byteArrayOutputStream.write(Integer.parseInt(value.substring(2), 2));
            } else if (value.startsWith("\\x")) {
                byteArrayOutputStream.write(Integer.parseInt(value.substring(2), 16));
            } else {
                byteArrayOutputStream.write(Integer.parseInt(value));
            }
        }

        if (!matched){
            throw new IOException("Invalid input values.");
        }

        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void parseTo(byte[] bytes) throws IOException {
        String formattedOutput = formatOutput(bytes);
        options.getOutputFile().write(formattedOutput.getBytes());
    }

    @Override
    public byte[] parseFrom() throws IOException {
        String input = new String(getInput()).trim();
        if (!input.matches("^[\\[\\(\\{].*[\\]\\)\\}]$")) {
            throw new IOException("Invalid input format.");
        }

        return parseArray(input);
    }
}