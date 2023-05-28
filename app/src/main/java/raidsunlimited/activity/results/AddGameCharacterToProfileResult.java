package raidsunlimited.activity.results;

import raidsunlimited.models.GameCharacter;

import java.util.ArrayList;
import java.util.List;

public class AddGameCharacterToProfileResult {
    private final List<GameCharacter> gameCharacterList;

    private AddGameCharacterToProfileResult(List<GameCharacter> characterList) {
        this.gameCharacterList = characterList;
    }

    public List<GameCharacter> getGameCharacterList() {
        return new ArrayList<>(gameCharacterList);
    }

    @Override
    public String toString() {
        return "AddGameCharacterToProfileResult{" +
                "gameCharacterList=" + gameCharacterList +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private List<GameCharacter> gameCharacterList;

        public Builder withCharacterList(List<GameCharacter> characterList) {
            this.gameCharacterList = new ArrayList<>(characterList);
            return this;
        }

        public AddGameCharacterToProfileResult build() {
            return new AddGameCharacterToProfileResult(gameCharacterList);
        }
    }
}
