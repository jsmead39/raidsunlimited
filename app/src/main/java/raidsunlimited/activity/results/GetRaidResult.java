package raidsunlimited.activity.results;

import raidsunlimited.models.RaidModel;

public class GetRaidResult {
    private final RaidModel raidModel;

    private GetRaidResult(RaidModel raidModel) {
        this.raidModel = raidModel;
    }

    public RaidModel getRaidModel() {
        return raidModel;
    }

    @Override
    public String toString() {
        return "GetRaidResult{" +
                "raidModel=" + raidModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RaidModel raidModel;

        public Builder withRaidModel(RaidModel raidModel) {
            this.raidModel = raidModel;
            return this;
        }

        public GetRaidResult build() {
            return new GetRaidResult(raidModel);
        }
    }
}
