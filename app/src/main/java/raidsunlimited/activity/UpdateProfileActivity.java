package raidsunlimited.activity;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.UpdateProfileRequest;
import raidsunlimited.activity.results.UpdateProfileResult;
import raidsunlimited.converters.ModelConverter;
import raidsunlimited.dynamodb.UserDao;
import raidsunlimited.dynamodb.models.User;
import raidsunlimited.exceptions.InvalidAttributeChangeException;
import raidsunlimited.exceptions.InvalidAttributeValueException;
import raidsunlimited.metrics.MetricsConstants;
import raidsunlimited.metrics.MetricsPublisher;
import raidsunlimited.utils.ServiceUtils;

import javax.inject.Inject;

/**
 * Implementation of the UpdateProfileActivity for the RaidsUnlimited UpdateProfile API.
 *
 * This API allows the customer to update their saved playlist's information.
 */
public class UpdateProfileActivity {
    private final Logger log = LogManager.getLogger();
    private final UserDao userDao;
    private final MetricsPublisher metricsPublisher;

    /**
     * Instantiates a new UpdateProfileActivity object.
     *
     * @param userDao UserDao to access the users table.
     * @param metricsPublisher MetricsPublisher to publish metrics.
     */
    @Inject
    public UpdateProfileActivity(UserDao userDao, MetricsPublisher metricsPublisher) {
        this.userDao = userDao;
        this.metricsPublisher = metricsPublisher;
    }

    /**
     * This method handles the incoming request by retrieving the user, updating it,
     * and persisting the user.
     * <p>
     * It then returns the updated user.
     * <p>
     * If the user does not exist, this should throw a UserProfileNotFoundException.
     * <p>
     * If the provided display name has invalid characters, throws an
     * InvalidAttributeValueException
     * <p>
     * If the request tries to update the userId,
     * this should throw an InvalidAttributeChangeException
     *
     * @param updateProfileRequest request object containing the userId, user displayName, user CharacterList, and user
     * Logs associated with it.
     * @return updateProfileResult result object containing the API defined {@link raidsunlimited.models.ProfileModel}
     */
    public UpdateProfileResult handleRequest(final UpdateProfileRequest updateProfileRequest) {
        log.info("Received UpdateProfileRequest {}", updateProfileRequest);

        if (!ServiceUtils.isValidString(updateProfileRequest.getDisplayName())) {
            publishExceptionMetrics(true, false);
            throw new InvalidAttributeValueException("Display name [" + updateProfileRequest.getDisplayName() +
                    "] contains illegal characters.  Characters allowed are a-z and 0-9");
        }

        User user = userDao.getUserById(updateProfileRequest.getUserId());

        if (!user.getEmail().equals(updateProfileRequest.getEmail())) {
            publishExceptionMetrics(false, true);
            throw new SecurityException("You must own a profile to update it.");
        }

        //Check if the userID is being changed
        if (!user.getUserId().equals(updateProfileRequest.getUserId())) {
            throw new InvalidAttributeChangeException("Cannot change the userId");
        }

        user.setDisplayName(updateProfileRequest.getDisplayName());
        user.setLogs(updateProfileRequest.getLogs());
        user.setGameCharacterList(user.getGameCharacterList());
        user = userDao.saveUser(user);


        publishExceptionMetrics(false, false);
        return UpdateProfileResult.builder()
                .withProfile(new ModelConverter().toProfileModel(user))
                .build();
    }

    /**
     * Helper method to publish exception metrics.
     * @param isInvalidAttributeValue indicates whether InvalidAttributeValueException is thrown
     * @param isInvalidAttributeChange indicates whether InvalidAttributeChangeException is thrown
     */
    private void publishExceptionMetrics(final boolean isInvalidAttributeValue,
                                         final boolean isInvalidAttributeChange) {
        metricsPublisher.addCount(MetricsConstants.UPDATEPROFILE_INVALIDATTRIBUTEVALUE_COUNT,
                isInvalidAttributeValue ? 1 : 0);
        metricsPublisher.addCount(MetricsConstants.UPDATEPROFILE_INVALIDATTRIBUTECHANGE_COUNT,
                isInvalidAttributeChange ? 1 : 0);
    }
}
