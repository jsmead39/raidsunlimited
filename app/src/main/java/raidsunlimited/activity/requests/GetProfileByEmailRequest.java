package raidsunlimited.activity.requests;

public class GetProfileByEmailRequest {
    private final String email;

    private GetProfileByEmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "GetProfileByEmailRequest{" +
                "email='" + email + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }
    public static class Builder {
        private String email;

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public GetProfileByEmailRequest build() {
            return new GetProfileByEmailRequest(email);
        }
    }
}
