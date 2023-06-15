package raidsunlimited.activity.results;

import raidsunlimited.models.FeedbackModel;

public class CreateFeedbackResult {
    private final FeedbackModel feedbackModel;

    private CreateFeedbackResult(FeedbackModel feedbackModel) {
        this.feedbackModel = feedbackModel;
    }

    public FeedbackModel getFeedbackModel() {
        return feedbackModel;
    }

    @Override
    public String toString() {
        return "CreateFeedbackResult{" +
                "feedbackModel=" + feedbackModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private FeedbackModel feedbackModel;

        public Builder withFeedback(FeedbackModel feedbackModel) {
            this.feedbackModel = feedbackModel;
            return this;
        }

        public CreateFeedbackResult build() {
            return new CreateFeedbackResult(feedbackModel);
        }
    }
}
