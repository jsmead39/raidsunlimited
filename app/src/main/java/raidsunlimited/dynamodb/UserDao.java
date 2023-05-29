package raidsunlimited.dynamodb;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import raidsunlimited.dynamodb.models.User;
import raidsunlimited.metrics.MetricsPublisher;

import javax.inject.Inject;
import javax.inject.Singleton;

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
}