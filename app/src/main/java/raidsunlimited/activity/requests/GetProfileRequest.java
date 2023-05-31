package raidsunlimited.activity.requests;

public class GetProfileRequest {
    private final String userId;

    private GetProfileRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetProfileRequest{" +
                "userId='" + userId + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public GetProfileRequest build() {
            return new GetProfileRequest(userId);
        }
    }
}
