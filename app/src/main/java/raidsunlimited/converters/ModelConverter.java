package raidsunlimited.converters;

import raidsunlimited.dynamodb.models.RaidEvent;
import raidsunlimited.models.FeedbackModel;
import raidsunlimited.models.ParticipantModel;
import raidsunlimited.models.RaidModel;
import raidsunlimited.models.RequiredRoleModel;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

public class ModelConverter {

    public RaidModel toRaidModel(RaidEvent raidEvent) {
        String formattedDate = convertLongToDate(raidEvent.getRaidDate());
        Map<String, Integer> requiredRoles = convertListToMap(raidEvent.getRequiredRoles());

        return RaidModel.builder()
                .withRaidId(raidEvent.getRaidId())
                .withRaidName(raidEvent.getRaidName())
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
