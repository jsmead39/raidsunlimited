package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.GetRaidRequest;
import raidsunlimited.activity.results.GetRaidResult;

public class GetRaidLambda
        extends LambdaActivityRunner<GetRaidRequest, GetRaidResult>
        implements RequestHandler<LambdaRequest<GetRaidRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetRaidRequest> input, Context context) {
        log.info("handleRequest");
        return super.runActivity(
                () -> input.fromPath(path ->
                        GetRaidRequest.builder()
                                .withRaidId(path.get("id"))
                                .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetRaidActivity().handleRequest(request)
        );
    }
}
