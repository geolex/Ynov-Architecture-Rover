package org.ynov.shared;

import org.junit.jupiter.api.Test;

class IntParserTest {

    @Test
    void tryIntParse_WithgoodString() {
        String test = "123";
        Integer result = IntParser.TryIntParse(test);
        assert result != null;
        assert result == 123;
    }
}
