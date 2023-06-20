package raidsunlimited.activity.results;

import raidsunlimited.models.RaidModel;

public class RaidSignupResult {
    private final RaidModel raidModel;

    private RaidSignupResult(RaidModel raidModel) {
        this.raidModel = raidModel;
    }

    public RaidModel getRaidModel() {
        return raidModel;
    }

    @Override
    public String toString() {
        return "RaidSignupResult{" +
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

        public RaidSignupResult build() {
            return new RaidSignupResult(raidModel);
        }
    }
}
