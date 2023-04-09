package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.exceptions.InvalidArrayArgumentException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidArrayBracketException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidArrayValueException;
import cz.muni.fi.pv286.mmt.model.BracketType;
import cz.muni.fi.pv286.mmt.model.IoOption;
import cz.muni.fi.pv286.mmt.model.Options;
import cz.muni.fi.pv286.mmt.model.ResultTree;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Array parser.
 */
public class ArrayParser extends RepresentationParser {

    public ArrayParser(Options options) {
        super(options);
    }

    /**
     * Splits the given string into a list of top-level tokens.
     *
     * @param entry The input string.
     * @return A list of top-level tokens.
     * @throws InvalidArrayBracketException If the brackets or parentheses
     *                                      are mismatched or improperly nested.
     */
    public static List<String> topLevelSplitTokens(String entry) throws InvalidArrayBracketException {
        List<String> tokens = new ArrayList<>();
        List<Character> stack = new ArrayList<>();
        int start = 0;

        for (int i = 0; i < entry.length(); i++) {
            char c = entry.charAt(i);

            if (c == '{' || c == '[' || c == '(') {
                stack.add(c);
            } else if (c == '}' || c == ']' || c == ')') {
                if (stack.isEmpty()) {
                    throw new InvalidArrayBracketException();
                }
                char top = stack.remove(stack.size() - 1);
                if (c == '}' && top != '{') {
                    throw new InvalidArrayBracketException();
                }
                if (c == ']' && top != '[') {
                    throw new InvalidArrayBracketException();
                }
                if (c == ')' && top != '(') {
                    throw new InvalidArrayBracketException();
                }
            } else if (c == ',' && stack.isEmpty()) {
                tokens.add(entry.substring(start, i).trim());
                start = i + 1;
            }
        }

        if (start < entry.length()) {
            tokens.add(entry.substring(start).trim());
        }

        if (!stack.isEmpty()) {
            throw new InvalidArrayBracketException();
        }

        return tokens;
    }

    @Override
    protected byte[] parseTo(byte[] bytes) throws IOException {
        ResultTree parsedTree = translateTo(bytes);
        return parse(parsedTree);
    }

