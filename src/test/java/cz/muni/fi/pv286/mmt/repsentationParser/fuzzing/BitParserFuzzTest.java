package cz.muni.fi.pv286.mmt.repsentationParser.fuzzing;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import cz.muni.fi.pv286.mmt.repsentationParser.*;

import java.io.IOException;

public class BitParserFuzzTest extends ParserFuzzTest {
    @FuzzTest
    void roundTripFuzzTest(FuzzedDataProvider data) throws IOException {
        byte[] bytes = data.consumeBytes(0);
        assertRoundTrip(BitParser.class, bytes);

        bytes = data.consumeBytes(10);
        assertRoundTrip(BitParser.class, bytes);

        bytes = data.consumeBytes(100);
        assertRoundTrip(BitParser.class, bytes);

        bytes = data.consumeBytes(1000);
        assertRoundTrip(BitParser.class, bytes);

        bytes = data.consumeRemainingAsBytes();
        assertRoundTrip(BitParser.class, bytes);
    }
}
