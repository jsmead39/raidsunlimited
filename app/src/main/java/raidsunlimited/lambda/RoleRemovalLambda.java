package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.RoleRemovalRequest;
import raidsunlimited.activity.results.RoleRemovalResult;

public class RoleRemovalLambda extends LambdaActivityRunner<RoleRemovalRequest, RoleRemovalResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RoleRemovalRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RoleRemovalRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    RoleRemovalRequest unauthenticatedRequest = input.fromBody(RoleRemovalRequest.class);
                    return input.fromPath(path -> {
                        String raidId = path.get("raidId"); // get raidId from path parameters
                        return input.fromUserClaims(claims -> {
                            String raidOwner = claims.get("email");
                            return RoleRemovalRequest.builder()
                                    .withRaidId(raidId)
                                    .withUserId(unauthenticatedRequest.getUserId())
                                    .withRaidOwner(raidOwner)
                                    .build();
                        });
                    });
                },
                (request, serviceComponent) -> serviceComponent.provideRoleRemovalActivity().handleRequest(request)
        );
    }
}



