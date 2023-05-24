package raidsunlimited.activity.requests;

public class GetRaidRequest {
    private String raidId;

    private GetRaidRequest(String raidId) {
        this.raidId = raidId;
    }

    public String getRaidId() {
        return raidId;
    }

    public void setRaidId(String raidId) {
        this.raidId = raidId;
    }

    @Override
    public String toString() {
        return "GetRaidRequest{" +
                "raidId='" + raidId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String raidId;

        public Builder withRaidId(String raidId) {
            this.raidId = raidId;
            return this;
        }

        public GetRaidRequest build() {
            return new GetRaidRequest(raidId);
        }
    }

}
