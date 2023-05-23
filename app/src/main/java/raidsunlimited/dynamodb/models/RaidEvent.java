package raidsunlimited.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

import java.util.List;

@DynamoDBTable(tableName = "raid_events")
public class RaidEvent {
    private String raidId;
    private String raidName;
    private Long raidDate;
    private String time;
    private Integer raidSize;
    private String raidObjective;
    private String lootDistribution;
    private List<String> requiredRoles;
    private List<String> participants;
    private List<String> feedback;
    private String raidOwner;
    private String raidStatus;

    @DynamoDBHashKey(attributeName = "raidId")
    public String getRaidId() {
        return raidId;
    }

    public void setRaidId(String raidId) {
        this.raidId = raidId;
    }

    @DynamoDBAttribute(attributeName = "raidName")
    public String getRaidName() {
        return raidName;
    }

    public void setRaidName(String raidName) {
        this.raidName = raidName;
    }

    @DynamoDBIndexHashKey(globalSecondaryIndexName = "RaidDateIndex", attributeName = "raidDate")
    public Long getRaidDate() {
        return raidDate;
    }

    public void setRaidDate(Long raidDate) {
        this.raidDate = raidDate;
    }

    @DynamoDBAttribute(attributeName = "time")
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    @DynamoDBAttribute(attributeName = "raidSize")
    public Integer getRaidSize() {
        return raidSize;
    }

    public void setRaidSize(Integer raidSize) {
        this.raidSize = raidSize;
    }

    @DynamoDBAttribute(attributeName = "raidObjective")
    public String getRaidObjective() {
        return raidObjective;
    }

    public void setRaidObjective(String raidObjective) {
        this.raidObjective = raidObjective;
    }

    @DynamoDBAttribute(attributeName = "lootDistribution")
    public String getLootDistribution() {
        return lootDistribution;
    }

    public void setLootDistribution(String lootDistribution) {
        this.lootDistribution = lootDistribution;
    }

    @DynamoDBAttribute(attributeName = "requiredRoles")
    public List<String> getRequiredRoles() {
        return requiredRoles;
    }

    public void setRequiredRoles(List<String> requiredRoles) {
        this.requiredRoles = requiredRoles;
    }

    @DynamoDBAttribute(attributeName = "participants")
    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    @DynamoDBAttribute(attributeName = "feedback")
    public List<String> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<String> feedback) {
        this.feedback = feedback;
    }

    @DynamoDBAttribute(attributeName = "raidOwner")
    public String getRaidOwner() {
        return raidOwner;
    }

    public void setRaidOwner(String raidOwner) {
        this.raidOwner = raidOwner;
    }

    @DynamoDBAttribute(attributeName = "raidStatus")
    public String getRaidStatus() {
        return raidStatus;
    }

    public void setRaidStatus(String raidStatus) {
        this.raidStatus = raidStatus;
    }
}
