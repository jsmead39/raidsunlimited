package raidsunlimited.converters;

import raidsunlimited.activity.requests.RaidSignupRequest;
import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.dynamodb.models.User;
import raidsunlimited.models.ParticipantModel;
import raidsunlimited.models.ProfileModel;
import raidsunlimited.models.RaidModel;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ModelConverter {

    /**
     * Convert a RaidEvent object from the RaidEvent table to a RaidModel object.
     * @param raidEvent A raidEventObject.
     * @return a RaidModel representation to be passed back to front end
     */
    public RaidModel toRaidModel(RaidEvent raidEvent) {
        String formattedDate = convertLongToDate(raidEvent.getRaidDate());
        Map<String, Integer> requiredRoles = convertListToMap(raidEvent.getRequiredRoles());

        return RaidModel.builder()
                .withRaidId(raidEvent.getRaidId())
                .withRaidName(raidEvent.getRaidName())
                .withRaidServer(raidEvent.getRaidServer())
                .withRaidDate(formattedDate)
                .withTime(raidEvent.getTime())
                .withRaidSize(raidEvent.getRaidSize())
                .withRaidObjective(raidEvent.getRaidObjective())
                .withLootDistribution(raidEvent.getLootDistribution())
                .withRequiredRoles(requiredRoles)
                .withParticipant(raidEvent.getParticipants())
                .withFeedback(raidEvent.getFeedback())
                .withRaidOwner(raidEvent.getRaidOwner())
                .withRaidStatus(raidEvent.getRaidStatus())
                .build();
    }

    /**
     * Converts a list of RaidEvent objects into a list of RaidModel objects.
     * @param raidEvents A list of RaidEvent objects to be converted.
     * @return A list of RaidModel objects converted from the provided RaidEvent objects.
     */
    public List<RaidModel> toRaidModelList(List<RaidEvent> raidEvents) {
        List<RaidModel> raidModels = new ArrayList<>();

        for (RaidEvent raidEvent : raidEvents) {
            RaidModel raidModel = toRaidModel(raidEvent);
            raidModels.add(raidModel);
        }

        return raidModels;
    }
    /**
     * Convert a User object from the User table to a ProfileModel object.
     * @param user the User object from the User table.
     * @return A ProfileModel representation of the user profile.
     */
    public ProfileModel toProfileModel(User user) {
        return ProfileModel.builder()
                .withUserId(user.getUserId())
                .withDisplayName(user.getDisplayName())
                .withEmail(user.getEmail())
                .withCharacterList(user.getCharactersList())
                .withLogs(user.getLogs())
                .build();
    }

    /**
     * Converts a raid participant to a ParticipantModel.
     * @param raidSignupRequest the request object containing UserId, displayName, and participant details.
     * @return a ParticipantModel representation of the participant.
     */
    public ParticipantModel toParticipantModel(RaidSignupRequest raidSignupRequest) {
        //Create Participant model from the request
        return ParticipantModel.builder()
                .withUserId(raidSignupRequest.getUserId())
                .withDisplayName(raidSignupRequest.getDisplayName())
                .withParticipantClass(raidSignupRequest.getGameCharacter().getCharClass())
                .withParticipantSpecialization(raidSignupRequest.getGameCharacter().getSpecialization())
                .withRole(raidSignupRequest.getGameCharacter().getRole())
                .build();
    }

    /**
     * Private helper method for converting a epoch standard date to a string format.
     * @param epoch the epoch value of the date.
     * @return the string format of the epoch date value.
     */
    private String convertLongToDate(Long epoch) {
        if (epoch == null) {
            return null;
        }
        Instant instant = Instant.ofEpochSecond(epoch);
        ZonedDateTime convertedDate = ZonedDateTime.ofInstant(instant, ZoneId.of("America/Los_Angeles"));
        return convertedDate.toLocalDate().toString();
    }

    private Map<String, Integer> convertListToMap(List<String> roles) {
        if (roles == null) {
            return null;
        }
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
