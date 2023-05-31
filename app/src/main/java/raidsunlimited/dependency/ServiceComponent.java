package raidsunlimited.dependency;

import dagger.Component;
import raidsunlimited.activity.CreateProfileActivity;
import raidsunlimited.activity.CreateRaidActivity;
import raidsunlimited.activity.GetProfileActivity;
import raidsunlimited.activity.GetRaidActivity;

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

}
