package cz.muni.fi.pv286.mmt.representation.fuzzing;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import cz.muni.fi.pv286.mmt.representation.*;

import java.io.IOException;

class ByteParserFuzzTest extends ParserFuzzTest {
    @FuzzTest
    void roundTripFuzzTest(FuzzedDataProvider data)  throws IOException {
        byte[] bytes = data.consumeBytes(10);
        assertRoundTrip(ByteParser.class, bytes);

        bytes = data.consumeBytes(100);
        assertRoundTrip(ByteParser.class, bytes);

        bytes = data.consumeBytes(1000);
        assertRoundTrip(ByteParser.class, bytes);

        bytes = data.consumeRemainingAsBytes();
        assertRoundTrip(ByteParser.class, bytes);
    }
}
