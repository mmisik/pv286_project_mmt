package cz.muni.fi.pv286.mmt.representation.fuzzing;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.code_intelligence.jazzer.junit.FuzzTest;
import cz.muni.fi.pv286.mmt.exceptions.InvalidIntCountException;
import cz.muni.fi.pv286.mmt.exceptions.InvalidIntInputException;
import cz.muni.fi.pv286.mmt.representation.*;

import java.io.IOException;

class IntParserFuzzTest extends ParserFuzzTest {
    @FuzzTest
    void roundTripFuzzTest(FuzzedDataProvider data) throws IOException {
        try {
            int fuzzData = data.consumeInt();
            String str = Integer.toString(fuzzData);
            byte[] bytes = str.getBytes();
            assertRoundTrip(IntParser.class, bytes);
        } catch (InvalidIntInputException | InvalidIntCountException e) {
            // Ignore
        }
    }
}
