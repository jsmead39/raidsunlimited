package raidsunlimited.dynamodb.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;

import raidsunlimited.converters.FeedbackModelConverter;
import raidsunlimited.converters.ParticipantModelConverter;
import raidsunlimited.models.FeedbackModel;
import raidsunlimited.models.ParticipantModel;

import java.util.List;
import java.util.Objects;

@DynamoDBTable(tableName = "raid_events")
public class RaidEvent {
    private String raidId;
    private String raidName;
    private String raidServer;
    private Long raidDate;
    private String time;
    private Integer raidSize;
    private String raidObjective;
    private String lootDistribution;
    private List<String> requiredRoles;
    private List<ParticipantModel> participants;
    private List<FeedbackModel> feedback;
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

    @DynamoDBAttribute(attributeName = "raidServer")
    public String getRaidServer() {
        return raidServer;
    }

    public void setRaidServer(String raidServer) {
        this.raidServer = raidServer;
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
    @DynamoDBTypeConverted(converter = ParticipantModelConverter.class)
    public List<ParticipantModel> getParticipants() {
        return participants;
    }

    public void setParticipants(List<ParticipantModel> participants) {
        this.participants = participants;
    }

    @DynamoDBAttribute(attributeName = "feedback")
    @DynamoDBTypeConverted(converter = FeedbackModelConverter.class)
    public List<FeedbackModel> getFeedback() {
        return feedback;
    }

    public void setFeedback(List<FeedbackModel> feedback) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RaidEvent raidEvent = (RaidEvent) o;
        return Objects.equals(raidId, raidEvent.raidId) && Objects.equals(raidName, raidEvent.raidName) &&
                Objects.equals(raidServer, raidEvent.raidServer) && Objects.equals(raidDate, raidEvent.raidDate) &&
                Objects.equals(time, raidEvent.time) && Objects.equals(raidSize, raidEvent.raidSize) &&
                Objects.equals(raidObjective, raidEvent.raidObjective) &&
                Objects.equals(lootDistribution, raidEvent.lootDistribution) &&
                Objects.equals(requiredRoles, raidEvent.requiredRoles) &&
                Objects.equals(participants, raidEvent.participants) && Objects.equals(feedback, raidEvent.feedback) &&
                Objects.equals(raidOwner, raidEvent.raidOwner) && Objects.equals(raidStatus, raidEvent.raidStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raidId, raidName, raidServer, raidDate, time, raidSize, raidObjective, lootDistribution,
                requiredRoles, participants, feedback, raidOwner, raidStatus);
    }

    @Override
    public String toString() {
        return "RaidEvent{" +
                "raidId='" + raidId + '\'' +
                ", raidName='" + raidName + '\'' +
                ", raidServer='" + raidServer + '\'' +
                ", raidDate=" + raidDate +
                ", time='" + time + '\'' +
                ", raidSize=" + raidSize +
                ", raidObjective='" + raidObjective + '\'' +
                ", lootDistribution='" + lootDistribution + '\'' +
                ", requiredRoles=" + requiredRoles +
                ", participants=" + participants +
                ", feedback=" + feedback +
                ", raidOwner='" + raidOwner + '\'' +
                ", raidStatus='" + raidStatus + '\'' +
                '}';
    }
}
