package raidsunlimited.activity.results;

public class RoleAssignmentResult {
    private final String raidId;
    private final String userId;
    private final String raidRole;
    private final Boolean status;


    private RoleAssignmentResult(String raidId, String userId, String raidRole, Boolean status) {
        this.raidId = raidId;
        this.userId = userId;
        this.raidRole = raidRole;
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

    public String getRaidRole() {
        return raidRole;
    }

    @Override
    public String toString() {
        return "RoleAssignmentResult{" +
                "raidId='" + raidId + '\'' +
                ", userId='" + userId + '\'' +
                ", raidRole='" + raidRole + '\'' +
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
        private String raidRole;
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

        public Builder withRaidRole(String raidRole) {
            this.raidRole = raidRole;
            return this;
        }

        public RoleAssignmentResult build() {
            return new RoleAssignmentResult(raidId, userId, raidRole, status);
        }
    }
}
