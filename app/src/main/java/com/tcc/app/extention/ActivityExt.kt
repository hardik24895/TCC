package com.tcc.app.extention

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.customview.TextviewBold
import com.tcc.app.utils.Constant
import java.text.SimpleDateFormat
import java.util.*


inline fun <reified T : Activity> AppCompatActivity.goToActivityAndClearTask() {
    val intent = Intent(this, T::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    Animatoo.animateCard(this)
    finish()
}

inline fun <reified T : Activity> Fragment.goToActivityAndClearTask() {
    val intent = Intent(context, T::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    activity?.finish()
}

inline fun <reified T : Activity> AppCompatActivity.goToActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
    Animatoo.animateCard(this)
}

inline fun <reified T : Activity> Fragment.goToActivity() {
    startActivity(Intent(activity, T::class.java))
    Animatoo.animateCard(activity)
}

inline fun <reified T : Activity> Fragment.goToActivityBundle(bundle: Bundle) {
    val intent = Intent(activity, T::class.java)
    intent.putExtra(Constant.DATA, bundle)
    startActivity(intent)
    Animatoo.animateCard(context)
}

fun AppCompatActivity.addFragments(fragments: List<Fragment>, containerId: Int) {
    fragments.forEach {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(containerId, it)
        ft.commitAllowingStateLoss()
    }
}

inline fun <reified T : Activity> AppCompatActivity.onBackPress() {
    Animatoo.animateSlideLeft(this)
    finish()
}

fun AppCompatActivity.replaceFragments(fragments: List<Fragment>, containerId: Int) {
    fragments.forEach {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(containerId, it)
        ft.commitAllowingStateLoss()
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, containerId: Int) {
    val ft = supportFragmentManager.beginTransaction()
    ft.replace(containerId, fragment)
    ft.commitAllowingStateLoss()
}

fun Fragment.replaceFragment(fragment: Fragment, containerId: Int) {
    val ft = fragmentManager?.beginTransaction()
    ft?.replace(containerId, fragment)
    ft?.commitAllowingStateLoss()
}

fun AppCompatActivity.addFragment(
    fragment: Fragment,
    containerId: Int,
    addToStack: Boolean = true
) {
    val ft = supportFragmentManager.beginTransaction()
    ft.add(containerId, fragment)
    if (addToStack) ft.addToBackStack(fragment.javaClass.name)
    ft.commitAllowingStateLoss()
}

fun Fragment.addFragment(fragment: Fragment, containerId: Int, addToStack: Boolean = true) {
    val ft = fragmentManager?.beginTransaction()
    ft?.add(containerId, fragment)
    if (addToStack) ft?.addToBackStack(fragment.javaClass.name)
    ft?.commitAllowingStateLoss()
}

fun AppCompatActivity.showFragment(fragment: Fragment) {
    val ft = supportFragmentManager.beginTransaction()
    ft.show(fragment)
    ft.commitAllowingStateLoss()
}

fun AppCompatActivity.hideFragment(fragment: Fragment) {
    val ft = supportFragmentManager.beginTransaction()
    ft.hide(fragment)
    ft.commitAllowingStateLoss()
}

fun statusbarColor(activity: AppCompatActivity, color: Int) {
    val window: Window = activity.getWindow()
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.getDecorView()
            .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        // finally change the color
        window.setStatusBarColor(color);
    }
}

fun Activity.setStatusBarNormal() {
    window.apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            statusBarColor = getColorCompat(R.color.colorPrimaryDark)
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
    }
}

fun setHomeScreenTitle(requireActivity: Activity, title: String) {
    val toolbar = requireActivity.findViewById<View>(R.id.toolbar)
    val tvTitle: TextviewBold = toolbar.findViewById(R.id.tvTitle)
    tvTitle.setText(title)
}


fun getCurrentDate(): String {

    var date: String? = ""
    var selectedMonth: String = ""
    var selectedDay: String = ""

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)


    if (day < 10) {
        selectedDay = "0" + day
    } else
        selectedDay = day.toString()


    if (month < 10) {
        selectedMonth = "0" + (month + 1)
    } else
        selectedMonth = month.toString()

    date = "" + selectedDay + "/" + selectedMonth + "/" + year

    return date.toString()
}


