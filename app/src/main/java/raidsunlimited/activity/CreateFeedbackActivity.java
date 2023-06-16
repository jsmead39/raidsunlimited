package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.CreateFeedbackRequest;
import raidsunlimited.activity.results.CreateFeedbackResult;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.*;
import raidsunlimited.models.FeedbackModel;
import raidsunlimited.models.ParticipantModel;

import javax.inject.Inject;
import java.util.List;

public class CreateFeedbackActivity {
    private final Logger log = LogManager.getLogger();
    private final UserRaidDao userRaidDao;
    private final RaidDao raidDao;

    /**
     * Instantiates a new CreateFeedbackActivity.
     * @param userRaidDao UserRaidDao to access the userRaid table.
     * @param raidDao RaidDao to access the raid table.
     */
    @Inject
    public CreateFeedbackActivity(UserRaidDao userRaidDao, RaidDao raidDao) {
        this.userRaidDao = userRaidDao;
        this.raidDao = raidDao;
    }

    public CreateFeedbackResult handleRequest(final CreateFeedbackRequest createFeedbackRequest) {
        log.info("Handle Request received {}", createFeedbackRequest);

        String raidId = createFeedbackRequest.getRaidId();
        String userId = createFeedbackRequest.getUserId();

        if (raidId == null || raidId.isEmpty()) {
            throw new InvalidAttributeException("Raid ID cannot be null");
        }

        if (userId == null || userId.isEmpty()) {
            throw new InvalidAttributeException("User ID cannot be null");
        }

        RaidEvent raid = raidDao.getRaid(raidId);
        if (raid == null) {
            throw new RaidEventNotFoundException("No raid found with ID " + raidId);
        }

        if (!raid.getRaidStatus().equals("Completed")) {
            throw new RaidEventCompletionException("You cannot provide feedback on a raid that hasn't been completed.");
        }

        UserRaid raidStatus = userRaidDao.getUserRaid(userId, raidId);
        if (!raidStatus.isConfirmed()) {
            throw new RaidSignupException("You must have attended the raid to leave feedback.");
        }

        List<FeedbackModel> feedbackModelList = raid.getFeedback();

        boolean isDuplicateFeedback = feedbackModelList.stream()
                .anyMatch(feedback -> feedback.getUserId().equals(userId));

        if (isDuplicateFeedback) {
            throw new DuplicateFeedbackException("Duplicate feedback by the same user");
        }

        FeedbackModel feedback = new FeedbackModel.Builder().withUserId(userId)
                        .withComments(createFeedbackRequest.getComments())
                        .withRating(createFeedbackRequest.getRating())
                        .build();

        feedbackModelList.add(feedback);
        raid.setFeedback(feedbackModelList);

        raidDao.saveRaid(raid);

        return CreateFeedbackResult.builder()
                .withFeedback(feedback)
                .build();
    }
}
