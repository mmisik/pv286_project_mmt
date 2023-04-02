package cz.muni.fi.pv286.mmt.repsentationParser.fuzzing;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import cz.muni.fi.pv286.mmt.repsentationParser.*;

class ByteParserFuzzTest extends ParserTest {
    @FuzzTest
    void roundTripFuzzTest(FuzzedDataProvider data) {
        byte[] bytes = data.consumeBytes(0);
        assertRoundTrip(ByteParser.class, bytes);

        bytes = data.consumeBytes(10);
        assertRoundTrip(ByteParser.class, bytes);

        bytes = data.consumeBytes(100);
        assertRoundTrip(ByteParser.class, bytes);

        bytes = data.consumeBytes(1000);
        assertRoundTrip(ByteParser.class, bytes);

        bytes = data.consumeRemainingAsBytes();
        assertRoundTrip(ByteParser.class, bytes);
    }
}
