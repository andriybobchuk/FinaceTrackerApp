package com.andriybobchuk.myroomdemo.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.andriybobchuk.myroomdemo.R
import com.andriybobchuk.myroomdemo.room.AccountDao
import com.andriybobchuk.myroomdemo.room.AccountEntity
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build

import android.util.DisplayMetrics
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import com.andriybobchuk.myroomdemo.activities.CreateAccountActivity
import com.andriybobchuk.myroomdemo.activities.UpdateAccountActivity
import com.andriybobchuk.myroomdemo.dialogs.AccountDesignDialog
import com.andriybobchuk.myroomdemo.room.TransactionDao
import com.andriybobchuk.myroomdemo.room.TransactionEntity
import android.text.format.DateFormat;
import android.widget.Button
import androidx.core.content.ContextCompat
import com.andriybobchuk.myroomdemo.util.MyDateConverter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

open class MonthsItemAdapter(
    private val context: Context,
    private var list: ArrayList<TransactionEntity>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    var checkedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(context).inflate(
            R.layout.item_month,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {

            // Bind items
            val tvYear = holder.itemView.findViewById<TextView>(R.id.tv_year)
            val tvMonth = holder.itemView.findViewById<TextView>(R.id.tv_month)

            // Setup items
            tvYear.text = MyDateConverter(model.date).year
            tvMonth.text = MyDateConverter(model.date).monthString

            // Set selection
            if (checkedPosition == -1) {
                // Switch it off
                tvMonth.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.grey_background_color
                    )
                )
                tvMonth.setTextColor(ContextCompat.getColor(context, R.color.black))
            } else {
                if (checkedPosition == position) {
                    // Switch on
                    tvMonth.setBackgroundColor(ContextCompat.getColor(context, R.color.black))
                    tvMonth.setTextColor(ContextCompat.getColor(context, R.color.white))
                } else {
                    // Switch off
                    tvMonth.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.grey_background_color
                        )
                    )
                    tvMonth.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }


            holder.itemView.setOnClickListener {
                if (onClickListener != null) {

                    // Change selection
                    if (checkedPosition != position) {
                        // Update the two items we swap selection between
                        notifyDataSetChanged()
                        // Now this item is selected
                        checkedPosition = position

                    }

                    // Change report data
                    onClickListener!!.onClick(position, model) // model = list[position]
                }
            }
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener // Basic setter function
    }

    interface OnClickListener {
        fun onClick(position: Int, model: TransactionEntity)
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}