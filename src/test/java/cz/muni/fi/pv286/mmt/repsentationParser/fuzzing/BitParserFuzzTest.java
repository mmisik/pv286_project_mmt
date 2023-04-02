package cz.muni.fi.pv286.mmt.repsentationParser.fuzzing;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import cz.muni.fi.pv286.mmt.exceptions.InvalidBitCharacterException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidBitInputException;
import cz.muni.fi.pv286.mmt.repsentationParser.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BitParserFuzzTest extends ParserFuzzTest {
    @FuzzTest
    void roundTripFuzzTest(FuzzedDataProvider data) throws IOException {

        try {
            byte[] bytes = data.consumeBytes(10);
            assertRoundTrip(BitParser.class, bytes, sanitize(bytes));

            bytes = data.consumeBytes(100);
            assertRoundTrip(BitParser.class, bytes, sanitize(bytes));

            bytes = data.consumeBytes(1000);
            assertRoundTrip(BitParser.class, bytes, sanitize(bytes));

            bytes = data.consumeRemainingAsBytes();
            assertRoundTrip(BitParser.class, bytes, sanitize(bytes));
        } catch (InvalidBitCharacterException | InvalidBitInputException e) {
            // Ignore
        }
    }

    private byte[] sanitize(byte[] bytes) {
        String str = new String(bytes, StandardCharsets.UTF_8);
        return str.replace(" ", "").getBytes();
    }
}
