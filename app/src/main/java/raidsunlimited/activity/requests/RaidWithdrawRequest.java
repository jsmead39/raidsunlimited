package raidsunlimited.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = RaidWithdrawRequest.Builder.class)
public class RaidWithdrawRequest {
    private final String raidId;
    private final String userId;

    private RaidWithdrawRequest(String raidId, String userId) {
        this.raidId = raidId;
        this.userId = userId;
    }

    public String getRaidId() {
        return raidId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "RaidWithdrawRequest{" +
                "raidId='" + raidId + '\'' +
                ", userId='" + userId + '\'' +
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

        public Builder withRaidId(String raidId) {
            this.raidId = raidId;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public RaidWithdrawRequest build() {
            return new RaidWithdrawRequest(raidId, userId);
        }
    }
}
