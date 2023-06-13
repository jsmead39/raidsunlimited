package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.RoleRemovalRequest;
import raidsunlimited.activity.results.RoleRemovalResult;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.NotRaidOwnerException;
import raidsunlimited.exceptions.RaidSignupException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RoleRemovalActivityTest {

    @Mock
    RaidDao raidDao;

    @Mock
    UserRaidDao userRaidDao;

    private RoleRemovalActivity RoleRemovalActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        RoleRemovalActivity = new RoleRemovalActivity(userRaidDao, raidDao);
    }

    @Test
    void handleRequest_validInput_roleRemoved() {
        //GIVEN
        String raidId = "raid1";
        String userId = "user1";
        String raidOwner = "owner1";

        RaidEvent raid = new RaidEvent();
        raid.setRaidOwner(raidOwner);
        when(raidDao.getRaid(raidId)).thenReturn(raid);

        UserRaid userRaid = new UserRaid();
        when(userRaidDao.getUserRaid(userId, raidId)).thenReturn(userRaid);

        RoleRemovalRequest request = new RoleRemovalRequest.Builder()
                  .withUserId(userId)
                  .withRaidId(raidId)
                  .withRaidOwner(raidOwner)
                  .build();

        //WHEN
        RoleRemovalResult result = RoleRemovalActivity.handleRequest(request);

        //THEN
        verify(userRaidDao).saveToEvent(any(UserRaid.class));
        assertEquals(userId, result.getUserId());
        assertEquals(raidId, result.getRaidId());
        assertFalse(result.getStatus());
    }

    @Test
    void handleRequest_raidIdEmpty_throwsException() {
        RoleRemovalRequest request = new RoleRemovalRequest.Builder().withRaidId("").build();
        assertThrows(RaidSignupException.class, () -> RoleRemovalActivity.handleRequest(request));
    }

    @Test
    void handleRequest_userIdEmpty_throwsException() {
        RoleRemovalRequest request = new RoleRemovalRequest.Builder().withUserId("").build();
        assertThrows(RaidSignupException.class, () -> RoleRemovalActivity.handleRequest(request));
    }

    @Test
    void handleRequest_userNotRaidOwner_throwsException() {
        //GIVEN
        String raidId = "raid1";
        RaidEvent raid = new RaidEvent();
        raid.setRaidOwner("owner1");
        when(raidDao.getRaid(raidId)).thenReturn(raid);

        RoleRemovalRequest request = new RoleRemovalRequest.Builder()
                .withRaidId(raidId)
                .withUserId("owner2")
                .build();


        //WHEN + THEN
        assertThrows(NotRaidOwnerException.class, () -> RoleRemovalActivity.handleRequest(request));
    }

    @Test
    void handleRequest_userNotSignedUp_throwsException() {
        //GIVEN
        String raidId = "raid1";
        String userId = "user1";
        RaidEvent raid = new RaidEvent();
        raid.setRaidOwner("owner1");
        when(raidDao.getRaid(raidId)).thenReturn(raid);

        when(userRaidDao.getUserRaid(userId, raidId)).thenReturn(null);

        RoleRemovalRequest request = new RoleRemovalRequest.Builder()
                .withRaidId(raidId)
                .withUserId(userId)
                .withRaidOwner("owner1")
                .build();


        //WHEN + THEN
        assertThrows(RaidSignupException.class, () -> RoleRemovalActivity.handleRequest(request));
    }
}
