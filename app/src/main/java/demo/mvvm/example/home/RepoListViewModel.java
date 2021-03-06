package demo.mvvm.example.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;

import demo.mvvm.example.model.Repo;
import demo.mvvm.example.network.RepoApi;
import demo.mvvm.example.network.RepoService;
import demo.mvvm.example.viewmodel.ViewModelFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoListViewModel extends ViewModel {

    private final MutableLiveData<List<Repo>> repos = new MutableLiveData<>();
    private final MutableLiveData<Boolean> hasRepoLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private Call<List<Repo>> repoCall;
    private RepoService repoService;

    LiveData<List<Repo>> getRepos(){
        return repos;
    }

    LiveData<Boolean> getError(){
        return hasRepoLoadError;
    }

    LiveData<Boolean> getLoading(){
        return isLoading;
    }

    @Inject
    RepoListViewModel(RepoService repoService){
        this.repoService = repoService;
        fetchRepos();
    }

    private void fetchRepos() {
        isLoading.setValue(true);
        repoCall = repoService.getRepos();
        repoCall.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(@NonNull Call<List<Repo>> call, @NonNull Response<List<Repo>> response) {
                hasRepoLoadError.setValue(false);
                repos.setValue(response.body());
                isLoading.setValue(false);
                repoCall = null;
            }

            @Override
            public void onFailure(@NonNull Call<List<Repo>> call, @NonNull Throwable t) {
                Log.e(getClass().getSimpleName(), "Error loading repos");
                hasRepoLoadError.setValue(true);
                isLoading.setValue(false);
                repoCall = null;
            }
        });
    }

    @Override
    protected void onCleared() {
        if (repoCall != null){
            repoCall.cancel();
            repoCall = null;
        }

    }
}