    private byte[] translateFrom(ResultTree resultTree) throws InvalidArrayValueException {
        List<Byte> bytesList = new ArrayList<>();
        for (ResultTree t : resultTree.getChildren()) {
            if (!t.getChildren().isEmpty()) {
                throw new InvalidArrayValueException();
            } else {
                bytesList.add(t.getValue()[0]);
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bytesList.stream().mapToInt(Byte::intValue).forEach(byteArrayOutputStream::write);
        return byteArrayOutputStream.toByteArray();
    }

    private ResultTree translateTo(byte[] bytes) {
        ResultTree resultTree = new ResultTree(new ArrayList<>());

        for (byte b : bytes) {
            var rt = new ResultTree(new byte[] {b});
            resultTree.getChildren().add(rt);
        }

        return resultTree;
    }

    @Override
    protected byte[] parseFrom(byte[] bytes) throws IOException {
        ResultTree parsedTree = parse(bytes);
        if (parsedTree == null) {
            return new byte[] {};
        }
        return translateFrom(parsedTree);
    }

    @Override
    public void parse(RepresentationParser parseTo) throws IOException {
        if (!(parseTo instanceof ArrayParser)) {
            super.parse(parseTo);
            return;
        }
        //In case of array -> array
        var results = getInput();

        for (var result : results) {
            byte[] representation = result.getValue();
            ResultTree tree = this.parse(representation);
            byte[] parsed = parse(tree);

            options.getOutputFile().write(parsed);

            // write delimiter to output only if:
            // - the delimiter was set
            // - and there is more than one element in the list
            // - and the current element is not the last
            if (options.wasDelimiterSet() && results.size() > 1 && result != results.get(results.size() - 1)) {
                options.getOutputFile().write(options.getDelimiter().getBytes());
            }
        }
    }

    private byte[] parse(ResultTree resultTree) throws IOException { //This destroys the resultTree
        byte openingBracket;
        byte closingBracket;

        BracketType bracketType = super.options.getBracketType().orElseThrow(
            InvalidArrayBracketException::new
        );

        IoOption outputOption = super.options.getOutputFromToOption().orElseThrow(
            InvalidArrayArgumentException::new
        );

        if (bracketType == BracketType.CURLY_BRACKET) {
            openingBracket = '{';
            closingBracket = '}';
        } else if (bracketType == BracketType.SQUARE_BRACKET) {
            openingBracket = '[';
            closingBracket = ']';
        } else if (bracketType == BracketType.REGULAR_BRACKET) {
            openingBracket = '(';
            closingBracket = ')';
        } else {
            throw new InvalidArrayBracketException();
        }

        List<Byte> bytesList = new ArrayList<>() {
        };
        bytesList.add(openingBracket);
        List<ResultTree> stack = new LinkedList<>() {
        };
        stack.add(resultTree);

        while (!stack.isEmpty()) {
            ResultTree top = stack.get(stack.size() - 1);
            if (top.getChildIndex() <= top.getChildren().size() - 1) { // Done for each one
                ResultTree child = top.getChildren().get(top.getChildIndex());

                if (top.getChildIndex() != 0) { // If not first
                    bytesList.add((byte) ',');
                    bytesList.add((byte) ' ');
                }

                if (!child.getChildren().isEmpty() || child.getValue() == null) {
                    bytesList.add(openingBracket);
                    stack.add(child);
                } else if (child.getValue() != null && child.getValue().length >= 1) {
                    byte[] parsedValue = parseFromByte(child.getValue()[0], outputOption);
                    for (byte b : parsedValue) {
                        bytesList.add(b);
                    }
                }

                top.setChildIndex(top.getChildIndex() + 1);

            } else {
                stack.remove(stack.size() - 1);
                bytesList.add(closingBracket);
            }
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bytesList.stream().mapToInt(Byte::intValue).forEach(byteArrayOutputStream::write);
        return byteArrayOutputStream.toByteArray();
    }

    private ResultTree parse(byte[] bytes) throws IOException {
        List<ResultTree> stack = new LinkedList<>();
        stack.add(new ResultTree(new String(bytes)));
        while (!stack.isEmpty()) {
            ResultTree current = stack.remove(stack.size() - 1);
            if (current.getIntermediate() != null) {
                String extracted = null;
                Matcher curly = Patterns.curlyBracketsExtractor.matcher(current.getIntermediate());
                Matcher square = Patterns.squareBracketsExtractor.matcher(current.getIntermediate());
                Matcher regular = Patterns.regularBracketsExtractor.matcher(current.getIntermediate());
                if (curly.find()) {
                    extracted = curly.group(1);
                } else if (square.find()) {
                    extracted = square.group(1);
                } else if (regular.find()) {
                    extracted = regular.group(1);
                }
                if ("".equals(extracted)) {
                    current.setIntermediate(null);
                    current.setValue(null);
                    stack.add(current);
                } else if (extracted != null) {
                    stack.add(current);
                    List<String> tokens = topLevelSplitTokens(extracted);
                    for (String token : tokens) {
                        ResultTree t = new ResultTree(token.trim());
                        current.getChildren().add(t);
                        stack.add(t);
                    }
                    current.setIntermediate(null);
                } else {

                    Matcher hexExtracted = Patterns.hexExtractor.matcher(current.getIntermediate());
                    Matcher decimalExtracted = Patterns.decimalExtractor.matcher(current.getIntermediate());
                    Matcher bytesExtracted = Patterns.bytesExtractor.matcher(current.getIntermediate());
                    Matcher bitsExtracted = Patterns.bitsExtractor.matcher(current.getIntermediate());

                    if (hexExtracted.find()) {
                        extracted = hexExtracted.group(1);
                        if (extracted.length() > 1) {
                            byte[] v = new byte[] {HexParser.getByteFromHex(extracted.charAt(0), extracted.charAt(1))};
                            current.setValue(v);
                        } else {
                            byte[] v = new byte[] {HexParser.getByteFromHex('0', extracted.charAt(0))};
                            current.setValue(v);
                        }
                    } else if (decimalExtracted.find()) {
                        extracted = decimalExtracted.group(1);
                        int value = Integer.parseInt(extracted, 10);
                        if (value > 255 || value < 0) {
                            throw new InvalidArrayValueException();
                        }
                        byte[] v = new byte[] {(byte) value};
                        current.setValue(v);
                    } else if (bytesExtracted.find()) {
                        extracted = bytesExtracted.group(1);
                        byte[] v = new byte[] {HexParser.getByteFromHex(extracted.charAt(0), extracted.charAt(1))};
                        current.setValue(v);
                    } else if (bitsExtracted.find()) {
                        extracted = bitsExtracted.group(1);
                        byte[] v = new byte[] {Byte.parseByte(extracted, 2)};
                        current.setValue(v);
                    } else {
                        throw new InvalidArrayValueException();
                    }
                    current.setIntermediate(null);
                }

            } else if (stack.isEmpty()) {
                return current;
            }
        }
        return null;
    }

    private byte[] parseFromByte(byte b, IoOption ioOption) throws InvalidArrayArgumentException {
        if (ioOption == IoOption.HEX) {
            return ("0x" + String.format("%01x", b)).getBytes();
        } else if (ioOption == IoOption.DECIMAL) {
            return String.format("%d", b).getBytes();
        } else if (ioOption == IoOption.CHARACTER) {
            return ("'\\x" + HexParser.getHexFromByte(b) + "'").getBytes();
        } else if (ioOption == IoOption.BINARY) {
            return ("0b" + Integer.toBinaryString(b & 0xFF)).getBytes();
        } else {
            throw new InvalidArrayArgumentException("Io option provided was ");
        }

    }

    /**
     * A utility class containing regular expression for string parsing tasks.
     */
    public static final class Patterns {

        static Pattern regularBracketsExtractor = Pattern.compile("^\\((.*)\\)$");
        static Pattern curlyBracketsExtractor = Pattern.compile("^\\{(.*)}$");
        static Pattern squareBracketsExtractor = Pattern.compile("^\\[(.*)]$");
        static Pattern decimalExtractor = Pattern.compile("^(\\d{1,3})$");
        static Pattern hexExtractor = Pattern.compile("^0x([0-9a-fA-F]{1,2})");
        static Pattern bytesExtractor = Pattern.compile("^'\\\\x([0-9a-fA-F]{2})'$");
        static Pattern bitsExtractor = Pattern.compile("^0b([0-1]{1,8})$");

        private Patterns() {
        }
    }
}