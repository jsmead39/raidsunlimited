package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import raidsunlimited.activity.requests.RaidSignupRequest;
import raidsunlimited.activity.results.RaidSignupResult;

public class RaidSignupLambda
        extends LambdaActivityRunner<RaidSignupRequest, RaidSignupResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RaidSignupRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RaidSignupRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    RaidSignupRequest unauthenticatedRequest = input.fromBody(RaidSignupRequest.class);
                        return RaidSignupRequest.builder()
                                .withRaidId(unauthenticatedRequest.getRaidId())
                                .withUserId(unauthenticatedRequest.getUserId())
                                .withDisplayName(unauthenticatedRequest.getDisplayName())
                                .withGameCharacter(unauthenticatedRequest.getGameCharacter())
                                .build();

                },
                (request, serviceComponent) ->
                        serviceComponent.provideRaidSignupActivity().handleRequest(request)
        );

    }
}
