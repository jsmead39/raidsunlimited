package raidsunlimited.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import javax.inject.Singleton;

/**
 * Accesses data for a raidevent using {@Link RaidEvent} to represent the model in DynamoDB.
 */
@Singleton
public class RaidDao {
    private final DynamoDBMapper dynamoDBMapper;
    private final MetricsPublisher metricsPublisher;
}
