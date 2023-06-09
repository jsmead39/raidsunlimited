package raidsunlimited.converters;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import raidsunlimited.models.FeedbackModel;
import raidsunlimited.models.GameCharacter;
import raidsunlimited.models.ParticipantModel;

import java.util.Collections;
import java.util.List;

public class FeedbackModelConverter implements DynamoDBTypeConverter<String, List<FeedbackModel>> {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convert(List<FeedbackModel> feedbackModel) {
        try {
            return objectMapper.writeValueAsString(feedbackModel);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting Participant to string", e);
        }
    }

    @Override
    public List<FeedbackModel> unconvert(String participantJson) {
        if (participantJson == null || participantJson.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(participantJson, new TypeReference<List<FeedbackModel>>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting JSON string to ParticipantModel", e);
        }
    }
}

