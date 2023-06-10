package raidsunlimited.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import raidsunlimited.models.ParticipantModel;

import java.util.Collections;
import java.util.List;

public class ParticipantModelConverter implements DynamoDBTypeConverter<String, List<ParticipantModel>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(List<ParticipantModel> participantModel) {
        try {
            return objectMapper.writeValueAsString(participantModel);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting Participant to string", e);
        }
    }

    @Override
    public List<ParticipantModel> unconvert(String participantJson) {
        if (participantJson == null || participantJson.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(participantJson, new TypeReference<List<ParticipantModel>>() { });
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON string to ParticipantModel", e);
        }
    }
}

