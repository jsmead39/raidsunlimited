package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.RaidEventUpdateRequest;
import raidsunlimited.activity.results.RaidEventUpdateResult;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.exceptions.NotRaidOwnerException;
import raidsunlimited.exceptions.RaidEventCompletionException;
import raidsunlimited.exceptions.RaidEventNotFoundException;
import raidsunlimited.models.RaidModel;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

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

    /**
     * Handles requests to update a specific raid event.
     * <p>
     * Validates if the raid event to be updated exists, if the requester is the owner of the raid event,
     * and if the raid event is not already completed or null.
     * <p>
     * If these conditions are met, the request to update the raid event is processed.
     * The raid event details (such as name, server, date, time, size, objective, loot distribution, required roles,
     * status)
     * can be updated if they are not null and not empty.
     * <p>
     * Once the updates are made, the updated raid event is saved and returned as the result.
     *
     * @param raidEventUpdateRequest The object containing the update details for a raid event.
     * @return RaidEventUpdateResult The result object containing the updated raid event.
     *
     * @throws RaidEventNotFoundException If no raid can be found with the provided ID.
     * @throws NotRaidOwnerException If the user requesting the update is not the owner of the raid.
     * @throws RaidEventCompletionException If the raid event is already completed or null.
     */
    public RaidEventUpdateResult handleRequest(RaidEventUpdateRequest raidEventUpdateRequest) {
        log.info("Handle request received {}", raidEventUpdateRequest);

        RaidEvent raidEvent = raidDao.getRaid(raidEventUpdateRequest.getRaidId());

        if (raidEvent == null) {
            throw new RaidEventNotFoundException("No raid with ID " + raidEventUpdateRequest.getRaidId() + " exists!");
        }

        if (!raidEvent.getRaidOwner().equals(raidEventUpdateRequest.getRaidOwner())) {
            throw new NotRaidOwnerException("You must be the owner of the raid to update it");
        }

        if (raidEvent.getRaidStatus().equals("Completed") || raidEvent.getRaidStatus() == null) {
            throw new RaidEventCompletionException("You cannot modify a raid that was already completed");
        }

        if (raidEventUpdateRequest.getRaidName() != null && !raidEventUpdateRequest.getRaidName().isEmpty()) {
            raidEvent.setRaidName(raidEventUpdateRequest.getRaidName());
        }

        if (raidEventUpdateRequest.getRaidServer() != null && !raidEventUpdateRequest.getRaidServer().isEmpty()) {
            raidEvent.setRaidServer(raidEventUpdateRequest.getRaidServer());
        }

        if (raidEventUpdateRequest.getRaidDate() != null && !raidEventUpdateRequest.getRaidDate().isEmpty()) {
            raidEvent.setRaidDate(parseDateToLong(raidEventUpdateRequest.getRaidDate()));
        }

        if (raidEventUpdateRequest.getTime() != null && !raidEventUpdateRequest.getTime().isEmpty()) {
            raidEvent.setTime(raidEventUpdateRequest.getTime());
        }

        if (raidEventUpdateRequest.getRaidSize() != null) {
            raidEvent.setRaidSize(raidEventUpdateRequest.getRaidSize());
        }

        if (raidEventUpdateRequest.getRaidObjective() != null && !raidEventUpdateRequest.getRaidObjective().isEmpty()) {
            raidEvent.setRaidObjective(raidEventUpdateRequest.getRaidObjective());
        }

        if (raidEventUpdateRequest.getLootDistribution() != null &&
                !raidEventUpdateRequest.getLootDistribution().isEmpty()) {
            raidEvent.setLootDistribution(raidEventUpdateRequest.getLootDistribution());
        }

        if (raidEventUpdateRequest.getRequiredRoles() != null) {
            List<String> requiredRoles = raidEventUpdateRequest.getRequiredRoles().entrySet().stream()
                    .map(entry -> entry.getKey() + " " + entry.getValue())
                    .collect(Collectors.toList());
            raidEvent.setRequiredRoles(requiredRoles);
        }

        if (raidEventUpdateRequest.getRaidStatus() != null && !raidEventUpdateRequest.getRaidStatus().isEmpty()) {
            raidEvent.setRaidStatus(raidEventUpdateRequest.getRaidStatus());
        }

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



