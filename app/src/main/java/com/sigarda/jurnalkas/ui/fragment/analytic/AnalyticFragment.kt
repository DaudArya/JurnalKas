package com.sigarda.jurnalkas.ui.fragment.analytic

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.sigarda.jurnalkas.R
import com.sigarda.jurnalkas.databinding.FragmentAnalyticBinding
import com.sigarda.jurnalkas.utils.enums.Type
import com.sigarda.jurnalkas.model.TransactionUiModel
import com.sigarda.jurnalkas.ui.fragment.base.BaseFragment
import com.sigarda.jurnalkas.wrapper.Extension.gone
import com.sigarda.jurnalkas.wrapper.Extension.visible
import com.sigarda.jurnalkas.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AnalyticFragment : BaseFragment() {

    private var _binding: FragmentAnalyticBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AnalyticsViewModel by viewModels()

    private var salary = 0f
    private var bonus = 0f
    private var parttime = 0f
    private var traffic = 0f
    private var housing = 0f
    private var education = 0f
    private var present = 0f
    private var funny = 0f
    private var eat = 0f
    private var food = 0f
    private var invest = 0f
    private var daily = 0f
    private var others = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBudgetFromFirestore()
        initViews()
        observeEvent()
    }

    private fun initViews() {

        binding.switchType.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.barChart.visible()
                binding.pieChart.gone()
            } else {
                binding.barChart.gone()
                binding.pieChart.visible()
            }
        }


        bottomNavigationViewVisibility = View.VISIBLE
    }

    private fun observeEvent() {
        viewModel.result.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    if (it.data != null && it.data.isEmpty().not()) {
                        setBarChart(it.data)
                        setPieChart(it.data)
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
                    binding.barChart.gone()
                    binding.pieChart.gone()
                }
                is Resource.Loading -> {
                    binding.textViewError.gone()
                    binding.progressBar.visible()
                    binding.barChart.gone()
                    binding.pieChart.gone()
                }
            }
        }
    }

    private fun setBarChart(list: List<TransactionUiModel>) {

        val pieEntry = ArrayList<PieEntry>()


        list.forEach {
            when (it.type) {
                Type.SALARY.type -> salary += it.amount
                Type.TRAFFIC.type -> traffic += it.amount
                Type.EAT.type -> eat += it.amount
                Type.FUN.type -> funny += it.amount
                Type.FOOD.type -> food += it.amount
                Type.INVEST.type -> invest += it.amount
                Type.PRESENT.type -> present += it.amount
                Type.PARTTIME.type -> parttime += it.amount
                Type.BONUS.type -> bonus += it.amount
                Type.HOUSING.type -> housing += it.amount
                Type.DAILY.type -> daily += it.amount
                Type.EDUCATION.type -> education += it.amount
                else -> others += it.amount
            }
        }
        pieEntry.add(PieEntry( salary, "salary"))
        pieEntry.add(PieEntry( bonus, "bonus"))
        pieEntry.add(PieEntry( parttime, "part time"))
        pieEntry.add(PieEntry( invest, "invest"))
        pieEntry.add(PieEntry( eat, "eat"))
        pieEntry.add(PieEntry( food, "food"))
        pieEntry.add(PieEntry( housing, "housing"))
        pieEntry.add(PieEntry( daily, "daily"))
        pieEntry.add(PieEntry( traffic, "traffic"))
        pieEntry.add(PieEntry( education, "education"))
        pieEntry.add(PieEntry( present, "present"))
        pieEntry.add(PieEntry( funny, "fun"))
        pieEntry.add(PieEntry( others, "others"))

        val pieDataSet = PieDataSet(pieEntry, "")
        pieDataSet.setColors(*ColorTemplate.PASTEL_COLORS)
        pieDataSet.setColors(*ColorTemplate.PASTEL_COLORS)

        pieDataSet.valueTextSize = 30f
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.formSize = 30f
        pieDataSet.form = Legend.LegendForm.CIRCLE
        val data = PieData(pieDataSet)

        binding.barChart.data = data
        binding.barChart.animateY(2000)
    }

    private fun setPieChart(list: List<TransactionUiModel>) {

        var incomeAmount = 0f
        var expenseAmount = 0f

        list.forEach {
            when (it.isIncome) {
                true -> {
                    incomeAmount += it.amount
                }
                else -> {
                    expenseAmount += it.amount
                }
            }
        }

        val pieEntry = mutableListOf<PieEntry>()
        pieEntry.add(PieEntry(incomeAmount, "income", "income"))
        pieEntry.add(PieEntry(expenseAmount, "expense", "expense"))

        val pieDataSet = PieDataSet(pieEntry, "")
        pieDataSet.setColors(*ColorTemplate.PASTEL_COLORS)

        pieDataSet.valueTextSize = 30f
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.formSize = 30f
        pieDataSet.form = Legend.LegendForm.CIRCLE
        val data = PieData(pieDataSet)
        binding.pieChart.data = data
        binding.pieChart.animateY(2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}