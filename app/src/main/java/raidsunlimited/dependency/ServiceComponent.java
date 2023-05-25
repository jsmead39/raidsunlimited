package raidsunlimited.dependency;

import dagger.Component;
import raidsunlimited.activity.CreateRaidActivity;
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

}
