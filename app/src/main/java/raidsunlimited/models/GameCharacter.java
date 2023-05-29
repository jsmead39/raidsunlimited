package raidsunlimited.models;

import java.util.Objects;

public class GameCharacter {
    private String charName;
    private String specialization;
    private String role;

    public GameCharacter() {

    }
    public GameCharacter(String charName, String specialization, String role) {
        this.charName = charName;
        this.specialization = specialization;
        this.role = role;
    }

    public String getCharName() {
        return charName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameCharacter that = (GameCharacter) o;
        return Objects.equals(charName, that.charName) && Objects.equals(specialization, that.specialization) &&
                Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(charName, specialization, role);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String charName;
        private String specialization;
        private String role;

        public Builder withCharName(String charName) {
            this.charName = charName;
            return this;
        }

        public Builder withSpecialization(String specialization) {
            this.specialization = specialization;
            return this;
        }

        public Builder withRole(String role) {
            this.role = role;
            return this;
        }

        public GameCharacter build() {
            GameCharacter gameCharacter = new GameCharacter();
            gameCharacter.charName = this.charName;
            gameCharacter.specialization = this.specialization;
            gameCharacter.role = this.role;
            return gameCharacter;
        }
    }
}
