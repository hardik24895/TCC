package com.tcc.app.fragment

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.tcc.app.R
import com.tcc.app.extention.dismissAlertDialog
import com.tcc.app.interfaces.SnackbarActionListener
import com.tcc.app.network.AutoDisposable
import com.tcc.app.ui.dialog.ProgressDialog
import com.tcc.app.utils.SessionManager


/**
 * Created by Hardik
 */

open class BaseFragment : Fragment() {
    var mContext: Context? = null
    var mActivity: Activity? = null
    private var lastClickTime: Long = 0
    lateinit var session: SessionManager
    var progressDialog: ProgressDialog? = null
    val autoDisposable = AutoDisposable()
    var snackbar: Snackbar? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
        mActivity = activity
        session = SessionManager(context)
        autoDisposable.bindTo(this.lifecycle)
    }

    /* fun isThisMe(userId: String?): Boolean {
         return session.user != null && userId == session.user?._id
     }*/

    fun showProgressbar() {
        showProgressbar(null)
    }

    fun showProgressbar(message: String? = getString(R.string.please_wait)) {
        hideProgressbar()
        progressDialog = ProgressDialog(mContext!!, message)
        progressDialog?.show()
    }

    fun hideProgressbar() {
        if (progressDialog != null && progressDialog?.isShowing!!) progressDialog!!.dismiss()
    }

    fun showSoftKeyboard(view: EditText) {
        view.requestFocus(view.text.length)
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideSoftKeyboard(): Boolean {
        try {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            return imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
        } catch (e: Exception) {
            return false
        }
    }

    /* fun setupToolbar(view: View, title: String? = null) {
         (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.toolbar))
         val actionBar = (activity as AppCompatActivity).supportActionBar
         if (actionBar != null) {
             actionBar.setDisplayShowTitleEnabled(false)
             actionBar.setDisplayHomeAsUpEnabled(false)
             if (title != null) tvTitle.text = title
         }
     }

     fun setupToolBarWithMenu(view: View, title: String, icon: Int = R.drawable.v_ic_menu) {
         (activity as AppCompatActivity).setSupportActionBar(view.findViewById(R.id.toolbar))
         val actionBar = (activity as AppCompatActivity).supportActionBar
         if (actionBar != null) {
             actionBar.setDisplayShowTitleEnabled(false)
             actionBar.setDisplayHomeAsUpEnabled(true)
             actionBar.setHomeAsUpIndicator(icon)
             tvTitle.text = title
         }
     }*/

    fun showSnackbar(view: View?, msg: String, LENGTH: Int) {
        if (view == null) return
        snackbar = Snackbar.make(view, msg, LENGTH)
        val sbView = snackbar?.view
        val textView =
            sbView?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView?.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorAccent))
        snackbar?.show()
    }

    fun showSnackbar(
        view: View?,
        msg: String,
        LENGTH: Int,
        action: String,
        actionListener: SnackbarActionListener?
    ) {
        if (view == null) return
        snackbar = Snackbar.make(view, msg, LENGTH)
        snackbar?.setActionTextColor(ContextCompat.getColor(mContext!!, R.color.colorAccent))
        if (actionListener != null) {
            snackbar?.setAction(action) { view1 ->
                snackbar?.dismiss()
                actionListener.onAction()
            }
        }
        val sbView = snackbar?.view
        val textView =
            sbView?.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView?.setTextColor(ContextCompat.getColor(mContext!!, R.color.colorAccent))
        snackbar?.show()
    }

    fun preventDoubleClick(view: View) {
        // preventing double, using threshold of 1000 ms
        if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
    }

    companion object {
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onDestroy() {
        snackbar?.dismiss()
        dismissAlertDialog()
        hideProgressbar()
        super.onDestroy()
    }
}