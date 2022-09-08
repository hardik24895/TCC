package com.tcc.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tcc.app.R
import com.tcc.app.modal.CustomerDataItem
import kotlinx.android.synthetic.main.fragment_customer_info.*


class CustomerInfoFragment() : BaseFragment() {

    var customerdata: CustomerDataItem? = null
    constructor(customerData: CustomerDataItem?) : this() {
        this.customerdata = customerData
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_customer_info, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtName.text = customerdata?.name
        txtMobile.text = customerdata?.mobileNo
        txtAddress.text = customerdata?.address
        txtEmail.text = customerdata?.emailID

    }

}