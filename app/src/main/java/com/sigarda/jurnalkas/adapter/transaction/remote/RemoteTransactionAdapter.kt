package com.sigarda.jurnalkas.adapter.transaction.remote

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sigarda.jurnalkas.databinding.ItemSpendingBinding
import com.sigarda.jurnalkas.model.TransactionUiModel
import com.sigarda.jurnalkas.utils.Constant
import com.sigarda.jurnalkas.wrapper.Extension.toFormat

class RemoteTransactionAdapter : RecyclerView.Adapter<RemoteTransactionAdapter.ViewHolder>() {
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
    val differ = AsyncListDiffer(this, differCallback)

    var budgetList: List<TransactionUiModel>
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
        return budgetList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply {
            transactionAmount.text = budgetList[position].amount.toString()
            transactionCategory.text = budgetList[position].type
            transactionName.text = budgetList[position].type
            date.text = budgetList[position].date.toFormat(Constant.CURRENT_DATE_FORMAT)
            transactionIconView.setImageResource(budgetList[position].icon)
            transactionAmount.setTextColor(Color.parseColor(budgetList[position].cardColor))

            cardItem.setOnClickListener {
                onDeleteClick?.invoke(
                    budgetList[position].type,
                    budgetList[position].amount.toString(),
                    budgetList[position].isIncome.toString(),
                    budgetList[position].date.toFormat(Constant.CURRENT_DATE_FORMAT),
                    budgetList[position].id!!
                )
            }
        }
    }

    var onDeleteClick: ((
        type: String,
        amount: String,
        isIncome: String,
        date: String,
        documentId: String
    ) -> Unit)? = null

}