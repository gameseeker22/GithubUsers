package com.example.githubusers.ui.adapter

import android.content.Intent
import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.githubusers.R
import com.example.githubusers.data.model.User
import com.example.githubusers.ui.view.UserProfile
import com.example.githubusers.utils.invertColors
import kotlinx.android.synthetic.main.item_user.view.*


class DataViewHolder( itemView: View) : RecyclerView.ViewHolder( itemView ) {

    fun bind(position : Int, user : User?) {

        itemView.userName?.text = user?.login.toString()
        itemView.userHtmlUrl?.text = user?.html_url.toString()

        // Note Icon checker
        itemView.userNoteIcon.visibility = View.GONE
        if ( user!!.note.isNotEmpty() ) {
            itemView.userNoteIcon.visibility = View.VISIBLE
        }

        itemView.setOnClickListener {
            val intent = Intent(itemView.context, UserProfile::class.java)
            intent.putExtra("USER_OBJECT", user)
            itemView.context.startActivity(intent)
        }

        itemView.userAvatar.setImageDrawable(null)

        /**
         * Check every fourth data.
         * Image is converted to bitmap before inverted.
         */
        val invertImage = position % 3
        if (invertImage != 0 || position == 0) {
            Glide.with( itemView.userAvatar.context ).load( user!!.avatar_url )
                .into( itemView.userAvatar )
        }
        else {
            Glide.with( itemView.userAvatar.context ).asBitmap().load( user!!.avatar_url )
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        resource.apply {
                            // set inverted colors bitmap to second image view
                            invertColors()?.apply {
                                itemView.userAvatar.setImageBitmap(this)
                            }
                        }
                    }
                })
        }
        itemView.userAvatar.invalidate()
    }

    companion object {
        fun create(parent: ViewGroup): DataViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.item_user, parent, false)
            return DataViewHolder(view)
        }
    }
}