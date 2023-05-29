package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.CreateProfileRequest;
import raidsunlimited.activity.results.CreateProfileResult;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.models.User;
import raidsunlimited.models.ProfileModel;
import raidsunlimited.utils.ServiceUtils;

import javax.inject.Inject;

public class CreateProfileActivity {
    private final Logger log = LogManager.getLogger();
    private final UserDao userDao;

    /**
     * Instantiates a new CreateProfileActivity.
     * @param userDao UserDao to access the users table.
     */
    @Inject
    public CreateProfileActivity(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Handled the creation of a user profile and stores it in DynamoDB.
     * @param createProfileRequest request object containing the user profile information.
     * @return createProfileResult result object containing the API defined {@link ProfileModel}
     */
    public CreateProfileResult handleRequest(final CreateProfileRequest createProfileRequest) {
        log.info("Received CreateProfileActivity Request: {}", createProfileRequest);


        User user = new User();
        user.setUserId(ServiceUtils.generateRandomId());
        user.setDisplayName(createProfileRequest.getDisplayName());
        user.setEmail(createProfileRequest.getEmail());
        user.setGameCharacterList(createProfileRequest.getCharactersList());
        user.setLogs(createProfileRequest.getLogs());

        userDao.saveUser(user);

        ProfileModel profileModel = new ModelConverter().toProfileModel(user);

        return CreateProfileResult.builder()
                .withProfile(profileModel)
                .build();
    }
}
