package io.mii.coin.injection.component;

import dagger.Subcomponent;
import io.mii.coin.features.favorite.FavoriteFragment;
import io.mii.coin.features.main.MainFragment;
import io.mii.coin.injection.PerFragment;
import io.mii.coin.injection.module.FragmentModule;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(MainFragment mainFragment);

    void inject(FavoriteFragment favoriteFragment);

}
