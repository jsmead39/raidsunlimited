package raidsunlimited.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import raidsunlimited.models.GameCharacter;

import java.util.List;

public class GameCharacterConverter implements DynamoDBTypeConverter<String, List<GameCharacter>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(List<GameCharacter> gameCharacterList) {
        try {
            return objectMapper.writeValueAsString(gameCharacterList);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error convertering List<GameCharacter> to string", e);
        }
    }

    @Override
    public List<GameCharacter> unconvert(String gameCharactersJson) {
        try {
            return objectMapper.readValue(gameCharactersJson, new TypeReference<List<GameCharacter>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON string to List<GameCharacter>", e);
        }
    }
}
