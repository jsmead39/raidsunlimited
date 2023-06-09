package raidsunlimited.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.RaidEventNotFoundException;
import raidsunlimited.metrics.MetricsConstants;
import raidsunlimited.metrics.MetricsPublisher;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Accesses the user_Raid table to store information on what raids a user has signed up for and if they are confirmed.
 */
@Singleton
public class UserRaidDao {
    private final DynamoDBMapper dynamoDBMapper;
    private final MetricsPublisher metricsPublisher;

    /**
     *
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with the raidevents table
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public UserRaidDao(DynamoDBMapper dynamoDBMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * Add or updates a record in the user_raid table
     * @param userRaid The userRaid objects representing the record to be added.
     * @return
     */
    public UserRaid saveToEvent(UserRaid userRaid) {
        this.dynamoDBMapper.save(userRaid);
        return userRaid;
    }

    /**
     * Retrieves a record from the user_raid table using userId and raidId
     * @param userId the user ID
     * @param raidId the raid ID
     * @return the UserRaid object representing the retrieved record, or null if no record exists
     */
    public UserRaid getUserRaid(String userId, String raidId) {
        return dynamoDBMapper.load(UserRaid.class, userId, raidId);
    }

    /**
     * Retrieves a list of UserRaid objects from the UserRaid table that match the raidId
     * @param raidId The raidId matching the events to be retrieved from the table.
     * @return a List of UserRaid objects matching the criteria specified.
     */
    public List<UserRaid> getAllUserRaids(String raidId) {
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        expressionAttributeValues.put(":raidId", new AttributeValue().withS(raidId));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("raidId = :raidId")
                .withExpressionAttributeValues(expressionAttributeValues);

        return this.dynamoDBMapper.scan(UserRaid.class, scanExpression);
    }

    /**
     * Deletes a UserRaid event from the database given the UserRaid object
     * @param userRaid the object containing the UserRaid Info.
     */
    public void deleteUserRaidEvent(UserRaid userRaid) {
        this.dynamoDBMapper.delete(userRaid);
    }
}
