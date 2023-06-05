package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.RaidSignupRequest;
import raidsunlimited.activity.results.RaidSignupResult;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.exceptions.RaidEventNotFoundException;
import raidsunlimited.exceptions.RaidSignupException;
import raidsunlimited.models.ParticipantModel;

import javax.inject.Inject;
import java.util.List;


public class RaidSignupActivity {
    private final Logger log = LogManager.getLogger();
    private final RaidDao raidDao;

    /**
     * Instantiates a new CreateProfileActivity.
     * @param raidDao RaidDao to access the raid table.
     */
    @Inject
    public RaidSignupActivity(RaidDao raidDao) {
        this.raidDao = raidDao;
    }

    /**
     * Handles the signup of a participant to a raidEvent in DynamoDB
     * @param raidSignupRequest request object containing the raid Signup information
     * @return raidSignupResult result object containing the API defined {@link ParticipantModel}
     */
    public RaidSignupResult handleRequest(final RaidSignupRequest raidSignupRequest) {
        log.info("Received RaidSignupActivity Request: {}", raidSignupRequest);

        String raidId = raidSignupRequest.getRaidId();
        String userId = raidSignupRequest.getUserId();

        if (raidId == null || raidId.isEmpty() || userId == null || userId.isEmpty()) {
            throw new RaidSignupException("Raid ID and User ID must be provided");
        }

        //Retrieve raidEvent from table using raidId
        RaidEvent raid = raidDao.getRaid(raidId);
        //check if raid exists
        if (raid == null) {
            throw new RaidEventNotFoundException("No raid exists with id " + raidId);
        }


        //Create Participant model from the request
        ParticipantModel participant = ParticipantModel.builder()
                .withUserId(raidSignupRequest.getUserId())
                .withDisplayName(raidSignupRequest.getDisplayName())
                .withParticipantClass(raidSignupRequest.getGameCharacter().getCharClass())
                .withRole(raidSignupRequest.getGameCharacter().getRole())
                .build();

        //Check if user is signed up
        List<String> participantsList = raid.getParticipants();
        if(participantsList.contains(userId)) {
            throw new RaidSignupException("User with id " + userId + "is already signed up");
        }

        //Add participant to the raid
        participantsList.add(participant.toString());
        raid.setParticipants(participantsList);

        //Save the updated raid to the DB
        raidDao.saveRaid(raid);

        return RaidSignupResult.builder()
                .withParticipantModel(participant)
                .build();

    }
}
