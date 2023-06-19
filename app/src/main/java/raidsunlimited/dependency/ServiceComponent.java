package raidsunlimited.dependency;

import dagger.Component;
import raidsunlimited.activity.CreateFeedbackActivity;
import raidsunlimited.activity.CreateProfileActivity;
import raidsunlimited.activity.CreateRaidActivity;
import raidsunlimited.activity.DeleteRaidEventActivity;
import raidsunlimited.activity.GetAllRaidsActivity;
import raidsunlimited.activity.GetProfileActivity;
import raidsunlimited.activity.GetRaidActivity;
import raidsunlimited.activity.GetRaidHistoryActivity;
import raidsunlimited.activity.RaidEventUpdateActivity;
import raidsunlimited.activity.RaidSignupActivity;
import raidsunlimited.activity.RaidWithdrawActivity;
import raidsunlimited.activity.RoleAssignmentActivity;
import raidsunlimited.activity.RoleRemovalActivity;
import raidsunlimited.activity.UpdateProfileActivity;

import javax.inject.Singleton;
@Singleton
@Component(modules = {DaoModule.class, MetricsModule.class})
public interface ServiceComponent {

    /**
     * Provides the relevant activity.
     * @return CreateRaidActivity
     */
    CreateRaidActivity provideCreateRaidActivity();

    /**
     * Provides the relevant activity.
     * @return GetRaidActivity
     */
    GetRaidActivity provideGetRaidActivity();

    /**
     * Provides the relevant activity.
     * @return GetRaidActivity
     */
    CreateProfileActivity provideCreateProfileActivity();

    /**
     * Provides the relevant activity.
     * @return GetProfileActivity
     */
    GetProfileActivity provideGetProfileActivity();

    /**
     * Provides the relevant activity.
     * @return UpdateProfileActivity
     */
    UpdateProfileActivity provideUpdateProfileActivity();

    /**
     * Provides the relevant activity.
     * @return RaidSignupActivity
     */
    RaidSignupActivity provideRaidSignupActivity();

    /**
     * Provides the relevant activity.
     * @return GetAllRaidsActivity
     */
    GetAllRaidsActivity provideGetAllRaidsActivity();

    /**
     * Provides the relevant activity.
     * @return DeleteRaidEventActivity
     */
    DeleteRaidEventActivity provideDeleteRaidEventActivity();

    /**
     * Provides the relevant activity.
     * @return GetRaidHistoryActivity
     */
    GetRaidHistoryActivity provideGetRaidHistoryActivity();

    /**
     * Provides the relevant activity.
     * @return RoleAssignmentActivity
     */
    RoleAssignmentActivity provideRoleAssignmentActivity();

    /**
     * Provides the relevant activity.
     * @return RoleRemovalActivity
     */
    RoleRemovalActivity provideRoleRemovalActivity();

    /**
     * Provides the relevant activity.
     * @return RaidEventUpdateActivity
     */
    RaidEventUpdateActivity provideRaidEventUpdateActivity();

    /**
     * Provides the relevant activity.
     * @return RaidWithdrawActivity
     */
    RaidWithdrawActivity provideRaidWithdrawActivity();

    /**
     * Provides the relevant activity.
     * @return CreateFeedbackActivity
     */
    CreateFeedbackActivity provideCreateFeedbackActivity();
}
