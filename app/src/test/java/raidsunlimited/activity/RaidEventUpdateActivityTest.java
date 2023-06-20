package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.RaidEventUpdateRequest;
import raidsunlimited.activity.results.RaidEventUpdateResult;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.exceptions.NotRaidOwnerException;
import raidsunlimited.exceptions.RaidEventNotFoundException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RaidEventUpdateActivityTest {
    @Mock
    private RaidDao raidDao;
    private RaidEventUpdateActivity raidEventUpdateActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        raidEventUpdateActivity = new RaidEventUpdateActivity(raidDao);
    }

    @Test
    public void handleRequest_validInput_updatesRaidEvent() {
        // Test input values
        String raidId = "12345";
        String raidName = "Test Raid";
        String raidServer = "Whitemane";
        String raidDate = "2023-06-30";
        String time = "19:00";
        Integer raidSize = 25;
        String raidObjective = "Farm";
        String lootDistribution = "GDKP";
        Map<String, Integer> requiredRoles = Map.of(
                "Tank", 2,
                "Healer", 2,
                "Dps", 21
        );
        String raidOwner = "test@test.com";

        // Mock raidDao.getRaid() to return a raid event
        RaidEvent existingRaidEvent = new RaidEvent();
        existingRaidEvent.setRaidStatus("Scheduled");
        existingRaidEvent.setRaidOwner(raidOwner);
        when(raidDao.getRaid(raidId)).thenReturn(existingRaidEvent);

        // Create the request
        RaidEventUpdateRequest request = RaidEventUpdateRequest.builder()
                .withRaidId(raidId)
                .withRaidName(raidName)
                .withRaidServer(raidServer)
                .withRaidDate(raidDate)
                .withTime(time)
                .withRaidSize(raidSize)
                .withRaidObjective(raidObjective)
                .withLootDistribution(lootDistribution)
                .withRequiredRoles(requiredRoles)
                .withRaidOwner(raidOwner)
                .withRaidStatus("Scheduled")
                .build();

        // Call the handleRequest method
        RaidEventUpdateResult result = raidEventUpdateActivity.handleRequest(request);

        // Verify raidDao.saveRaid() is called with the updated raid event
        verify(raidDao).saveRaid(existingRaidEvent);

        // Assertions
        assertEquals(raidName, result.getRaid().getRaidName());
        assertEquals(raidServer, result.getRaid().getRaidServer());
        assertEquals(raidDate, result.getRaid().getRaidDate());
        assertEquals(time, result.getRaid().getTime());
        assertEquals(raidSize, result.getRaid().getRaidSize());
        assertEquals(raidObjective, result.getRaid().getRaidObjective());
        assertEquals(lootDistribution, result.getRaid().getLootDistribution());
        assertEquals(requiredRoles, result.getRaid().getRequiredRoles());
        assertEquals(raidOwner, result.getRaid().getRaidOwner());
    }

    @Test
    public void handleRequest_nullRaidId_throwsRaidEventNotFoundException() {
        // Test input values
        String raidId = null;
        String raidOwner = "test@test.com";

        // Create the request
        RaidEventUpdateRequest request = RaidEventUpdateRequest.builder()
                .withRaidId(raidId)
                .withRaidOwner(raidOwner)
                .build();

        // Mock raidDao.getRaid() to return null
        when(raidDao.getRaid(raidId)).thenReturn(null);

        // Call the handleRequest method and assert the exception
        assertThrows(RaidEventNotFoundException.class, () -> raidEventUpdateActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_invalidRaidOwner_throwsNotRaidOwnerException() {
        // Test input values
        String raidId = "12345";
        String raidOwner = "test@test.com";
        String differentOwner = "other@test.com";

        // Create the request
        RaidEventUpdateRequest request = RaidEventUpdateRequest.builder()
                .withRaidId(raidId)
                .withRaidOwner(differentOwner)
                .build();

        // Mock raidDao.getRaid() to return a raid event
        RaidEvent existingRaidEvent = new RaidEvent();
        existingRaidEvent.setRaidOwner(raidOwner);
        when(raidDao.getRaid(raidId)).thenReturn(existingRaidEvent);

        // Call the handleRequest method and assert the exception
        assertThrows(NotRaidOwnerException.class, () -> raidEventUpdateActivity.handleRequest(request));
    }
}
