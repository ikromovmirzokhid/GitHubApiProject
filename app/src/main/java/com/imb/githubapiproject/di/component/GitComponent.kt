package com.imb.githubapiproject.di.component

import com.imb.githubapiproject.api.GitApiService
import com.imb.githubapiproject.di.module.GitModule
import com.imb.githubapiproject.viewmodels.GitViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [GitModule::class])
interface GitComponent {

    fun getGitApi(): GitApiService

    fun inject(viewModel: GitViewModel)
}