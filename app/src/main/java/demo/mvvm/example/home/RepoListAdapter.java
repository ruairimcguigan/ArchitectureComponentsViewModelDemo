package demo.mvvm.example.home;

import android.arch.lifecycle.LifecycleOwner;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import demo.mvvm.example.R;
import demo.mvvm.example.common.RepoSelectedListener;
import demo.mvvm.example.model.Repo;

import static java.lang.String.valueOf;

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.RepoViewHolder> {

    private RepoSelectedListener repoSelectedListener;

    private final List<Repo> data = new ArrayList<>();

    public RepoListAdapter(ListViewModel viewModel,
                           LifecycleOwner owner,
                           RepoSelectedListener repoSelectedListener) {
        this.repoSelectedListener = repoSelectedListener;

        viewModel.getRepos().observe(owner, repos -> {
            data.clear();
            if (repos != null){
                data.addAll(repos);
                notifyDataSetChanged();
            }
        });
        setHasStableIds(true);
    }

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item, parent, false);
        return new RepoViewHolder(view, repoSelectedListener);
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder, int position) {
        holder.populate(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).id;
    }


    static final class RepoViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.repo_name) TextView repoName;
        @BindView(R.id.repo_description) TextView repoDescription;
        @BindView(R.id.repo_fork) TextView repoForkCount;
        @BindView(R.id.repo_starred) TextView repoStarCount;

        private Repo repo;

        RepoViewHolder(View itemView, RepoSelectedListener repoSelectedListener) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(v -> {
                if (repo != null){
                    repoSelectedListener.onRepoSelected(repo);
                }
            });
        }

        void populate(Repo repo){
            this.repo = repo;
            repoName.setText(repo.name);
            repoDescription.setText(repo.description);
            repoForkCount.setText(valueOf(repo.forks));
            repoStarCount.setText(valueOf(repo.stars));
        }
    }
}
