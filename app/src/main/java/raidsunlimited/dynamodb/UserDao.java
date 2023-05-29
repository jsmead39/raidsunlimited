package raidsunlimited.dynamodb;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import raidsunlimited.dynamodb.models.User;
import raidsunlimited.metrics.MetricsPublisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Accesses data for a user using {@link raidsunlimited.dynamodb.models.User} to represent the model in
 * DynamoDB.
 */
@Singleton
public class UserDao {
    private final DynamoDBMapper dynamoDBMapper;
    private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a UserDao object.
     *
     * @param dynamoDBMapper   the {@link DynamoDBMapper} used to interact with the User table
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public UserDao(DynamoDBMapper dynamoDBMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Saves (creates or updates) the given user.
     *
     * @param  user The user to save
     * @return The User object that was saved
     */
    public User saveUser(User user) {
        this.dynamoDBMapper.save(user);
        return user;
    }

    /**
     * Checks if a user with the given email exists in the User table.
     *
     * @param email The email to check
     * @return True if a user with the email exists, false otherwise
     */
    public boolean existsByEmail(String email) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":email", new AttributeValue().withS(email));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("email = :email")
                .withExpressionAttributeValues(expressionAttributeValues);

        List<User> result = dynamoDBMapper.scan(User.class, scanExpression);

        return !result.isEmpty();
    }
}
