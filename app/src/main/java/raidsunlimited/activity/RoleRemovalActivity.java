package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.RoleRemovalRequest;
import raidsunlimited.activity.results.RoleRemovalResult;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.NotRaidOwnerException;
import raidsunlimited.exceptions.RaidEventCompletionException;
import raidsunlimited.exceptions.RaidSignupException;

import javax.inject.Inject;

public class RoleRemovalActivity {
    private final Logger log = LogManager.getLogger();
    private final UserRaidDao userRaidDao;
    private final RaidDao raidDao;

    /**
     * Instantiates a new RoleRemovalActivity.
     * @param userRaidDao userRaidDao to access the userRaid table.
     * @param raidDao raidDao to access the raidDao table.
     */
    @Inject
    public RoleRemovalActivity(UserRaidDao userRaidDao, RaidDao raidDao) {
        this.userRaidDao = userRaidDao;
        this.raidDao = raidDao;
    }

    /**
     * Handles the request to remove a user's role in a raid.
     *
     * <p>This method validates the provided {@code RoleRemovalRequest} to make sure it includes a raid ID and user ID.
     * It then checks if the request has been made by the raid owner and whether the user is signed up for the raid.
     * After validation, the user's raid role is set to an empty string and their confirmation status is set to false.
     * The updated event information is then saved to the database.
     *
     * @param roleRemovalRequest the role removal request, containing raid ID, user ID, and raid owner.
     * @return a RoleRemovalResult with the user ID, raid ID, and the updated confirmation status of the user.
     *
     * @throws RaidSignupException if either the raid ID or user ID is not provided in the request, or if the user is
     * not signed up for the raid.
     * @throws NotRaidOwnerException if the raid owner in the request does not match the raid owner of the raid event.
     */
    public RoleRemovalResult handleRequest(final RoleRemovalRequest roleRemovalRequest) {
        log.info("Received RoleRemovalActivity request: {}", roleRemovalRequest);

        String raidId = roleRemovalRequest.getRaidId();
        if (raidId == null || raidId.isEmpty()) {
            throw new RaidSignupException("Raid ID must be provided");
        }

        String userId = roleRemovalRequest.getUserId();
        if (userId == null || userId.isEmpty()) {
            throw new RaidSignupException("User ID must be provided");
        }

        RaidEvent raid = raidDao.getRaid(raidId);

        if (!raid.getRaidOwner().equals(roleRemovalRequest.getRaidOwner())) {
            throw new NotRaidOwnerException("You must be the raid owner to remove attendees");
        }

        if (raid.getRaidStatus().equals("Completed")) {
            throw new RaidEventCompletionException("You cannot modify a raid that has already been completed");
        }

        UserRaid event = userRaidDao.getUserRaid(userId, raidId);
        if (event == null) {
            throw new RaidSignupException("This user is not signed up for this raid");
        }

        event.setConfirmed(false);
        log.info(event.isConfirmed());
        event.setRole("");

        userRaidDao.saveToEvent(event);

        return RoleRemovalResult.builder()
                .withUserId(userId)
                .withRaidId(raidId)
                .withStatus(event.isConfirmed())
                .build();
    }
}
