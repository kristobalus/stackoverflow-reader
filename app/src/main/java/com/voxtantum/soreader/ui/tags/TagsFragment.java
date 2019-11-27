package com.voxtantum.soreader.ui.tags;


import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.voxtantum.soreader.R;
import com.voxtantum.soreader.api.entities.Tag;
import com.voxtantum.soreader.ui.base.BaseFragment;
import com.voxtantum.soreader.ui.faq.FaqActivity;

import butterknife.BindView;

public class TagsFragment extends BaseFragment {

    public static TagsFragment newInstance() {

        Bundle args = new Bundle();

        TagsFragment fragment = new TagsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBarView;

    TagAdapter adapter;
    TagsViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tag_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new TagAdapter(bindingController, diffUtilCallback);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(TagsViewModel.class);

        viewModel.getSourceError().observe(getViewLifecycleOwner(), new Observer<Throwable>() {
            @Override
            public void onChanged(Throwable throwable) {

            }
        });

        viewModel.getIsSourceLoading().observe(getViewLifecycleOwner(), this::onSourceLoading);

        viewModel.getPagedListTags().observe(getViewLifecycleOwner(), new Observer<PagedList<Tag>>() {
            @Override
            public void onChanged(PagedList<Tag> tags) {
                adapter.submitList(tags);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                viewModel.invalidate();
            }
        });
    }

    private void onSourceLoading(Boolean isLoading) {
        if ( isLoading != null ){
            progressBarView.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        }
    }


    private DiffUtil.ItemCallback<Tag> diffUtilCallback = new DiffUtil.ItemCallback<Tag>() {
        @Override
        public boolean areItemsTheSame(@NonNull Tag oldItem, @NonNull Tag newItem) {
            return oldItem.name != null && newItem.name != null && oldItem.name.equals(newItem.name);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Tag oldItem, @NonNull Tag newItem) {
            return oldItem.count != null && newItem.count != null && oldItem.count.equals(newItem.count);
        }
    };

    private BindingController bindingController = new BindingController() {
        @Override
        public void bind(TagViewHolder holder, Tag model) {
            holder.bind(model);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(requireActivity(), FaqActivity.class);
                    intent.putExtra(FaqActivity.ARG_TAG, model.name);
                    startActivity(intent);
                }
            });
        }
    };

    static class TagAdapter extends PagedListAdapter<Tag, TagViewHolder> {

        private BindingController bindingController;

        TagAdapter(BindingController bindingController, DiffUtil.ItemCallback<Tag> diffUtilCallback) {
            super(diffUtilCallback);
            this.bindingController = bindingController;
        }

        @NonNull
        @Override
        public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
            TagViewHolder holder = new TagViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
            bindingController.bind(holder, getItem(position));
        }

    }

    static class TagViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView nameView;
        AppCompatTextView countView;

        TagViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.name);
            countView = itemView.findViewById(R.id.count);
        }

        void clean(){
            nameView.setText(null);
            countView.setText(null);
        }

        void bind(Tag model){
            if ( model != null ){
                nameView.setText(model.name != null ? Html.fromHtml(model.name) : null);
                countView.setText(itemView.getResources().getString(R.string.format_tag_count, model.count));
            } else {
                clean();
            }
        }

    }

    interface BindingController {
        void bind(TagViewHolder viewHolder, Tag model);
    }


}
