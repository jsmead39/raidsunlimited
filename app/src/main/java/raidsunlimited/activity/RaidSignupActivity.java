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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;


public class RaidSignupActivity {
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
    public RaidSignupActivity(RaidDao raidDao, UserDao userDao, UserRaidDao userRaidDao) {
        this.userDao = userDao;
        this.raidDao = raidDao;
        this.userRaidDao = userRaidDao;
    }

    /**
     * Handles the signup of a participant to a raidEvent in DynamoDB.
     * @param raidSignupRequest request object containing the raid Signup information
     * @return raidSignupResult result object containing the API defined {@link ParticipantModel}
     */
    public RaidSignupResult handleRequest(final RaidSignupRequest raidSignupRequest) {
        log.info("Received RaidSignupActivity Request: {}", raidSignupRequest);

        Optional.ofNullable(raidSignupRequest.getGameCharacter())
                .orElseThrow(() -> new RaidSignupException("A character must be provided to sign up for an event"));

        String raidId = Optional.ofNullable(raidSignupRequest.getRaidId())
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new RaidSignupException("Raid ID must be provided"));

        String userId = Optional.ofNullable(raidSignupRequest.getUserId())
                .filter(s -> !s.isEmpty())
                .orElseThrow(() -> new RaidSignupException("User ID must be provided"));

        Optional.ofNullable(userDao.getUserById(userId))
                .orElseThrow(() -> new UserProfileNotFoundException("This user with ID " + userId + "does not exist."));

        //Retrieve raidEvent from table using raidId
        //check if raid exists
        RaidEvent raid = Optional.ofNullable(raidDao.getRaid(raidId))
                .orElseThrow(() -> new RaidEventNotFoundException("No raid exists with id " + raidId));

        //Check if the raid is already completed
        if (raid.getRaidStatus().equals("Completed")) {
            throw new RaidSignupException("You cannot signup for a raid that has already been completed");
        }

        UserRaid userRaid = userRaidDao.getUserRaid(userId, raidId);
        List<ParticipantModel> participantsList = Optional.ofNullable(raid.getParticipants()).orElseGet(ArrayList::new);

        //check if the user is signed up for the raid
        if (userRaid != null || participantsList.stream().anyMatch(p -> p.getUserId().equals(userId))) {
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
