package raidsunlimited.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;


@JsonDeserialize(builder = CreateFeedbackRequest.Builder.class)
public class CreateFeedbackRequest {
    private final String userId;
    private final String raidId;
    private final Integer rating;
    private final String comments;

    private CreateFeedbackRequest(String userId, String raidId, Integer rating, String comments) {
        this.userId = userId;
        this.raidId = raidId;
        this.rating = rating;
        this.comments = comments;
    }

    public String getUserId() {
        return userId;
    }

    public String getRaidId() {
        return raidId;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public String toString() {
        return "CreateFeedbackRequest{" +
                "userId='" + userId + '\'' +
                ", raidId='" + raidId + '\'' +
                ", rating=" + rating +
                ", comments='" + comments + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    @JsonPOJOBuilder
    public static class Builder {
        private String userId;
        private String raidId;
        private Integer rating;
        private String comments;

        public Builder withUserId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder withRaidId(String raidId) {
            this.raidId = raidId;
            return this;
        }

        public Builder withRating(Integer rating) {
            this.rating = rating;
            return this;
        }

        public Builder withComments(String comments) {
            this.comments = comments;
            return this;
        }

        public CreateFeedbackRequest build() {
            return new CreateFeedbackRequest(userId, raidId, rating, comments);
        }
    }
}
