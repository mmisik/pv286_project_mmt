package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.exceptions.InvalidArrayValueException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidNestingException;
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
import java.util.stream.Collectors;

public class AdvancedArrayParser extends RepresentationParser {

    public AdvancedArrayParser(Options options) {
        super(options);
    }

    @Override
    protected byte[] parseTo(byte[] bytes) throws IOException {
        return new byte[0];
    }

    private byte[] translate(ResultTree resultTree) throws InvalidArrayValueException {
        List<Byte> bytesList = new ArrayList<>();
        for(ResultTree t: resultTree.getChildren()) {
            if(!t.getChildren().isEmpty()) {
                throw new InvalidArrayValueException();
            }
            else {
                bytesList.add(t.getValue()[0]);
            }
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bytesList.stream().mapToInt(Byte::intValue).forEach(byteArrayOutputStream::write);
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    protected byte[] parseFrom(byte[] bytes) throws IOException {
        ResultTree parsedTree = parse(bytes);
        if(parsedTree == null) {
            return new byte[]{};
        }
        return translate(parsedTree);
    }

    @Override
    public void parse(RepresentationParser parseTo) throws IOException {
        if (!(parseTo instanceof AdvancedArrayParser)) {
            super.parse(parseTo);
            return;
        }

    }

    private ResultTree parse(byte[] bytes) throws IOException {
        List<ResultTree> stack = new LinkedList<>();
        stack.add(new ResultTree(new String(bytes)));
        while(!stack.isEmpty()) {
            ResultTree current = stack.remove(stack.size() - 1);
            if (current.getIntermediate() != null) {
                stack.add(current);
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
                if (extracted != null) {
                    String[] tokens = extracted.split(",");
                    for(String token : tokens) {
                        ResultTree t = new ResultTree(token.trim());
                        current.getChildren().add(t);
                        stack.add(t);
                    }
                } else {
                    Matcher hexExtracted = Patterns.hexExtractor.matcher(current.getIntermediate());
                    Matcher decimalExtracted = Patterns.decimalExtractor.matcher(current.getIntermediate());
                    Matcher bytesExtracted = Patterns.bytesExtractor.matcher(current.getIntermediate());
                    Matcher bitsExtracted = Patterns.bitsExtractor.matcher(current.getIntermediate());

                    if (hexExtracted.find()) {
                        extracted = hexExtracted.group(1);
                        if (extracted.length() > 1) {
                            ResultTree t = new ResultTree(new byte[]{ HexParser.getByteFromHex(extracted.charAt(0), extracted.charAt(1))});
                            current.getChildren().add(t);
                            stack.add(t);
                        } else {
                            ResultTree t = new ResultTree(new byte[] {HexParser.getByteFromHex('0', extracted.charAt(0))});
                            current.getChildren().add(t);
                        }
                    }
                    else if (decimalExtracted.find()) {
                        extracted = decimalExtracted.group(1);
                        int value = Integer.parseInt(extracted, 10);
                        if (value > 255 || value < 0) {
                            throw new InvalidArrayValueException();
                        }
                        ResultTree t = new ResultTree(new byte[]{(byte) value});
                        current.getChildren().add(t);
                    } else if (bytesExtracted.find()) {
                        extracted = bytesExtracted.group(1);
                        ResultTree t = new ResultTree(new byte[]{HexParser.getByteFromHex(extracted.charAt(0), extracted.charAt(1))});
                        current.getChildren().add(t);
                        stack.add(t);
                    } else if (bitsExtracted.find()) {
                        extracted = bytesExtracted.group(1);
                        ResultTree t = new ResultTree(BitParser.encodeToByteArray(extracted.getBytes()));
                        current.getChildren().add(t);
                    } else {
                        throw new InvalidArrayValueException();
                    }
                }
                current.setIntermediate(null);
            } else if(stack.isEmpty()) {
                return current;
            }
        }
        return null;
    }

    public static final class Patterns {
        private Patterns() {}
        static Pattern regularBracketsExtractor = Pattern.compile("$\\((.*)\\)^");
        static Pattern curlyBracketsExtractor = Pattern.compile("^\\{(.*)}$");
        static Pattern squareBracketsExtractor = Pattern.compile("^\\[(.*)]$");
        static Pattern decimalExtractor = Pattern.compile("^(\\d{1,3})$");
        static Pattern hexExtractor = Pattern.compile("^0x([0-9a-fA-F]{1,2})");
        static Pattern bytesExtractor = Pattern.compile("^'\\\\x([0-9a-fA-F]{2})'$");
        static Pattern bitsExtractor = Pattern.compile("^0b([0-1]{1,8})$");
        //static Pattern
    }
}