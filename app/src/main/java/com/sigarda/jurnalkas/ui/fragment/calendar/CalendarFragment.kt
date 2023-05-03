package com.sigarda.jurnalkas.ui.fragment.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.applandeo.materialcalendarview.EventDay
import com.sigarda.jurnalkas.adapter.calendar.CalendarAdapter
import com.sigarda.jurnalkas.databinding.FragmentCalendarBinding
import com.sigarda.jurnalkas.model.TransactionUiModel
import com.sigarda.jurnalkas.ui.fragment.base.BaseFragment
import com.sigarda.jurnalkas.wrapper.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class CalendarFragment : BaseFragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CalendarViewModel by viewModels()
    private val calendarListAdapter = CalendarAdapter()


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
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBudgetFromFirestore()

        observeEvent()
        initViews()

    }

    private fun observeEvent() {
        viewModel.result.observe(viewLifecycleOwner) { list ->
            when (list) {
                is Resource.Success -> {
                    list.data?.let {
                        showCalendar(it)
                        showRecyclerView(it)
                    }
                    binding.calendarView.visibility = View.VISIBLE
                    binding.textViewError.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Error -> {
                    binding.calendarView.visibility = View.GONE
                    binding.textViewError.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }
                is Resource.Loading -> {
                    binding.calendarView.visibility = View.GONE
                    binding.textViewError.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun initViews() {
        bottomNavigationViewVisibility = View.VISIBLE
        toolbarVisibility = false

        binding.recyclerView.adapter = calendarListAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
    }

    private fun showCalendar(list: List<TransactionUiModel>) {

        val event = ArrayList<EventDay>()

        list.forEach {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it.date.time
            event.add(EventDay(calendar, it.icon))
        }
        binding.calendarView.setEvents(event)

    }

    private fun showRecyclerView(list: List<TransactionUiModel>) {
        binding.calendarView.setOnDayClickListener { eventDay ->
            list.filter { budget ->
                budget.date.time == eventDay.calendar.time.time
            }.run {
                calendarListAdapter.calendarList = this
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}