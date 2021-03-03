package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.visible
import com.tcc.app.modal.SalaryDataItem
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.activity_salary_detail.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import java.text.DecimalFormat

class SalaryDetailActivity : BaseActivity() {
    var salaryDataIteam: SalaryDataItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_salary_detail)

        if (intent.hasExtra(Constant.DATA)) {
            salaryDataIteam = intent.getSerializableExtra(Constant.DATA) as SalaryDataItem
        }


        txtMonthSalary.text = getString(R.string.RS) + " " + salaryDataIteam?.salary
        txtPayAmount.text = getString(R.string.RS) + " " + (salaryDataIteam?.present?.toDouble()
            ?.times(salaryDataIteam?.rate?.toDouble()!!)).toString()

        txtTotalPresent.text = salaryDataIteam?.present + " Presents"

        txtPresentAmount.text = getString(R.string.RS) + " " + (salaryDataIteam?.rate?.toDouble())


        var df: DecimalFormat = DecimalFormat("##.##")
        txtOverTimeAmount.text =
            getString(R.string.RS) + " " + df.format((salaryDataIteam?.rate?.toDouble())?.times((salaryDataIteam?.halfOverTime?.toDouble()!!) + (salaryDataIteam?.fullOverTime?.toDouble()!!)))

        textviewSemiBold2.text = salaryDataIteam?.salaryDate

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text =
            salaryDataIteam?.firstName.toString() + " " + salaryDataIteam?.lastName.toString()
    }

}