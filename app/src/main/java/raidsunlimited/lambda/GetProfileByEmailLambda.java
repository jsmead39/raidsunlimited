package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.GetProfileByEmailRequest;
import raidsunlimited.activity.results.GetProfileResult;

public class GetProfileByEmailLambda
        extends LambdaActivityRunner<GetProfileByEmailRequest, GetProfileResult>
        implements RequestHandler<AuthenticatedLambdaRequest<GetProfileByEmailRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<GetProfileByEmailRequest> input, Context context) {
        log.info("handleRequest");
        return super.runActivity(
            () -> input.fromUserClaims(claims ->
                        GetProfileByEmailRequest.builder()
                                .withEmail(claims.get("email"))
                                .build()),
            (request, serviceComponent) ->
                        serviceComponent.provideGetProfileActivity().handleRequestByEmail(request)
        );
    }
}

