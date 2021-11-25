package com.andriybobchuk.myroomdemo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andriybobchuk.myroomdemo.HistoryFragment
import com.andriybobchuk.myroomdemo.databinding.ItemsRowBinding
import com.andriybobchuk.myroomdemo.room.TransactionEntity

class TransactionItemAdapter(private val items: ArrayList<TransactionEntity>
                  ) : RecyclerView.Adapter<TransactionItemAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemsRowBinding) : RecyclerView.ViewHolder(binding.root) {
        val llTransactionItemRow = binding.llTransactionItemRow
        val ivTransactionItemRowCategory = binding.ivTransactionItemRowCategory
        val tvTransactionItemRowCategory = binding.tvTransactionItemRowCategory
        val tvTransactionItemRowDate = binding.tvTransactionItemRowDate
        val tvTransactionItemRowDescription = binding.tvTransactionItemRowDescription
        val tvTransactionItemRowAmount = binding.tvTransactionItemRowAmount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemsRowBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        // Let's bind individual items of each view
        //holder.ivTransactionItemRowCategory
        holder.tvTransactionItemRowCategory.text = item.category
        holder.tvTransactionItemRowDate.text = item.date
        holder.tvTransactionItemRowDescription.text = item.description
        holder.tvTransactionItemRowAmount.text = item.amount

    }

    override fun getItemCount(): Int {
        return items.size
    }

}