package raidsunlimited.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import raidsunlimited.models.FeedbackModel;

import java.util.Map;

public class FeedBackModelConverter implements DynamoDBTypeConverter<Map<String, AttributeValue>, FeedbackModel> {
    @Override
    public Map<String, AttributeValue> convert(FeedbackModel object) {
        return null;
    }

    @Override
    public FeedbackModel unconvert(Map<String, AttributeValue> object) {
        return null;
    }
}
