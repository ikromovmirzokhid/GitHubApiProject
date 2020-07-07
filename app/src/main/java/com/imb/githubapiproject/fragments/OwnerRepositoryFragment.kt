package com.imb.githubapiproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.imb.githubapiproject.R
import com.imb.githubapiproject.adapters.RepositoriesListAdapter
import com.imb.githubapiproject.utils.Settings
import com.imb.githubapiproject.viewmodels.GitViewModel
import kotlinx.android.synthetic.main.owner_repository_fragment.*

class OwnerRepositoryFragment : Fragment(), RepositoriesListAdapter.ReposClickListener {
    private lateinit var navController: NavController
    private lateinit var adapter: RepositoriesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.owner_repository_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        val userName = Settings.userName()

        adapter = RepositoriesListAdapter()
        adapter.setListener(this)
        rView.adapter = adapter
        rView.layoutManager = LinearLayoutManager(activity)

        val viewModel = ViewModelProvider(this).get(GitViewModel::class.java)
        viewModel.getUserRepos(userName!!).observe(viewLifecycleOwner, Observer {
            if (it != null)
                adapter.updateReposList(it)
        })

        backBtn.setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun onReposClickListener(bundle: Bundle) {
        navController.navigate(R.id.action_ownerRepositoryFragment_to_gitReposWebViewFragment, bundle)
    }
}