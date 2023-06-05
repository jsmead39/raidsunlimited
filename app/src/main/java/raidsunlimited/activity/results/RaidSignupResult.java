package raidsunlimited.activity.results;

import raidsunlimited.models.ParticipantModel;

public class RaidSignupResult {
    private final ParticipantModel participantModel;

    public RaidSignupResult(ParticipantModel participantModel) {
        this.participantModel = participantModel;
    }

    public ParticipantModel getParticipantModel() {
        return participantModel;
    }

    @Override
    public String toString() {
        return "RaidSignupResult{" +
                "participantModel=" + participantModel +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ParticipantModel participantModel;

        public Builder withParticipantModel(ParticipantModel participantModel) {
            this.participantModel = participantModel;
            return this;
        }

        public RaidSignupResult build() {
            return new RaidSignupResult(participantModel);
        }
    }
}
