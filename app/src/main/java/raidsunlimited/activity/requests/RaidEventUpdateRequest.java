package raidsunlimited.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import raidsunlimited.models.RaidModel;

import java.util.Map;

@JsonDeserialize(builder = RaidEventUpdateRequest.Builder.class)
public class RaidEventUpdateRequest {
    private final RaidModel raidEvent;

    private RaidEventUpdateRequest(RaidModel raidEvent) {
        this.raidEvent = raidEvent;
    }

    public RaidModel getRaidEvent() {
        return raidEvent;
    }

    @Override
    public String toString() {
        return "RaidEventUpdateRequest{" +
                "raidEvent=" + raidEvent +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private RaidModel raidEvent;

        public Builder withRaidEvent(RaidModel raidEvent) {
            this.raidEvent = raidEvent;
            return this;
        }

        public RaidEventUpdateRequest build() {
            return new RaidEventUpdateRequest(raidEvent);
        }
    }
}
