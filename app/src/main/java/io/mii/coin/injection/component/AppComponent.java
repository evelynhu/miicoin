package io.mii.coin.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import io.mii.coin.data.DataManager;
import io.mii.coin.injection.ApplicationContext;
import io.mii.coin.injection.module.AppModule;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ApplicationContext
    Context context();

    Application application();

    DataManager apiManager();
}
