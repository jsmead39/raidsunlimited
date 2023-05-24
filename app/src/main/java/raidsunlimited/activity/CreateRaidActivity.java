package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.CreateRaidRequest;
import raidsunlimited.activity.results.CreateRaidResult;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.models.RaidModel;
import raidsunlimited.utils.ServiceUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class CreateRaidActivity {
    private final Logger log = LogManager.getLogger();
    private final RaidDao raidDao;


    /**
     * Instantiates a new CreateRaidActivity.
     * @param raidDao RaidDao to access the raid_event table
     */
    @Inject
    public CreateRaidActivity(RaidDao raidDao) {
        this.raidDao = raidDao;
    }

    /**
     * Handles the creation of a Raid and stores it calls to DynamoDB to store it.
     * @param createRaidRequest this is the request object passed in by our lambda
     * @return a CreateRaidResult that will be passed back to our front end
     */
    public CreateRaidResult handleRequest(final CreateRaidRequest createRaidRequest) {
        log.info("Received CreateRaidActivity Request{}", createRaidRequest);

        List<String> requiredRoles = createRaidRequest.getRequiredRoles().entrySet().stream()
                .map(entry -> entry.getKey() + " " + entry.getValue())
                .collect(Collectors.toList());


        RaidEvent raidEvent = new RaidEvent();
        raidEvent.setRaidId(ServiceUtils.generateRandomId());
        raidEvent.setRaidName(createRaidRequest.getRaidName());
        raidEvent.setRaidServer(createRaidRequest.getRaidServer());
        raidEvent.setRaidDate(parseDateToLong(createRaidRequest.getRaidDate()));
        raidEvent.setTime(createRaidRequest.getTime());
        raidEvent.setRaidSize(createRaidRequest.getRaidSize());
        raidEvent.setRaidObjective(createRaidRequest.getRaidObjective());
        raidEvent.setLootDistribution(createRaidRequest.getLootDistribution());
        raidEvent.setRequiredRoles(requiredRoles);
        raidEvent.setParticipants(new ArrayList<>());
        raidEvent.setFeedback(new ArrayList<>());
        raidEvent.setRaidOwner(createRaidRequest.getRaidOwner());
        raidEvent.setRaidStatus("Pending");

        raidDao.saveRaid(raidEvent);

        RaidModel raidModel = new ModelConverter().toRaidModel(raidEvent);

        return CreateRaidResult.builder()
                .withRaid(raidModel)
                .build();
    }

    private Long parseDateToLong(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
            Instant dateInstant = date.atStartOfDay(ZoneId.of("America/Los_Angeles")).toInstant();
            return dateInstant.toEpochMilli();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }
    }
}
