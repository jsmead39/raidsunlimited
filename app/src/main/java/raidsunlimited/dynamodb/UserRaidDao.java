package raidsunlimited.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.RaidEventNotFoundException;
import raidsunlimited.metrics.MetricsConstants;
import raidsunlimited.metrics.MetricsPublisher;

import javax.inject.Inject;
import javax.inject.Singleton;

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
}
