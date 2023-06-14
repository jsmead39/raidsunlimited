package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.RaidEventUpdateRequest;
import raidsunlimited.activity.results.RaidEventUpdateResult;

public class RaidEventUpdateLambda
        extends LambdaActivityRunner<RaidEventUpdateRequest, RaidEventUpdateResult>
        implements RequestHandler<AuthenticatedLambdaRequest<RaidEventUpdateRequest>, LambdaResponse> {
    private final Logger log = LogManager.getLogger();
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<RaidEventUpdateRequest> input, Context context) {
        log.info("Received request", input.toString());
        return super.runActivity(
            () -> {
                RaidEventUpdateRequest unauthenticatedRequest = input.fromBody(RaidEventUpdateRequest.class);
                log.info("Parsed request", unauthenticatedRequest.toString());
                return input.fromUserClaims(claims -> input.fromPath(path -> RaidEventUpdateRequest.builder()
                        .withRaidName(unauthenticatedRequest.getRaidName())
                        .withRaidServer(unauthenticatedRequest.getRaidServer())
                        .withRaidDate(unauthenticatedRequest.getRaidDate())
                        .withTime(unauthenticatedRequest.getTime())
                        .withRaidSize(unauthenticatedRequest.getRaidSize())
                        .withRaidObjective(unauthenticatedRequest.getRaidObjective())
                        .withLootDistribution(unauthenticatedRequest.getLootDistribution())
                        .withRequiredRoles(unauthenticatedRequest.getRequiredRoles())
                        .withRaidOwner(claims.get("email"))
                        .withRaidId(path.get("raidId"))
                        .withRaidStatus(unauthenticatedRequest.getRaidStatus())
                        .build()));
            },
            (request, serviceComponent) ->
                        serviceComponent.provideRaidEventUpdateActivity().handleRequest(request)
        );
    }
}

