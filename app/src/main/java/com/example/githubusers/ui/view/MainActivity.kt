package com.example.githubusers.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusers.R
import com.example.githubusers.data.model.User
import com.example.githubusers.ui.adapter.SearchAdapter
import com.example.githubusers.ui.adapter.UserItemAdapter
import com.example.githubusers.utils.Network
import com.example.githubusers.utils.NetworkErrorHandler
import com.example.githubusers.viewmodel.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userViewModel: UserViewModel
    private lateinit var userItemAdapter: UserItemAdapter
    private lateinit var searchAdapter: SearchAdapter
    private val networkHandler = NetworkErrorHandler()
    private var firstBoot: Boolean = true
    private var searchText: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userViewModel = UserViewModel(this)

        setupNetworkStatusListener()
        setUpAdapter()
        setUpObserver()
        setSearchListener()
        networkCheck()
    }

    override fun onResume() {
        super.onResume()

        if (!firstBoot){
            notifyAdapter()
        }
        else {
            //start effect
            startShimmer()
        }
        firstBoot = false

    }

    private fun startShimmer() {
        shimmerFrameLayout.startShimmerAnimation()
    }

    private fun removeShimmer() {
        shimmerFrameLayout.stopShimmerAnimation()
        shimmerFrameLayout.visibility = View.GONE
        usersRecyclerView.visibility = View.VISIBLE
    }

    private fun setUpAdapter() {

        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        userItemAdapter = UserItemAdapter()
        usersRecyclerView.layoutManager = linearLayoutManager
        usersRecyclerView.adapter = userItemAdapter
    }

    private fun setUpObserver() {

        userViewModel.userList.observe(this, Observer<PagedList<User>> {
            val initialPageSize = userViewModel.userList.value?.size
            if (initialPageSize != null) {
                userViewModel.initializePageBuilder( initialPageSize )
            }
            userItemAdapter.submitList(it)
            userItemAdapter.notifyDataSetChanged()
        })
    }

    private fun notifyAdapter() {
        userViewModel.refresh()
    }

    private fun setSearchListener() {

        userSearch.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchText = newText!!
                if ( newText.isNullOrEmpty() ){
                    usersRecyclerView.adapter = userItemAdapter
                }
                else {
                    searchMode(newText!!)
                }
                return true
            }
        })
    }

    /**
     *  Changes the recyclerview adapter for search mode
     */
    private fun searchMode(data:String){

        userViewModel.searchUser(data).observe(this, Observer {
            if ( searchText == data ) {
                searchAdapter = SearchAdapter(ArrayList(it))
                usersRecyclerView.adapter = searchAdapter
            }
        })
    }

    private fun setupNetworkStatusListener() {
        userViewModel.getNetworkStatus().observe( this, Observer{
            if ( it == Network.LOADED ){
                removeShimmer()
            }
            else {
                networkCheck()
            }
        })
    }

    /**
     *  Internet availability Checker
     *  Calls during initial boot and paging
     */
    private fun networkCheck() {
        val network = networkHandler.checkInternetConnectivity(this)
        if ( network ) {
            networkBarIndicator.visibility = View.GONE
        }
        else{
            networkBarIndicator.visibility = View.VISIBLE
        }
    }
}