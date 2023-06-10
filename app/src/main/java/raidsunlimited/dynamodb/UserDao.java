package raidsunlimited.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import raidsunlimited.dynamodb.models.User;
import raidsunlimited.exceptions.UserProfileNotFoundException;
import raidsunlimited.metrics.MetricsConstants;
import raidsunlimited.metrics.MetricsPublisher;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for a user using {@link User} to represent the model in
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

    /**
     * This method retrieves a User object from the database using its id.
     *
     * @param id The unique identifier (userId) of the user to be retrieved.
     * @return A User object representing the user with the provided id.
     * @throws UserProfileNotFoundException If no user is found with the provided id.
     */
    public User getUserById(String id) {
        User user = this.dynamoDBMapper.load(User.class, id);

        if (user == null) {
            metricsPublisher.addCount(MetricsConstants.GETPROFILE_PROFILENOTFOUND_COUNT, 1);
            throw new UserProfileNotFoundException("No profile exists with id " + id);
        }
        metricsPublisher.addCount(MetricsConstants.GETPROFILE_PROFILENOTFOUND_COUNT, 0);
        return user;
    }

    /**
     * This method retrieves a User object from the database using its email.
     * This method uses the secondary index "EmailIndex" to fetch the user.
     *
     * @param email The email of the user to be retrieved.
     * @return A User object representing the user with the provided email.
     * @throws UserProfileNotFoundException If no user is found with the provided email.
     */
    public User getUserByEmail(String email) {
        DynamoDBQueryExpression<User> queryExpression = new DynamoDBQueryExpression<User>()
                .withIndexName("EmailIndex")
                .withConsistentRead(false)
                .withKeyConditionExpression("email = :email")
                .withExpressionAttributeValues(
                        Collections.singletonMap(":email", new AttributeValue().withS(email))
                );

        PaginatedQueryList<User> results = this.dynamoDBMapper.query(User.class, queryExpression);


        if (results.isEmpty()) {
            metricsPublisher.addCount(MetricsConstants.GETPROFILE_PROFILENOTFOUND_COUNT, 1);
            throw new UserProfileNotFoundException("No profile exists with email " + email);
        }
        metricsPublisher.addCount(MetricsConstants.GETPROFILE_PROFILENOTFOUND_COUNT, 0);
        return results.get(0);
    }
}
