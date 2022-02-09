package com.andriybobchuk.myroomdemo.adapters
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.andriybobchuk.myroomdemo.R
import com.andriybobchuk.myroomdemo.activities.CategoryActivity
import com.andriybobchuk.myroomdemo.databinding.ItemCategoryBinding
import com.andriybobchuk.myroomdemo.room.CategoryEntity


class CategoryItemAdapter(
    private val items: ArrayList<CategoryEntity>,
    private val updateListener: (id: Int) -> Unit
) : RecyclerView.Adapter<CategoryItemAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivCategory = binding.ivCategory
        val tvCategory = binding.tvCategory
        val tvCategoryType = binding.tvCategoryType
        val cvCategory = binding.cvCategory
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        holder.ivCategory.setImageResource(item.icon)
        holder.tvCategory.text = item.name
        holder.tvCategoryType.text = item.type

        holder.cvCategory.setOnClickListener {
            updateListener.invoke(item.id)
        }


//        for (category in categories) {
//
//            if (category.name == item.category) {
//
//                if (category.type == Constants.EXPENSE) {
//                    holder.tvTransactionItemRowAmount.text = "-${item.amount} ${item.currency}"
//                    holder.tvTransactionItemRowAmount.setTextColor(
//                        getColor(
//                            context,
//                            R.color.primary_text_color
//                        )
//                    )
//                } else {
//                    holder.tvTransactionItemRowAmount.text = "+${item.amount} ${item.currency}"
//                    holder.tvTransactionItemRowAmount.setTextColor(
//                        getColor(
//                            context,
//                            R.color.income_color
//                        )
//                    )
//                }
//                holder.ivTransactionItemRowCategory.setImageResource(category.icon)
//            }
//        }

    }

    override fun getItemCount(): Int {
        return items.size
    }


}