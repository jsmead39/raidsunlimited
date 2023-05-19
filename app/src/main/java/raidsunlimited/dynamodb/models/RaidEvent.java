package raidsunlimited.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
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
    private String lootDistribution;
    private List<Map<String, AttributeValue>> requiredRoles;
    private List<String> participants;
    private List<Map<String, AttributeValue>> feedback;
    private String raidOwner;

    public String getRaidId() {
        return raidId;
    }

    public void setRaidId(String raidId) {
        this.raidId = raidId;
    }

    public String getRaidName() {
        return raidName;
    }

    public void setRaidName(String raidName) {
        this.raidName = raidName;
    }

    public Long getRaidDate() {
        return raidDate;
    }

    public void setRaidDate(Long raidDate) {
        this.raidDate = raidDate;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getRaidSize() {
        return raidSize;
    }

    public void setRaidSize(Integer raidSize) {
        this.raidSize = raidSize;
    }

    public String getRaidObjectives() {
        return raidObjectives;
    }

    public void setRaidObjectives(String raidObjectives) {
        this.raidObjectives = raidObjectives;
    }

    public String getLootDistribution() {
        return lootDistribution;
    }

    public void setLootDistribution(String lootDistribution) {
        this.lootDistribution = lootDistribution;
    }

    public List<Map<String, AttributeValue>> getRequiredRoles() {
        return requiredRoles;
    }

    public void setRequiredRoles(List<Map<String, AttributeValue>> requiredRoles) {
        this.requiredRoles = requiredRoles;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public List<Map<String, AttributeValue>> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<Map<String, AttributeValue>> feedback) {
        this.feedback = feedback;
    }

    public String getRaidOwner() {
        return raidOwner;
    }

    public void setRaidOwner(String raidOwner) {
        this.raidOwner = raidOwner;
    }
}
