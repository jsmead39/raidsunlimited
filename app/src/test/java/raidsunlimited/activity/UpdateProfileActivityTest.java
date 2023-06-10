package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.UpdateProfileRequest;
import raidsunlimited.activity.results.UpdateProfileResult;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.models.User;
import raidsunlimited.exceptions.InvalidAttributeValueException;
import raidsunlimited.metrics.MetricsConstants;
import raidsunlimited.metrics.MetricsPublisher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class UpdateProfileActivityTest {
    @Mock
    private UserDao userDao;

    @Mock
    private MetricsPublisher metricsPublisher;

    private UpdateProfileActivity updateProfileActivity;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        updateProfileActivity = new UpdateProfileActivity(userDao, metricsPublisher);
    }

    @Test
    public void handleRequest_goodRequest_updatesProfile() {
        // GIVEN
        String userId = "userId";
        String expectedDisplayName = "Test1";
        String expectedEmail = "test@test.com";
        String expectedLogs = "www.test.com";

        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .withUserId(userId)
                .withDisplayName(expectedDisplayName)
                .withEmail(expectedEmail)
                .withLogs(expectedLogs)
                .build();

        User originalUser = new User();
        originalUser.setUserId(userId);
        originalUser.setDisplayName("Test2");
        originalUser.setEmail(expectedEmail);
        originalUser.setLogs("http://www.test.example.com");

        when(userDao.getUserById(userId)).thenReturn(originalUser);
        when(userDao.saveUser(originalUser)).thenReturn(originalUser);

        // WHEN
        UpdateProfileResult result = updateProfileActivity.handleRequest(request);

        // THEN
        assertEquals(expectedDisplayName, result.getProfileModel().getDisplayName());
        assertEquals(expectedEmail, result.getProfileModel().getEmail());
        assertEquals(expectedLogs, result.getProfileModel().getLogs());
    }

    @Test
    public void handleRequest_invalidDisplayName_throwsInvalidAttributeValueException() {
        // GIVEN
        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .withUserId("userId")
                .withDisplayName("Hello!")
                .withEmail("test@test.com")
                .withLogs("www.test.com")
                .build();

        // WHEN + THEN
        try {
            updateProfileActivity.handleRequest(request);
            fail("Expected InvalidAttributeValueException to be thrown");
        } catch (InvalidAttributeValueException e) {
            verify(metricsPublisher).addCount(MetricsConstants.UPDATEPROFILE_INVALIDATTRIBUTEVALUE_COUNT, 1);
            verify(metricsPublisher).addCount(MetricsConstants.UPDATEPROFILE_INVALIDATTRIBUTECHANGE_COUNT, 0);
        }
    }

    @Test
    public void handleRequest_emailNotMatch_throwsSecurityException() {
        // GIVEN
        String userId = "userId";
        UpdateProfileRequest request = UpdateProfileRequest.builder()
                .withUserId(userId)
                .withDisplayName("Test1")
                .withEmail("test@test.com")
                .withLogs("www.testlogs.com")
                .build();

        User user = new User();
        user.setUserId(userId);
        user.setDisplayName("Test2");
        user.setEmail("random@email.com");
        user.setLogs("www.testlogs.com");

        when(userDao.getUserById(userId)).thenReturn(user);

        // WHEN + THEN
        try {
            updateProfileActivity.handleRequest(request);
            fail("Expected SecurityException to be thrown");
        } catch (SecurityException e) {
            verify(metricsPublisher).addCount(MetricsConstants.UPDATEPROFILE_INVALIDATTRIBUTEVALUE_COUNT, 0);
            verify(metricsPublisher).addCount(MetricsConstants.UPDATEPROFILE_INVALIDATTRIBUTECHANGE_COUNT, 1);
        }
    }
}

