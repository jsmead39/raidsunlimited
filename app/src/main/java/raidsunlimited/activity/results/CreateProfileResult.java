package raidsunlimited.activity.results;

import raidsunlimited.models.ProfileModel;

public class CreateProfileResult {
    private final ProfileModel profileModel;

    private CreateProfileResult(ProfileModel profileModel) {
        this.profileModel = profileModel;
    }

    public ProfileModel getProfileModel() {
        return profileModel;
    }

    @Override
    public String toString() {
        return "CreateProfileResult{" +
                "profileModel=" + profileModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ProfileModel profileModel;

        public Builder withProfile(ProfileModel profile) {
            this.profileModel = profile;
            return this;
        }

        public CreateProfileResult build() {
            return new CreateProfileResult(profileModel);
        }
    }
}
