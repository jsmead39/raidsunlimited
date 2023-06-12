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

    /**
     * Instantiates a new GetProfileActivity object.
     * @param userDao UserDao to access the users table.
     */
    @Inject
    public GetProfileActivity(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Handles a GetProfileRequest by retrieving the user's profile based on the provided user ID.
     * Converts the retrieved User object to a ProfileModel and returns it in a GetProfileResult.
     *
     * @param getProfileRequest a GetProfileRequest object containing the user ID of the profile to be retrieved
     * @return a GetProfileResult object containing the ProfileModel of the retrieved user
     * @throws IllegalArgumentException if the user ID provided in the GetProfileRequest is null
     */
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

    /**
     * Handles a GetProfileByEmailRequest by retrieving the user's profile based on the provided email.
     * Converts the retrieved User object to a ProfileModel and returns it in a GetProfileResult.
     *
     * @param getProfileByEmailRequest a GetProfileByEmailRequest object containing the email of the profile
     * @return a GetProfileResult object containing the ProfileModel of the retrieved user
     * @throws IllegalArgumentException if the email provided in the GetProfileByEmailRequest is null
     */

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
