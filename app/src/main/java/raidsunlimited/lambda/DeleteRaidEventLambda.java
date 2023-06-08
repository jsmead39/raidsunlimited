package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import raidsunlimited.activity.requests.DeleteRaidEventRequest;
import raidsunlimited.activity.results.DeleteRaidEventResult;

public class DeleteRaidEventLambda
        extends LambdaActivityRunner<DeleteRaidEventRequest, DeleteRaidEventResult>
        implements RequestHandler<AuthenticatedLambdaRequest<DeleteRaidEventRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<DeleteRaidEventRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    DeleteRaidEventRequest unauthenticatedRequest = input.fromBody(DeleteRaidEventRequest.class);
                    return input.fromUserClaims(claims -> {
                        String raidOwner = claims.get("email");
                        return DeleteRaidEventRequest.builder()
                                .withEmail(raidOwner)
                                .withRaidId(unauthenticatedRequest.getRaidId())
                                .build();
                    });
                },
                (request, serviceComponent) ->
                        serviceComponent.provideDeleteRaidEventActivity().handleRequest(request)
        );
    }
}
