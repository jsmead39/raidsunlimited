package raidsunlimited.activity.results;

public class RoleAssignmentResult {
    private final String raidId;
    private final String userId;
    private final String status;

    private RoleAssignmentResult(String raidId, String userId, String status) {
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

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "RoleAssignmentResult{" +
                "raidId='" + raidId + '\'' +
                ", userId='" + userId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String raidId;
        private String userId;
        private String status;

        public Builder withRaidId(String raidId) {
            this.raidId = raidId;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withStatus(String status) {
            this.status = status;
            return this;
        }

        public RoleAssignmentResult build() {
            return new RoleAssignmentResult(raidId, userId, status);
        }
    }
}
