package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.model.BracketType;
import cz.muni.fi.pv286.mmt.model.IoFormat;
import cz.muni.fi.pv286.mmt.model.IoOption;
import cz.muni.fi.pv286.mmt.model.Options;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Array parser.
 *//*
public class ArrayParser extends RepresentationParser {

    public ArrayParser(Options options) {
        super(options);
    }

    private String getOpeningBracket(BracketType bracketType) {
        if (bracketType == BracketType.SQUARE_BRACKET) {
            return "[";
        }

        if (bracketType == BracketType.REGULAR_BRACKET) {
            return "(";
        }

        return "{";
    }


    private String getClosingBracket(BracketType bracketType) {
        if (bracketType == BracketType.SQUARE_BRACKET) {
            return "]";
        }

        if (bracketType == BracketType.REGULAR_BRACKET) {
            return ")";
        }

        return "}";
    }

    private boolean isClosingBracket(byte b) {
        char c = (char) b;
        return c == ')' || c == ']' || c == '}';
    }

    private String formatOutput(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        IoOption outputOption = options.getOutputFromToOption().orElse(IoOption.HEX);
        BracketType bracketType = options.getBracketType().orElse(BracketType.CURLY_BRACKET);

        boolean possibleComma = false;

        if (options.getInputFormat() != IoFormat.ARRAY) {
            sb.append(getOpeningBracket(bracketType));
        }

        for (int index = 0; index < bytes.length; index++) {
            boolean isBracket = false;
            byte b = bytes[index];

            if (possibleComma && !(isClosingBracket(bytes[index]))) {
                sb.append(", ");
            }

            switch ((char) bytes[index]) {
                case '{', '[', '(' -> {
                    sb.append(getOpeningBracket(bracketType));
                    isBracket = true;
                    possibleComma = false;
                }

                case '}', ']', ')' -> {
                    sb.append(getClosingBracket(bracketType));
                    isBracket = true;
                    possibleComma = true;
                }

                default -> {
                    // Ignore
                }
            }

            if (isBracket) {
                continue;
            }

            if (outputOption == IoOption.HEX) {
                sb.append(String.format("0x%02x", b));
            } else if (outputOption == IoOption.DECIMAL) {
                sb.append(b & 0xff);
            } else if (outputOption == IoOption.CHARACTER) {
                sb.append(String.format("'%s'", (char) b));
            } else if (outputOption == IoOption.BINARY) {
                sb.append("0b").append(Integer.toBinaryString(b & 0xff)
                        .replaceFirst("^0+(?!$)", ""));
            }

            possibleComma = true;
        }

        if (options.getInputFormat() != IoFormat.ARRAY) {
            sb.append(getClosingBracket(bracketType));
        }

        return sb.toString();
    }

    private byte[] parseArray(String input)
            throws IOException {
        Pattern valuePattern = Pattern.compile(
                "(([\\[\\{\\(\\]\\}\\)])|"
                        + "(0b[01]+)|(0x[0-9a-fA-F]+)|"
                        + "(\\\\x[0-9a-fA-F]{2})|"
                        + "(\\b(?!\\w*[a-zA-Z]\\w*)[0-9]+\\b))"
        );
        Matcher valueMatcher = valuePattern.matcher(input);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        boolean matched = false;

        while (valueMatcher.find()) {
            matched = true;
            String value = valueMatcher.group(1);

            if ("{[()]}".contains(value)) {
                if (options.getOutputFormat() == IoFormat.ARRAY) {
                    byteArrayOutputStream.write(value.getBytes());
                }
            } else if (value.startsWith("0x")) {
                byteArrayOutputStream.write(Integer.parseInt(value.substring(2), 16));
            } else if (value.startsWith("0b")) {
                byteArrayOutputStream.write(Integer.parseInt(value.substring(2), 2));
            } else if (value.startsWith("\\x")) {
                byteArrayOutputStream.write(Integer.parseInt(value.substring(2), 16));
            } else {
                byteArrayOutputStream.write(Integer.parseInt(value));
            }
        }

        if (!matched) {
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
}*/