package raidsunlimited.dependency;

import dagger.Component;
import raidsunlimited.activity.CreateRaidActivity;

import javax.inject.Singleton;
@Singleton
@Component(modules = {DaoModule.class, MetricsModule.class})
public interface ServiceComponent {

    /**
     * Provides the relevant activity
     * @return CreateRaidActivity
     */
    CreateRaidActivity provideCreateRaidActivity();

}
