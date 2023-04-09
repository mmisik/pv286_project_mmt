package cz.muni.fi.pv286.mmt.representation.fuzzing;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import cz.muni.fi.pv286.mmt.exceptions.InvalidHexCharacterException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidHexCountException;
import cz.muni.fi.pv286.mmt.representation.*;

import java.io.IOException;

public class HexParserFuzzTest extends ParserFuzzTest {
    @FuzzTest
    void roundTripFuzzTest(FuzzedDataProvider data) throws IOException {

        try {
            String bytes = data.consumeString(10);
            assertRoundTrip(HexParser.class, bytes.getBytes(), sanitize(bytes));

            bytes = data.consumeString(100);
            assertRoundTrip(HexParser.class, bytes.getBytes(), sanitize(bytes));

            bytes = data.consumeString(1000);
            assertRoundTrip(HexParser.class, bytes.getBytes(), sanitize(bytes));

            bytes = data.consumeRemainingAsString();
            assertRoundTrip(HexParser.class, bytes.getBytes(), sanitize(bytes));
        } catch (InvalidHexCharacterException | InvalidHexCountException e) {
            // Ignore
        }
    }

    private byte[] sanitize(String bytes) {
        return bytes.toLowerCase().replace(" ", "").getBytes();
    }
}
