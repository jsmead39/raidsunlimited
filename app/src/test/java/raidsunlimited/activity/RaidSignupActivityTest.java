package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.RaidSignupRequest;
import raidsunlimited.dynamodb.RaidDao;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.UserRaidDao;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.User;
import raidsunlimited.dynamodb.models.UserRaid;
import raidsunlimited.exceptions.RaidEventNotFoundException;
import raidsunlimited.exceptions.RaidSignupException;
import raidsunlimited.exceptions.UserProfileNotFoundException;
import raidsunlimited.models.FeedbackModel;
import raidsunlimited.models.GameCharacter;
import raidsunlimited.models.ParticipantModel;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class RaidSignupActivityTest {
    @Mock
    private RaidDao raidDao;

    @Mock
    private UserDao userDao;

    @Mock
    private UserRaidDao userRaidDao;

    private RaidSignupActivity raidSignupActivity;
    @BeforeEach
    void setUp() {
        openMocks(this);
        raidSignupActivity = new RaidSignupActivity(raidDao, userDao, userRaidDao);
    }

    @Test
    public void handleRequest_validInput_signsUpForRaid() {
        String expectedRaidId = "testId";
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
        raidEvent.setFeedback(expectedFeedback);
        raidEvent.setRaidOwner(expectedRaidOwner);
        raidEvent.setRaidStatus("Scheduled");

        GameCharacter character = new GameCharacter.Builder()
                .withCharName("Test")
                .withCharClass("TestClass")
                .withSpecialization("TestSpec")
                .withRole("TestRole")
                .build();

        RaidSignupRequest request = RaidSignupRequest.builder()
                .withUserId("testId")
                .withRaidId("testRaidId")
                .withGameCharacter(character)
                .build();



        when(userDao.getUserById(request.getUserId())).thenReturn(new User());
        when(raidDao.getRaid(request.getRaidId())).thenReturn(raidEvent);
        when(userRaidDao.getUserRaid(request.getUserId(), request.getRaidId())).thenReturn(null);

        //WHEN-

        raidSignupActivity.handleRequest(request);

        //THEN-
        verify(raidDao).saveRaid(any(RaidEvent.class));
        verify(userRaidDao).saveToEvent(any(UserRaid.class));
        verify(userRaidDao).getUserRaid(anyString(), anyString());
    }

    @Test
    public void handleRequest_noGameCharacter_throwsException() {
        // GIVEN
        RaidSignupRequest request = RaidSignupRequest.builder()
                .withRaidId("testRaidId")
                .withUserId("testUserId")
                .build();

        // WHEN + THEN
        assertThrows(RaidSignupException.class, () -> raidSignupActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_emptyRaidId_throwsException() {
        // GIVEN
        GameCharacter character = new GameCharacter.Builder()
                .withCharName("Test")
                .withCharClass("TestClass")
                .withSpecialization("TestSpec")
                .withRole("TestRole")
                .build();

        RaidSignupRequest request = RaidSignupRequest.builder()
                .withRaidId("")
                .withUserId("testUserId")
                .withGameCharacter(character)
                .build();

        // WHEN + THEN
        assertThrows(RaidSignupException.class, () -> raidSignupActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_userProfileNotFound_throwsException() {
        // GIVEN
        GameCharacter character = new GameCharacter.Builder()
                .withCharName("Test")
                .withCharClass("TestClass")
                .withSpecialization("TestSpec")
                .withRole("TestRole")
                .build();

        RaidSignupRequest request = RaidSignupRequest.builder()
                .withRaidId("testRaidId")
                .withUserId("nonExistentUserId")
                .withGameCharacter(character)
                .build();

        when(userDao.getUserById(request.getUserId())).thenReturn(null);

        // WHEN + THEN
        assertThrows(UserProfileNotFoundException.class, () -> raidSignupActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_raidAlreadyCompleted_throwsException() {
        // GIVEN
        GameCharacter character = new GameCharacter.Builder()
                .withCharName("Test")
                .withCharClass("TestClass")
                .withSpecialization("TestSpec")
                .withRole("TestRole")
                .build();

        RaidSignupRequest request = RaidSignupRequest.builder()
                .withRaidId("testRaidId")
                .withUserId("testUserId")
                .withGameCharacter(character)
                .build();

        RaidEvent completedRaid = new RaidEvent();
        completedRaid.setRaidStatus("Completed");

        when(userDao.getUserById(request.getUserId())).thenReturn(new User());
        when(raidDao.getRaid(request.getRaidId())).thenReturn(completedRaid);

        // WHEN + THEN
        assertThrows(RaidSignupException.class, () -> raidSignupActivity.handleRequest(request));
    }

    @Test
    public void handleRequest_userAlreadySignedUp_throwsException() {
        // GIVEN
        GameCharacter character = new GameCharacter.Builder()
                .withCharName("Test")
                .withCharClass("TestClass")
                .withSpecialization("TestSpec")
                .withRole("TestRole")
                .build();

        RaidSignupRequest request = RaidSignupRequest.builder()
                .withRaidId("testRaidId")
                .withUserId("testUserId")
                .withGameCharacter(character)
                .build();

        UserRaid existingUserRaid = new UserRaid();
        existingUserRaid.setUserId("testUserId");
        existingUserRaid.setRaidId("testRaidId");

        RaidEvent raid = new RaidEvent();
        ParticipantModel participant = new ParticipantModel.Builder().withUserId("testUserId").build();
        raid.setParticipants(List.of(participant));

        when(userDao.getUserById(request.getUserId())).thenReturn(new User());
        when(raidDao.getRaid(request.getRaidId())).thenReturn(raid);
        when(userRaidDao.getUserRaid(request.getUserId(), request.getRaidId())).thenReturn(existingUserRaid);

        // WHEN + THEN
        assertThrows(RaidSignupException.class, () -> raidSignupActivity.handleRequest(request));
    }

}
