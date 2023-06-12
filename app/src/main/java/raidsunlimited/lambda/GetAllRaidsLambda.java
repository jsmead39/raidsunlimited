package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import raidsunlimited.activity.requests.GetAllRaidsRequest;
import raidsunlimited.activity.results.GetAllRaidsResult;

public class GetAllRaidsLambda
        extends LambdaActivityRunner<GetAllRaidsRequest, GetAllRaidsResult>
        implements RequestHandler<LambdaRequest<GetAllRaidsRequest>, LambdaResponse> {

    private final Logger log = LogManager.getLogger();

    @Override
    public LambdaResponse handleRequest(LambdaRequest<GetAllRaidsRequest> input, Context context) {
        log.info("handleRequest");
        return super.runActivity(
            () -> input.fromPath(path ->
                        GetAllRaidsRequest.builder()
                                .build()),
            (request, serviceComponent) ->
                        serviceComponent.provideGetAllRaidsActivity().handleRequest(request)
        );
    }
}
