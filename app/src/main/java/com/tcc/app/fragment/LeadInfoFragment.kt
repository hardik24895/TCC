package com.tcc.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tcc.app.R
import com.tcc.app.modal.LeadItem
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.fragment_lead_detail.*


class LeadInfoFragment : BaseFragment() {

    var leadItem: LeadItem? = null

    companion object {
        fun getInstance(bundle: Bundle): LeadInfoFragment {
            val fragment = LeadInfoFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun getBundleData() {
        val bundle = arguments
        if (bundle != null) {
            leadItem = bundle.getSerializable(Constant.DATA) as LeadItem
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_lead_detail, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundleData()
        setData()


    }

    fun setData() {
        txtName.text = leadItem?.name.toString()
        txtEmail.text = leadItem?.emailID.toString()
        txtMobile.text = leadItem?.mobileNo.toString()
        txtCity.text = leadItem?.cityName.toString()
        txtPincode.text = leadItem?.pinCode.toString()
        txtAddress.text = leadItem?.address.toString()
        txtLeadType.text = leadItem?.leadType.toString()
    }


}