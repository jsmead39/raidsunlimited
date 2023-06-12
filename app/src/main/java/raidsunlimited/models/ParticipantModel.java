package raidsunlimited.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

@DynamoDBDocument
public class ParticipantModel {
    private final String userId;
    private final String displayName;
    private final String participantClass;
    private final String participantSpecialization;
    private final String role;
    private final Boolean participantStatus;

    @JsonCreator
    private ParticipantModel(@JsonProperty("userId")String userId,
                             @JsonProperty("displayName")String displayName,
                             @JsonProperty("participantClass")String participantClass,
                             @JsonProperty("participantSpecialization")String participantSpecialization,
                             @JsonProperty("role")String role,
                             @JsonProperty("participantStatus")Boolean participantStatus) {
        this.userId = userId;
        this.displayName = displayName;
        this.participantClass = participantClass;
        this.participantSpecialization = participantSpecialization;
        this.role = role;
        this.participantStatus = participantStatus;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getParticipantClass() {
        return participantClass;
    }

    public String getRole() {
        return role;
    }

    public String getParticipantSpecialization() {
        return participantSpecialization;
    }

    public Boolean getParticipantStatus() {
        return participantStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParticipantModel that = (ParticipantModel) o;
        return Objects.equals(userId, that.userId) && Objects.equals(displayName, that.displayName) &&
                Objects.equals(participantClass, that.participantClass) &&
                Objects.equals(participantSpecialization, that.participantSpecialization) &&
                Objects.equals(role, that.role) &&
                Objects.equals(participantStatus, that.participantStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, displayName, participantClass, participantSpecialization, role, participantStatus);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;
        private String displayName;
        private String participantClass;
        private String participantSpecialization;
        private String role;
        private Boolean participantStatus;

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder withParticipantClass(String participantClass) {
            this.participantClass = participantClass;
            return this;
        }

        public Builder withParticipantSpecialization(String participantSpecialization) {
            this.participantSpecialization = participantSpecialization;
            return this;
        }

        public Builder withRole(String role) {
            this.role = role;
            return this;
        }

        public Builder withParticipantStatus(Boolean participantStatus) {
            this.participantStatus = participantStatus;
            return this;
        }

        public ParticipantModel build() {
            return new ParticipantModel(userId, displayName, participantClass, participantSpecialization, role,
                    participantStatus);
        }
    }
}
