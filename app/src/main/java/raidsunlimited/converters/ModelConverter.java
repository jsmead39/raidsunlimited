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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ModelConverter {

    public RaidModel toRaidModel(RaidEvent raidEvent) {
        String formattedDate = convertLongToDate(raidEvent.getRaidDate());
        List<String> participants = raidEvent.getParticipants();
        if (participants == null) {
            participants = new ArrayList<>();
        }

        List<FeedbackModel> feedback = raidEvent.getFeedback();
        if (feedback == null) {
            feedback = new ArrayList<>();
        }

        List<RequiredRoleModel> requiredRoles = raidEvent.getRequiredRoles();
        if (requiredRoles == null) {
            requiredRoles = new ArrayList<>();
        }

        return RaidModel.builder()
                .withRaidId(raidEvent.getRaidId())
                .withRaidName(raidEvent.getRaidName())
                .withRaidDate(formattedDate)
                .withTime(raidEvent.getTime())
                .withRaidSize(raidEvent.getRaidSize())
                .withRaidObjective(raidEvent.getRaidObjective())
                .withLootDistribution(raidEvent.getLootDistribution())
                .withRequiredRoles(requiredRoles)
                .withParticipant(participants)
                .withFeedback(feedback)
                .withRaidOwner(raidEvent.getRaidOwner())
                .withRaidStatus(raidEvent.getRaidStatus())
                .build();
    }

    private String convertLongToDate(Long epoch) {
        Instant instant = Instant.ofEpochMilli(epoch);
        ZonedDateTime convertedDate = ZonedDateTime.ofInstant(instant, ZoneId.of("America/Los_Angeles"));
        return convertedDate.toLocalDate().toString();
    }
}
