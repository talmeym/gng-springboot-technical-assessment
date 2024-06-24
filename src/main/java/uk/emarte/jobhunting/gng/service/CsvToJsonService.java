package uk.emarte.jobhunting.gng.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CsvToJsonService implements ICsvToJsonService {
    private final List<String> dataToRetain;

    public CsvToJsonService(@Value("${csv.to.json.data.to.retain}") String dataToRetain) {
        this.dataToRetain = List.of(dataToRetain.split(","));
    }

    @Override
    public String convertToJson(ParseResult parseResult) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = mapper.createArrayNode();

        for (int i = 0; i < parseResult.getRowCount(); i++) {
            ObjectNode objectNode = mapper.createObjectNode();

            for (String key : dataToRetain) {
                String value = parseResult.getValue(i, key);

                if (value != null) {
                    objectNode.put(key, value);
                }
            }

            if (objectNode.size() > 0) {
                arrayNode.add(objectNode);
            }
        }

        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayNode);
    }
}

