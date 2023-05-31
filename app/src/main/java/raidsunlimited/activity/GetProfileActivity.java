package raidsunlimited.activity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.GetProfileByEmailRequest;
import raidsunlimited.activity.requests.GetProfileRequest;
import raidsunlimited.activity.results.GetProfileResult;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.models.User;
import raidsunlimited.models.ProfileModel;

import javax.inject.Inject;

/**
 * Implementation of the GetProfileActivity for the RaidsUnlimited GetProfile API.
 *
 * This API allows the customer to get a saved profile.
 */
public class GetProfileActivity {
    private final Logger log = LogManager.getLogger();
    private final UserDao userDao;

    @Inject
    public GetProfileActivity(UserDao userDao) {
        this.userDao = userDao;
    }

    public GetProfileResult handleRequest(final GetProfileRequest getProfileRequest) {
        log.info("Received GetProfileRequest {}", getProfileRequest);
        String requestedId = getProfileRequest.getUserId();

        if (requestedId == null) {
            throw new IllegalArgumentException("The userId cannot be null");
        }

        User user = userDao.getUserById(requestedId);
        ProfileModel profileModel = new ModelConverter().toProfileModel(user);

        return GetProfileResult.builder()
                .withProfileModel(profileModel)
                .build();
    }

    public GetProfileResult handleRequestByEmail(final GetProfileByEmailRequest getProfileByEmailRequest) {
        log.info("Received GetProfileRequestByEmail {}", getProfileByEmailRequest);
        String requestedEmail = getProfileByEmailRequest.getEmail();

        if (requestedEmail == null) {
            throw new IllegalArgumentException("The email cannot be null");
        }

        User user = userDao.getUserByEmail(requestedEmail);
        ProfileModel profileModel = new ModelConverter().toProfileModel(user);

        return GetProfileResult.builder()
                .withProfileModel(profileModel)
                .build();
    }
}
