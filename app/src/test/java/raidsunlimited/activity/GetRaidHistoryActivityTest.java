package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.GetRaidHistoryRequest;
import raidsunlimited.activity.results.GetRaidHistoryResult;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.dynamodb.models.User;
import raidsunlimited.exceptions.InvalidAttributeException;
import raidsunlimited.exceptions.UserProfileNotFoundException;
import raidsunlimited.models.RaidModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetRaidHistoryActivityTest {
    @Mock
    private UserRaidDao userRaidDao;

    @Mock
    private RaidDao raidDao;

    @Mock
    private UserDao userDao;

    private GetRaidHistoryActivity getRaidHistoryActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        getRaidHistoryActivity = new GetRaidHistoryActivity(userRaidDao, raidDao, userDao);
    }

    @Test
    void handleRequest_validUserId_returnsRaidHistory() {
        // GIVEN
        String testUserId = "testUserId";
        GetRaidHistoryRequest request = GetRaidHistoryRequest.builder()
                .withUserId(testUserId)
                .build();

        User testUser = new User();
        testUser.setUserId(testUserId);

        UserRaid testUserRaid = new UserRaid();
        testUserRaid.setUserId(testUserId);
        testUserRaid.setRaidId("testRaidId");

        List<UserRaid> userRaidList = new ArrayList<>();
        userRaidList.add(testUserRaid);

        RaidEvent testRaidEvent = new RaidEvent();
        testRaidEvent.setRaidId("testRaidId");

        when(userDao.getUserById(testUserId)).thenReturn(testUser);
        when(userRaidDao.getAllUserRaidsByUserId(testUserId)).thenReturn(userRaidList);
        when(raidDao.getRaid(anyString())).thenReturn(testRaidEvent);

        // WHEN
        GetRaidHistoryResult result = getRaidHistoryActivity.handleRequest(request);

        // THEN
        assertFalse(result.getRaidModelList().isEmpty());
    }

    @Test
    void handleRequest_invalidUserId_throwsException() {
        // GIVEN
        GetRaidHistoryRequest request = GetRaidHistoryRequest.builder()
                .withUserId(null)
                .build();

        // WHEN + THEN
        assertThrows(InvalidAttributeException.class, () -> getRaidHistoryActivity.handleRequest(request),
                "User ID must be provided");
    }

    @Test
    void handleRequest_userNotFound_throwsException() {
        // GIVEN
        String testUserId = "testUserId";
        GetRaidHistoryRequest request = GetRaidHistoryRequest.builder()
                .withUserId(testUserId)
                .build();

        when(userDao.getUserById(testUserId)).thenReturn(null);

        // WHEN + THEN
        assertThrows(UserProfileNotFoundException.class, () -> getRaidHistoryActivity.handleRequest(request),
                "The specified user with ID " + testUserId + " does not exist.");
    }
}
