package demo.mvvm.example.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import demo.mvvm.example.R;
import demo.mvvm.example.home.SelectedRepoViewModel;

import static android.arch.lifecycle.ViewModelProviders.of;
import static java.lang.String.valueOf;

public class DetailFragment extends Fragment{

    @BindView(R.id.repo_name) TextView repoName;
    @BindView(R.id.repo_description) TextView repoDescription;
    @BindView(R.id.repo_forks) TextView repoForks;
    @BindView(R.id.repo_stars) TextView repoStars;

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.screen_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        setTitle();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayRepo();
    }

    private void displayRepo() {
        SelectedRepoViewModel selectedRepoViewModel = of(getActivity()).get(SelectedRepoViewModel.class);
        selectedRepoViewModel.getSelectedRepo().observe(this, repo -> {
                    repoName.setText(repo.name);
                    repoDescription.setText(repo.description);
                    repoForks.setText(valueOf(repo.forks));
                    repoStars.setText(valueOf(repo.stars));
                });
    }

    private void setTitle() {
        getActivity().setTitle(getString(R.string.repo_detail_title));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null){
            unbinder.unbind();
            unbinder = null;
        }
    }
}
