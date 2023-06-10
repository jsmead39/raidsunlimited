package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.DeleteRaidEventRequest;
import raidsunlimited.activity.results.DeleteRaidEventResult;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.InvalidAttributeException;
import raidsunlimited.exceptions.RaidEventDeletionException;
import raidsunlimited.exceptions.RaidEventNotFoundException;

import java.util.List;

import javax.inject.Inject;


public class DeleteRaidEventActivity {
    private final Logger log = LogManager.getLogger();
    private final RaidDao raidDao;
    private final UserRaidDao userRaidDao;

    /**
     * Instantiates a new DeleteRaidEventActivity.
     * @param raidDao RaidDao to access the raid table.
     * @param userRaidDao UserRaidDao to access the UserRaidDao table.
     */
    @Inject
    public DeleteRaidEventActivity(RaidDao raidDao, UserRaidDao userRaidDao) {
        this.raidDao = raidDao;
        this.userRaidDao = userRaidDao;
    }

    /**
     * Handles a request to delete a specific raid event all UserRaid entries.
     *
     * This method validates a given raidId and email and compared that the raidId exists as well as the raid belongs to
     * the user matching this email.
     * If either do not exist an exception is thrown, otherwise the raid event is deleted from the raid event table.
     * Also checks if the email in the request is the raidOwner email , if it is not it throws an exception.
     *
     * After deleting the raid event the method retrieves a list of all UserRaids from the UserRaid table.
     * Each UserRaid is then deleted from the UserRaid table.
     *
     * @param deleteRaidEventRequest the request object containing the raidId and email.
     * @return a DeleteRaidEventResult object containing the raidId of the deleted raid.
     */
    public DeleteRaidEventResult handleRequest(final DeleteRaidEventRequest deleteRaidEventRequest) {
        String raidId = deleteRaidEventRequest.getRaidId();
        String email = deleteRaidEventRequest.getEmail();

        if (raidId == null || raidId.isEmpty() || email == null || email.isEmpty()) {
            throw new IllegalArgumentException("RaidId and email must be provided");
        }

        //retrieve the raid event from the database, check if it exists
        RaidEvent raid = raidDao.getRaid(raidId);
        if (raid == null) {
            throw new RaidEventNotFoundException("No raid exists with this Id, " + raidId);
        }

        //validates if the email in the request is the raidOwner email
        if (!raid.getRaidOwner().equals(email)) {
            throw new InvalidAttributeException("You must be the owner of a raid to delete it");
        }

        //validates to see if the raid has been completed or not
        if (raid.getRaidStatus().equals("Completed")) {
            throw new RaidEventDeletionException("You cannot delete a raid that was already completed");
        }

        //delete the raidEvent from the raidEvent table

        raidDao.deleteRaid(raidId);

        //retrieves all matching user_raid associates from the user_raid table and removes them as well
        List<UserRaid> userRaidList = userRaidDao.getAllUserRaids(raidId);
        for (UserRaid u : userRaidList) {
            userRaidDao.deleteUserRaidEvent(u);
        }

        return DeleteRaidEventResult.builder()
                .withRaidId(deleteRaidEventRequest.getRaidId())
                .build();
    }
}
