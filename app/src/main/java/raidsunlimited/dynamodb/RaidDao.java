package raidsunlimited.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.exceptions.RaidEventNotFoundException;
import raidsunlimited.metrics.MetricsConstants;
import raidsunlimited.metrics.MetricsPublisher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Accesses data for a raidevent using {@link RaidEvent} to represent the model in
 * DynamoDB.
 */
@Singleton
public class RaidDao {
    private final DynamoDBMapper dynamoDBMapper;
    private final MetricsPublisher metricsPublisher;

    /**
     *
     * @param dynamoDBMapper the {@link DynamoDBMapper} used to interact with the raidevents table
     * @param metricsPublisher the {@link MetricsPublisher} used to record metrics.
     */
    @Inject
    public RaidDao(DynamoDBMapper dynamoDBMapper, MetricsPublisher metricsPublisher) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     *
     * @param raidEvent the raidEvent objected to be saved in DynamoDB.
     * @return a representation of the raidEvent
     */
    public RaidEvent saveRaid(RaidEvent raidEvent) {
        this.dynamoDBMapper.save(raidEvent);
        return raidEvent;
    }

    /**
     *
     * @param id the raidEvent Id to be retrieved from the DynamoDB.
     * @return an instance of a RaidEvent with all the fields from the database.
     */
    public RaidEvent getRaid(String id) {
        RaidEvent raid = this.dynamoDBMapper.load(RaidEvent.class, id);

        if (raid == null) {
            metricsPublisher.addCount(MetricsConstants.GETRAID_RAIDNOTFOUND_COUNT, 1);
            throw new RaidEventNotFoundException("No raid exists with id " + id);
        }
        metricsPublisher.addCount(MetricsConstants.GETRAID_RAIDNOTFOUND_COUNT, 0);
        return raid;
    }

    /**
     * Retrieves all raid events from the database and sorts them in descending order.
     * @return a List of RaidEvent objects, sorted in descending order by date.
     */
    public List<RaidEvent> getAllRaidEvents() {

        //Scan the table for all RaidEvents
        PaginatedScanList<RaidEvent> result = this.dynamoDBMapper.scan(RaidEvent.class, new DynamoDBScanExpression());

        if (result == null) {
            return new ArrayList<>();
        }
        //Convert the scan to a regular ArrayList
        List<RaidEvent> raidEvents = new ArrayList<>(result);

        //Sort the raid List descending by Date
        raidEvents.sort(Comparator.comparing(RaidEvent::getRaidDate).reversed());

        return raidEvents;
    }

    /**
     * Deletes a raidEvent from the dynamoDB table.
     * @param raidId the raidId specifying which object to be removed.
     */
    public void deleteRaid(String raidId) {
        RaidEvent raid = new RaidEvent();
        raid.setRaidId(raidId);
        this.dynamoDBMapper.delete(raid);
    }
}
