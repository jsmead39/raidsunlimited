package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.GetProfileRequest;
import raidsunlimited.activity.results.GetProfileResult;

public class GetProfileLambda
        extends LambdaActivityRunner<GetProfileRequest, GetProfileResult>
        implements RequestHandler<LambdaRequest<GetProfileRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetProfileRequest> input, Context context) {
        log.info("handleRequest");
        return super.runActivity(
            () -> input.fromPath(path ->
                        GetProfileRequest.builder()
                                .withUserId(path.get("userId"))
                                .build()),
            (request, serviceComponent) ->
                        serviceComponent.provideGetProfileActivity().handleRequest(request)
        );
    }
}
