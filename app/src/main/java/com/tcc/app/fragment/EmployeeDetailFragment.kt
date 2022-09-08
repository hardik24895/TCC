package com.tcc.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tcc.app.R
import com.tcc.app.activity.FullscreenSingleImageActivity
import com.tcc.app.extention.invisible
import com.tcc.app.extention.visible
import com.tcc.app.modal.EmployeeDataItem
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.fragment_employee_detail.*


class EmployeeDetailFragment() : BaseFragment() {


    var empItemData: EmployeeDataItem? = null

    constructor(empData: EmployeeDataItem?) : this() {
        this.empItemData = empData
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_employee_detail, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!session.roleData.data?.employee?.isView.toString().equals("1")) {

            txtNoRight.visible()
            crd_data.invisible()

        } else {
            txtNoRight.invisible()
            crd_data.visible()

            txtName.text = empItemData?.firstName + " " + empItemData?.lastName
            if (!empItemData?.mobileNo.equals(""))
                txtMobile.text = empItemData?.mobileNo
            else
                txtMobile.text = "N/A"

            if (!empItemData?.joiningDate.equals(""))
                txtJoin.text = empItemData?.joiningDate
            else
                txtJoin.text = "N/A"

            // if (!empItemData?.salary.equals(""))
            //     txtPerDaySalary.text = empItemData?.salary
            // else
            //     txtPerDaySalary.text = "N/A"

            if (!empItemData?.salary.equals(""))
                txtSalary.text = empItemData?.salary
            else
                txtSalary.text = "N/A"

            if (!empItemData?.workingHours.equals(""))
                txtWorkingHRS.text = empItemData?.workingHours
            else
                txtWorkingHRS.text = "N/A"

            if (!empItemData?.address.equals(""))
                txtAddress.text = empItemData?.address
            else
                txtAddress.text = "N/A"

            if (!empItemData?.bankName.equals(""))
                txtBankName.text = empItemData?.bankName
            else
                txtBankName.text = "N/A"

            if (!empItemData?.branchName.equals(""))
                txtBranchName.text = empItemData?.branchName
            else
                txtBranchName.text = "N/A"

            if (!empItemData?.accountNo.equals(""))
                txtAccountNumber.text = empItemData?.accountNo
            else
                txtAccountNumber.text = "N/A"

            if (!empItemData?.iFSCCode.equals(""))
                txtIFSC.text = empItemData?.iFSCCode
            else
                txtIFSC.text = "N/A"

            if (!empItemData?.usertype.equals(""))
                txtUserType.text = empItemData?.usertype
            else
                txtUserType.text = "N/A"

            if (!empItemData?.cityName.equals(""))
                txtCity.text = empItemData?.cityName
            else
                txtCity.text = "N/A"


            ivProfile.setOnClickListener {

                val intent = Intent(requireContext(), FullscreenSingleImageActivity::class.java)
                intent.putExtra(Constant.DATA, Constant.EMP_PROFILE + empItemData?.profilePic)
                startActivity(intent)
            }
            imgAdhaarCard.setOnClickListener {

                val intent = Intent(requireContext(), FullscreenSingleImageActivity::class.java)
                intent.putExtra(Constant.DATA, Constant.USERDOCUMENT_URL + empItemData?.documents)
                startActivity(intent)
            }


            Glide.with(this).load(Constant.EMP_PROFILE + empItemData?.profilePic)
                .placeholder(R.drawable.ic_profile).into(ivProfile)


            Glide.with(this)
                .load(Constant.USERDOCUMENT_URL + empItemData?.documents)
                .apply(
                    com.bumptech.glide.request.RequestOptions().centerCrop()
                        .placeholder(R.drawable.ic_profile)
                )
                .into(imgAdhaarCard)


        }
    }


}