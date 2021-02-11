package com.tcc.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.Adapter.ProcessAdapter
import com.tcc.app.R
import kotlinx.android.synthetic.main.reclerview_swipelayout.*


class CustomerProcessFragment : Fragment(), ProcessAdapter.OnItemSelected {

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