package demo.mvvm.example.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.Unbinder;
import demo.mvvm.example.R;
import demo.mvvm.example.common.RepoSelectedListener;
import demo.mvvm.example.detail.DetailFragment;
import demo.mvvm.example.model.Repo;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static butterknife.ButterKnife.bind;

public class ListFragment extends Fragment implements RepoSelectedListener {

    @BindView(R.id.recycler_view) RecyclerView listView;
    @BindView(R.id.error_view) TextView errorView;
    @BindView(R.id.progress_view) ProgressBar loadingView;

    private Unbinder unbinder;
    private ListViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container, false);
        unbinder = bind(this, v);
        setTitle();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this).get(ListViewModel.class);

        listView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(new RepoListAdapter(viewModel, this, this));

        observeViewModel();
    }

    @Override
    public void onRepoSelected(Repo repo) {
        /**
         * Passing getActivity - as this mak this viewmodel scoped to the host activity,
         * rather than the fragment - useful for accessing one view model from within multiple fragments
         */

        SelectedRepoViewModel selectedRepoViewModel = ViewModelProviders.of(getActivity()).get(SelectedRepoViewModel.class);
        selectedRepoViewModel.setSelectedRepo(repo);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.screen_container, new DetailFragment())
                .addToBackStack(null)
                .commit();

    }

    private void observeViewModel() {
        viewModel.getRepos().observe(this, repos -> {
            if (repos != null) {
                listView.setVisibility(VISIBLE);

            }
        });
        viewModel.getError().observe(this, isError -> {
            if (isError) {
                errorView.setVisibility(VISIBLE);
                listView.setVisibility(GONE);
                errorView.setText(R.string.api_error_repo);
            }else {
                errorView.setVisibility(GONE);
                errorView.setText(null);
            }
        });
        viewModel.getLoading().observe(this, isLoading -> {
            loadingView.setVisibility(isLoading ? VISIBLE : GONE);
            if (isLoading){
                errorView.setVisibility(GONE);
                listView.setVisibility(GONE);
            }
            }
        );
    }

    private void setTitle() {
        getActivity().setTitle(getString(R.string.repo_list_title));
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
