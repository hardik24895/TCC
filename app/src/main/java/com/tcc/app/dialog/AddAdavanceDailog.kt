package com.tcc.app.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.CommonAddModal
import com.tcc.app.network.AutoDisposable
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.ui.dialog.ProgressDialog
import com.tcc.app.utils.BlurDialogFragment
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.TimeStamp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_add_advance.*
import org.json.JSONException
import org.json.JSONObject


class AddAdavanceDailog(context: Context) : BlurDialogFragment(), LifecycleOwner {
    private val autoDisposable = AutoDisposable()
    private lateinit var session: SessionManager
    var progressDialog: ProgressDialog? = null
    var empID: String = "0"

    companion object {
        lateinit var listener: onItemClick
        var context: Context? = null
        fun newInstance(
            context: Context,
            listeners: onItemClick
        ): AddAdavanceDailog {
            this.listener = listeners
            this.context = context
            return AddAdavanceDailog(context)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppTheme_Dialog_Custom)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        session = SessionManager(requireContext())
        autoDisposable.bindTo(this.lifecycle)
        return inflater.inflate(R.layout.dialog_add_advance, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateData()
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)
        rbReceived.isChecked = true
        edtDate.setText(getCurrentDate())
        btnSubmit.setOnClickListener {
            validation()
        }
        btn_close.setOnClickListener {
            dismissAllowingStateLoss()
        }
        edtDate.setOnClickListener { showDateTimePicker(requireActivity(), edtDate) }


    }

    fun validation() {
        when {
            edtAmount.isEmpty() -> {
                root.showSnackBar("Enter Amount")
            }
            else -> {
                view?.let { addAdvance(it) }
            }
        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    private fun populateData() {
        val bundle = arguments
        if (bundle != null) {
            //val title = bundle.getString(Constant.TITLE)
            empID = bundle.getString(Constant.DATA).toString()

        }
    }

    interface onItemClick {
        fun onItemCLicked()
    }

    interface onDissmiss {
        fun onDismiss()
    }

    fun showProgressbar(message: String? = getString(R.string.please_wait)) {
        hideProgressbar()
        progressDialog = ProgressDialog(requireContext(), message)
        progressDialog?.show()
    }

    fun hideProgressbar() {
        if (progressDialog != null && progressDialog?.isShowing!!) progressDialog!!.dismiss()
    }

    fun addAdvance(view: View) {
        showProgressbar()
        val selectedId: Int = rg.getCheckedRadioButtonId()
        val rbType = view.findViewById<View>(selectedId) as? RadioButton
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", empID)
            // jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("Amount", edtAmount.getValue())
            jsonBody.put("AdvanceDate", TimeStamp.formatDateFromString(edtDate.getValue()))
            jsonBody.put("Type", rbType?.text.toString())
            result = Networking.setParentJsonData(
                Constant.METHOD_ADD_ADVANCE,
                jsonBody
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(requireContext())
            .getServices()
            .AddCheckIn(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    hideProgressbar()
                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
                        listener.onItemCLicked()
                        dismissAllowingStateLoss()
                    } else {
                        showAlert(response.message.toString())
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                    showAlert(message)
                }
            }).addTo(autoDisposable)
    }
}








