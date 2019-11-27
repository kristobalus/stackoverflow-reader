package com.voxtantum.soreader.injection;


import com.voxtantum.soreader.api.ApiModule;
import com.voxtantum.soreader.ui.faq.FaqViewModel;
import com.voxtantum.soreader.ui.tags.TagsViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {ApiModule.class})
@Singleton
public interface AppComponent {
    void inject(TagsViewModel viewModel);
    void inject(FaqViewModel viewModel);
}

