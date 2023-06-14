package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.CreateRaidRequest;
import raidsunlimited.activity.requests.RaidEventUpdateRequest;
import raidsunlimited.activity.results.CreateRaidResult;
import raidsunlimited.activity.results.RaidEventUpdateResult;
import raidsunlimited.dynamodb.RaidDao;

import javax.inject.Inject;

public class RaidEventUpdateActivity {
    private final Logger log = LogManager.getLogger();
    private final RaidDao raidDao;


    /**
     * Instantiates a new RaidEventUpdateActivity.
     *
     * @param raidDao RaidDao to access the raid_event table
     */
    @Inject
    public RaidEventUpdateActivity(RaidDao raidDao) {
        this.raidDao = raidDao;
    }

    public RaidEventUpdateResult handleRequest(RaidEventUpdateRequest raidEventUpdateRequest) {

    }
}



