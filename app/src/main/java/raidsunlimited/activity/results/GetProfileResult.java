package raidsunlimited.activity.results;

import raidsunlimited.models.ProfileModel;

public class GetProfileResult {
    private final ProfileModel profileModel;

    private GetProfileResult(ProfileModel profileModel) {
        this.profileModel = profileModel;
    }

    public ProfileModel getProfileModel() {
        return profileModel;
    }

    @Override
    public String toString() {
        return "GetProfileResult{" +
                "profileModel=" + profileModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ProfileModel profileModel;

        public Builder withProfileModel(ProfileModel profileModel) {
            this.profileModel = profileModel;
            return this;
        }

        public GetProfileResult build() {
            return new GetProfileResult(profileModel);
        }
    }
}
