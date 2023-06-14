package raidsunlimited.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import raidsunlimited.models.RaidModel;

import java.util.Map;

@JsonDeserialize(builder = RaidEventUpdateRequest.Builder.class)
public class RaidEventUpdateRequest {
    private final RaidModel raidEvent;
    private final String raidOwner;
    private final String raidId;

    private RaidEventUpdateRequest(RaidModel raidEvent, String raidOwner, String raidId) {
        this.raidEvent = raidEvent;
        this.raidOwner = raidOwner;
        this.raidId = raidId;
    }

    public RaidModel getRaidEvent() {
        return raidEvent;
    }

    public String getRaidOwner() {
        return raidOwner;
    }

    public String getRaidId() {
        return raidId;
    }

    @Override
    public String toString() {
        return "RaidEventUpdateRequest{" +
                "raidEvent=" + raidEvent +
                ", raidOwner='" + raidOwner + '\'' +
                ", raidId='" + raidId + '\'' +
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
        private String raidId;

        public Builder withRaidEvent(RaidModel raidEvent) {
            this.raidEvent = raidEvent;
            return this;
        }

        public Builder withRaidOwner(String raidOwner) {
            this.raidOwner = raidOwner;
            return this;
        }

        public Builder withRaidId(String raidId) {
            this.raidId = raidId;
            return this;
        }

        public RaidEventUpdateRequest build() {
            return new RaidEventUpdateRequest(raidEvent, raidOwner, raidId);
        }
    }
}
