package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.GetAllRaidsRequest;
import raidsunlimited.activity.results.GetAllRaidsResult;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.exceptions.RaidEventNotFoundException;
import raidsunlimited.models.RaidModel;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

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

        Optional.ofNullable(getAllRaidsRequest)
                .orElseThrow(() -> new IllegalArgumentException("Received null GetAllRaidsRequest"));

        List<RaidEvent> raidEvents = raidDao.getAllRaidEvents();

        Optional.ofNullable(raidEvents)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new RaidEventNotFoundException("No RaidEvents retrieved from the database"));

        List<RaidModel> raidModels = new ModelConverter().toRaidModelList(raidEvents);

        Optional.ofNullable(raidModels)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new RaidEventNotFoundException("No RaidModels converted from the RaidEvents"));

        return GetAllRaidsResult.builder()
                .withRaidModelList(raidModels)
                .build();
    }
}
