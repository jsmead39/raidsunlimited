package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.GetAllRaidsRequest;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.models.RaidModel;

import javax.inject.Inject;
import java.util.List;

/**
 * Implementation of the GetRaidActivity for the RaidsUnlimited GetRaid API.
 *
 * This API allows the customer to get all raid objects in the database.
 */
public class GetAllRaidsActivity {
    private final Logger log = LogManager.getLogger();
    private final RaidDao raidDao;

    @Inject
    public GetAllRaidsActivity(RaidDao raidDao) {
        this.raidDao = raidDao;
    }


    public GetAllRaidsResult handleRequest(final GetAllRaidsRequest getAllRaidsRequest) {
        log.info("Received GetAllRaidsRequest {}", getAllRaidsRequest);

        List<RaidEvent> raidEvents = raidDao.getAllRaidEvents();

        List<RaidModel> raidModels = new ModelConverter().toRaidModelList(raidEvents);

        return GetAllRaidsResult.builder()
                .withRaidList(raidModels)
                .build();
    }
}
