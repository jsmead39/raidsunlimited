package raidsunlimited.activity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import raidsunlimited.activity.requests.CreateProfileRequest;
import raidsunlimited.activity.results.CreateProfileResult;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.models.User;
import raidsunlimited.exceptions.UserProfileCreationException;
import raidsunlimited.models.GameCharacter;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class CreateProfileActivityTest {
    @Mock UserDao userDao;
    private CreateProfileActivity createProfileActivity;

    @BeforeEach
    void setUp() {
        openMocks(this);
        createProfileActivity = new CreateProfileActivity(userDao);
    }

    @Test
    public void handleRequest_validInput_createsNewProfile() {
        //GIVEN
        GameCharacter character = new GameCharacter.Builder()
                .withCharName("TestName")
                .withCharClass("TestClass")
                .withSpecialization("TestSpecialization")
                .withRole("TestRole")
                .build();

        CreateProfileRequest request = CreateProfileRequest.builder()
                .withDisplayName("test")
                .withEmail("test@test.com")
                .withCharactersList(Collections.singletonList(character))
                .withLogs("testLogs")
                .build();


        when(userDao.existsByEmail(request.getEmail())).thenReturn(false);

        //WHEN-
        CreateProfileResult result = createProfileActivity.handleRequest(request);

        //THEN -
        verify(userDao).saveUser(any(User.class));
        assertNotNull(result.getProfileModel().getUserId());
        assertEquals(request.getCharactersList(), result.getProfileModel().getCharacterList());
        assertEquals(request.getDisplayName(), result.getProfileModel().getDisplayName());
        assertEquals(request.getEmail(), result.getProfileModel().getEmail());
        assertEquals(request.getLogs(), result.getProfileModel().getLogs());
    }

    @Test
    public void handleRequest_nullDisplayName_throwsException() {
        //GIVEN
        GameCharacter character = new GameCharacter.Builder()
                .withCharName("TestName")
                .withCharClass("TestClass")
                .withSpecialization("TestSpecialization")
                .withRole("TestRole")
                .build();



        CreateProfileRequest request = CreateProfileRequest.builder()
                .withDisplayName(null)
                .withEmail("test@test.com")
                .withCharactersList(Collections.singletonList(character))
                .withLogs("testLogs")
                .build();

        //WHEN + THEN -
        assertThrows(UserProfileCreationException.class, () -> createProfileActivity.handleRequest(request),
                "You must enter a display name");
    }

    @Test
    public void handleRequest_invalidDisplayName_throwsException() {
        //GIVEN
        GameCharacter character = new GameCharacter.Builder()
                .withCharName("TestName")
                .withCharClass("TestClass")
                .withSpecialization("TestSpecialization")
                .withRole("TestRole")
                .build();


        CreateProfileRequest request = CreateProfileRequest.builder()
                .withDisplayName("testUser!")
                .withEmail("test@test.com")
                .withCharactersList(Collections.singletonList(character))
                .withLogs("testLogs")
                .build();

        //WHEN + THEN
        assertThrows(UserProfileCreationException.class, () -> createProfileActivity.handleRequest(request),
                "Display name [testUser!] contains illegal characters. Only letters and numbers are allowed.");
    }

    @Test
    public void handleRequest_existingEmail_throwsException() {
        GameCharacter character = new GameCharacter.Builder()
                .withCharName("TestName")
                .withCharClass("TestClass")
                .withSpecialization("TestSpecialization")
                .withRole("TestRole")
                .build();


        CreateProfileRequest request = CreateProfileRequest.builder()
                .withDisplayName("testUser")
                .withEmail("test@test.com")
                .withCharactersList(Collections.singletonList(character))
                .withLogs("testLogs")
                .build();

        when(userDao.existsByEmail(request.getEmail())).thenReturn(true);

        //WHEN + THEN
        assertThrows(UserProfileCreationException.class, () -> createProfileActivity.handleRequest(request),
                "A profile with this email already exists");
    }

    @Test
    public void handleRequest_emptyCharactersList_throwsException() {
        //GIVEN -
        CreateProfileRequest request = CreateProfileRequest.builder()
                .withDisplayName("testUser")
                .withEmail("test@test.com")
                .withCharactersList(Collections.emptyList())
                .withLogs("testLogs")
                .build();

        //WHEN + THEN
        assertThrows(UserProfileCreationException.class, () -> createProfileActivity.handleRequest(request),
                "You must add at least one character to your profile");

    }
}