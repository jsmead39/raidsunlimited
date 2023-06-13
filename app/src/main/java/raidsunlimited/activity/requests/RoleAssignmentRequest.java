package raidsunlimited.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = RoleAssignmentRequest.Builder.class)
public class RoleAssignmentRequest {
    private final String raidId;
    private final String userId;
    private final String raidRole;
    private final String raidOwner;

    private RoleAssignmentRequest(String raidId, String userId, String raidRole, String raidOwner) {
        this.raidId = raidId;
        this.userId = userId;
        this.raidRole = raidRole;
        this.raidOwner = raidOwner;
    }

    public String getRaidRole() {
        return raidRole;
    }

    public String getRaidId() {
        return raidId;
    }

    public String getUserId() {
        return userId;
    }

    public String getRaidOwner() {
        return raidOwner;
    }

    @Override
    public String toString() {
        return "RoleAssignmentRequest{" +
                "raidId='" + raidId + '\'' +
                ", userId='" + userId + '\'' +
                ", raidRole='" + raidRole + '\'' +
                ", raidOwner='" + raidOwner + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String raidId;
        private String userId;
        private String raidRole;
        private String raidOwner;

        public Builder withRaidId(String raidId) {
            this.raidId = raidId;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withRaidRole(String raidRole) {
            this.raidRole = raidRole;
            return this;
        }

        public Builder withRaidOwner(String raidOwner) {
            this.raidOwner = raidOwner;
            return this;
        }

        public RoleAssignmentRequest build() {
            return new RoleAssignmentRequest(raidId, userId, raidRole, raidOwner);
        }
    }
}
