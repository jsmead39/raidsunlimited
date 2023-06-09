package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.DeleteRaidEventRequest;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.InvalidAttributeException;
import raidsunlimited.exceptions.RaidEventDeletionException;
import raidsunlimited.exceptions.RaidEventNotFoundException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class DeleteRaidEventActivityTest {
    @Mock
    private RaidDao raidDao;

    @Mock
    private UserRaidDao userRaidDao;

    private DeleteRaidEventActivity deleteRaidEventActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        deleteRaidEventActivity = new DeleteRaidEventActivity(raidDao, userRaidDao);
    }

    @Test
    void handleRequest_ValidRequest_DeleteRaidAndUserRaids() {
        String expectedRaidId = "testId";
        String expectedEmail = "test@test.com";

        RaidEvent raidEvent = new RaidEvent();
        raidEvent.setRaidId(expectedRaidId);
        raidEvent.setRaidOwner(expectedEmail);
        raidEvent.setRaidStatus("Pending");

        UserRaid userRaid = new UserRaid();
        userRaid.setRaidId(expectedRaidId);

        when(raidDao.getRaid(expectedRaidId)).thenReturn(raidEvent);
        when(userRaidDao.getAllUserRaids(expectedRaidId)).thenReturn(Arrays.asList(userRaid));

        DeleteRaidEventRequest request = DeleteRaidEventRequest.builder()
                .withRaidId(expectedRaidId)
                .withEmail(expectedEmail)
                .build();

        // WHEN-
        deleteRaidEventActivity.handleRequest(request);

        //THEN-
        // Verify the interactions with the mocks
        verify(raidDao, times(1)).deleteRaid(expectedRaidId);
        verify(userRaidDao, times(1)).deleteUserRaidEvent(userRaid);
    }

    @Test
    void handleRequest_NoRaidId_ThrowsException() {
        DeleteRaidEventRequest request = new DeleteRaidEventRequest.Builder()
                .withRaidId(null)
                .withEmail("test@test.com")
                .build();

        assertThrows(IllegalArgumentException.class, () -> deleteRaidEventActivity.handleRequest(request));
    }

    @Test
    void handleRequest_NoEmail_ThrowsException() {
        DeleteRaidEventRequest request = new DeleteRaidEventRequest.Builder()
                .withRaidId("testId")
                .withEmail(null)
                .build();
        assertThrows(IllegalArgumentException.class, () -> deleteRaidEventActivity.handleRequest(request));
    }

    @Test
    void handleRequest_RaidNotFound_ThrowsException() {
        DeleteRaidEventRequest request = new DeleteRaidEventRequest.Builder()
                .withRaidId("testId")
                .withEmail("test@test.com")
                .build();
        when(raidDao.getRaid(anyString())).thenReturn(null);

        assertThrows(RaidEventNotFoundException.class, () -> deleteRaidEventActivity.handleRequest(request));
    }

    @Test
    void handleRequest_NotOwner_ThrowsException() {
        DeleteRaidEventRequest request = new DeleteRaidEventRequest.Builder()
                .withRaidId("testId")
                .withEmail("test@test.com")
                .build();
        RaidEvent raidEvent = new RaidEvent();
        raidEvent.setRaidOwner("other@test.com");

        when(raidDao.getRaid(anyString())).thenReturn(raidEvent);

        assertThrows(InvalidAttributeException.class, () -> deleteRaidEventActivity.handleRequest(request));
    }

    @Test
    void handleRequest_RaidCompleted_ThrowsException() {
        DeleteRaidEventRequest request = new DeleteRaidEventRequest.Builder()
                .withRaidId("testId")
                .withEmail("test@test.com")
                .build();
        RaidEvent raidEvent = new RaidEvent();
        raidEvent.setRaidOwner("test@test.com");
        raidEvent.setRaidStatus("Completed");

        when(raidDao.getRaid(anyString())).thenReturn(raidEvent);

        assertThrows(RaidEventDeletionException.class, () -> deleteRaidEventActivity.handleRequest(request));
    }
}