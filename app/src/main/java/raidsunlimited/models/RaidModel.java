package raidsunlimited.models;

import java.util.List;
import java.util.Objects;

public class RaidModel {
    private final String raidId;
    private final String raidName;
    private final String raidDate;
    private final String time;
    private final Integer raidSize;
    private final String raidObjective;
    private final String lootDistribution;
    private final List<String> requiredRoles;
    private final List<String> participants;
    private final List<FeedbackModel> feedback;
    private final String raidOwner;
    private final String raidStatus;

    private RaidModel(String raidId, String raidName, String raidDate, String time, Integer raidSize,
                      String raidObjective,
                     String lootDistribution, List<String> requiredRoles,
                     List<String> participants, List<FeedbackModel> feedback, String raidOwner,
                     String raidStatus) {
        this.raidId = raidId;
        this.raidName = raidName;
        this.raidDate = raidDate;
        this.time = time;
        this.raidSize = raidSize;
        this.raidObjective = raidObjective;
        this.lootDistribution = lootDistribution;
        this.requiredRoles = requiredRoles;
        this.participants = participants;
        this.feedback = feedback;
        this.raidOwner = raidOwner;
        this.raidStatus = raidStatus;
    }

    public String getRaidId() {
        return raidId;
    }

    public String getRaidName() {
        return raidName;
    }

    public String getRaidDate() {
        return raidDate;
    }

    public String getTime() {
        return time;
    }

    public Integer getRaidSize() {
        return raidSize;
    }

    public String getRaidObjective() {
        return raidObjective;
    }

    public String getLootDistribution() {
        return lootDistribution;
    }

    public List<String> getRequiredRoles() {
        return requiredRoles;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public List<FeedbackModel> getFeedback() {
        return feedback;
    }

    public String getRaidOwner() {
        return raidOwner;
    }

    public String getRaidStatus() {
        return raidStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RaidModel raidModel = (RaidModel) o;
        return Objects.equals(raidId, raidModel.raidId) && Objects.equals(raidName, raidModel.raidName) &&
                Objects.equals(raidDate, raidModel.raidDate) && Objects.equals(time, raidModel.time) &&
                Objects.equals(raidSize, raidModel.raidSize) &&
                Objects.equals(raidObjective, raidModel.raidObjective) &&
                Objects.equals(lootDistribution, raidModel.lootDistribution) &&
                Objects.equals(requiredRoles, raidModel.requiredRoles) &&
                Objects.equals(participants, raidModel.participants) &&
                Objects.equals(feedback, raidModel.feedback) &&
                Objects.equals(raidOwner, raidModel.raidOwner) && Objects.equals(raidStatus, raidModel.raidStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(raidId, raidName, raidDate, time, raidSize, raidObjective, lootDistribution, requiredRoles, participants, feedback, raidOwner, raidStatus);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String raidId;
        private String raidName;
        private String raidDate;
        private String time;
        private Integer raidSize;
        private String raidObjective;
        private String lootDistribution;
        private List<String> requiredRoles;
        private List<String> participants;
        private List<FeedbackModel> feedback;
        private String raidOwner;
        private String raidStatus;

        public Builder withRaidId(String raidId) {
            this.raidId = raidId;
            return this;
        }

        public Builder withRaidName(String raidName) {
            this.raidName = raidName;
            return this;
        }

        public Builder withRaidDate(String raidDate) {
            this.raidDate = raidDate;
            return this;
        }

        public Builder withTime(String time) {
            this.time = time;
            return this;
        }

        public Builder withRaidSize(Integer raidSize) {
            this.raidSize = raidSize;
            return this;
        }

        public Builder withRaidObjective(String raidObjective) {
            this.raidObjective = raidObjective;
            return this;
        }

        public Builder withLootDistribution(String lootDistribution) {
            this.lootDistribution = lootDistribution;
            return this;
        }

        public Builder withRequiredRoles(List<String> requiredRoles) {
            this.requiredRoles = requiredRoles;
            return this;
        }

        public Builder withParticipant(List<String> participant) {
            this.participants = participant;
            return this;
        }

        public Builder withFeedback(List<FeedbackModel> feedback) {
            this.feedback = feedback;
            return this;
        }

        public Builder withRaidOwner(String raidOwner) {
            this.raidOwner = raidOwner;
            return this;
        }

        public Builder withRaidStatus(String raidStatus) {
            this.raidStatus = raidStatus;
            return this;
        }

        public RaidModel build() {
            return new RaidModel(raidId, raidName, raidDate, time, raidSize, raidObjective, lootDistribution,
                    requiredRoles, participants, feedback, raidOwner, raidStatus);
        }
    }
}
