package raidsunlimited.models;

import java.util.Objects;

public class FeedbackModel {
    private final String userId;
    private final Integer rating;
    private final String comments;

    private FeedbackModel(String userId, Integer rating, String comments) {
        this.userId = userId;
        this.rating = rating;
        this.comments = comments;
    }

    public String getUserId() {
        return userId;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComments() {
        return comments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FeedbackModel that = (FeedbackModel) o;
        return Objects.equals(userId, that.userId) && Objects.equals(rating, that.rating) && Objects.equals(comments, that.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, rating, comments);
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String userId;
        private Integer rating;
        private String comments;

        public Builder withUserId(String userId) {
            this.userId = userId;
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

        public FeedbackModel build() {
            return new FeedbackModel(userId, rating, comments);
        }
    }
}
