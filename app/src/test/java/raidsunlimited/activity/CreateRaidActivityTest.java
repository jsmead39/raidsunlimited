package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.CreateRaidRequest;
import raidsunlimited.activity.results.CreateRaidResult;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.openMocks;

class CreateRaidActivityTest {
    @Mock
    private RaidDao raidDao;
    private CreateRaidActivity createRaidActivity;
    @BeforeEach
    void setUp() {
        openMocks(this);
        createRaidActivity = new CreateRaidActivity(raidDao);
    }

    @Test
    public void handleRequest_validInput_createsNewRaidEvent() {
        String inputName = "test";
        String inputServer = "Whitemane";
        String inputDate = "2023-06-30";
        String inputTime = "19:00";
        Integer inputRaidSize = 25;
        String inputRaidObjective = "Farm";
        String inputLootDistribution = "GDKP";
        Map<String, Integer> inputRoles = Map.of("Tank", 2
                , "Healer", 2
                , "Dps", 21);
        String inputRaidOwner = "test@test.com";

        //Build request
        CreateRaidRequest request = CreateRaidRequest.builder()
                .withRaidName(inputName)
                .withRaidServer(inputServer)
                .withRaidDate(inputDate)
                .withTime(inputTime)
                .withRaidSize(inputRaidSize)
                .withRaidObjective(inputRaidObjective)
                .withLootDistribution(inputLootDistribution)
                .withRequiredRoles(inputRoles)
                .withRaidOwner(inputRaidOwner)
                .build();


        //WHEN - handleRequest call
        CreateRaidResult result = createRaidActivity.handleRequest(request);

        //THEN -
        verify(raidDao).saveRaid(any(RaidEvent.class));

        assertNotNull(result.getRaid().getRaidId());
        assertEquals(inputName, result.getRaid().getRaidName());
        assertEquals(inputServer, result.getRaid().getRaidServer());
        assertEquals(inputDate, result.getRaid().getRaidDate());
        assertEquals(inputTime, result.getRaid().getTime());
        assertEquals(inputRaidSize, result.getRaid().getRaidSize());
        assertEquals(inputRaidObjective, result.getRaid().getRaidObjective());
        assertEquals(inputLootDistribution, result.getRaid().getLootDistribution());
        assertEquals(inputRoles, result.getRaid().getRequiredRoles());
        assertEquals(inputRaidOwner, result.getRaid().getRaidOwner());
        assertEquals("Pending", result.getRaid().getRaidStatus());
    }
}