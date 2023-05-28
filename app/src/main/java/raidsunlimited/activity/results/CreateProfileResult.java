package raidsunlimited.activity.results;

import raidsunlimited.models.GameCharacter;

import java.util.ArrayList;
import java.util.List;

public class CreateProfileResult {
    private final String userId;
    private final String displayName;
    private final String email;
    private final List<GameCharacter> charactersList;
    private final String logs;

    private CreateProfileResult(String userId, String displayName, String email, List<GameCharacter> charactersList,
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
        return new ArrayList<>(charactersList);
    }

    public String getLogs() {
        return logs;
    }

    @Override
    public String toString() {
        return "CreateProfileResult{" +
                "userId='" + userId + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", charactersList=" + charactersList +
                ", logs='" + logs + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static CreateRaidResult.Builder builder() {
        return new CreateRaidResult.Builder();
    }

    public static class Builder {
        private String userId;
        private String displayName;
        private String email;
        private List<GameCharacter> charactersList;
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

        public Builder withCharacterList(List<GameCharacter> charactersList) {
            this.charactersList = new ArrayList<>(charactersList);
            return this;
        }

        public Builder withLogs(String logs) {
            this.logs = logs;
            return this;
        }

        public CreateProfileResult build() {
            return new CreateProfileResult(userId, displayName, email, charactersList, logs);
        }
    }
}
