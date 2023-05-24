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

    @Inject
    public GetRaidActivity(RaidDao raidDao) {
        this.raidDao = raidDao;
    }

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
