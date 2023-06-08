package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.UserRaidDao;

import javax.inject.Inject;

public class DeleteRaidEventActivity {
    private final Logger log = LogManager.getLogger();
    private final RaidDao raidDao;
    private final UserRaidDao userRaidDao;

    /**
     * Instantiates a new DeleteRaidEventActivity.
     * @param raidDao RaidDao to access the raid table.
     * @param userRaidDao UserRaidDao to access the UserRaidDao table.
     */
    @Inject
    public DeleteRaidEventActivity(RaidDao raidDao, UserRaidDao userRaidDao) {
        this.raidDao = raidDao;
        this.userRaidDao = userRaidDao;
    }
}
