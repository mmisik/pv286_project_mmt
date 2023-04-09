package cz.muni.fi.pv286.mmt.representation;

import cz.muni.fi.pv286.mmt.model.Options;
import java.io.IOException;

/**
 * Byte parser.
 */
public class ByteParser extends RepresentationParser {

    public ByteParser(Options options) {
        super(options);
    }

    @Override
    public byte[] parseTo(byte[] bytes) throws IOException {
        return bytes;
    }

    @Override
    public byte[] parseFrom(byte[] bytes) throws IOException {
        // no additional processing is necessary
        return bytes;
    }
}
