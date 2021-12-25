package com.andriybobchuk.myroomdemo.adapters

import android.graphics.Color
import android.provider.SyncStateContract
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.andriybobchuk.myroomdemo.R
import com.andriybobchuk.myroomdemo.databinding.ItemsRowBinding
import com.andriybobchuk.myroomdemo.fragments.MainFragment
import com.andriybobchuk.myroomdemo.room.CategoryEntity
import com.andriybobchuk.myroomdemo.room.TransactionEntity
import com.andriybobchuk.myroomdemo.util.Constants
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

class TransactionItemAdapter(
    private val items: ArrayList<TransactionEntity>,
    private val categories: ArrayList<CategoryEntity>
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
        holder.tvTransactionItemRowCategory.text = item.category
        holder.tvTransactionItemRowDate.text = item.date
        holder.tvTransactionItemRowDescription.text = item.description

        for (category in categories) {

            if(category.name == item.category) {

                if(category.type == Constants.EXPENSE) {
                    holder.tvTransactionItemRowAmount.text = "-${item.amount} ${item.currency}"
                    holder.tvTransactionItemRowAmount.setTextColor(getColor(context, R.color.primary_text_color))
                } else {
                    holder.tvTransactionItemRowAmount.text = "+${item.amount} ${item.currency}"
                    holder.tvTransactionItemRowAmount.setTextColor(getColor(context, R.color.income_color))
                }
                holder.ivTransactionItemRowCategory.setImageResource(category.icon)
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

}