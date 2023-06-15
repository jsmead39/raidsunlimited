package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.RaidWithdrawRequest;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.User;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.RaidEventNotFoundException;
import raidsunlimited.exceptions.RaidWithdrawException;
import raidsunlimited.exceptions.UserProfileNotFoundException;
import raidsunlimited.models.ParticipantModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RaidWithdrawActivityTest {
    @Mock
    private RaidDao raidDao;

    @Mock
    private UserDao userDao;

    @Mock
    private UserRaidDao userRaidDao;

    private RaidWithdrawActivity raidWithdrawActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        raidWithdrawActivity = new RaidWithdrawActivity(raidDao, userDao, userRaidDao);
    }

    @Test
    public void handleRequest_validInput_withdrawsFromRaid() {
        String raidId = "testRaidId";
        String userId = "testUserId";
        RaidEvent raidEvent = new RaidEvent();
        UserRaid userRaid = new UserRaid();
        ParticipantModel participant = new ParticipantModel.Builder()
                .withUserId(userId)
                .build();
        List<ParticipantModel> participants = new ArrayList<>();
        participants.add(participant);
        raidEvent.setParticipants(participants);
        raidEvent.setRaidStatus("Scheduled");

        RaidWithdrawRequest request = RaidWithdrawRequest.builder()
                .withUserId(userId)
                .withRaidId(raidId)
                .build();

        when(userDao.getUserById(userId)).thenReturn(new User());
        when(raidDao.getRaid(raidId)).thenReturn(raidEvent);
        when(userRaidDao.getUserRaid(userId, raidId)).thenReturn(userRaid);

        // WHEN
        raidWithdrawActivity.handleRequest(request);

        // THEN
        verify(userRaidDao).deleteUserRaidEvent(userRaid);
        verify(raidDao).saveRaid(any(RaidEvent.class));
        assertEquals(0, participants.size());
    }

    @Test
    public void handleRequest_invalidRaidId_throwsException() {
        // GIVEN
        String invalidRaidId = "invalidRaidId";
        String userId = "testUserId";

        RaidWithdrawRequest request = RaidWithdrawRequest.builder()
                .withUserId(userId)
                .withRaidId(invalidRaidId)
                .build();

        when(userDao.getUserById(userId)).thenReturn(new User());
        when(raidDao.getRaid(invalidRaidId)).thenReturn(null);

        // WHEN + THEN
        assertThrows(RaidEventNotFoundException.class, () -> raidWithdrawActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_invalidUserId_throwsException() {
        // GIVEN
        String raidId = "testRaidId";
        String invalidUserId = "invalidUserId";

        RaidWithdrawRequest request = RaidWithdrawRequest.builder()
                .withUserId(invalidUserId)
                .withRaidId(raidId)
                .build();

        when(userDao.getUserById(invalidUserId)).thenReturn(null);

        // WHEN + THEN
        assertThrows(UserProfileNotFoundException.class, () -> raidWithdrawActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_userNotInParticipants_throwsException() {
        // GIVEN
        String raidId = "testRaidId";
        String userId = "testUserId";
        RaidEvent raidEvent = new RaidEvent();
        raidEvent.setParticipants(new ArrayList<>());
        raidEvent.setRaidStatus("Scheduled");

        RaidWithdrawRequest request = RaidWithdrawRequest.builder()
                .withUserId(userId)
                .withRaidId(raidId)
                .build();

        when(userDao.getUserById(userId)).thenReturn(new User());
        when(raidDao.getRaid(raidId)).thenReturn(raidEvent);

        // WHEN + THEN
        assertThrows(RaidWithdrawException.class, () -> raidWithdrawActivity.handleRequest(request));
    }
}
