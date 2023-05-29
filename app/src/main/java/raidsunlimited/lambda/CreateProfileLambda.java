package raidsunlimited.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import raidsunlimited.activity.CreateProfileActivity;
import raidsunlimited.activity.requests.CreateProfileRequest;
import raidsunlimited.activity.results.CreateProfileResult;

public class CreateProfileLambda
        extends LambdaActivityRunner<CreateProfileRequest, CreateProfileResult>
        implements RequestHandler<AuthenticatedLambdaRequest<CreateProfileRequest>, LambdaResponse> {
    @Override
    public LambdaResponse handleRequest(AuthenticatedLambdaRequest<CreateProfileRequest> input, Context context) {
        return super.runActivity(
                () -> {
                    CreateProfileRequest unauthenticatedRequest = input.fromBody(CreateProfileRequest.class);
                    return input.fromUserClaims(claims -> {
                        String profileOwner = claims.get("email");
                        return CreateProfileRequest.builder()
                                .withDisplayName(unauthenticatedRequest.getDisplayName())
                                .withEmail(profileOwner)
                                .withGameCharacters(unauthenticatedRequest.getCharactersList())
                                .withLogs(unauthenticatedRequest.getLogs())
                                .build();
                    });
                },
                (request, serviceComponent) ->
                        serviceComponent.provideCreateProfileActivity().handleRequest(request)
        );
    }
}
