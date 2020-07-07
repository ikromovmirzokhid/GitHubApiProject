package com.imb.githubapiproject.fragments

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.imb.githubapiproject.MainActivity
import com.imb.githubapiproject.R
import com.imb.githubapiproject.adapters.PaginationAdapter
import com.imb.githubapiproject.utils.PaginationScrollListener
import com.imb.githubapiproject.utils.Settings
import com.imb.githubapiproject.viewmodels.GitViewModel
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.navigation_drawer_header.view.*

class MainFragment : Fragment(), PaginationAdapter.ReposClickListener {
    private lateinit var navController: NavController
    private lateinit var mToggle: ActionBarDrawerToggle
    private lateinit var viewModel: GitViewModel

    lateinit var adapter: PaginationAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    private val PAGE_START = 1
    private var isLoading = false
    private var isLastPage = false

    private var TOTAL_PAGES = 1
    private var currentPage: Int = PAGE_START

    private lateinit var searchQuery: String

    private lateinit var imm: InputMethodManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        navController = Navigation.findNavController(view)

        (activity as MainActivity).setToolbar(toolbar)

        mToggle = ActionBarDrawerToggle(
            activity,
            dLayout,
            R.string.open,
            R.string.close
        )
        dLayout.addDrawerListener(mToggle)
        mToggle.syncState()
        (activity as MainActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val navHeaderView = nView.getHeaderView(0)

        val userName = Settings.userName()
        val avatarUrl = Settings.userAvatarURL()

        navHeaderView.fullName.text = userName
        Glide.with(this).load(avatarUrl).placeholder(R.drawable.github_icon)
            .into(navHeaderView.profileImage)

        nView.setNavigationItemSelectedListener {
            if (it.itemId == R.id.logOut) {
                Snackbar.make(view, "Goodbye, Comeback Soon!", Snackbar.LENGTH_SHORT).show()
                navController.navigate(R.id.action_mainFragment_to_authorizationFragment)
                Settings.setUserIn(false)
                dLayout.close()

            } else if (it.itemId == R.id.repository) {
                navController.navigate(R.id.action_mainFragment_to_ownerRepositoryFragment)
                dLayout.close()
            }
            return@setNavigationItemSelectedListener true
        }

        viewModel = ViewModelProvider(this).get(GitViewModel::class.java)

        adapter = PaginationAdapter()
        adapter.setListener(this)
        linearLayoutManager = LinearLayoutManager(activity)

        rVIew.layoutManager = linearLayoutManager
        rVIew.itemAnimator = DefaultItemAnimator()
        rVIew.adapter = adapter

        rVIew.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1

                loadNextPage()
            }

            override fun getTotalPageCount(): Int = TOTAL_PAGES
            override fun isLastPage(): Boolean = isLastPage
            override fun isLoading(): Boolean = isLoading

        })

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.toolbar_menu, menu)
        val manager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        searchView.setSearchableInfo(manager.getSearchableInfo(activity?.componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hideNotFoundText()
                searchQuery = query
                loadFirstPage()
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
    }

    private fun loadFirstPage() {
        currentPage = PAGE_START
        adapter.clear()

        viewModel.searchRepositoryByName(searchQuery, currentPage)
            .observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    adapter.addAll(it.repositories)
                    hideAll()

                    TOTAL_PAGES = (it.totalCount / 30).toInt() + 1
                    if (TOTAL_PAGES >= 34)
                        TOTAL_PAGES = 34

                    if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter()
                    else isLastPage = true

                }
            })
    }

    private fun loadNextPage() {

        viewModel.searchRepositoryByName(searchQuery, currentPage)
            .observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    adapter.removeLoadingFooter()
                    isLoading = false

                    if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter()
                    else isLastPage = true
                }
            })
    }

    private fun hideNotFoundText() {
        mainProgressBar.visibility = View.VISIBLE
        notFoundPic.visibility = View.GONE
        notFoundText.visibility = View.GONE
    }

    private fun showNotFoundText() {
        mainProgressBar.visibility = View.GONE
        notFoundPic.visibility = View.VISIBLE
        notFoundText.visibility = View.VISIBLE
    }

    private fun hideAll() {
        mainProgressBar.visibility = View.GONE
        notFoundPic.visibility = View.GONE
        notFoundText.visibility = View.GONE
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (mToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun hideKeyboard() {
        imm =
            context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onReposClickListener(bundle: Bundle) {
        navController.navigate(R.id.action_mainFragment_to_gitReposWebViewFragment, bundle)
    }
}