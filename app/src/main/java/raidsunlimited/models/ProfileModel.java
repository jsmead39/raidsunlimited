package raidsunlimited.models;

import java.util.List;
import java.util.Objects;

import static raidsunlimited.utils.CollectionUtils.copyToList;

public class ProfileModel {
    private final String userId;
    private final String displayName;
    private final String email;
    private final List<GameCharacter> characterList;
    private final String logs;

    private ProfileModel(String userId, String displayName, String email, List<GameCharacter> characterList, String logs) {
        this.userId = userId;
        this.displayName = displayName;
        this.email = email;
        this.characterList = characterList;
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

    public List<GameCharacter> getCharacterList() {
        return copyToList(characterList);
    }

    public String getLogs() {
        return logs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProfileModel profileModel = (ProfileModel) o;
        return Objects.equals(userId, profileModel.userId) && Objects.equals(displayName, profileModel.displayName) &&
                Objects.equals(email, profileModel.email) && Objects.equals(characterList, profileModel.characterList) &&
                Objects.equals(logs, profileModel.logs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, displayName, email, characterList, logs);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;
        private String displayName;
        private String email;
        private List<GameCharacter> characterList;
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

        public Builder withCharacterList(List<GameCharacter> characterList) {
            this.characterList = copyToList(characterList);
            return this;
        }

        public Builder withLogs(String logs) {
            this.logs = logs;
            return this;
        }

        public ProfileModel build() {
            return new ProfileModel(userId, displayName, email, characterList, logs);
        }
    }
}
