package raidsunlimited.activity.requests;

public class GetAllRaidsRequest {

    private GetAllRaidsRequest() {
    }

    @Override
    public String toString() {
        return "GetAllRaidsRequest{}";
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        public GetAllRaidsRequest build() {
            return new GetAllRaidsRequest();
        }
    }
}
