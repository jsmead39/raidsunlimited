package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.GetProfileByEmailRequest;
import raidsunlimited.activity.requests.GetProfileRequest;
import raidsunlimited.activity.results.GetProfileResult;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.models.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetProfileActivityTest {
    @Mock
    private UserDao userDao;
    private GetProfileActivity getProfileActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        getProfileActivity = new GetProfileActivity(userDao);
    }

    @Test
    public void handleRequestById_validId_returnsUser() {
        //GIVEN -
        String userId = "testUser";
        User user = new User();
        user.setUserId(userId);
        when(userDao.getUserById(userId)).thenReturn(user);

        //WHEN
        GetProfileRequest request = new GetProfileRequest.Builder()
                .withUserId(userId)
                .build();

        //THEN -
        GetProfileResult result = getProfileActivity.handleRequest(request);

        assertEquals(userId, result.getProfileModel().getUserId());
        verify(userDao).getUserById(userId);
    }

    @Test
    public void handleRequestByEmail_validEmail_returnsUser() {
        //GIVEN -
        String email = "test@test.com";
        User user = new User();
        user.setEmail(email);
        when(userDao.getUserByEmail(email)).thenReturn(user);

        //WHEN
        GetProfileByEmailRequest request = new GetProfileByEmailRequest.Builder()
                .withEmail(email)
                .build();

        //THEN -
        GetProfileResult result = getProfileActivity.handleRequestByEmail(request);

        assertEquals(email, result.getProfileModel().getEmail());
        verify(userDao).getUserByEmail(email);
    }
}
