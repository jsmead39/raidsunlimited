package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.RoleAssignmentRequest;
import raidsunlimited.activity.results.RoleAssignmentResult;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.RaidEventNotFoundException;
import raidsunlimited.exceptions.RaidSignupException;

import javax.inject.Inject;
import java.util.Optional;

public class RoleAssignmentActivity {
    private final Logger log = LogManager.getLogger();
    private final UserRaidDao userRaidDao;

    /**
     * Instantiates a new RoleAssignmentActivity
     * @param userRaidDao userRaidDao to access the userRaid table.
     */
    @Inject
    public RoleAssignmentActivity(UserRaidDao userRaidDao) {
        this.userRaidDao = userRaidDao;
    }

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
                .withRaidRole(event.getRole)
                .withStatus(event.getConfirmed())
                .build();
    }
}
