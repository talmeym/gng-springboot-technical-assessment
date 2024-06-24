package uk.emarte.jobhunting.gng.service;

import java.io.IOException;

public interface ICsvToJsonService {
    String convertToJson(ParseResult parseResult) throws IOException;
}
