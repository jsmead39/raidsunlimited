package raidsunlimited.activity.requests;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import raidsunlimited.models.RequiredRoleModel;

import java.util.List;

@JsonDeserialize(builder = CreateRaidRequest.Builder.class)
public class CreateRaidRequest {
    private final String raidName;
    private final String raidDate;
    private final String time;
    private final Integer raidSize;
    private final String raidObjective;
    private final String lootDistribution;
    private final List<RequiredRoleModel> requiredRoles;
    private final String raidOwner;

    private CreateRaidRequest(String raidName, String raidDate, String time, Integer raidSize,
                             String raidObjective, String lootDistribution, List<RequiredRoleModel> requiredRoles,
                             String raidOwner) {
        this.raidName = raidName;
        this.raidDate = raidDate;
        this.time = time;
        this.raidSize = raidSize;
        this.raidObjective = raidObjective;
        this.lootDistribution = lootDistribution;
        this.requiredRoles = requiredRoles;
        this.raidOwner = raidOwner;
    }


    public String getRaidName() {
        return raidName;
    }

    public String getRaidDate() {
        return raidDate;
    }

    public String getTime() {
        return time;
    }

    public Integer getRaidSize() {
        return raidSize;
    }

    public String getRaidObjective() {
        return raidObjective;
    }

    public String getLootDistribution() {
        return lootDistribution;
    }

    public List<RequiredRoleModel> getRequiredRoles() {
        return requiredRoles;
    }

    public String getRaidOwner() {
        return raidOwner;
    }


    @Override
    public String toString() {
        return "CreateRaidRequest{" +
                ", raidName='" + raidName + '\'' +
                ", raidDate=" + raidDate +
                ", time='" + time + '\'' +
                ", raidSize=" + raidSize +
                ", raidObjective='" + raidObjective + '\'' +
                ", lootDistruction='" + lootDistribution + '\'' +
                ", requiredRoles=" + requiredRoles +
                ", raidOwner='" + raidOwner + '\'' +
                '}';
    }

    //CHECKSTYLE:OFF:Builder
    public static Builder builder() {
        return new Builder();

    }

    @JsonPOJOBuilder
    public static class Builder {
        private String raidName;
        private String raidDate;
        private String time;
        private Integer raidSize;
        private String raidObjective;
        private String lootDistribution;
        private List<RequiredRoleModel> requiredRoles;
        private String raidOwner;

        public Builder withRaidName(String raidName) {
            this.raidName = raidName;
            return this;
        }
        public Builder withRaidDate(String raidDate) {
            this.raidDate = raidDate;
            return this;
        }
        public Builder withTime(String time) {
            this.time = time;
            return this;
        }
        public Builder withRaidSize(Integer raidSize) {
            this.raidSize = raidSize;
            return this;
        }
        public Builder withRaidObjective(String raidObjective) {
            this.raidObjective = raidObjective;
            return this;
        }

        public Builder withLootDistribution(String lootDistribution) {
            this.lootDistribution = lootDistribution;
            return this;
        }
        public Builder withRequiredRoles(List<RequiredRoleModel> requiredRoles) {
            this.requiredRoles = requiredRoles;
            return this;
        }
        public Builder withRaidOwner(String raidOwner) {
            this.raidOwner = raidOwner;
            return this;
        }

        public CreateRaidRequest build() {
            return new CreateRaidRequest(raidName, raidDate, time, raidSize, raidObjective, lootDistribution,
                    requiredRoles, raidOwner);
        }
    }
}
