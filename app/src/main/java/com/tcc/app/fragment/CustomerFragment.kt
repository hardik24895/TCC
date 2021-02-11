package com.tcc.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.Adapter.CustomerListAdapter
import com.tcc.app.R
import com.tcc.app.activity.CustomerDetailActivity
import com.tcc.app.extention.goToActivity
import com.tcc.app.extention.setHomeScreenTitle
import kotlinx.android.synthetic.main.reclerview_swipelayout.*


class CustomerFragment : BaseFragment(), CustomerListAdapter.OnItemSelected {

    var adapter: CustomerListAdapter? = null
    var customerArray: ArrayList<String>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.reclerview_swipelayout, container, false)


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  swipeRefreshLayout.isRefreshing = true
        setHomeScreenTitle(requireActivity(), getString(R.string.customer))



        customerArray = ArrayList()
        var layoutmanger = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutmanger

        adapter = CustomerListAdapter(requireContext(), customerArray!!, this)
        recyclerView.adapter = adapter

        swipeRefreshLayout.setOnRefreshListener {
            setCustomerData()
        }

    }


    private fun setCustomerData() {

        customerArray!!.clear()
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")
        customerArray?.add("")

    }

    override fun onItemSelect(position: Int, data: String) {
        goToActivity<CustomerDetailActivity>()
    }


    override fun onResume() {
        super.onResume()
        setCustomerData()
    }

}