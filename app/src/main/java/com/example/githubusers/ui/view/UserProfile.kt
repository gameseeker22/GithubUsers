package com.example.githubusers.ui.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.githubusers.R
import com.example.githubusers.data.model.User
import com.example.githubusers.viewmodel.ProfileViewModel
import kotlinx.android.synthetic.main.activity_user_profile.*



class UserProfile : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    lateinit var user : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        // get user object
        user = (intent.getSerializableExtra("USER_OBJECT") as User?)!!

        // check & initialize
        if (user != null) {
            // setup toolbar
            val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
            setActionBar( toolbar)
            profile_title.text = user?.login
            initViewModel()
            setUpClickListener()
        }

    }

    override fun onResume() {
        super.onResume()
        shimmerFrameLayout2.startShimmerAnimation()
    }

    private fun stopShimmer() {
        shimmerFrameLayout2.stopShimmerAnimation()
        shimmerFrameLayout2.visibility = View.GONE
        profileScroll.visibility = View.VISIBLE
    }

    private fun setUpClickListener() {

        btn_back.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            if ( textNotes.text.isNotEmpty() ) {
                val note = textNotes.text.toString()
                profileViewModel.updateNote(note)
                Toast.makeText(this, "Save Successfully", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun initViewModel() {
        profileViewModel = ProfileViewModel( this)
        profileViewModel.fetchProfile(user)
        setProfileObserver()
    }

    private fun setProfileObserver() {
        profileViewModel.profile.observe( this, Observer {
            displayProfileInfo( it )
        })
    }

    private fun displayProfileInfo(info : User) {

        // remove shimmer loading
        stopShimmer()

        lblFollowers.text = "Followers : " + info.followers.toString()
        lblFollowing.text = "Following : " + info.following.toString()
        lblName.text = "Name : " + (info.name ?: "")
        lblCompany.text = "Company : " + (info.company ?: "")
        lblBlog.text = "Blog : " + (info.blog ?: "")
        textNotes.setText(info.note)

        // Set header photo
        Glide.with( profileAvatar ).load( info!!.avatar_url )
            .into( profileAvatar )

    }
}