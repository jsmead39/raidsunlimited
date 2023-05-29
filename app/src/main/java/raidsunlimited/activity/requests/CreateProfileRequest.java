package raidsunlimited.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import raidsunlimited.models.GameCharacter;

import java.util.List;

import static raidsunlimited.utils.CollectionUtils.copyToList;

@JsonDeserialize(builder = CreateProfileRequest.Builder.class)
public class CreateProfileRequest {
    private final String displayName;
    private final String email;
    private final List<GameCharacter> charactersList;
    private final String logs;

    private CreateProfileRequest(String displayName, String email, List<GameCharacter> charactersList,
                                String logs) {
        this.displayName = displayName;
        this.email = email;
        this.charactersList = charactersList;
        this.logs = logs;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public List<GameCharacter> getCharactersList() {
        return copyToList(charactersList);
    }

    public String getLogs() {
        return logs;
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();

    }

    @JsonPOJOBuilder
    public static class Builder {
        private String displayName;
        private String email;
        private List<GameCharacter> gameCharacters;
        private String logs;

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withGameCharacters(List<GameCharacter> gameCharacters) {
            this.gameCharacters = copyToList(gameCharacters);
            return this;
        }

        public Builder withLogs(String logs) {
            this.logs = logs;
            return this;
        }

        public CreateProfileRequest build() {
            return new CreateProfileRequest(displayName, email, gameCharacters, logs);
        }
    }
}
