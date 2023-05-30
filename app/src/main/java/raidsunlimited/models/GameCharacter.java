package raidsunlimited.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class GameCharacter {
    private final String charName;
    private final String charClass;
    private final String specialization;
    private final String role;

    @JsonCreator
    private GameCharacter(@JsonProperty("charName")String charName,
                          @JsonProperty("charClass")String charClass,
                          @JsonProperty("specialization") String specialization,
                          @JsonProperty("role") String role) {
        this.charName = charName;
        this.charClass = charClass;
        this.specialization = specialization;
        this.role = role;
    }

    public String getCharName() {
        return charName;
    }

    public String getCharClass() {
        return charClass;
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
        return Objects.equals(charName, that.charName) && Objects.equals(charClass, that.charClass) &&
                Objects.equals(specialization, that.specialization) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(charName, charClass, specialization, role);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String charName;
        private String charClass;
        private String specialization;
        private String role;

        public Builder withCharName(String charName) {
            this.charName = charName;
            return this;
        }

        public Builder withCharClass(String charClass) {
            this.charClass = charClass;
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
            return new GameCharacter(charName, charClass, specialization, role);
        }
    }
}
