package raidsunlimited.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = RoleRemovalRequest.Builder.class)
public class RoleRemovalRequest {
    private final String userId;
    private final String raidId;
    private final String raidOwner;

    private RoleRemovalRequest(String userId, String raidId, String raidOwner) {
        this.userId = userId;
        this.raidId = raidId;
        this.raidOwner = raidOwner;
    }

    public String getUserId() {
        return userId;
    }

    public String getRaidId() {
        return raidId;
    }

    public String getRaidOwner() {
        return raidOwner;
    }

    @Override
    public String toString() {
        return "RoleRemovalRequest{" +
                "userId='" + userId + '\'' +
                ", raidId='" + raidId + '\'' +
                ", raidOwner='" + raidOwner + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String userId;
        private String raidId;
        private String raidOwner;

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withRaidId(String raidId) {
            this.raidId = raidId;
            return this;
        }

        public Builder withRaidOwner(String raidOwner) {
            this.raidOwner = raidOwner;
            return this;
        }

        public RoleRemovalRequest build() {
            return new RoleRemovalRequest(userId, raidId, raidOwner);
        }
    }
}
