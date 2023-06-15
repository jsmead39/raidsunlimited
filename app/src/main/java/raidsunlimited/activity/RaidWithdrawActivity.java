package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.RaidWithdrawRequest;
import raidsunlimited.activity.results.RaidWithdrawResult;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.RaidEventNotFoundException;
import raidsunlimited.exceptions.RaidWithdrawException;
import raidsunlimited.exceptions.UserProfileNotFoundException;
import raidsunlimited.models.ParticipantModel;
import raidsunlimited.models.RaidModel;

import javax.inject.Inject;
import java.util.List;

public class RaidWithdrawActivity {
    private final Logger log = LogManager.getLogger();
    private final RaidDao raidDao;
    private final UserDao userDao;
    private final UserRaidDao userRaidDao;

    /**
     * Instantiates a new CreateProfileActivity.
     * @param userDao UserDao to access the user table.
     * @param raidDao RaidDao to access the raid table.
     * @param userRaidDao UserRaidDao to access the userRaid table.
     */
    @Inject
    public RaidWithdrawActivity(RaidDao raidDao, UserDao userDao, UserRaidDao userRaidDao) {
        this.userDao = userDao;
        this.raidDao = raidDao;
        this.userRaidDao = userRaidDao;
    }


    /**
     * Handles the signup of a participant to a raidEvent in DynamoDB.
     * @param raidWithdrawRequest request object containing the raid information
     * @return raidSignupResult result object containing the API defined {@link ParticipantModel}
     */
    public RaidWithdrawResult handleRequest(final RaidWithdrawRequest raidWithdrawRequest) {
        log.info("Received RaidWithdrawRequest Request: {}", raidWithdrawRequest);

        String raidId = raidWithdrawRequest.getRaidId();
        String userId = raidWithdrawRequest.getUserId();

        if (raidId == null || raidId.isEmpty()) {
            throw new RaidWithdrawException("Raid ID must be provided to withdraw from a raid.");
        }

        if (userId == null || userId.isEmpty()) {
            throw new RaidWithdrawException("User ID must be provided to withdraw from a raid.");
        }

        if (userDao.getUserById(userId) == null) {
            throw new UserProfileNotFoundException("User with ID " + userId + " does not exist.");
        }

        RaidEvent raid = raidDao.getRaid(raidId);

        if (raid == null) {
            throw new RaidEventNotFoundException("No raid exists with id " + raidId);
        }

        if (raid.getRaidStatus().equals("Completed")) {
            throw new RaidWithdrawException("You cannot withdraw from a raid that has already been completed");
        }

        UserRaid userRaid = userRaidDao.getUserRaid(userId, raidId);
        userRaidDao.deleteUserRaidEvent(userRaid);

        List<ParticipantModel> participants = raid.getParticipants();

        //Find the participant in the list
        ParticipantModel participantToRemove = null;
        for (ParticipantModel p : participants) {
            if (p.getUserId().equals(userId)) {
                participantToRemove = p;
                break;
            }
        }

        //Remove the participant from the list
        if (participantToRemove != null) {
            participants.remove(participantToRemove);
        } else {
            throw new RaidWithdrawException("Participant with user ID " + userId + " was not found in the raid " +
                    "participants.");
        }

        //Update the raid with the new list of participants
        raid.setParticipants(participants);
        raidDao.saveRaid(raid);

        RaidModel raidModel = new ModelConverter().toRaidModel(raid);

        return RaidWithdrawResult.builder()
                .withRaidModel(raidModel)
                .build();
    }
}
