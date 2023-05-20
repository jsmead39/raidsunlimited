package raidsunlimited.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.models.RequiredRoleModel;


public class RequiredRoleModelConverter implements DynamoDBTypeConverter<String, RequiredRoleModel> {
    private static final Gson GSON = new Gson();
    private final Logger log = LogManager.getLogger();
    @Override
    public String convert(RequiredRoleModel object) {
        return GSON.toJson(object);
    }

    @Override
    public RequiredRoleModel unconvert(String dynamoDbRepresentation) {
        return GSON.fromJson(dynamoDbRepresentation, RequiredRoleModel.class);
    }
}
