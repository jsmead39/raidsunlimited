package raidsunlimited.dependency;

import dagger.Component;
import raidsunlimited.activity.*;

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
     * Provides the relevant activity
     * @return GetRaidActivity
     */
    GetRaidActivity provideGetRaidActivity();

    /**
     * Provides the relevant activity
     * @return GetRaidActivity
     */
    CreateProfileActivity provideCreateProfileActivity();

    /**
     * Provides the relevant activity
     * @return GetProfileActivity
     */
    GetProfileActivity provideGetProfileActivity();

    /**
     * Provides the relevant activity
     * @return GetProfileActivity
     */
    UpdateProfileActivity provideUpdateProfileActivity();

    /**
     * Provides the relevant activity
     * @return RaidSignupActivity
     */
    RaidSignupActivity provideRaidSignupActivity();

    /**
     * Provides the relevant activity
     * @return RaidSignupActivity
     */
    GetAllRaidsActivity provideGetAllRaidsActivity();
}
