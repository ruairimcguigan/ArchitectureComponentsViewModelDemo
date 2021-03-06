package demo.mvvm.example.network;

import java.util.List;

import demo.mvvm.example.model.Repo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RepoService {

    @GET("orgs/Google/repos")
    Call<List<Repo>> getRepos();

    @GET("repos/{owner}/{name}")
    Call<Repo> getSingleRepo(@Path("owner") String repoOwner, @Path("name") String repoName);

}
