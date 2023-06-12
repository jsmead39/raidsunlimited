package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import raidsunlimited.activity.requests.RoleAssignmentRequest;
import raidsunlimited.activity.results.RoleAssignmentResult;

public class RoleAssignmentRequestLambda
        extends LambdaActivityRunner<RoleAssignmentRequest, RoleAssignmentResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RoleAssignmentRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RoleAssignmentRequest> input, Context context) {
        return super.runActivity(
            () -> {
                RoleAssignmentRequest unauthenticatedRequest = input.fromBody(RoleAssignmentRequest.class);
                return input.fromUserClaims(claims ->
                            RoleAssignmentRequest.builder()
                                    .withRaidId(unauthenticatedRequest.getRaidId())
                                    .withUserId(unauthenticatedRequest.getUserId())
                                    .withRaidRole(unauthenticatedRequest.getRaidRole())
                                    .withRaidOwner(claims.get("email"))
                                    .build());
            },
            (request, serviceComponent) ->
                        serviceComponent.provideRoleAssignmentActivity().handleRequest(request)
        );
    }
}
