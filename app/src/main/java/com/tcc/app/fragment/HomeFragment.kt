package com.tcc.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.Adapter.HomeCounterAdapter
import com.tcc.app.Adapter.HomeServiceAdapter
import com.tcc.app.R
import com.tcc.app.extention.setHomeScreenTitle
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : BaseFragment(), HomeCounterAdapter.OnItemSelected,
    HomeServiceAdapter.OnItemSelected {

    var adapter: HomeCounterAdapter? = null
    var adapter1: HomeServiceAdapter? = null
    lateinit var chipArray: ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHomeScreenTitle(requireActivity(), getString(R.string.menu_home))
        chipArray = ArrayList()
        setChipList()
        setuprvHomeCounterMarchant()
    }


    private fun setChipList() {
        chipArray.add("Visitors")
        chipArray.add("Active Sites")
        chipArray.add("Customers")
        chipArray.add("Collections")
        chipArray.add("Follow Ups")

    }

    fun setuprvHomeCounterMarchant() {

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvHomeCounter.layoutManager = layoutManager
        adapter = HomeCounterAdapter(requireContext(), chipArray, this)
        rvHomeCounter.adapter = adapter

        val layoutManager1 =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvDeepCleaing.layoutManager = layoutManager1
        adapter1 = HomeServiceAdapter(requireContext(), chipArray, this)
        rvDeepCleaing.adapter = adapter1

    }

    override fun onItemSelect(position: Int, data: String) {

    }


}