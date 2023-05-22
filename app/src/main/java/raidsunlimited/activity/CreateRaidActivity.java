package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.CreateRaidRequest;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.models.RaidModel;
import raidsunlimited.utils.ServiceUtils;

import javax.inject.Inject;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class CreateRaidActivity {
    private final Logger log = LogManager.getLogger();
    private final RaidDao raidDao;


    /**
     * Instantiates a new CreateRaidActivity
     * @param raidDao RaidDao to access the raid_event table
     */
    @Inject
    public CreateRaidActivity(RaidDao raidDao) {
        this.raidDao = raidDao;
    }

    public CreateRaidActivity handleRequest(final CreateRaidRequest createRaidRequest) {
        log.info("Received CreateRaidActivity Request{}", createRaidRequest);



        RaidEvent raidEvent = new RaidEvent();
        raidEvent.setRaidId(ServiceUtils.generateRandomId());
        raidEvent.setRaidName(createRaidRequest.getRaidName());
        raidEvent.setRaidDate(parseDateToLong(createRaidRequest.getRaidDate()));
        raidEvent.setTime(createRaidRequest.getTime());
        raidEvent.setRaidSize(createRaidRequest.getRaidSize());
        raidEvent.setRaidObjective(createRaidRequest.getRaidObjective());
        raidEvent.setLootDistribution(createRaidRequest.getLootDistribution());
        raidEvent.setRequiredRoles(createRaidRequest.getRequiredRoles());
        raidEvent.setParticipants(new ArrayList<>());
        raidEvent.setFeedback(new ArrayList<>());
        raidEvent.setRaidOwner(createRaidRequest.getRaidOwner());
        raidEvent.setRaidStatus("Pending");

        raidDao.saveRaid(raidEvent);

        RaidModel raidModel = new ModelConverter().toRaidModel(raidEvent);

        return CreateRaidResult.builder()
                .withRaidEvent(raidModel)
                .build();
    }

    private Long parseDateToLong(String dateString) {
        try {
            Instant dateInstant = Instant.parse(dateString);
            return dateInstant.toEpochMilli();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }
    }
}
