package raidsunlimited.activity.results;

import raidsunlimited.models.RaidModel;

public class RaidWithdrawResult {
    private final RaidModel raidModel;

    private RaidWithdrawResult(RaidModel raidModel) {
        this.raidModel = raidModel;
    }

    public RaidModel getRaidModel() {
        return raidModel;
    }

    @Override
    public String toString() {
        return "RaidWithdrawResult{" +
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

        public RaidWithdrawResult build() {
            return new RaidWithdrawResult(raidModel);
        }
    }
}
