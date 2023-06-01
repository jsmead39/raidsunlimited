package raidsunlimited.activity.results;

import raidsunlimited.models.ProfileModel;

public class UpdateProfileResult {
    private final ProfileModel profileModel;

    private UpdateProfileResult(ProfileModel profileModel) {
        this.profileModel = profileModel;
    }

    public ProfileModel getProfileModel() {
        return profileModel;
    }

    @Override
    public String toString() {
        return "UpdateProfileResult{" +
                "profileModel=" + profileModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ProfileModel profileModel;

        public Builder withProfile(ProfileModel profileModel) {
            this.profileModel = profileModel;
            return this;
        }

        public UpdateProfileResult build() {
            return new UpdateProfileResult(profileModel);
        }
    }
}
