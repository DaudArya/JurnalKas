package com.sigarda.jurnalkas.adapter.calendar

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sigarda.jurnalkas.databinding.ItemIncomeBinding
import com.sigarda.jurnalkas.databinding.ItemSpendingBinding
import com.sigarda.jurnalkas.model.TransactionUiModel
import com.sigarda.jurnalkas.utils.Constant
import com.sigarda.jurnalkas.wrapper.Extension.toFormat
import java.text.NumberFormat
import java.util.Locale

class CalendarAdapter : RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemSpendingBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<TransactionUiModel>() {
        override fun areItemsTheSame(oldItem: TransactionUiModel, newItem: TransactionUiModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TransactionUiModel, newItem: TransactionUiModel): Boolean {
            return oldItem == newItem
        }
    }
    private val differ = AsyncListDiffer(this, differCallback)

    var calendarList: List<TransactionUiModel>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSpendingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }
    override fun getItemCount(): Int {
        return calendarList.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val strAmount: String = NumberFormat.getNumberInstance(Locale.US).format(calendarList[position].amount)
            transactionAmount.text = strAmount
            transactionCategory.text = calendarList[position].type
            transactionIconView.setImageResource(calendarList[position].icon)
            transactionAmount.setTextColor(Color.parseColor(calendarList[position].cardColor))
            transactionName.text = calendarList[position].title
            date.text = calendarList[position].date.toFormat(Constant.CURRENT_DATE_FORMAT)

        }
    }
}