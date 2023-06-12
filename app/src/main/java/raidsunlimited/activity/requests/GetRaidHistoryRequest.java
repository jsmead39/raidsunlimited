package raidsunlimited.activity.requests;


public class GetRaidHistoryRequest {
    private final String userId;

    private GetRaidHistoryRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "GetRaidHistoryRequest{" +
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

        public GetRaidHistoryRequest build() {
            return new GetRaidHistoryRequest(userId);
        }
    }
}
