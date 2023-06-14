package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import raidsunlimited.activity.requests.RaidEventUpdateRequest;
import raidsunlimited.activity.results.RaidEventUpdateResult;

public class RaidEventUpdateLambda
        extends LambdaActivityRunner<RaidEventUpdateRequest, RaidEventUpdateResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RaidEventUpdateRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RaidEventUpdateRequest> input, Context context) {
        return super.runActivity(
            () -> {
                RaidEventUpdateRequest unauthenticatedRequest = input.fromBody(RaidEventUpdateRequest.class);
                return input.fromUserClaims(claims -> {
                    return RaidEventUpdateRequest.builder()
                                    .withRaidOwner(claims.get("email"))
                                    .withRaidEvent(unauthenticatedRequest.getRaidEvent())
                                    .build();
                });
            },
            (request, serviceComponent) ->
                        serviceComponent.provideRaidEventUpdateActivity().handleRequest(request)
        );
    }
}

