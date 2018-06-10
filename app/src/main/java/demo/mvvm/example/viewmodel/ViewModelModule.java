package demo.mvvm.example.viewmodel;

import android.arch.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import demo.mvvm.example.home.RepoListViewModel;
import demo.mvvm.example.home.SelectedRepoViewModel;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(RepoListViewModel.class)
    abstract ViewModel bindListViewModel(RepoListViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(SelectedRepoViewModel.class)
    abstract ViewModel bindSelectedViewModel(SelectedRepoViewModel viewModel);
}
