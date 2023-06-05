package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.RaidSignupRequest;
import raidsunlimited.activity.results.RaidSignupResult;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
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

    public RaidSignupResult handleRequest(final RaidSignupRequest raidSignupRequest) {
        log.info("Received RaidSignupActivity Request: {}", raidSignupRequest);

        //Retrieve raidEvent from table using raidId
        RaidEvent raid = raidDao.getRaid(raidSignupRequest.getRaidId());

        //Create Participant model from the request
        ParticipantModel participant = ParticipantModel.builder()
                .withUserId(raidSignupRequest.getUserId())
                .withDisplayName(raidSignupRequest.getDisplayName())
                .withParticipantClass(raidSignupRequest.getGameCharacter().getCharClass())
                .withRole(raidSignupRequest.getGameCharacter().getRole())
                .build();

        //Add the participant to the raid
        List<String> participantsList = raid.getParticipants();
        participantsList.add(participant.toString());
        raid.setParticipants(participantsList);

        //Save the updated raid to the DB
        raidDao.saveRaid(raid);

        return RaidSignupResult.builder()
                .withParticipantModel(participant)
                .build();

    }
}
