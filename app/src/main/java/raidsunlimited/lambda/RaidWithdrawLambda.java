package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.RaidWithdrawRequest;
import raidsunlimited.activity.results.RaidWithdrawResult;

public class RaidWithdrawLambda
        extends LambdaActivityRunner<RaidWithdrawRequest, RaidWithdrawResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RaidWithdrawRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RaidWithdrawRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path -> {
                String raidId = path.get("raidId");
                String userId = path.get("userId");
                return input.fromUserClaims(claims -> {
                    String raidOwner = claims.get("email");
                    return RaidWithdrawRequest.builder()
                                    .withUserId(userId)
                                    .withRaidId(raidId)
                                    .build();
                });
            }),
            (request, serviceComponent) ->
                            serviceComponent.provideRaidWithdrawActivity().handleRequest(request)
        );
    }
}

