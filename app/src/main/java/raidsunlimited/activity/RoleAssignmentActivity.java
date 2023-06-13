package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.RoleAssignmentRequest;
import raidsunlimited.activity.results.RoleAssignmentResult;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.NotRaidOwnerException;
import raidsunlimited.exceptions.RaidSignupException;

import javax.inject.Inject;

public class RoleAssignmentActivity {
    private final Logger log = LogManager.getLogger();
    private final UserRaidDao userRaidDao;
    private final RaidDao raidDao;

    /**
     * Instantiates a new RoleAssignmentActivity
     * @param userRaidDao userRaidDao to access the userRaid table.
     */
    @Inject
    public RoleAssignmentActivity(UserRaidDao userRaidDao, RaidDao raidDao) {
        this.userRaidDao = userRaidDao;
        this.raidDao = raidDao;
    }

    /**
     * Handles a role assignment request.
     * @param roleAssignmentRequest The role assignment request object containing the necessary information.
     * @return The result of the role assignment.
     * @throws RaidSignupException If the raid ID or user ID is not provided, or if the user is not signed up for the raid.
     * @throws NotRaidOwnerException If the requestor is not the raid owner and cannot approve attendees.
     */
    public RoleAssignmentResult handleRequest(final RoleAssignmentRequest roleAssignmentRequest) {
        log.info("Received RoleSignupActivity Request: {}", roleAssignmentRequest);

        String raidId = roleAssignmentRequest.getRaidId();
        if (raidId == null || raidId.isEmpty()) {
            throw new RaidSignupException("Raid ID must be provided");
        }

        String userId = roleAssignmentRequest.getUserId();
        if (userId == null || userId.isEmpty()) {
            throw new RaidSignupException("User ID must be provided");
        }

        RaidEvent raid = raidDao.getRaid(raidId);

        if (!raid.getRaidOwner().equals(roleAssignmentRequest.getRaidOwner())) {
            throw new NotRaidOwnerException("You must be the raid owner to approve attendees");
        }

        UserRaid event = userRaidDao.getUserRaid(userId, raidId);
        if (event == null) {
            throw new RaidSignupException("This user is not signed up for this raid");
        }

        event.setConfirmed(true);
        event.setRole(roleAssignmentRequest.getRaidRole());

        userRaidDao.saveToEvent(event);

        return RoleAssignmentResult.builder()
                .withUserId(userId)
                .withRaidId(raidId)
                .withRaidRole(event.getRole())
                .withStatus(event.isConfirmed())
                .build();
    }
}
