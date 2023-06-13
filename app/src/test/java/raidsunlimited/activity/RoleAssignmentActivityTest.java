package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.RoleAssignmentRequest;
import raidsunlimited.activity.results.RoleAssignmentResult;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.NotRaidOwnerException;
import raidsunlimited.exceptions.RaidSignupException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RoleAssignmentActivityTest {

    @Mock
    RaidDao raidDao;

    @Mock
    UserRaidDao userRaidDao;

    private RoleAssignmentActivity roleAssignmentActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        roleAssignmentActivity = new RoleAssignmentActivity(userRaidDao, raidDao);
    }

    @Test
    void handleRequest_validInput_roleAssigned() {
        //GIVEN
        String raidId = "raid1";
        String userId = "user1";
        String raidOwner = "owner1";
        String role = "role1";

        RaidEvent raid = new RaidEvent();
        raid.setRaidOwner(raidOwner);
        raid.setRaidStatus("Scheduled");
        when(raidDao.getRaid(raidId)).thenReturn(raid);

        UserRaid userRaid = new UserRaid();
        when(userRaidDao.getUserRaid(userId, raidId)).thenReturn(userRaid);

        RoleAssignmentRequest request = new RoleAssignmentRequest.Builder()
                  .withUserId(userId)
                  .withRaidId(raidId)
                  .withRaidOwner(raidOwner)
                  .withRaidRole(role)
                  .build();

        //WHEN
        RoleAssignmentResult result = roleAssignmentActivity.handleRequest(request);

        //THEN
        verify(userRaidDao).saveToEvent(any(UserRaid.class));
        assertEquals(userId, result.getUserId());
        assertEquals(raidId, result.getRaidId());
        assertEquals(role, result.getRaidRole());
        assertTrue(result.getStatus());
    }

    @Test
    void handleRequest_raidIdEmpty_throwsException() {
        RoleAssignmentRequest request = new RoleAssignmentRequest.Builder().withRaidId("").build();
        assertThrows(RaidSignupException.class, () -> roleAssignmentActivity.handleRequest(request));
    }

    @Test
    void handleRequest_userIdEmpty_throwsException() {
        RoleAssignmentRequest request = new RoleAssignmentRequest.Builder().withUserId("").build();
        assertThrows(RaidSignupException.class, () -> roleAssignmentActivity.handleRequest(request));
    }

    @Test
    void handleRequest_userNotRaidOwner_throwsException() {
        //GIVEN
        String raidId = "raid1";
        RaidEvent raid = new RaidEvent();
        raid.setRaidOwner("owner1");
        raid.setRaidStatus("Scheduled");
        when(raidDao.getRaid(raidId)).thenReturn(raid);

        RoleAssignmentRequest request = new RoleAssignmentRequest.Builder()
                .withRaidId(raidId)
                .withUserId("owner2")
                .build();


        //WHEN + THEN
        assertThrows(NotRaidOwnerException.class, () -> roleAssignmentActivity.handleRequest(request));
    }

    @Test
    void handleRequest_userNotSignedUp_throwsException() {
        //GIVEN
        String raidId = "raid1";
        String userId = "user1";
        RaidEvent raid = new RaidEvent();
        raid.setRaidOwner("owner1");
        raid.setRaidStatus("Scheduled");
        when(raidDao.getRaid(raidId)).thenReturn(raid);

        when(userRaidDao.getUserRaid(userId, raidId)).thenReturn(null);

        RoleAssignmentRequest request = new RoleAssignmentRequest.Builder()
                .withRaidId(raidId)
                .withUserId(userId)
                .withRaidOwner("owner1")
                .build();


        //WHEN + THEN
        assertThrows(RaidSignupException.class, () -> roleAssignmentActivity.handleRequest(request));
    }
}
