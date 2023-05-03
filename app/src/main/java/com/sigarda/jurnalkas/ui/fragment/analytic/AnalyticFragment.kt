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

        val barEntry = ArrayList<BarEntry>()


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
        barEntry.add(BarEntry(1f, salary, R.drawable.icons8_money_with_wings_96))
        barEntry.add(BarEntry(2f, bonus, R.drawable.icons8_money_with_wings_96))
        barEntry.add(BarEntry(3f, parttime, R.drawable.icons8_money_with_wings_96))
        barEntry.add(BarEntry(4f, invest, R.drawable.icons8_money_with_wings_96))
        barEntry.add(BarEntry(5f, eat, R.drawable.icons8_steaming_bowl_96))
        barEntry.add(BarEntry(6f, food, R.drawable.icons8_chocolate_bar_emoji_96))
        barEntry.add(BarEntry(7f, housing, R.drawable.icons8_house_96))
        barEntry.add(BarEntry(8f, daily, R.drawable.icons8_broom_96))
        barEntry.add(BarEntry(9f, traffic, R.drawable.icons8_bus_96))
        barEntry.add(BarEntry(10f, education, R.drawable.icons8_graduation_cap_96))
        barEntry.add(BarEntry(11f, present, R.drawable.icons8_wrapped_gift_96))
        barEntry.add(BarEntry(12f, funny, R.drawable.icons8_admission_96))
        barEntry.add(BarEntry(13f, others, R.drawable.ic_others))

        val barDataSet = BarDataSet(barEntry, "list")
        barDataSet.valueTextSize = 15f
        barDataSet.setColors(*ColorTemplate.PASTEL_COLORS)
        barDataSet.valueTextColor = Color.BLACK

        val barData = BarData(barDataSet)
        binding.barChart.setFitBars(false)
        binding.barChart.data = barData
        binding.barChart.description.text = "Bar Chart"
        binding.barChart.xAxis.valueFormatter = IndexAxisValueFormatter()
        binding.barChart.xAxis.position = XAxis.XAxisPosition.TOP
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