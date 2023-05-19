package raidsunlimited.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import raidsunlimited.models.FeedbackModel;

import java.util.List;
import java.util.Map;

@DynamoDBTable(tableName = "raid_events")
public class RaidEvent {
    private String raidId;
    private String raidName;
    private Long raidDate;
    private String time;
    private Integer raidSize;
    private String raidObjectives;
    private Map<String, Integer> requiredRoles;
    private List<ParticipantModel> participants;
    private List<FeedbackModel> feedback;
    private String raidOwner;
}
