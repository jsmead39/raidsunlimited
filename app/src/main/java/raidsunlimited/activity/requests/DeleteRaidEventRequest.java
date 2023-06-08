package raidsunlimited.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

@JsonDeserialize(builder = DeleteRaidEventRequest.class)
public class DeleteRaidEventRequest {
    private final String raidId;
    private final String email;

    private DeleteRaidEventRequest(String raidId, String email) {
        this.raidId = raidId;
        this.email = email;
    }

    public String getRaidId() {
        return raidId;
    }

    public String getEmail() {
        return email;
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String raidId;
        private String email;

        public Builder withRaidId(String raidId) {
            this.raidId = raidId;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public DeleteRaidEventRequest build() {
            return new DeleteRaidEventRequest(raidId, email);
        }
    }
}
