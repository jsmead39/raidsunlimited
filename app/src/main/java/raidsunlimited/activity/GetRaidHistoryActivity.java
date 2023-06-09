package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.GetRaidHistoryRequest;
import raidsunlimited.activity.results.GetRaidHistoryResult;
import raidsunlimited.dynamodb.UserRaidDao;

import javax.inject.Inject;

/**
 * Implementation of the GetRaidActivity for the RaidsUnlimited GetRaid API.
 *
 * This API allows the customer to get a history of raids for a user.
 */
public class GetRaidHistoryActivity {
    private final Logger log = LogManager.getLogger();
    private final UserRaidDao userRaidDao;

    @Inject
    public GetRaidHistoryActivity(UserRaidDao userRaidDao) {
        this.userRaidDao = userRaidDao;
    }

    public GetRaidHistoryResult handleRequest(final GetRaidHistoryRequest getRaidHistoryRequest) {
        return GetRaidHistoryResult.builder()
                .withRaidModelList()
                .build();
    }
}
