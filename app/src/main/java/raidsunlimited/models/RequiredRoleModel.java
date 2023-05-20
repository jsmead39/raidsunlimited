package raidsunlimited.models;

import java.util.Objects;

public class RequiredRoleModel {
    private final String role;
    private final Integer number;

    private RequiredRoleModel(String role, Integer number) {
        this.role = role;
        this.number = number;
    }

    public String getRole() {
        return role;
    }

    public Integer getNumber() {
        return number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequiredRoleModel that = (RequiredRoleModel) o;
        return Objects.equals(role, that.role) && Objects.equals(number, that.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, number);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String role;
        private Integer number;

        public Builder withRole(String role) {
            this.role = role;
            return this;
        }

        public Builder withNumber(Integer number) {
            this.number = number;
            return this;
        }

        public RequiredRoleModel build() {
            return new RequiredRoleModel(role, number);
        }
    }

}
