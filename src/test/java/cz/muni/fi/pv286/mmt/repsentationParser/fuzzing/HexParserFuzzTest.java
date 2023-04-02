package cz.muni.fi.pv286.mmt.repsentationParser.fuzzing;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import cz.muni.fi.pv286.mmt.repsentationParser.*;

public class HexParserFuzzTest extends ParserTest {
    @FuzzTest
    void roundTripFuzzTest(FuzzedDataProvider data) {

        byte[] bytes = fixBytesLength(data.consumeBytes(10));
        assertRoundTrip(HexParser.class, bytes);

        bytes = fixBytesLength(data.consumeBytes(100));
        assertRoundTrip(HexParser.class, bytes);

        bytes = fixBytesLength(data.consumeBytes(1000));
        assertRoundTrip(HexParser.class, bytes);

        bytes = fixBytesLength(data.consumeRemainingAsBytes());
        assertRoundTrip(HexParser.class, bytes);
    }

    private byte[] fixBytesLength(byte[] bytes) {
        if (bytes.length % 2 != 0) {

            byte[] newBytes = new byte[bytes.length + 1];
            System.arraycopy(bytes, 0, newBytes, 0, bytes.length);

            return newBytes;
        }

        return bytes;
    }
}
