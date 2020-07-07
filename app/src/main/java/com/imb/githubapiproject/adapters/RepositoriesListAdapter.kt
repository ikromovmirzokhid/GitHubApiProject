package com.imb.githubapiproject.adapters

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.imb.githubapiproject.R
import com.imb.githubapiproject.models.Repository
import kotlinx.android.synthetic.main.repository_item_view.view.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*
import kotlin.collections.ArrayList

class RepositoriesListAdapter() :
    RecyclerView.Adapter<RepositoriesListAdapter.ViewHolder>() {

    private var repos = ArrayList<Repository>()
    private lateinit var reposClickListener: ReposClickListener

    fun updateReposList(repos: List<Repository>) {
        this.repos.clear()
        this.repos.addAll(repos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.repository_item_view, parent, false
        )
    )

    fun setListener(reposClickListener: ReposClickListener) {
        this.reposClickListener = reposClickListener
    }

    override fun getItemCount(): Int = repos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = repos[position]

        holder.itemView.reposName.text = repository.name
        holder.itemView.starNum.text = repository.starCounts.toString()
        holder.itemView.forkNum.text = repository.forksCount.toString()
        holder.itemView.reposLang.text = repository.language
        Glide.with(holder.itemView.context).load(repository.user?.avatarUrl)
            .into(holder.itemView.reposImage)

        val instant = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant.parse(repository.createdTime)
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        val date = Date.from(instant)
        val formatter =
            SimpleDateFormat("yyyy-MM-dd")
        val dateStr = formatter.format(date)

        holder.itemView.timeNum.text = dateStr

        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("URL", repository.url)
            reposClickListener.onReposClickListener(bundle)
        }
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface ReposClickListener {
        fun onReposClickListener(bundle: Bundle)
    }
}