package com.sigarda.jurnalkas.ui.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.adapter.transaction.local.TransactionAdapter
import com.sigarda.jurnalkas.adapter.transaction.remote.RemoteTransactionAdapter
import com.sigarda.jurnalkas.data.local.entity.Transaction
import com.sigarda.jurnalkas.data.local.entity.TransactionEntity
import com.sigarda.jurnalkas.databinding.FragmentHomeBinding
import com.sigarda.jurnalkas.model.TransactionUiModel
import com.sigarda.jurnalkas.ui.fragment.base.BaseFragment
import com.sigarda.jurnalkas.ui.fragment.spending.SpendingViewModel
import com.sigarda.jurnalkas.utils.TransactionViewModel
import com.sigarda.jurnalkas.wrapper.Extension.gone
import com.sigarda.jurnalkas.wrapper.Extension.visible
import com.sigarda.jurnalkas.wrapper.Resource
import com.sigarda.jurnalkas.wrapper.hide
import com.sigarda.jurnalkas.wrapper.rupiah
import com.sigarda.jurnalkas.wrapper.show
import com.sigarda.jurnalkas.wrapper.snack
import com.sigarda.jurnalkas.wrapper.viewState.ViewState
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private val transactionViewModel: TransactionViewModel by viewModels()
    private val spendingViewModel : SpendingViewModel by viewModels()
    private val remoteAdapter = RemoteTransactionAdapter()
    private var transactionAdapter = TransactionAdapter()


    private var amount: Int? = null
    private var isIncome: Boolean? = null
    private var type: String? = null
    private var title: String? = null
    private var selectedDate: Date? = null
    private var transacationType : String = ""
    private var isHomePage: Boolean = true
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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBudgetFromFirestore()
        observeEvent()
        observeFilter()
        observeTransaction()
        observeEventAmount()
        initViews()
        navigate()

    }



    private fun initViews() {

        binding.transactionRv.adapter = remoteAdapter
        binding.transactionRv.layoutManager = LinearLayoutManager(requireContext())

        bottomNavigationViewVisibility = View.VISIBLE

        binding.addTransaction.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSpendingFragment(
                isHomePage = true,
                type = null,
                amount = null,
                title = null,
                isIncome = null,
                date = null,
                documentId = null
            )
            Navigation.findNavController(it).navigate(action)
        }


        remoteAdapter.onDeleteClick = {  title, type, amount, isIncome, date, documentId ->

            val action = HomeFragmentDirections.actionHomeFragmentToSpendingFragment(
                type = type,
                amount = amount,
                date = date,
                isIncome = isIncome,
                documentId = documentId,
                isHomePage = false,
                title = title
            )
            Navigation.findNavController(requireView()).navigate(action)
        }
    }

    private fun initViewsLocal() = with(binding) {
        addTransaction.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_spendingFragment)
        }
        transactionAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("transaction", it)
            }
        }

    }

    private fun observeFilter() = with(binding) {
        lifecycleScope.launchWhenCreated {
            transactionViewModel.transactionFilter.collect { filter ->
                when (filter) {
                    "Overall" -> {
                        totalIncomExpense.show()
                        incomeCardView.totalTitle.text = getString(R.string.text_total_income)
                        expenseCardView.totalTitle.text = getString(R.string.text_total_expense)
                        expenseCardView.totalIcon.setImageResource(R.drawable.ic_expense)
                    }
                    "Income" -> {
                        totalmoney.text =
                            getString(R.string.text_total_income)
                        totalIncomExpense.hide()
                    }
                    "Expense" -> {
                        totalmoney.text =
                            getString(R.string.text_total_expense)
                        totalIncomExpense.hide()
                    }
                }
                transactionViewModel.getAllTransaction(filter)
            }
        }
    }

    private fun observeEventAmount() {
        viewModel.result.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (it.data != null && it.data.isEmpty().not()) {
                        setIncomeExpense(it.data)
                        binding.textViewError.gone()
                        binding.progressBar.gone()
//                        binding.textViewNullData.gone()
                    } else {
//                        binding.showLinearLayout.gone()
//                        binding.textViewNullData.visible()
                    }
                }
                is Resource.Error -> {
                    binding.textViewError.visible()
                    binding.progressBar.gone()
                }
                is Resource.Loading -> {
                    binding.textViewError.gone()
                    binding.progressBar.visible()
                }
            }
        }
    }

    private fun observeEvent() {
        viewModel.result.observe(viewLifecycleOwner) { list ->
            when (list) {
                is Resource.Success -> {
                    if (list.data != null && list.data.isEmpty().not()) {
                        val newList = list.data.sortedWith(compareByDescending { it.date })
                        remoteAdapter.budgetList = newList
                        binding.transactionRv.visible()
                        binding.progressBar.gone()
                        binding.textViewError.gone()
                        binding.emptyStateLayout.gone()
                    } else {
                        binding.emptyStateLayout.visible()
                        binding.transactionRv.gone()
                        binding.textViewError.gone()
                        binding.progressBar.gone()
                    }
                }
                is Resource.Error -> {
                    binding.textViewError.text = list.message.toString()

                    binding.textViewError.visible()
                    binding.transactionRv.gone()
                    binding.progressBar.gone()
                    binding.emptyStateLayout.gone()
                }
                is Resource.Loading -> {
                    binding.textViewError.gone()
                    binding.transactionRv.gone()
                    binding.progressBar.visible()
                    binding.emptyStateLayout.gone()
                }
            }
        }
    }

    private fun setIncomeExpense(list: List<TransactionUiModel>) {

        var incomeAmount = 0f
        var expenseAmount = 0f

        var income = 0
        var outcome = 0



        list.forEach {
            when (it.isIncome) {
                true -> {
                    incomeAmount += it.amount
                    val strIncome: String = NumberFormat.getNumberInstance(Locale.US).format(incomeAmount)
                    binding.incomeCardView.total.text = "+ "+strIncome
                    income = incomeAmount.toInt()
                }
                else -> {
                    expenseAmount += it.amount
                    val strExpense: String = NumberFormat.getNumberInstance(Locale.US).format(expenseAmount)
                    binding.expenseCardView.total.text = "- "+strExpense
                    outcome = expenseAmount.toInt()
                }
            }
            amount = income-outcome
            val str: String = NumberFormat.getNumberInstance(Locale.US).format(amount)
            binding.totalmoney.text = "IDR."+str
        }}

    private fun setupRV() = with(binding) {
        transactionAdapter = TransactionAdapter()
        transactionRv.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }


    private fun swipeToDelete() {
        // init item touch callback for swipe action
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // get item position & delete notes
                val position = viewHolder.adapterPosition
                val transaction = transactionAdapter.differ.currentList[position]
                val transactionItem = TransactionEntity(
                    transaction.title,
                    transaction.amount,
                    transaction.transactionType,
                    transaction.tag,
                    transaction.date,
                    transaction.createdAt,
                    transaction.id
                )

                val positionRemote = viewHolder.adapterPosition
                val transactionRemote = remoteAdapter.differ.currentList[positionRemote]
                val transactionItemRemote = Transaction(
                    transactionRemote.id,
                    transactionRemote.title,
                    transactionRemote.amount,
                    transactionRemote.isIncome,
                    transactionRemote.type,
                    transactionRemote.date
                )


                transactionViewModel.deleteTransaction(transactionItem)
                spendingViewModel.deleteTransactionById(transactionItemRemote.id!!.toInt())

                Snackbar.make(
                    binding.root,
                    getString(R.string.success_transaction_delete),
                    Snackbar.LENGTH_LONG
                )
                    .apply {
                        setAction(getString(R.string.text_undo)) {
                            transactionViewModel.insertTransaction(
                                transactionItem
                            )
                        }
                        show()
                    }
            }
        }

        // attach swipe callback to rv
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.transactionRv)
        }
    }

    private fun onTotalTransactionLoaded(transaction: List<TransactionEntity>) = with(binding) {
        val (totalIncome, totalExpense) = transaction.partition { it.transactionType == "Income" }
        val income = totalIncome.sumOf { it.amount }
        val expense = totalExpense.sumOf { it.amount }
        incomeCardView.total.text = "+ ".plus(rupiah(income))
        expenseCardView.total.text = "- ".plus(rupiah(expense))
        totalmoney.text = (rupiah(income - expense))
    }

    private fun observeTransaction() = lifecycleScope.launchWhenStarted {
        transactionViewModel.uiState.collect { uiState ->
            when (uiState) {
                is ViewState.Loading -> {
                }
                is ViewState.Success -> {
                    showAllViews()
                    onTransactionLoaded(uiState.transaction)
                    onTotalTransactionLoaded(uiState.transaction)
                }
                is ViewState.Error -> {
                    binding.root.snack(
                        string = R.string.text_error
                    )
                }
                is ViewState.Empty -> {
                    hideAllViews()
                }
            }
        }
    }

    private fun showAllViews() = with(binding) {
        emptyStateLayout.hide()
        transactionRv.show()
    }

    private fun hideAllViews() = with(binding) {
        emptyStateLayout.show()
    }

    private fun onTransactionLoaded(list: List<TransactionEntity>) =
        transactionAdapter.differ.submitList(list)



    private fun navigate(){
        goToSettings()
        goToCalendar()
    }


    private fun goToCalendar(){
        binding.calendar.setOnClickListener(){
            findNavController().navigate(R.id.action_homeFragment_to_calendarFragment)
        }
    }
    private fun goToSettings(){
        binding.setting.setOnClickListener(){
            findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}