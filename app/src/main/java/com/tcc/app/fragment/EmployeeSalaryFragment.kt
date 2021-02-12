package com.tcc.app.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.R
import com.tcc.app.activity.SalaryDetailActivity
import com.tcc.app.adapter.SalaryAdapter
import com.tcc.app.dialog.AddAdavanceDailog
import com.tcc.app.dialog.AddSalaryDailog
import com.tcc.app.extention.goToActivity
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.fragment_employee_salary.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*


class EmployeeSalaryFragment : BaseFragment(), SalaryAdapter.OnItemSelected {
    var adapter: SalaryAdapter? = null
    lateinit var chipArray: ArrayList<String>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_employee_salary, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chipArray = ArrayList()

        txtAdavance.setOnClickListener { showAdavanceDialog() }
        txtSalary.setOnClickListener { showSalaryDialog() }

        setChipList()

    }

    private fun setChipList() {
        chipArray.add("Hair")
        chipArray.add("Massage")
        chipArray.add("Nail")
        chipArray.add("Spa")
        chipArray.add("Barber")
        chipArray.add("Training")
        chipArray.add("Makeup")
        chipArray.add("Hair Removel")
        chipArray.add("All")

        setupRecyclerViewMarchant()
    }

    fun setupRecyclerViewMarchant() {

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = SalaryAdapter(requireContext(), chipArray, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: String) {
        goToActivity<SalaryDetailActivity>()

    }

    fun showAdavanceDialog() {
        val dialog = AddAdavanceDailog.newInstance(requireContext(),
            object : AddAdavanceDailog.onItemClick {
                override fun onItemCLicked() {
                    // goToActivity<AddVisitorActivity>()
                }

            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, getString(R.string.app_name))
//        bundle.putString(
//            Constant.TEXT,
//            getString(R.string.msg_get_data_from_server)
//        )
        dialog.arguments = bundle
        dialog.show(childFragmentManager, "YesNO")
    }

    fun showSalaryDialog() {
        val dialog = AddSalaryDailog.newInstance(requireContext(),
            object : AddSalaryDailog.onItemClick {
                override fun onItemCLicked() {
                    // goToActivity<AddVisitorActivity>()
                }
            })
        val bundle = Bundle()
        bundle.putString(Constant.TITLE, getString(R.string.app_name))
//        bundle.putString(
//            Constant.TEXT,
//            getString(R.string.msg_get_data_from_server)
//        )
        dialog.arguments = bundle
        dialog.show(childFragmentManager, "YesNO")
    }
}