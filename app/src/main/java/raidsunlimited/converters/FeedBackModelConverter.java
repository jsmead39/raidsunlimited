package raidsunlimited.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.models.FeedbackModel;


public class FeedBackModelConverter implements DynamoDBTypeConverter<String, FeedbackModel> {
    private static final Gson GSON = new Gson();
    private final Logger log = LogManager.getLogger();
    @Override
    public String convert(FeedbackModel object) {
        return GSON.toJson(object);
    }

    @Override
    public FeedbackModel unconvert(String dynamoDbRepresentation) {
        return GSON.fromJson(dynamoDbRepresentation, FeedbackModel.class);
    }
}
