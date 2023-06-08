package raidsunlimited.activity.results;

public class DeleteRaidEventResult {
    private final String raidId;

    private DeleteRaidEventResult(String raidId) {
        this.raidId = raidId;
    }

    public String getRaidId() {
        return raidId;
    }

    @Override
    public String toString() {
        return "DeleteRaidEventResult{" +
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

        public DeleteRaidEventResult build() {
            return new DeleteRaidEventResult(raidId);
        }
    }
}
