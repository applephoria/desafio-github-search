package br.com.igorbag.githubsearch.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.igorbag.githubsearch.R
import br.com.igorbag.githubsearch.domain.Repository

class RepositoryAdapter : ListAdapter<Repository, RepositoryAdapter.ViewHolder>(RepositoryDiffCallback()) {

    var carItemListener: (Repository) -> Unit = {}
    var btnShareListener: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.repository_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repository = getItem(position)
        holder.bind(repository)
        holder.itemView.setOnClickListener { carItemListener(repository) }
        holder.btnShare.setOnClickListener { btnShareListener(repository.htmlUrl) }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val repositoryName: TextView = view.findViewById(R.id.tv_preco)
        val btnShare: View = view.findViewById(R.id.iv_favorite)

        fun bind(repository: Repository) {
            repositoryName.text = repository.name
        }
    }
}

class RepositoryDiffCallback : DiffUtil.ItemCallback<Repository>() {
    override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
        return oldItem == newItem
    }
}
