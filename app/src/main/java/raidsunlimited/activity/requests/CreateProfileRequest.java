package raidsunlimited.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import raidsunlimited.models.GameCharacter;

import java.util.List;

@JsonDeserialize(builder = CreateProfileRequest.Builder.class)
public class CreateProfileRequest {
    private final String userId;
    private final String displayName;
    private final String email;
    private final List<GameCharacter> charactersList;
    private final String logs;

    public CreateProfileRequest(String userId, String displayName, String email, List<GameCharacter> charactersList,
                                String logs) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.charactersList = charactersList;
        this.logs = logs;
    }

    public String getUserId() {
        return userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public List<GameCharacter> getCharactersList() {
        return charactersList;
    }

    public String getLogs() {
        return logs;
    }

    //CHECKSTYLE:OFF:Builder
    public static CreateRaidRequest.Builder builder() {
        return new CreateRaidRequest.Builder();

    }

    @JsonPOJOBuilder
    public static class Builder {
        private String userId;
        private String displayName;
        private String email;
        private List<GameCharacter> gameCharacters;
        private String logs;


        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withGameCharacters(List<GameCharacter> gameCharacters) {
            this.gameCharacters = gameCharacters;
            return this;
        }

        public Builder withLogs(String logs) {
            this.logs = logs;
            return this;
        }

        public CreateProfileRequest build() {
            return new CreateProfileRequest(userId, displayName, email, gameCharacters, logs);
        }
    }
}
