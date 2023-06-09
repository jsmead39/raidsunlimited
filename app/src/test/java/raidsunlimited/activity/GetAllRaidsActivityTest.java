package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.GetAllRaidsRequest;
import raidsunlimited.activity.results.GetAllRaidsResult;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.models.FeedbackModel;
import raidsunlimited.models.RaidModel;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class GetAllRaidsActivityTest {
    @Mock
    private RaidDao raidDao;
    private GetAllRaidsActivity getAllRaidsActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        getAllRaidsActivity = new GetAllRaidsActivity(raidDao);
    }

    @Test
    void handleRequest_AllRaidsFound_returnsAllRaidModels() {
        //GIVEN -
        String expectedName = "test";
        String expectedServer = "Whitemane";
        Long expectedDate = 1674460800L;
        String expectedTime = "19:00";
        Integer expectedRaidSize = 25;
        String expectedRaidObjective = "Farm";
        String expectedLootDistribution = "GDKP";
        FeedbackModel feedback = new FeedbackModel.Builder().withUserId("test@test.com")
                .withRating(5)
                .withComments("Great Raid overall!")
                .build();
        List<FeedbackModel> expectedFeedback = List.of(feedback);
        List<String> expectedRequiredRoles = List.of("Tank 2", "Healer 2", "Dps 21");
        String expectedRaidOwner = "test@test.com";

        RaidEvent raidEvent1 = new RaidEvent();
        raidEvent1.setRaidId("raid1");
        raidEvent1.setRaidName(expectedName);
        raidEvent1.setRaidServer(expectedServer);
        raidEvent1.setRaidDate(expectedDate);
        raidEvent1.setTime(expectedTime);
        raidEvent1.setRaidSize(expectedRaidSize);
        raidEvent1.setRaidObjective(expectedRaidObjective);
        raidEvent1.setLootDistribution(expectedLootDistribution);
        raidEvent1.setRequiredRoles(expectedRequiredRoles);
        raidEvent1.setFeedback(expectedFeedback);
        raidEvent1.setRaidOwner(expectedRaidOwner);

        RaidEvent raidEvent2 = new RaidEvent();
        raidEvent2.setRaidId("raid2");
        raidEvent2.setRaidName(expectedName);
        raidEvent2.setRaidServer(expectedServer);
        raidEvent2.setRaidDate(expectedDate);
        raidEvent2.setTime(expectedTime);
        raidEvent2.setRaidSize(expectedRaidSize);
        raidEvent2.setRaidObjective(expectedRaidObjective);
        raidEvent2.setLootDistribution(expectedLootDistribution);
        raidEvent2.setRequiredRoles(expectedRequiredRoles);
        raidEvent2.setFeedback(expectedFeedback);
        raidEvent2.setRaidOwner(expectedRaidOwner);

        List<RaidEvent> raidEvents = Arrays.asList(raidEvent1, raidEvent2);

        when(raidDao.getAllRaidEvents()).thenReturn(raidEvents);

        GetAllRaidsRequest request = GetAllRaidsRequest.builder().build();

        //WHEN-

        GetAllRaidsResult result = getAllRaidsActivity.handleRequest(request);

        //THEN-
        List<RaidModel> raidModels = result.getRaidModelList();

        assertEquals(raidEvents.size(), raidModels.size());
        for (int i = 0; i <raidEvents.size(); i++) {
            assertEquals(raidEvents.get(i).getRaidId(), raidModels.get(i).getRaidId());
        }
    }
}