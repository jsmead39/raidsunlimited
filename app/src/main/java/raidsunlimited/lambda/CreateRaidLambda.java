package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import raidsunlimited.activity.requests.CreateRaidRequest;
import raidsunlimited.activity.results.CreateRaidResult;

public class CreateRaidLambda
        extends LambdaActivityRunner<CreateRaidRequest, CreateRaidResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateRaidRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateRaidRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    CreateRaidRequest unauthenticatedRequest = input.fromBody(CreateRaidRequest.class);
                    return input.fromUserClaims(claims -> {
                                    String raidOwner = claims.containsKey("email") ? claims.get("email") :
                                            unauthenticatedRequest.getRaidOwner();
                            return CreateRaidRequest.builder()
                                    .withRaidName(unauthenticatedRequest.getRaidName())
                                    .withRaidDate(unauthenticatedRequest.getRaidDate())
                                    .withTime(unauthenticatedRequest.getTime())
                                    .withRaidSize(unauthenticatedRequest.getRaidSize())
                                    .withRaidObjective(unauthenticatedRequest.getRaidObjective())
                                    .withLootDistribution(unauthenticatedRequest.getLootDistribution())
                                    .withRequiredRoles(unauthenticatedRequest.getRequiredRoles())
                                    .withRaidOwner(raidOwner)
                                    .build();
                    });
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreateRaidActivity().handleRequest(request)
        );
    }
}

