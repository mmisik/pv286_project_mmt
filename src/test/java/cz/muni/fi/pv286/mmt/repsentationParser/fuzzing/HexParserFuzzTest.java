package cz.muni.fi.pv286.mmt.repsentationParser.fuzzing;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import cz.muni.fi.pv286.mmt.repsentationParser.*;

public class HexParserFuzzTest extends ParserTest {
    @FuzzTest
    void roundTripFuzzTest(FuzzedDataProvider data) {

        byte[] bytes = data.consumeBytes(10);
        assertRoundTrip(HexParser.class, bytes);

        bytes = data.consumeBytes(100);
        assertRoundTrip(HexParser.class, bytes);

        bytes = data.consumeBytes(1000);
        assertRoundTrip(HexParser.class, bytes);

        // check if the remaining bytes are even
        int remainingBytes = data.remainingBytes();
        if (remainingBytes % 2 != 0) {
            remainingBytes--;
        }

        bytes = data.consumeBytes(remainingBytes);

        assertRoundTrip(HexParser.class, bytes);
    }
}
