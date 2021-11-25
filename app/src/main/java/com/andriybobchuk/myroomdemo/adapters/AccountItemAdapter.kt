package com.andriybobchuk.myroomdemo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andriybobchuk.myroomdemo.databinding.ItemAccountBinding
import com.andriybobchuk.myroomdemo.room.AccountEntity


class AccountItemAdapter(private val items: ArrayList<AccountEntity>
) : RecyclerView.Adapter<AccountItemAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemAccountBinding) : RecyclerView.ViewHolder(binding.root) {

        val cvAdd = binding.cvAddAccount
        val tvAdd = binding.tvAddAccount

        val llCard = binding.llCard
        val tvAmount = binding.tvAmountOnCard
        val tvType = binding.tvAccountType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAccountBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]

        // Let's bind individual items of each view
        holder.tvAmount.text = item.balance
        holder.tvType.text = item.type

    }

    override fun getItemCount(): Int {
        return items.size
    }

}