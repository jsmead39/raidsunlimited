package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.CreateFeedbackRequest;
import raidsunlimited.activity.results.CreateFeedbackResult;

public class CreateFeedbackLambda extends LambdaActivityRunner<CreateFeedbackRequest, CreateFeedbackResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateFeedbackRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateFeedbackRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    CreateFeedbackRequest unauthenticatedRequest = input.fromBody(CreateFeedbackRequest.class);
                    return input.fromPath(path -> {
                        String raidId = path.get("raidId"); // get raidId from path parameters
                            return CreateFeedbackRequest.builder()
                                    .withRaidId(raidId)
                                    .withComments(unauthenticatedRequest.getComments())
                                    .withRating(unauthenticatedRequest.getRating())
                                    .withUserId(unauthenticatedRequest.getUserId())
                                    .build();

                    });
                },
                (request, serviceComponent) -> serviceComponent.provideCreateFeedbackActivity().handleRequest(request)
        );
    }
}



