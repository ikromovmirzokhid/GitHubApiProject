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

class PaginationAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ITEM = 0
    private val LOADING = 1
    private var reposList: ArrayList<Repository> = ArrayList()
    private lateinit var reposClickListener: ReposClickListener

    private var isLoadingAdded = false
    private lateinit var errorMsg: String

    fun getRepositories(): List<Repository> = reposList

    fun setReposList(list: List<Repository>) {
        reposList.clear()
        reposList.addAll(list)
    }

    fun setListener(reposClickListener: ReposClickListener) {
        this.reposClickListener = reposClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder? = null
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            ITEM -> {
                val viewItem: View =
                    inflater.inflate(R.layout.repository_item_view, parent, false)
                viewHolder = ItemViewHolder(viewItem)
            }
            LOADING -> {
                val viewItem: View =
                    inflater.inflate(R.layout.item_progress, parent, false)
                viewHolder = ProgressViewHolder(viewItem)
            }
        }
        return viewHolder!!
    }

    override fun getItemCount(): Int = if (reposList.isNullOrEmpty()) 0 else reposList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val repository = reposList[position]
        when (getItemViewType(position)) {
            ITEM -> {
                holder.itemView.reposName.text = repository.fullName
                holder.itemView.starNum.text = repository.starCounts.toString()
                holder.itemView.forkNum.text = repository.forksCount.toString()
                holder.itemView.reposLang.text = repository.language
                Glide.with(holder.itemView.context).load(repository.user?.avatarUrl)
                    .into(holder.itemView.reposImage)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && repository.createdTime != null) {
                    val instant = Instant.parse(repository.createdTime)
                    val date = Date.from(instant)
                    val formatter =
                        SimpleDateFormat("yyyy-MM-dd")
                    val dateStr = formatter.format(date)

                    holder.itemView.timeNum.text = dateStr
                }

                holder.itemView.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("URL", repository.url)
                    reposClickListener.onReposClickListener(bundle)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (position == reposList.size - 1 && isLoadingAdded) LOADING else ITEM

    private fun getItem(position: Int): Repository {
        return reposList[position]
    }

    fun add(r: Repository) {
        reposList.add(r)
        notifyItemInserted(reposList.size - 1)
    }

    fun addAll(resList: List<Repository>) {
        for (res in resList) {
            add(res)
        }
    }

    fun clear() {
        isLoadingAdded = false
        while (itemCount > 0) {
            remove(getItem(0))
        }
    }

    fun remove(r: Repository?) {
        val position: Int = reposList.indexOf(r)
        if (position > -1) {
            reposList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun isEmpty(): Boolean {
        return itemCount == 0
    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Repository())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false
        val position: Int = reposList.size - 1
        val res: Repository = getItem(position)
        if (res != null) {
            reposList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private class ProgressViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface ReposClickListener {
        fun onReposClickListener(bundle: Bundle)
    }
}