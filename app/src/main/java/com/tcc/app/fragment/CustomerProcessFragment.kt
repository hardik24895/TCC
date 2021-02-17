package com.tcc.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.R
import com.tcc.app.adapter.ProcessAdapter
import com.tcc.app.modal.CustomerDataItem
import kotlinx.android.synthetic.main.reclerview_swipelayout.*


class CustomerProcessFragment() : BaseFragment(), ProcessAdapter.OnItemSelected {
    constructor(customerData: CustomerDataItem?) : this() {

    }

    var adapter: ProcessAdapter? = null
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


        customerArray = ArrayList()
        var layoutmanger = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutmanger

        adapter = ProcessAdapter(requireContext(), customerArray!!, this)
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
        adapter?.notifyDataSetChanged()

    }

    override fun onItemSelect(position: Int, data: String) {
        //  goToActivity<CustomerDetailActivity>()
    }


    override fun onResume() {
        super.onResume()
        setCustomerData()
    }


}