package demo.mvvm.example.home;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import demo.mvvm.example.model.Repo;
import demo.mvvm.example.network.RepoApi;
import demo.mvvm.example.network.RepoService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedRepoViewModel extends ViewModel {

    private final MutableLiveData<Repo> selectedRepo = new MutableLiveData<>();
    private Call<Repo> repoCall;
    private RepoService repoService;

    public LiveData<Repo> getSelectedRepo(){
        return selectedRepo;
    }

    void setSelectedRepo(Repo repo) {
        selectedRepo.setValue(repo);
    }

    @Inject
    SelectedRepoViewModel(RepoService repoService) {
        this.repoService = repoService;
    }

    public void saveToBundle(Bundle outState) {
        if (selectedRepo.getValue() != null){
            outState.putStringArray("repo_details",
                    new String[]{
                            selectedRepo.getValue().owner.login,
                            selectedRepo.getValue().name});
        }
    }

    public void restoreFromBundle(Bundle savedInstanceState) {
        if (selectedRepo.getValue() == null){
            // We are only restoring here if we dont have a selected repo set already

            if ((savedInstanceState != null) && (savedInstanceState.containsKey("repo_details"))){
                loadRepo(savedInstanceState.getStringArray("repo_details"));
            }
        }
    }

    private void loadRepo(String[] repoDetails) {
        repoCall = repoService.getSingleRepo(repoDetails[0], repoDetails[1]);
        repoCall.enqueue(new Callback<Repo>() {
            @Override
            public void onResponse(@NonNull Call<Repo> call, @NonNull Response<Repo> response) {
                selectedRepo.setValue(response.body());
                repoCall = null;
            }

            @Override
            public void onFailure(Call<Repo> call, Throwable t) {
                Log.e(getClass().getSimpleName(), "Error loading repo ", t);
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
