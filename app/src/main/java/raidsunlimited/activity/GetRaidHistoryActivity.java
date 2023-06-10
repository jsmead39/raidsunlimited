package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.GetRaidHistoryRequest;
import raidsunlimited.activity.results.GetRaidHistoryResult;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.InvalidAttributeException;
import raidsunlimited.exceptions.UserProfileNotFoundException;
import raidsunlimited.models.RaidModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

/**
 * Implementation of the GetRaidActivity for the RaidsUnlimited GetRaid API.
 *
 * This API allows the customer to get a history of raids for a user.
 */
public class GetRaidHistoryActivity {
    private final Logger log = LogManager.getLogger();
    private final UserRaidDao userRaidDao;
    private final RaidDao raidDao;
    private final UserDao userDao;

    /**
     * Instantiates a new GetRaidHistoryActivity object.
     * @param userRaidDao UserRaidDao to access the userRaid table.
     * @param raidDao RaidDao to access the raidEvent table.
     * @param userDao UserDao to access the User table.
     */
    @Inject
    public GetRaidHistoryActivity(UserRaidDao userRaidDao, RaidDao raidDao, UserDao userDao) {
        this.userRaidDao = userRaidDao;
        this.raidDao = raidDao;
        this.userDao = userDao;
    }

    /**
     * Processes GetRaidHistoryRequest to fetch a list of UserRaid objects from the database.
     * Then, retrieves related RaidEvent information and converts them to a list of RaidModel.
     *
     * If the UserRaid list associated with the userId is empty or null an empty list is created.
     * If the conversion of RaidEvents to RaidModels results in an empty or null list an empty list is returned.
     * If the userId is not provided in the request, an exception is thrown.
     * @param getRaidHistoryRequest the request object that contains the userId for which the raid history is retrieved.
     * @return a GetRaidHistoryResult object containing a list of RaidModel, each representing a raid the user has
     * participated in.
     */
    public GetRaidHistoryResult handleRequest(final GetRaidHistoryRequest getRaidHistoryRequest) {
        log.info("Handle Request received", getRaidHistoryRequest);

        String userId = Optional.ofNullable(getRaidHistoryRequest.getUserId())
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new InvalidAttributeException("User ID must be provided"));

        if (userDao.getUserById(userId) == null) {
            throw new UserProfileNotFoundException("The specified user with ID " + userId + " does not exist.");
        }

        List<UserRaid> userRaidList = Optional.ofNullable(userRaidDao.getAllUserRaidsByUserId(userId))
                .filter(list -> !list.isEmpty())
                .orElse(new ArrayList<>());

        List<RaidEvent> raidEvents = new ArrayList<>();

        for (UserRaid u : userRaidList) {
            String raidId = u.getRaidId();
            raidEvents.add(raidDao.getRaid(raidId));
        }

        List<RaidModel> raidModel = new ModelConverter().toRaidModelList(raidEvents);

        raidModel = Optional.ofNullable(raidModel)
                .filter(list -> !list.isEmpty())
                .orElse(Collections.emptyList());

        return GetRaidHistoryResult.builder()
                .withRaidModelList(raidModel)
                .build();
    }
}
