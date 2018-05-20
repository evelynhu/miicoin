package io.mii.coin.injection.module;

import android.app.Activity;
import android.content.Context;
import android.app.Fragment;

import dagger.Module;
import dagger.Provides;
import io.mii.coin.injection.ActivityContext;

@Module
public class FragmentModule {
    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    Fragment providesFragment() {
        return fragment;
    }

    @Provides
    Activity provideActivity() {
        return fragment.getActivity();
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return fragment.getActivity();
    }
}
