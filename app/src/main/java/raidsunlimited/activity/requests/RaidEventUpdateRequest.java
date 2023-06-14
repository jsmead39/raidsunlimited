package raidsunlimited.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import raidsunlimited.models.RaidModel;

import java.util.Map;

@JsonDeserialize(builder = RaidEventUpdateRequest.Builder.class)
public class RaidEventUpdateRequest {
    private final RaidModel raidEvent;
    private final String raidOwner;

    private RaidEventUpdateRequest(RaidModel raidEvent, String raidOwner) {
        this.raidEvent = raidEvent;
        this.raidOwner = raidOwner;
    }

    public RaidModel getRaidEvent() {
        return raidEvent;
    }

    public String getRaidOwner() {
        return raidOwner;
    }

    @Override
    public String toString() {
        return "RaidEventUpdateRequest{" +
                "raidEvent=" + raidEvent +
                ", raidOwner='" + raidOwner + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private RaidModel raidEvent;
        private String raidOwner;

        public Builder withRaidEvent(RaidModel raidEvent) {
            this.raidEvent = raidEvent;
            return this;
        }

        public Builder withRaidOwner(String raidOwner) {
            this.raidOwner = raidOwner;
            return this;
        }

        public RaidEventUpdateRequest build() {
            return new RaidEventUpdateRequest(raidEvent, raidOwner);
        }
    }
}
