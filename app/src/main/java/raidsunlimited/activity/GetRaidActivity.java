package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.GetRaidRequest;
import raidsunlimited.activity.results.GetRaidResult;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.models.RaidModel;

import javax.inject.Inject;

/**
 * Implementation of the GetRaidActivity for the RaidsUnlimited GetRaid API.
 *
 * This API allows the customer to get a saved raid.
 */
public class GetRaidActivity {
    private final Logger log = LogManager.getLogger();
    private final RaidDao raidDao;

    /**
     * Instantiates a new GetRaidActivity object.
     * @param raidDao the RaidDao to access the raidevent table.
     */
    @Inject
    public GetRaidActivity(RaidDao raidDao) {
        this.raidDao = raidDao;
    }

    /**
     * Handles a GetRaidRequest and retrieves the corresponding RaidModel.
     *
     * This method takes a GetRaidRequest object, retrieves the RaidEvent based on the requested
     * raidId from the raidDao.
     * converts the RaidEvent to a RaidModel using a ModelConverter, and returns a GetRaidResult object
     * with the retrieved RaidModel.
     *
     * @param getRaidRequest the GetRaidRequest object containing the raidId
     * @return a GetRaidResult object with the retrieved RaidModel
     */
    public GetRaidResult handleRequest(final GetRaidRequest getRaidRequest) {
        log.info("Received GetRaidRequest {}", getRaidRequest);
        String requestedId = getRaidRequest.getRaidId();
        RaidEvent event = raidDao.getRaid(requestedId);
        RaidModel raidModel = new ModelConverter().toRaidModel(event);

        return GetRaidResult.builder()
                .withRaidModel(raidModel)
                .build();
    }
}
