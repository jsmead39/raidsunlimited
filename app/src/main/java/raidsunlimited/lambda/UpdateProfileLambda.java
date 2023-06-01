package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import raidsunlimited.activity.requests.UpdateProfileRequest;
import raidsunlimited.activity.results.UpdateProfileResult;

public class UpdateProfileLambda
        extends LambdaActivityRunner<UpdateProfileRequest, UpdateProfileResult>
        implements RequestHandler<AuthenticatedLambdaRequest<UpdateProfileRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<UpdateProfileRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    UpdateProfileRequest unauthenticatedRequest = input.fromBody(UpdateProfileRequest.class);
                    return input.fromUserClaims(claims ->
                            UpdateProfileRequest.builder()
                                    .withUserId(unauthenticatedRequest.getUserId())
                                    .withDisplayName(unauthenticatedRequest.getDisplayName())
                                    .withCharactersList(unauthenticatedRequest.getCharactersList())
                                    .withLogs(unauthenticatedRequest.getLogs())
                                    .withEmail(claims.get("email"))
                                    .build());
                },
                (request, serviceComponent) ->
                        serviceComponent.provideUpdateProfileActivity().handleRequest(request)
        );
    }
}
