package raidsunlimited.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import raidsunlimited.models.GameCharacter;

import java.util.List;

import static raidsunlimited.utils.CollectionUtils.copyToList;

@JsonDeserialize(builder = UpdateProfileRequest.Builder.class)
public class UpdateProfileRequest {
    private final String userId;
    private final String email;
    private final String displayName;
    private final List<GameCharacter> charactersList;
    private final String logs;

    private UpdateProfileRequest(String userId, String email, String displayName, List<GameCharacter> charactersList,
                                 String logs) {
        this.userId = userId;
        this.email = email;
        this.displayName = displayName;
        this.charactersList = charactersList;
        this.logs = logs;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public List<GameCharacter> getCharactersList() {
        return copyToList(charactersList);
    }

    public String getLogs() {
        return logs;
    }

    @Override
    public String toString() {
        return "UpdateProfileRequest{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", charactersList=" + charactersList +
                ", logs='" + logs + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();

    }

    @JsonPOJOBuilder
    public static class Builder {
        private String userId;
        private String email;
        private String displayName;
        private List<GameCharacter> charactersList;
        private String logs;

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withDisplayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder withCharactersList(List<GameCharacter> gameCharacters) {
            this.charactersList= copyToList(gameCharacters);
            return this;
        }

        public Builder withLogs(String logs) {
            this.logs = logs;
            return this;
        }

        public UpdateProfileRequest build() {
            return new UpdateProfileRequest(userId, email, displayName, charactersList, logs);
        }
    }
}
