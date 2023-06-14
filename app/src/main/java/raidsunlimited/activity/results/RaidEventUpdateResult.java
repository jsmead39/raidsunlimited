package raidsunlimited.activity.results;

import raidsunlimited.models.RaidModel;

public class RaidEventUpdateResult {
    private final RaidModel raid;
    private RaidEventUpdateResult(RaidModel raid) {
        this.raid = raid;
    }

    public RaidModel getRaid() {
        return raid;
    }

    @Override
    public String toString() {
        return "CreateRaidResult{" +
                "raid=" + raid +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private RaidModel raid;

        public Builder withRaid(RaidModel raid) {
            this.raid = raid;
            return this;
        }

        public RaidEventUpdateResult build() {
            return new RaidEventUpdateResult(raid);
        }
    }
}
