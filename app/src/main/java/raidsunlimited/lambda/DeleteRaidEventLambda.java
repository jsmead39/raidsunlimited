package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.DeleteRaidEventRequest;
import raidsunlimited.activity.results.DeleteRaidEventResult;

public class DeleteRaidEventLambda
        extends LambdaActivityRunner<DeleteRaidEventRequest, DeleteRaidEventResult>
        implements RequestHandler<AuthenticatedLambdaRequest<DeleteRaidEventRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<DeleteRaidEventRequest> input, Context context) {
        return super.runActivity(
            () -> input.fromPath(path -> {
                String raidId = path.get("raidId");
                return input.fromUserClaims(claims -> {
                    String raidOwner = claims.get("email");
                    return DeleteRaidEventRequest.builder()
                                    .withEmail(raidOwner)
                                    .withRaidId(raidId)
                                    .build();
                });
            }),
            (request, serviceComponent) ->
                            serviceComponent.provideDeleteRaidEventActivity().handleRequest(request)
        );
    }
}
