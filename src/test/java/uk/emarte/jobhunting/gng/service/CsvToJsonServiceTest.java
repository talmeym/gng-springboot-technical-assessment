package uk.emarte.jobhunting.gng.service;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CsvToJsonServiceTest {
    private final CsvToJsonService toTest = new CsvToJsonService("Name");

    @Test()
    public void testCorrectDataRetained() throws IOException {
        List<String> headers = List.of("Name", "Age", "Shoe Size");
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"Eric", "32", "12"});
        rows.add(new String[]{"Douglas", "23", "9"});
        assertEquals("[ {\n  \"Name\" : \"Eric\"\n}, {\n  \"Name\" : \"Douglas\"\n} ]", toTest.convertToJson(new ParseResult(headers, rows)));
    }

    @Test
    public void testEmptyArrayResultsFromDifferingData() throws IOException {
        List<String> headers = List.of("Age", "Shoe Size");
        List<String[]> rows = new ArrayList<>();
        rows.add(new String[]{"32", "12"});
        rows.add(new String[]{"23", "9"});
        assertEquals("[ ]", toTest.convertToJson(new ParseResult(headers, rows)));
    }
}