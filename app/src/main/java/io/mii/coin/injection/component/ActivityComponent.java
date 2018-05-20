package io.mii.coin.injection.component;

import dagger.Subcomponent;
import io.mii.coin.features.MiicoinActivity;
import io.mii.coin.features.detail.DetailActivity;
import io.mii.coin.injection.PerActivity;
import io.mii.coin.injection.module.ActivityModule;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MiicoinActivity miicoinActivity);

    void inject(DetailActivity detailActivity);
}
