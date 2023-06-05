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
    private final String role;

    @JsonCreator
    private ParticipantModel(@JsonProperty("userId")String userId,
                             @JsonProperty("displayName")String displayName,
                             @JsonProperty("participantClass")String participantClass,
                             @JsonProperty("role")String role) {
        this.userId = userId;
        this.displayName = displayName;
        this.participantClass = participantClass;
        this.role = role;
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
                Objects.equals(participantClass, that.participantClass) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, displayName, participantClass, role);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;
        private String displayName;
        private String participantClass;
        private String role;

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

        public Builder withRole(String role) {
            this.role = role;
            return this;
        }

        public ParticipantModel build() {
            return new ParticipantModel(userId, displayName, participantClass, role);
        }
    }
}
