package raidsunlimited.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import raidsunlimited.models.GameCharacter;

@JsonDeserialize(builder = RaidSignupRequest.Builder.class)
public class RaidSignupRequest {
    private final String raidId;
    private final String userId;
    private final String displayName;
    private final GameCharacter gameCharacter;

    public RaidSignupRequest(String raidId, String userId, String displayName, GameCharacter gameCharacter) {
        this.raidId = raidId;
        this.userId = userId;
        this.displayName = displayName;
        this.gameCharacter = gameCharacter;
    }

    public String getRaidId() {
        return raidId;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public GameCharacter getGameCharacter() {
        return gameCharacter;
    }

    @Override
    public String toString() {
        return "RaidSignupRequest{" +
                "raidId='" + raidId + '\'' +
                ", userId='" + userId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", gameCharacter=" + gameCharacter +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String raidId;
        private String userId;
        private String displayName;
        private GameCharacter gameCharacter;

        public Builder withRaidId(String raidId) {
            this.raidId = raidId;
            return this;
        }

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder withGameCharacter(GameCharacter gameCharacter) {
            this.gameCharacter = gameCharacter;
            return this;
        }

        public RaidSignupRequest build() {
            return new RaidSignupRequest(raidId, userId, displayName, gameCharacter);
        }
    }
}
