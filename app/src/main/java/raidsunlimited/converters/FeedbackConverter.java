package raidsunlimited.converters;

import raidsunlimited.models.FeedbackModel;

import java.util.ArrayList;
import java.util.List;

public class FeedbackConverter {
    public static FeedbackModel toFeedbackModel(String feedback) {
        String[] parts = feedback.split("\\s+", 3);
        if (parts.length == 3) {
            String userId = parts[0];
            int rating = Integer.parseInt(parts[1]);
            String comments = parts[2];
            return new FeedbackModel.Builder()
                    .withUserId(userId)
                    .withRating(rating)
                    .withComments(comments)
                    .build();

        }
        return null;
    }

    public static List<FeedbackModel> toFeedbackModelList(List<String> feedbackList) {
        List<FeedbackModel> result = new ArrayList<>();
        if (feedbackList != null && !feedbackList.isEmpty()) {
            for (String feedback : feedbackList) {
                FeedbackModel feedbackModel = toFeedbackModel(feedback);
                if (feedbackModel != null) {
                    result.add(feedbackModel);
                }
            }
        }
        return result;
    }
}
