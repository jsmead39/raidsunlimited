package raidsunlimited.dependency;

import dagger.Component;
import raidsunlimited.activity.CreateProfileActivity;
import raidsunlimited.activity.CreateRaidActivity;
import raidsunlimited.activity.DeleteRaidEventActivity;
import raidsunlimited.activity.GetAllRaidsActivity;
import raidsunlimited.activity.GetProfileActivity;
import raidsunlimited.activity.GetRaidActivity;
import raidsunlimited.activity.GetRaidHistoryActivity;
import raidsunlimited.activity.RaidSignupActivity;
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
}
