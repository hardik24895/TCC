package com.tcc.app.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.R
import com.tcc.app.adapter.AttendanceAdapter
import com.tcc.app.dialog.AttendanceBottomSheetDialog
import com.tcc.app.dialog.LateFineBottomSheetDialog
import com.tcc.app.dialog.OverTimeBottomSheetDialog
import com.tcc.app.extention.invisible
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.visible
import com.tcc.app.modal.QuotationItem
import com.tcc.app.utils.Constant
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener
import kotlinx.android.synthetic.main.activity_attendance.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*
import java.util.*
import kotlin.collections.ArrayList

class AddAttendanceActivity : BaseActivity(), AttendanceAdapter.OnItemSelected {

    var adapter: AttendanceAdapter? = null
    var quotationItem: QuotationItem? = null
    lateinit var chipArray: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_attendance)

        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }
        txtTitle.text = "Attendance"

        chipArray = ArrayList()
        setChipList()

        horizontalCalender()

        if (intent.hasExtra(Constant.DATA)) {
            quotationItem = intent.getSerializableExtra(Constant.DATA) as QuotationItem
            calendarView.invisible()
        } else {
            calendarView.visible()
        }
    }

    fun horizontalCalender() {


        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -1)
        val endDate: Calendar = Calendar.getInstance()

        val horizontalCalendar: HorizontalCalendar =
            HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(7)
                .build()
        horizontalCalendar.calendarListener = object : HorizontalCalendarListener() {
            override fun onDateSelected(date: Calendar?, position: Int) {
                //do something
            }
        }
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

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = AttendanceAdapter(this, chipArray, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: String) {
        showBottomSheetDialog()
    }

    fun showBottomSheetDialog() {
        val dialog = AttendanceBottomSheetDialog
            .newInstance(
                this,
                object : AttendanceBottomSheetDialog.OnItemClick {
                    override fun onItemClicked(message: String) {
                        if (Constant.OVERTIME.equals(message)) {
                            showOverTimeDialog()
                        } else if (Constant.LATEFINE.equals(message)) {
                            showLateFineDialog()
                        }
                    }


                    override fun onError(message: String) {
                        showAlert(message)
                    }
                })
        dialog.show(supportFragmentManager, "ImagePicker")

    }

    fun showOverTimeDialog() {
        val dialog = OverTimeBottomSheetDialog
            .newInstance(
                this,
                object : OverTimeBottomSheetDialog.OnItemClick {
                    override fun onItemClicked(message: String) {
                        if (Constant.OVERTIME.equals(message)) {

                        }
                    }


                    override fun onError(message: String) {
                        showAlert(message)
                    }
                })
        dialog.show(supportFragmentManager, "ImagePicker")
    }

    fun showLateFineDialog() {
        val dialog = LateFineBottomSheetDialog
            .newInstance(
                this,
                object : LateFineBottomSheetDialog.OnItemClick {
                    override fun onItemClicked(message: String) {
                        if (Constant.OVERTIME.equals(message)) {

                        }
                    }


                    override fun onError(message: String) {
                        showAlert(message)
                    }
                })
        dialog.show(supportFragmentManager, "ImagePicker")
    }
}