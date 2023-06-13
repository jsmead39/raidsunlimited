package raidsunlimited.activity.results;

public class RoleRemovalResult {
    private final String raidId;
    private final String userId;
    private final Boolean status;

    private RoleRemovalResult(String raidId, String userId, Boolean status) {
        this.raidId = raidId;
        this.userId = userId;
        this.status = status;
    }

    public String getRaidId() {
        return raidId;
    }

    public String getUserId() {
        return userId;
    }

    public Boolean getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "RoleRemovalResult{" +
                "raidId='" + raidId + '\'' +
                ", userId='" + userId + '\'' +
                ", status=" + status +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String raidId;
        private String userId;
        private Boolean status;

        public Builder withRaidId(String raidId) {
            this.raidId = raidId;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withStatus(Boolean status) {
            this.status = status;
            return this;
        }

        public RoleRemovalResult build() {
            return new RoleRemovalResult(raidId, userId, status);
        }
    }
}
