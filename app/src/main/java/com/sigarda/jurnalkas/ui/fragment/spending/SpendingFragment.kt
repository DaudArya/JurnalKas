package com.sigarda.jurnalkas.ui.fragment.spending

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.data.local.entity.TransactionEntity
import com.sigarda.jurnalkas.databinding.FragmentSpendingBinding
import com.sigarda.jurnalkas.ui.fragment.base.BaseFragment
import com.sigarda.jurnalkas.utils.Constant
import com.sigarda.jurnalkas.wrapper.Extension.toFormat
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import com.sigarda.jurnalkas.wrapper.*
import com.sigarda.jurnalkas.utils.*
import com.sigarda.jurnalkas.utils.enums.Type
import com.sigarda.jurnalkas.wrapper.Extension.gone
import com.sigarda.jurnalkas.wrapper.Extension.visible
import me.abhinay.input.CurrencySymbols
import java.util.*


@AndroidEntryPoint
class SpendingFragment : BaseFragment() {

    private var _binding: FragmentSpendingBinding? = null
    private val binding get() = _binding!!

    val dataTrans by navArgs<SpendingFragmentArgs>()

    private val viewModel: SpendingViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by viewModels()

    private var amount: Float? = null
    private var title : String? = null
    private var isIncome: Boolean? = null
    private var type: String? = null
    private var selectedDate: Date? = null
    private var transacationType : String = ""
    private var isHomePage: Boolean = true
    private var argsTitle: String? = null
    private var argsType: String? = null
    private var argsAmount: String? = null
    private var argsIsIncome: String? = null
    private var argsDate: String? = null
    private var argsDocumentId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSpendingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            isHomePage = SpendingFragmentArgs.fromBundle(it).isHomePage
            argsTitle = SpendingFragmentArgs.fromBundle(it).title
            argsType = SpendingFragmentArgs.fromBundle(it).type
            argsAmount = SpendingFragmentArgs.fromBundle(it).amount
            argsIsIncome = SpendingFragmentArgs.fromBundle(it).isIncome
            argsDate = SpendingFragmentArgs.fromBundle(it).date
            argsDocumentId = SpendingFragmentArgs.fromBundle(it).documentId
        }
        initViews()
        observeEvents()


    }



    private fun initViews() {
        if (!isHomePage) {
            deleteOrUpdateBudget()
        } else {
            addBudget()
        }
        bottomNavigationViewVisibility = View.GONE
        toolbarVisibility = false

    }







    private fun getTransactionContent(): TransactionEntity = binding.let {
        chipItems()
        val title = it.etTitle.text.toString()
        val amount = parseDouble(it.etAmount.text.toString())
        val transactionType = transacationType
        val tag = type.toString()
        val date = it.etWhen.text.toString()

        return TransactionEntity(title, amount, transactionType, tag, date)
    }



    private fun observeEvents() {

        viewModel.addBudget.observe(viewLifecycleOwner) {
            when (it) {
                is BudgetState.OnSuccess -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()


                }
                is BudgetState.OnFailure -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        viewModel.deleteBudget.observe(viewLifecycleOwner) {
            when (it) {
                is BudgetState.OnSuccess -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
                is BudgetState.OnFailure -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        viewModel.updateBudget.observe(viewLifecycleOwner) {
            when (it) {
                is BudgetState.OnSuccess -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                }
                is BudgetState.OnFailure -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun addBudget() {

        chipItems()
        switchItems()
        calendarItem()
        binding.accept.setOnClickListener {
            binding.apply {
                // validate if transaction content is empty or not
                if (etTitle == null) {
                    binding.etTitle.error = "Email must not be empty"
                    }
                    if (etAmount == null){
                        binding.etAmount.error = "Email must not be empty"
                    }
                    else  {
                        title = binding.etTitle.text.toString()
                        amount = binding.etAmount.text.toString().toFloatOrNull()
                        isIncome = binding.switchIncome.isChecked
                        viewModel.addBudget(
                            amount = amount,
                            title = title,
                            isIncome = isIncome,
                            type = type,
                            date = selectedDate
                        )
                        findNavController().navigate(
                            R.id.action_spendingFragment_to_homeFragment
                        )

                        }
                    }
                }
        }




    private fun deleteOrUpdateBudget() {
        when (argsType) {
            Type.SALARY.type -> binding.chipSalary.isChecked = true
            Type.BONUS.type -> binding.chipBonus.isChecked = true
            Type.INVEST.type -> binding.chipInvest.isChecked = true
            Type.PARTTIME.type -> binding.chipParttime.isChecked = true
            Type.PRESENT.type -> binding.chipPresent.isChecked = true
            Type.EAT.type -> binding.chipEat.isChecked = true
            Type.FOOD.type -> binding.chipFoody.isChecked = true
            Type.DAILY.type -> binding.chipDaily.isChecked = true
            Type.HOUSING.type -> binding.chipHousing.isChecked = true
            Type.TRAFFIC.type -> binding.chipOthers.isChecked = true
            Type.EDUCATION.type -> binding.chipEdu.isChecked = true
            else -> binding.chipOthers.isChecked = true
        }

        binding.etAmount.setText(argsAmount)
        binding.switchIncome.isChecked = argsIsIncome.toBoolean()
        binding.switchExpense.isChecked = !argsIsIncome.toBoolean()
        binding.etWhen.text = argsDate
        binding.accept.gone()
        binding.buttonDelete.visible()
//        binding.buttonUpdate.visible()



        binding.buttonDelete.setOnClickListener {
            viewModel.deleteBudget(documentId = argsDocumentId!!)
            findNavController().navigate(
                R.id.action_spendingFragment_to_homeFragment
            )
        }


        chipItems()
        switchItems()
        calendarItem()

        binding.buttonUpdate.setOnClickListener {
            viewModel.updateBudget(
                amount = binding.etAmount.text.toString().toFloat(),
                title = binding.etTitle.text.toString(),
                isIncome = binding.switchIncome.isChecked,
                type = type,
                date = selectedDate,
                documentId = argsDocumentId
            )
            findNavController().navigate(
                R.id.action_spendingFragment_to_homeFragment)
        }
    }

    private fun switchItems() {
        binding.switchIncome.setOnClickListener {
            binding.switchExpense.isChecked = !binding.switchExpense.isChecked
            transacationType = "Income"
        }
        binding.switchExpense.setOnClickListener {
            binding.switchIncome.isChecked = !binding.switchIncome.isChecked
            transacationType = "Expense"
        }
    }

    private fun calendarItem() {

        binding.etWhen.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText(getString(R.string.select_date_button))
                    .setSelection(selectedDate?.time)
                    .build()
            datePicker.addOnPositiveButtonClickListener { timestamp ->
                val selectedUtc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                selectedUtc.timeInMillis = timestamp
                val selectedLocal = Calendar.getInstance()
                selectedLocal.clear()
                selectedLocal.set(
                    selectedUtc.get(Calendar.YEAR),
                    selectedUtc.get(Calendar.MONTH),
                    selectedUtc.get(Calendar.DATE)
                )
                selectedDate = selectedLocal.time
                binding.etWhen.text =
                    selectedLocal.time.toFormat(Constant.CURRENT_DATE_FORMAT)
            }
            datePicker.show(parentFragmentManager, Constant.TAG_DATE_PICKER)
        }
    }

    private fun chipItems() {
        binding.chipGroup.setOnCheckedChangeListener(){ _, checkedId ->
            when (checkedId) {
                R.id.chipSalary -> type = binding.chipSalary.text.toString()
                R.id.chipBonus -> type = binding.chipBonus.text.toString()
                R.id.chipInvest -> type = binding.chipInvest.text.toString()
                R.id.chipParttime -> type = binding.chipParttime.text.toString()
                R.id.chipPresent -> type = binding.chipParttime.text.toString()
                R.id.chipEat -> type = binding.chipEat.text.toString()
                R.id.chipFoody -> type = binding.chipFoody.text.toString()
                R.id.chipEdu -> type = binding.chipEdu.text.toString()
                R.id.chipDaily -> type = binding.chipDaily.text.toString()
                R.id.chipHousing -> type = binding.chipHousing.text.toString()
                R.id.chipTraffic -> type = binding.chipTraffic.text.toString()
                R.id.chipOthers -> type = binding.chipOthers.text.toString()
            }
        }
    }

    private fun validateInput(): Boolean {
        var isValid = true
        val title = binding.etTitle.text.toString().trim()
        val amount = binding.etAmount.text.toString().trim()

        if (title.isEmpty()) {
            isValid = false
            binding.etTitle.error = "Title must not be empty"
        }
        if (amount.isEmpty()) {
            isValid = false
            binding.etAmount.error = "Amount must not be empty"
        }
        return isValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

sealed class BudgetState {
    data class OnSuccess(val message: String) : BudgetState()
    data class OnFailure(val message: String) : BudgetState()
}


//                    Snackbar.make(
//                        binding.root,
//                        getString(R.string.success_transaction_export),
//                        Snackbar.LENGTH_LONG
//                    )
//                        .apply {
//                            setAction(getString(R.string.ok)) {
//                            }
//                            show()
//                        }
//                    findNavController().navigate(R.id.action_spendingFragment_to_homeFragment)