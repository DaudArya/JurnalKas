package com.sigarda.jurnalkas.adapter.transaction.local

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.data.local.entity.TransactionEntity
import com.sigarda.jurnalkas.databinding.ItemSpendingBinding

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionVH>() {

    inner class TransactionVH(val binding: ItemSpendingBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallback = object : DiffUtil.ItemCallback<TransactionEntity>() {
        override fun areItemsTheSame(oldItem: TransactionEntity, newItem: TransactionEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TransactionEntity, newItem: TransactionEntity): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionVH {
        val binding =
            ItemSpendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionVH(binding)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TransactionVH, position: Int) {

        val item = differ.currentList[position]


        holder.binding.apply {

            transactionName.text = item.title
            date.text = item.date
            transactionCategory.text = item.tag

            when (item.transactionType) {
                "Income" -> {
                    transactionAmount.setTextColor(
                        ContextCompat.getColor(
                            transactionAmount.context,
                            R.color.income
                        )
                    )

                    transactionAmount.text = "+ ".plus("Rp."+(item.amount))
                }
                "Expense" -> {
                    transactionAmount.setTextColor(
                        ContextCompat.getColor(
                            transactionAmount.context,
                            R.color.expense
                        )
                    )
                    transactionAmount.text = "- ".plus("Rp."+(item.amount))
                }
            }

            when (item.tag) {
                "jajan" -> {
                    transactionIconView.setImageResource(R.drawable.icons8_chocolate_bar_emoji_96)
                }
                "lalu lintas" -> {
                    transactionIconView.setImageResource(R.drawable.icons8_bus_96)
                }
                "makan" -> {
                    transactionIconView.setImageResource(R.drawable.icons8_steaming_bowl_96)
                }
                "harian" -> {
                    transactionIconView.setImageResource(R.drawable.icons8_broom_96)
                }
                "hadiah" -> {
                    transactionIconView.setImageResource(R.drawable.icons8_wrapped_gift_96)
                }
                "rumah" -> {
                    transactionIconView.setImageResource(R.drawable.icons8_house_96)
                }
                "pendidikan" -> {
                    transactionIconView.setImageResource(R.drawable.icons8_graduation_cap_96)
                }
                "hiburan" -> {
                    transactionIconView.setImageResource(R.drawable.icons8_admission_96)
                }
                "gaji" -> {
                    transactionIconView.setImageResource(R.drawable.icons8_money_with_wings_96)
                }
                "parttime" -> {
                    transactionIconView.setImageResource(R.drawable.icons8_money_with_wings_96)
                }
                "bonus" -> {
                    transactionIconView.setImageResource(R.drawable.icons8_money_with_wings_96)
                }
                "investasi" -> {
                    transactionIconView.setImageResource(R.drawable.icons8_money_with_wings_96)
                }
                else -> {
                    transactionIconView.setImageResource(R.drawable.ic_others)
                }
            }

            // on item click
            holder.itemView.setOnClickListener {
                onItemClickListener?.let { it(item) }
            }
        }
    }

    // on item click listener
    private var onItemClickListener: ((TransactionEntity) -> Unit)? = null
    fun setOnItemClickListener(listener: (TransactionEntity) -> Unit) {
        onItemClickListener = listener
    }
}