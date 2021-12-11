package com.andriybobchuk.myroomdemo.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.andriybobchuk.myroomdemo.HistoryFragment
import com.andriybobchuk.myroomdemo.R
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
        holder.tvTransactionItemRowCategory.text = item.category
        holder.tvTransactionItemRowDate.text = item.date
        holder.tvTransactionItemRowDescription.text = item.description

        when (item.category) {
            "Groceries" -> {
                holder.ivTransactionItemRowCategory.setImageResource(R.drawable.ic_groceries)
                holder.tvTransactionItemRowAmount.text = "-${item.amount} ${item.currency}"
            }
            "Transportation" -> {
                holder.ivTransactionItemRowCategory.setImageResource(R.drawable.ic_transportation)
                holder.tvTransactionItemRowAmount.text = "-${item.amount} ${item.currency}"
            }
            "Rent" -> {
                holder.ivTransactionItemRowCategory.setImageResource(R.drawable.ic_rent)
                holder.tvTransactionItemRowAmount.text = "-${item.amount} ${item.currency}"
            }
            "Subscriptions" -> {
                holder.ivTransactionItemRowCategory.setImageResource(R.drawable.ic_subscriptions)
                holder.tvTransactionItemRowAmount.text = "-${item.amount} ${item.currency}"
            }
            "Healthcare" -> {
                holder.ivTransactionItemRowCategory.setImageResource(R.drawable.ic_healthcare)
                holder.tvTransactionItemRowAmount.text = "-${item.amount} ${item.currency}"
            }
            "Entertainment" -> {
                holder.ivTransactionItemRowCategory.setImageResource(R.drawable.ic_entertainment)
                holder.tvTransactionItemRowAmount.text = "-${item.amount} ${item.currency}"
            }
            "Internet" -> {
                holder.ivTransactionItemRowCategory.setImageResource(R.drawable.ic_internet)
                holder.tvTransactionItemRowAmount.text = "-${item.amount} ${item.currency}"
            }
            "Barber" -> {
                holder.ivTransactionItemRowCategory.setImageResource(R.drawable.ic_other_expense)
                holder.tvTransactionItemRowAmount.text = "-${item.amount} ${item.currency}"
            }
            "Other Expenses" -> {
                holder.ivTransactionItemRowCategory.setImageResource(R.drawable.ic_other_expense)
                holder.tvTransactionItemRowAmount.text = "-${item.amount} ${item.currency}"
            }

            // INCOMES:
            "Salary" -> {
                holder.ivTransactionItemRowCategory.setImageResource(R.drawable.ic_salary)
                holder.tvTransactionItemRowAmount.text = "+${item.amount} ${item.currency}"
                holder.tvTransactionItemRowAmount.setTextColor(getColor(context, R.color.income_color))
            }
            "Royalty" -> {
                holder.ivTransactionItemRowCategory.setImageResource(R.drawable.ic_royalties)
                holder.tvTransactionItemRowAmount.text = "+${item.amount} ${item.currency}"
                holder.tvTransactionItemRowAmount.setTextColor(getColor(context, R.color.income_color))
            }
            "Other Incomes" -> {
                holder.ivTransactionItemRowCategory.setImageResource(R.drawable.ic_other_income)
                holder.tvTransactionItemRowAmount.text = "+${item.amount} ${item.currency}"
                holder.tvTransactionItemRowAmount.setTextColor(getColor(context, R.color.income_color))
            }

        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

}