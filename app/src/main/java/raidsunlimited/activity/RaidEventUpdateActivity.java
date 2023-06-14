package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.CreateRaidRequest;
import raidsunlimited.activity.requests.RaidEventUpdateRequest;
import raidsunlimited.activity.results.CreateRaidResult;
import raidsunlimited.activity.results.RaidEventUpdateResult;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.exceptions.InvalidAttributeException;
import raidsunlimited.exceptions.NotRaidOwnerException;
import raidsunlimited.exceptions.RaidEventNotFoundException;
import raidsunlimited.models.RaidModel;

import javax.inject.Inject;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

public class RaidEventUpdateActivity {
    private final Logger log = LogManager.getLogger();
    private final RaidDao raidDao;


    /**
     * Instantiates a new RaidEventUpdateActivity.
     *
     * @param raidDao RaidDao to access the raid_event table
     */
    @Inject
    public RaidEventUpdateActivity(RaidDao raidDao) {
        this.raidDao = raidDao;
    }

    public RaidEventUpdateResult handleRequest(RaidEventUpdateRequest raidEventUpdateRequest) {
        log.info("Handle request received {}", raidEventUpdateRequest);


        List<String> requiredRoles = raidEventUpdateRequest.getRequiredRoles().entrySet().stream()
                .map(entry -> entry.getKey() + " " + entry.getValue())
                .collect(Collectors.toList());

        RaidEvent raidEvent = raidDao.getRaid(raidEventUpdateRequest.getRaidId());

        if (raidEvent == null) {
            throw new RaidEventNotFoundException("No raid with ID " + raidEventUpdateRequest.getRaidId() + " exists!");
        }

        if (!raidEvent.getRaidOwner().equals(raidEventUpdateRequest.getRaidOwner())) {
            throw new NotRaidOwnerException("You must be the owner of the raid to update it");
        }

        raidEvent.setRaidName(raidEventUpdateRequest.getRaidName());
        raidEvent.setRaidServer(raidEventUpdateRequest.getRaidServer());
        raidEvent.setRaidDate(parseDateToLong(raidEventUpdateRequest.getRaidDate()));
        raidEvent.setTime(raidEventUpdateRequest.getTime());
        raidEvent.setRaidSize(raidEventUpdateRequest.getRaidSize());
        raidEvent.setRaidObjective(raidEventUpdateRequest.getRaidObjective());
        raidEvent.setLootDistribution(raidEventUpdateRequest.getLootDistribution());
        raidEvent.setRequiredRoles(requiredRoles);
        raidEvent.setRaidStatus(raidEventUpdateRequest.getRaidStatus());

        raidDao.saveRaid(raidEvent);

        RaidModel updatedRaidModel = new ModelConverter().toRaidModel(raidEvent);

        return RaidEventUpdateResult.builder()
                .withRaid(updatedRaidModel)
                .build();
    }

    private Long parseDateToLong(String dateString) {
        try {
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
            Instant dateInstant = date.atStartOfDay(ZoneId.of("America/Los_Angeles")).toInstant();
            return dateInstant.getEpochSecond();
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }
    }
}



