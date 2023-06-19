package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.RoleAssignmentRequest;
import raidsunlimited.activity.results.RoleAssignmentResult;

public class RoleAssignmentLambda extends LambdaActivityRunner<RoleAssignmentRequest, RoleAssignmentResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RoleAssignmentRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RoleAssignmentRequest> input, Context context) {
        return super.runActivity(
            () -> {
                RoleAssignmentRequest unauthenticatedRequest = input.fromBody(RoleAssignmentRequest.class);
                return input.fromPath(path -> {
                    String raidId = path.get("raidId");
                    return input.fromUserClaims(claims -> {
                        String raidOwner = claims.get("email");
                        return RoleAssignmentRequest.builder()
                                    .withRaidId(raidId)
                                    .withUserId(unauthenticatedRequest.getUserId())
                                    .withRaidRole(unauthenticatedRequest.getRaidRole())
                                    .withRaidOwner(raidOwner)
                                    .build();
                    });
                });
            },
            (request, serviceComponent) -> serviceComponent.provideRoleAssignmentActivity().handleRequest(request)
        );
    }
}



