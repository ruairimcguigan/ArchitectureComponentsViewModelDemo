package demo.mvvm.example.common;

import javax.inject.Singleton;

import dagger.Component;
import demo.mvvm.example.detail.RepoDetailFragment;
import demo.mvvm.example.home.RepoListFragment;
import demo.mvvm.example.network.NetworkModule;
import demo.mvvm.example.viewmodel.ViewModelModule;

@Singleton
@Component(modules = {
        NetworkModule.class,
        ViewModelModule.class
})
public interface AppComponent {

    void inject(RepoListFragment fragment);

    void inject(RepoDetailFragment fragment);

}
