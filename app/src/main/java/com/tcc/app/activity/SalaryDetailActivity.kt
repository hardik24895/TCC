package com.tcc.app.activity

import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.getValue
import com.tcc.app.extention.visible
import com.tcc.app.modal.SalaryDataItem
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.activity_salary_detail.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import java.text.DecimalFormat

class SalaryDetailActivity : BaseActivity() {
    var salaryDataIteam: SalaryDataItem? = null
    var df: DecimalFormat = DecimalFormat("##.##")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_salary_detail)

        if (intent.hasExtra(Constant.DATA)) {
            salaryDataIteam = intent.getSerializableExtra(Constant.DATA) as SalaryDataItem
        }


        txtMonthSalary.text =
            getString(R.string.RS) + " " + df.format(salaryDataIteam?.salary?.toBigDecimal())
        txtPayAmount.text = getString(R.string.RS) + " " + df.format(
            (salaryDataIteam?.present?.toBigDecimal()
                ?.times(salaryDataIteam?.rate?.toBigDecimal()!!))
        )

        txtTotalPresent.text = salaryDataIteam?.present + " Presents"

        txtPresentAmount.text =
            getString(R.string.RS) + " " + df.format((salaryDataIteam?.rate?.toDouble()))


        txtPenaltyAmount.text =
            getString(R.string.RS) + " " + df.format((salaryDataIteam?.penalty?.toDouble()))



        txtOverTimeAmount.text =
            getString(R.string.RS) + " " + df.format((salaryDataIteam?.rate?.toDouble())?.times((salaryDataIteam?.halfOverTime?.toDouble()!!) + (salaryDataIteam?.fullOverTime?.toDouble()!!)))

        textviewSemiBold2.text = salaryDataIteam?.salaryDate


        txtAdvanceAmount.text = getString(R.string.RS) + " " + df.format(
            (salaryDataIteam?.payAmount?.toBigDecimal()!! - ((salaryDataIteam?.present?.toBigDecimal()!! + salaryDataIteam?.halfDay?.toBigDecimal()!! + salaryDataIteam?.halfOverTime?.toBigDecimal()!! + salaryDataIteam?.fullOverTime?.toBigDecimal()!!)?.times(
                salaryDataIteam?.rate?.toBigDecimal()!!
            )!!))
        )
        txtAdvanceAmount.text = txtAdvanceAmount.getValue().replace("-", "")

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text =
            salaryDataIteam?.firstName.toString() + " " + salaryDataIteam?.lastName.toString()
    }

}