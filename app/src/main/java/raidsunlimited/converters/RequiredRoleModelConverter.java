package raidsunlimited.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import raidsunlimited.models.RequiredRoleModel;

import java.util.Map;

public class RequiredRoleModelConverter implements DynamoDBTypeConverter<Map<String, AttributeValue>, RequiredRoleModel> {
    @Override
    public Map<String, AttributeValue> convert(RequiredRoleModel object) {
        return null;
    }

    @Override
    public RequiredRoleModel unconvert(Map<String, AttributeValue> object) {
        return null;
    }
}
