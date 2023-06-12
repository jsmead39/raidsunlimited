package raidsunlimited.activity.results;

import raidsunlimited.models.RaidModel;

import java.util.List;

public class GetRaidHistoryResult {
    private final List<RaidModel> raidModelList;

    private GetRaidHistoryResult(List<RaidModel> raidModelList) {
        this.raidModelList = raidModelList;
    }

    public List<RaidModel> getRaidModelList() {
        return raidModelList;
    }

    @Override
    public String toString() {
        return "GetRaidHistoryResult{" +
                "raidModelList=" + raidModelList +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<RaidModel> raidModelList;

        public Builder withRaidModelList(List<RaidModel> raidModelList) {
            this.raidModelList = raidModelList;
            return this;
        }

        public GetRaidHistoryResult build() {
            return new GetRaidHistoryResult(raidModelList);
        }
    }
}
