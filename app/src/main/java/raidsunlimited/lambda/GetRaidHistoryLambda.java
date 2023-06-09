package raidsunlimited.lambda;



import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.GetRaidHistoryRequest;
import raidsunlimited.activity.results.GetRaidHistoryResult;

public class GetRaidHistoryLambda
        extends LambdaActivityRunner<GetRaidHistoryRequest, GetRaidHistoryResult>
        implements RequestHandler<LambdaRequest<GetRaidHistoryRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetRaidHistoryRequest> input, Context context) {
        log.info("handleRequest");
        return super.runActivity(
                () -> input.fromPath(path ->
                        GetRaidHistoryRequest.builder()
                                .withUserId(path.get("userId"))
                                .build()),
                (request, serviceComponent) ->
                        serviceComponent.provideGetRaidHistoryActivity().handleRequest(request)
        );
    }
}

