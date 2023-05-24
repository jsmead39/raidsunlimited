package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.GetRaidRequest;
import raidsunlimited.activity.results.GetRaidResult;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.models.RaidModel;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetRaidActivityTest {
    @Mock
    private RaidDao raidDao;
    private GetRaidActivity getRaidActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        getRaidActivity = new GetRaidActivity(raidDao);
    }

    @Test
    void handleRequest_RaidFound_returnsRaidModel() {
        String expectedRaidId ="testId";
        String expectedName = "test";
        String expectedServer = "Whitemane";
        Long expectedDate = 1676534400L;
        String expectedTime = "19:00";
        Integer expectedRaidSize = 25;
        String expectedRaidObjective = "Farm";
        String expectedLootDistribution = "GDKP";
        List<String> expectedRequiredRoles = List.of("Tank 2", "Healer 2", "Dps 21");
        String expectedRaidOwner = "test@test.com";

        RaidEvent raidEvent = new RaidEvent();
        raidEvent.setRaidId(expectedRaidId);
        raidEvent.setRaidName(expectedName);
        raidEvent.setRaidServer(expectedServer);
        raidEvent.setRaidDate(expectedDate);
        raidEvent.setTime(expectedTime);
        raidEvent.setRaidSize(expectedRaidSize);
        raidEvent.setRaidObjective(expectedRaidObjective);
        raidEvent.setLootDistribution(expectedLootDistribution);
        raidEvent.setRequiredRoles(expectedRequiredRoles);
        raidEvent.setRaidOwner(expectedRaidOwner);

        when(raidDao.getRaid(expectedRaidId)).thenReturn(raidEvent);

        GetRaidRequest request = GetRaidRequest.builder()
                .withRaidId(expectedRaidId)
                .build();

        //WHEN -
        GetRaidResult result = getRaidActivity.handleRequest(request);


        //THEN
        RaidModel raidModel = result.getRaidModel();
        assertEquals(expectedRaidId, raidModel.getRaidId());
        assertEquals(expectedName, raidModel.getRaidName());
        assertEquals(expectedServer, raidModel.getRaidServer());
        assertEquals(convertLongToDate(expectedDate), raidModel.getRaidDate());
        assertEquals(expectedRaidSize, raidModel.getRaidSize());
        assertEquals(expectedRaidObjective, raidModel.getRaidObjective());
        assertEquals(expectedLootDistribution, raidModel.getLootDistribution());
        assertEquals(convertListToMap(expectedRequiredRoles), raidModel.getRequiredRoles());
    }

    private String convertLongToDate(Long epoch) {
        Instant instant = Instant.ofEpochMilli(epoch);
        ZonedDateTime convertedDate = ZonedDateTime.ofInstant(instant, ZoneId.of("America/Los_Angeles"));
        return convertedDate.toLocalDate().toString();
    }

    private Map<String, Integer> convertListToMap(List<String> roles) {
        Map<String, Integer> result = new HashMap<>();
        for (String entry : roles) {
            String[] split = entry.split(" ");
            if (split.length == 2) {
                String role = split[0];
                int value = Integer.parseInt(split[1]);
                result.put(role, value);
            }
        }
        return result;
    }
}