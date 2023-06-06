package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.RaidSignupRequest;
import raidsunlimited.activity.results.RaidSignupResult;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.RaidEventNotFoundException;
import raidsunlimited.exceptions.RaidSignupException;
import raidsunlimited.exceptions.UserProfileNotFoundException;
import raidsunlimited.models.ParticipantModel;
import raidsunlimited.models.RaidModel;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class RaidSignupActivity {
    private final Logger log = LogManager.getLogger();
    private final RaidDao raidDao;
    private final UserDao userDao;
    private final UserRaidDao userRaidDao;

    /**
     * Instantiates a new CreateProfileActivity.
     * @param raidDao RaidDao to access the raid table.
     */
    @Inject
    public RaidSignupActivity(RaidDao raidDao, UserDao userDao, UserRaidDao userRaidDao) {
        this.userDao = userDao;
        this.raidDao = raidDao;
        this.userRaidDao = userRaidDao;
    }

    /**
     * Handles the signup of a participant to a raidEvent in DynamoDB
     * @param raidSignupRequest request object containing the raid Signup information
     * @return raidSignupResult result object containing the API defined {@link ParticipantModel}
     */
    public RaidSignupResult handleRequest(final RaidSignupRequest raidSignupRequest) {
        log.info("Received RaidSignupActivity Request: {}", raidSignupRequest);
        log.info(raidSignupRequest.getGameCharacter().getCharClass());
        String raidId = raidSignupRequest.getRaidId();
        String userId = raidSignupRequest.getUserId();

        if (userDao.getUserById(userId) == null || userId.isEmpty()) {
            throw new UserProfileNotFoundException("This user with ID " + userId + "does not exist.");
        }

        if (raidId == null || raidId.isEmpty()) {
            throw new RaidSignupException("Raid ID and User ID must be provided");
        }
        //Retrieve raidEvent from table using raidId
        RaidEvent raid = raidDao.getRaid(raidId);
        //check if raid exists
        if (raid == null) {
            throw new RaidEventNotFoundException("No raid exists with id " + raidId);
        }

        UserRaid userRaid = userRaidDao.getUserRaid(userId, raidId);

        //check if the user is signed up for the raid
        if (userRaid != null) {
            throw new RaidSignupException("User with id " + userId + " is already signed up");
        }

        UserRaid newUserRaid = new UserRaid();
        newUserRaid.setUserId(userId);
        newUserRaid.setRaidId(raidId);
        newUserRaid.setConfirmed(false);

        userRaidDao.saveToEvent(newUserRaid);


        //Create a new participantModel for the user
        ParticipantModel newParticipant = new ModelConverter().toParticipantModel(raidSignupRequest);
        //Add participant to the raid
        List<ParticipantModel> participantsList = raid.getParticipants();
        if (participantsList == null) {
            participantsList = new ArrayList<>();
        }
        participantsList.add(newParticipant);


        raid.setParticipants(participantsList);
        //Save the updated raid to the DB
        raidDao.saveRaid(raid);

        RaidModel raidModel = new ModelConverter().toRaidModel(raid);

        return RaidSignupResult.builder()
                .withRaidModel(raidModel)
                .build();
    }
}
