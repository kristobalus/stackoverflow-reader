package com.voxtantum.soreader.ui.questions;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

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
import com.voxtantum.soreader.api.entities.Question;
import com.voxtantum.soreader.ui.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;

public class QuestionsFragment extends BaseFragment {

    public static QuestionsFragment newInstance() {

        Bundle args = new Bundle();

        QuestionsFragment fragment = new QuestionsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ContentLoadingProgressBar progressBarView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    QuestionAdapter adapter;
    QuestionsViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new QuestionAdapter(bindingController, diffUtilCallback);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setEnabled(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(QuestionsViewModel.class);
        viewModel.getIsSourceLoading().observe(getViewLifecycleOwner(), this::onSourceLoading);
        viewModel.getQuestionList().observe(getViewLifecycleOwner(), new Observer<PagedList<Question>>() {
            @Override
            public void onChanged(PagedList<Question> questions) {
                adapter.submitList(questions);
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
        if (isLoading != null) {
            progressBarView.setVisibility(isLoading ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private DiffUtil.ItemCallback<Question> diffUtilCallback = new DiffUtil.ItemCallback<Question>() {
        @Override
        public boolean areItemsTheSame(@NonNull Question oldItem, @NonNull Question newItem) {
            return oldItem.questionId != null && newItem.questionId != null && oldItem.questionId.equals(newItem.questionId);
        }

        @Override
        public boolean areContentsTheSame(@NonNull Question oldItem, @NonNull Question newItem) {
            return oldItem.body != null && newItem.body != null
                    && oldItem.body.equals(newItem.body)
                    && oldItem.title != null && newItem.title != null
                    && oldItem.title.equals(newItem.title);
        }
    };

    private BindingController bindingController = new BindingController() {
        @Override
        public void bind(QuestionViewHolder holder, Question model) {
            holder.bind(model);
        }
    };

    static class QuestionAdapter extends PagedListAdapter<Question, QuestionViewHolder> {

        private BindingController bindingController;

        QuestionAdapter(BindingController bindingController, DiffUtil.ItemCallback<Question> diffUtilCallback) {
            super(diffUtilCallback);
            this.bindingController = bindingController;
        }

        @NonNull
        @Override
        public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
            QuestionViewHolder holder = new QuestionViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
            bindingController.bind(holder, getItem(position));
        }

    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {

        static SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm", Locale.getDefault());

        AppCompatTextView titleView;
        AppCompatTextView descriptionView;
        AppCompatTextView dateView;
        AppCompatTextView authorView;
        WebView webView;

        QuestionViewHolder(@NonNull View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.title);
            descriptionView = itemView.findViewById(R.id.description);
            dateView = itemView.findViewById(R.id.date);
            authorView = itemView.findViewById(R.id.author);

            webView = itemView.findViewById(R.id.webview);
            webView.getSettings().setUseWideViewPort(false);
            webView.getSettings().setSupportZoom(false);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                webView.getSettings().setOffscreenPreRaster(true);
            }
            webView.setVerticalScrollBarEnabled(false);
            webView.setScrollbarFadingEnabled(false);
        }

        void clean() {
            titleView.setText(null);
            descriptionView.setText(null);
            dateView.setText(null);
            authorView.setText(null);
        }

        void bind(Question model) {
            if (model != null) {

                if (model.creationDate != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeZone(TimeZone.getDefault());
                    calendar.setTimeInMillis(model.creationDate * 1000L);
                    dateView.setText(dateFormat.format(calendar.getTime()));
                } else {
                    dateView.setText(null);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    titleView.setText(!TextUtils.isEmpty(model.title) ? Html.fromHtml(model.title, Html.FROM_HTML_MODE_COMPACT) : null);
                } else {
                    titleView.setText(!TextUtils.isEmpty(model.title) ? Html.fromHtml(model.title) : null);
                }

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                    descriptionView.setText(!TextUtils.isEmpty(model.body) ? Html.fromHtml(model.body, Html.FROM_HTML_MODE_COMPACT) : null);
//                } else {
//                    descriptionView.setText(model.body != null ? Html.fromHtml(model.body) : null);
//                }

                webView.loadDataWithBaseURL("https://www.stackoverflow.com", getHtmlData(model.body), "text/html", "UTF-8", null);

                authorView.setText(model.owner != null ?
                        (!TextUtils.isEmpty(model.owner.displayName) ? Html.fromHtml(model.owner.displayName) : null) : null);

            } else {
                clean();
            }
        }

        private String getHtmlData(String bodyHTML) {
            String head = "<head><style>img{max-width: 100%; width:auto; height: auto;}</style></head>";
            return "<html>" + head + "<body>" + bodyHTML + "</body></html>";
        }

    }

    interface BindingController {
        void bind(QuestionViewHolder viewHolder, Question model);
    }

}


