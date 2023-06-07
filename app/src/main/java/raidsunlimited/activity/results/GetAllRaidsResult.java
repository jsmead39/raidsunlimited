package raidsunlimited.activity.results;

import raidsunlimited.models.RaidModel;

import java.util.List;

public class GetAllRaidsResult {
    private final List<RaidModel> raidModelList;

    private GetAllRaidsResult(List<RaidModel> raidModelList) {
        this.raidModelList = raidModelList;
    }

    public List<RaidModel> getRaidModelList() {
        return raidModelList;
    }

    @Override
    public String toString() {
        return "GetAllRaidsResult{" +
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

        public GetAllRaidsResult build() {
            return new GetAllRaidsResult(raidModelList);
        }
    }
}