fun getCurrentDateTime(): String {

    val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    val currentDate = sdf.format(Date())

    return currentDate.toString()
}

fun getCurentTime(): String {
    val c = Calendar.getInstance()
    val hour = c.get(Calendar.HOUR_OF_DAY)
    val minute = c.get(Calendar.MINUTE)

    return hour.toString() + ":" + minute.toString()
}

fun showDateTimePicker(requireActivity: Activity, edittext: EditText) {

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        requireActivity, R.style.DialogTheme,
        { view, year, monthOfYear, dayOfMonth ->

            var selectedMonth: String = ""
            var selectedDay: String = ""
            if (dayOfMonth < 10) {
                selectedDay = "0" + dayOfMonth
            } else
                selectedDay = dayOfMonth.toString()


            if (monthOfYear < 10) {
                selectedMonth = "0" + (monthOfYear + 1)
            } else
                selectedMonth = monthOfYear.toString()

            edittext.setText("" + selectedDay + "/" + selectedMonth + "/" + year)
        },
        year,
        month,
        day
    )
    dpd.show()
    dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        .setTextColor(requireActivity.getColorCompat(R.color.colorPrimary))
    dpd.getButton(DatePickerDialog.BUTTON_POSITIVE)
        .setTextColor(requireActivity.getColorCompat(R.color.colorPrimary))
}


fun showPastDateTimePicker(requireActivity: Activity, edittext: EditText) {

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        requireActivity, R.style.DialogTheme,
        { view, year, monthOfYear, dayOfMonth ->

            var selectedMonth: String = ""
            var selectedDay: String = ""
            if (dayOfMonth < 10) {
                selectedDay = "0" + dayOfMonth
            } else
                selectedDay = dayOfMonth.toString()


            if (monthOfYear < 10) {
                selectedMonth = "0" + (monthOfYear + 1)
            } else
                selectedMonth = monthOfYear.toString()

            edittext.setText("" + selectedDay + "/" + selectedMonth + "/" + year)
        },
        year,
        month,
        day
    )
    dpd.getDatePicker().setMaxDate(c.getTimeInMillis())
    //  dpd.getDatePicker().setMinDate(c.getTimeInMillis())
    dpd.show()
    dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        .setTextColor(requireActivity.getColorCompat(R.color.colorPrimary))
    dpd.getButton(DatePickerDialog.BUTTON_POSITIVE)
        .setTextColor(requireActivity.getColorCompat(R.color.colorPrimary))
}


fun showNextFromStartDateTimePicker(
    requireActivity: Activity,
    edittext: EditText,
    startDate: String
) {

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val dpd = DatePickerDialog(
        requireActivity, R.style.DialogTheme,
        { view, year, monthOfYear, dayOfMonth ->

            var selectedMonth: String = ""
            var selectedDay: String = ""
            if (dayOfMonth < 10) {
                selectedDay = "0" + dayOfMonth
            } else
                selectedDay = dayOfMonth.toString()


            if (monthOfYear < 10) {
                selectedMonth = "0" + (monthOfYear + 1)
            } else
                selectedMonth = monthOfYear.toString()

            edittext.setText("" + selectedDay + "/" + selectedMonth + "/" + year)
        },
        year,
        month,
        day
    )

    val f = SimpleDateFormat("dd/MM/yyyy")
    val d = f.parse(startDate)

    dpd.getDatePicker().setMinDate(d.time)

    dpd.show()
    dpd.getButton(DatePickerDialog.BUTTON_NEGATIVE)
        .setTextColor(requireActivity.getColorCompat(R.color.colorPrimary))
    dpd.getButton(DatePickerDialog.BUTTON_POSITIVE)
        .setTextColor(requireActivity.getColorCompat(R.color.colorPrimary))
}

fun Context.getColorCompat(@ColorRes colorId: Int) = ContextCompat.getColor(this, colorId)

