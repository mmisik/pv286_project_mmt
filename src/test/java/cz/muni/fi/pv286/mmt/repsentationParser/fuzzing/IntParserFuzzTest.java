package cz.muni.fi.pv286.mmt.repsentationParser.fuzzing;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import cz.muni.fi.pv286.mmt.repsentationParser.*;

class IntParserFuzzTest extends ParserTest {
    @FuzzTest
    void roundTripFuzzTest(FuzzedDataProvider data) {
        byte[] bytes = data.consumeBytes(0);
        assertRoundTrip(IntParser.class, bytes);

        bytes = data.consumeBytes(10);
        assertRoundTrip(IntParser.class, bytes);

        bytes = data.consumeBytes(100);
        assertRoundTrip(IntParser.class, bytes);

        bytes = data.consumeBytes(1000);
        assertRoundTrip(IntParser.class, bytes);

        bytes = data.consumeRemainingAsBytes();
        assertRoundTrip(IntParser.class, bytes);
    }
}
