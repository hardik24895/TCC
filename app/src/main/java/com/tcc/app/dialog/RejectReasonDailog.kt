package com.tcc.app.dialog

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.tcc.app.R
import com.tcc.app.extention.showAlert
import com.tcc.app.modal.RejectReasonDataItem
import com.tcc.app.modal.RejectReasonListModel
import com.tcc.app.network.AutoDisposable
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.BlurDialogFragment
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Logger
import com.tcc.app.utils.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.dialog_reject_reason.*
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem


class RejectReasonDailog(context: Context) : BlurDialogFragment(), LifecycleOwner {
    private val autoDisposable = AutoDisposable()
    private lateinit var session: SessionManager
    var ReasonID: String = ""
    var ReasonNameList: ArrayList<String> = ArrayList()
    var ReasonListArray: ArrayList<RejectReasonDataItem> = ArrayList()
    var adapterReason: ArrayAdapter<String>? = null
    var ReasonIteams: List<SearchableItem>? = null

    companion object {
        private lateinit var listener: onItemClick
        fun newInstance(
            context: Context,
            listeners: onItemClick
        ): RejectReasonDailog {
            this.listener = listeners
            return RejectReasonDailog(context)
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
        return inflater.inflate(R.layout.dialog_reject_reason, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateData()
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)


        getReasonList()
        ReasonViewClick()
        ReasonSpinnerListner()

        btnSubmit.setOnClickListener {
            listener.onItemCLicked(ReasonID)
            dismissAllowingStateLoss()
        }


    }

    fun validation() {
        when {
            // edtPhone.isEmpty() -> {
            //     root.showSnackBar("Enter Mobile Number")
            //     edtPhone.requestFocus()
            // }
            // edtPhone.getValue().length < 10 -> {
            //     root.showSnackBar("Enter Valid Mobile Number")
            //     edtPhone.requestFocus()
            // }
            // else -> {
            //     listener.onItemCLicked(edtPhone.getValue())
            //     dismissAllowingStateLoss()
            // }

        }
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
    }

    private fun populateData() {
        val bundle = arguments
        if (bundle != null) {
            /* val title = bundle.getString(Constant.TITLE)
             val text = bundle.getString(Constant.TEXT)
             txtTitle.text = title
             tvText.text = text*/
        }
    }

    interface onItemClick {
        fun onItemCLicked(mobile: String)
    }

    interface onDissmiss {
        fun onDismiss()
    }


    fun getReasonList() {
        //     showProgressbar()
        ReasonListArray.clear()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("ReasonType", "Quotation")

            result = Networking.setParentJsonData(Constant.METHOD_GET_REASON, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(requireContext())
            .getServices()
            .getRejectReasonList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<RejectReasonListModel>() {
                override fun onSuccess(response: RejectReasonListModel) {
                    //     hideProgressbar()

                    ReasonListArray?.addAll(response.data)
                    var myList: MutableList<SearchableItem> = mutableListOf()

                    for (items in response.data.indices) {
                        ReasonNameList.add(response.data.get(items).reason.toString())
                        myList.add(SearchableItem(items.toLong(), ReasonNameList.get(items)))
                    }
                    ReasonIteams = myList

                    adapterReason =
                        ArrayAdapter(requireContext(), R.layout.custom_spinner_item, ReasonNameList)
                    spReason.setAdapter(adapterReason)

                }

                override fun onFailed(code: Int, message: String) {
                    showAlert(message)
                }

            }).addTo(autoDisposable)
    }

    fun ReasonViewClick() {
        view1.setOnClickListener {

            SearchableDialog(requireContext(),
                ReasonIteams!!,
                getString(R.string.select_reason),
                { item, _ ->
                    spReason.setSelection(item.id.toInt())
                }).show()
        }
    }

    fun ReasonSpinnerListner() {
        spReason.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && ReasonListArray.size > position) {
                    ReasonID = ReasonListArray.get(position).reasonID.toString()
                    Logger.d("companyID", ReasonID)
                }

            }
        }
    }

}








