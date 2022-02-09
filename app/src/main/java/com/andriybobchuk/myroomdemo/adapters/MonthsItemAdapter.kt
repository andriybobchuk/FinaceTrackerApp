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
import com.andriybobchuk.myroomdemo.util.MyDateConverter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


open class MonthsItemAdapter(
    private val context: Context,
    private var list: ArrayList<TransactionEntity>,
    private var transactionDao: TransactionDao
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listOfMonths = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_month, parent, false)

        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("Range", "CutPasteId", "SetTextI18n", "SimpleDateFormat")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]


        if (holder is MyViewHolder) {

            holder.itemView.findViewById<TextView>(R.id.tv_year).text =
                MyDateConverter(model.date).year
            holder.itemView.findViewById<TextView>(R.id.tv_month).text =
                MyDateConverter(model.date).monthString


//        if (!listOfMonths.contains(MyDateConverter(model.date).monthString)) {
//            listOfMonths.add(MyDateConverter(model.date).monthString)
//
//            if (holder is MyViewHolder) {
//
//
//                holder.itemView.findViewById<TextView>(R.id.tv_year).text =
//                    MyDateConverter(model.date).year
//                holder.itemView.findViewById<TextView>(R.id.tv_month).text =
//                    MyDateConverter(model.date).monthString
//
//            } else {
//
//
//            }


//            holder.itemView.findViewById<TextView>(R.id.tv_account_type).text = model.type
//            holder.itemView.findViewById<TextView>(R.id.tv_account_name).text = "${model.name}*"
//
//            holder.itemView.findViewById<CardView>(R.id.cv_add_account).setOnClickListener {
//                context.startActivity(Intent(context, CreateAccountActivity::class.java))
//                // Inflate the dialog
//                //AccountDesignDialog(context, accountDao).show()
//            }
//
//            holder.itemView.findViewById<CardView>(R.id.cv_account_item).setOnClickListener {
//
//                context.startActivity(Intent(context, UpdateAccountActivity::class.java)
//                    .putExtra("id", model.id))
//            }
        }
    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return list.size
    }

    /**
     * A function to get density pixel from pixel
     */
    private fun Int.toDp(): Int =
        (this / Resources.getSystem().displayMetrics.density).toInt()

    /**
     * A function to get pixel from density pixel
     */
    private fun Int.toPx(): Int =
        (this * Resources.getSystem().displayMetrics.density).toInt()


    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}