package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.CreateFeedbackRequest;
import raidsunlimited.activity.results.CreateFeedbackResult;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.FeedbackSubmissionException;
import raidsunlimited.exceptions.InvalidAttributeException;
import raidsunlimited.exceptions.RaidEventNotFoundException;
import raidsunlimited.models.FeedbackModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class CreateFeedbackActivityTest {
    @Mock
    UserRaidDao userRaidDao;

    @Mock
    RaidDao raidDao;

    private CreateFeedbackActivity createFeedbackActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        createFeedbackActivity = new CreateFeedbackActivity(userRaidDao, raidDao);
    }

    @Test
    void handleRequest_validInput_createsNewFeedback() {
        //GIVEN-
        String testRaidId = "testRaidId";
        String testUserId = "testUserId";
        String testComment = "testComment";
        int testRating = 4;

        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .withRaidId(testRaidId)
                .withUserId(testUserId)
                .withComments(testComment)
                .withRating(testRating)
                .build();

        RaidEvent testRaidEvent = new RaidEvent();
        testRaidEvent.setRaidStatus("Completed");
        testRaidEvent.setFeedback(new ArrayList<>());

        UserRaid userRaid = new UserRaid();
        userRaid.setConfirmed(true);

        when(raidDao.getRaid(testRaidId)).thenReturn(testRaidEvent);
        when(userRaidDao.getUserRaid(testUserId, testRaidId)).thenReturn(userRaid);

        //WHEN -
        CreateFeedbackResult result = createFeedbackActivity.handleRequest(request);

        //THEN-
        verify(raidDao).saveRaid(any(RaidEvent.class));
        assertEquals(result.getFeedbackModel().getUserId(), testUserId);
        assertEquals(result.getFeedbackModel().getComments(), testComment);
        assertEquals(result.getFeedbackModel().getRating(), testRating);
    }

    @Test
    void handleRequest_nullRaidId_throwsException() {
        // GIVEN
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .withRaidId(null)
                .build();

        // WHEN AND THEN
        assertThrows(InvalidAttributeException.class, () -> createFeedbackActivity.handleRequest(request),
                "Raid ID cannot be null");
    }

    @Test
    void handleRequest_nullUserId_throwsException() {
        // GIVEN
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .withUserId(null)
                .build();

        // WHEN AND THEN
        assertThrows(InvalidAttributeException.class, () -> createFeedbackActivity.handleRequest(request),
                "User ID cannot be null");
    }

    @Test
    void handleRequest_raidNotFound_throwsException() {
        // GIVEN
        String testRaidId = "testRaidId";
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .withRaidId(testRaidId)
                .withUserId("testUserId")
                .build();

        when(raidDao.getRaid(testRaidId)).thenReturn(null);

        // WHEN AND THEN
        assertThrows(RaidEventNotFoundException.class, () -> createFeedbackActivity.handleRequest(request),
                "No raid found with ID " + testRaidId);
    }

    @Test
    void handleRequest_raidNotCompleted_throwsException() {
        // GIVEN
        String testRaidId = "testRaidId";
        CreateFeedbackRequest request = CreateFeedbackRequest.builder().
                withRaidId(testRaidId)
                .withUserId("testUserId")
                .build();

        RaidEvent testRaidEvent = new RaidEvent();
        testRaidEvent.setRaidStatus("Scheduled");

        when(raidDao.getRaid(testRaidId)).thenReturn(testRaidEvent);

        // WHEN AND THEN
        assertThrows(FeedbackSubmissionException.class, () -> createFeedbackActivity.handleRequest(request),
                "You cannot provide feedback on a raid that hasn't been completed.");
    }

    @Test
    void handleRequest_userDidNotAttendRaid_throwsException() {
        // GIVEN
        String testRaidId = "testRaidId";
        String testUserId = "testUserId";
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .withRaidId(testRaidId).
                withUserId(testUserId)
                .build();

        RaidEvent testRaidEvent = new RaidEvent();
        testRaidEvent.setRaidStatus("Completed");

        UserRaid userRaid = new UserRaid();
        userRaid.setConfirmed(false);

        when(raidDao.getRaid(testRaidId)).thenReturn(testRaidEvent);
        when(userRaidDao.getUserRaid(testUserId, testRaidId)).thenReturn(userRaid);

        // WHEN AND THEN
        assertThrows(FeedbackSubmissionException.class, () -> createFeedbackActivity.handleRequest(request),
                "You must have attended the raid to leave feedback.");
    }

    @Test
    void handleRequest_duplicateFeedback_throwsException() {
        // GIVEN
        String testRaidId = "testRaidId";
        String testUserId = "testUserId";
        CreateFeedbackRequest request = CreateFeedbackRequest.builder()
                .withRaidId(testRaidId)
                .withUserId(testUserId)
                .build();

        RaidEvent testRaidEvent = new RaidEvent();
        testRaidEvent.setRaidStatus("Completed");

        UserRaid userRaid = new UserRaid();
        userRaid.setConfirmed(true);

        FeedbackModel feedbackModel = new FeedbackModel.Builder().withUserId(testUserId).build();
        List<FeedbackModel> feedbackModels = new ArrayList<>(Collections.singletonList(feedbackModel));
        testRaidEvent.setFeedback(feedbackModels);

        when(raidDao.getRaid(testRaidId)).thenReturn(testRaidEvent);
        when(userRaidDao.getUserRaid(testUserId, testRaidId)).thenReturn(userRaid);

        // WHEN AND THEN
        assertThrows(FeedbackSubmissionException.class, () -> createFeedbackActivity.handleRequest(request),
                "Duplicate feedback by the same user");
    }
}
