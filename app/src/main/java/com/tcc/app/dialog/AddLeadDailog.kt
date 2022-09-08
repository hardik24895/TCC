package com.tcc.app.dialog

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.tcc.app.R
import com.tcc.app.extention.getValue
import com.tcc.app.extention.isEmpty
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.showSnackBar
import com.tcc.app.modal.ServiceDataItem
import com.tcc.app.modal.ServiceListModel
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
import kotlinx.android.synthetic.main.dialog_add_lead.*
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem


class AddLeadDailog(context: Context) : BlurDialogFragment(), LifecycleOwner {
    private val autoDisposable = AutoDisposable()
    private lateinit var session: SessionManager
    var serviceNameList: ArrayList<String> = ArrayList()
    var adapterService: ArrayAdapter<String>? = null
    var serviceListArray: ArrayList<ServiceDataItem> = ArrayList()
    var itemService: List<SearchableItem> = ArrayList()
    var serviceId: String = ""
    private val REQUEST_CONTACT = 201

    companion object {
        private lateinit var listener: onItemClick
        fun newInstance(
            context: Context,
            listeners: onItemClick
        ): AddLeadDailog {
            this.listener = listeners
            return AddLeadDailog(context)
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
        return inflater.inflate(R.layout.dialog_add_lead, container, false)
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        populateData()
        dialog?.setCancelable(true)
        dialog?.setCanceledOnTouchOutside(true)
        serviceNameList = ArrayList()
        serviceListArray = ArrayList()
        btnSubmit.setOnClickListener {
            validation()
        }

        btnClose.setOnClickListener {
            dismissAllowingStateLoss()
        }

        getServiceList()
        serviceSpinnerListner()
        serviceViewClick()


        edtPhone.setOnTouchListener(OnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= edtPhone.getRight() - edtPhone.getCompoundDrawables()
                        .get(DRAWABLE_RIGHT).getBounds().width()
                ) {

                    checkPermission()
                    return@OnTouchListener true
                }
            }
            false
        })


    }


    fun checkPermission() {
        askPermission(Manifest.permission.READ_CONTACTS) {
            val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            intent.type = CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(intent, REQUEST_CONTACT)
        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(requireContext()).setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    } //ask again
                    .show();
            }

            if (e.hasForeverDenied()) {
                AlertDialog.Builder(requireContext()).setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.goToSettings()
                    } //ask again
                    .show();
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        intent.type = CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(intent, REQUEST_CONTACT)

    }


    fun validation() {
        when {
            edtPhone.isEmpty() -> {
                root.showSnackBar("Enter Mobile Number")
                edtPhone.requestFocus()
            }
            edtPhone.getValue().length < 10 -> {
                root.showSnackBar("Enter Valid Mobile Number")
                edtPhone.requestFocus()
            }
            serviceId == "-1" -> {
                root.showSnackBar("Select Service")
                spService.requestFocus()
            }
            else -> {
                listener.onItemCLicked(edtPhone.getValue(), serviceId)
                dismissAllowingStateLoss()
            }

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
        fun onItemCLicked(mobile: String, serviceId: String)
    }

    interface onDissmiss {
        fun onDismiss()
    }

    private fun getServiceList() {
        var result = ""
        try {
            val jsonBody = JSONObject()
            result = Networking.setParentJsonData(Constant.METHOD_SERVICE_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(requireContext())
            .getServices()
            .getServiceList(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<ServiceListModel>() {
                override fun onSuccess(response: ServiceListModel) {
                    serviceListArray!!.addAll(response.data)
                    var myList: MutableList<SearchableItem> = mutableListOf()
                    serviceNameList!!.add("Select Service")
                    myList.add(
                        SearchableItem(
                            0,
                            "Select Service"
                        )
                    )

                    for (items in response.data.indices) {
                        serviceNameList!!.add(response.data.get(items).service.toString())
                        myList.add(
                            SearchableItem(
                                items.toLong() + 1,
                                serviceNameList.get(items + 1)
                            )
                        )

                    }
                    itemService = myList

                    adapterService = ArrayAdapter(
                        requireContext(),
                        R.layout.custom_spinner_item,
                        serviceNameList!!
                    )
                    spService.setAdapter(adapterService)

                }

                override fun onFailed(code: Int, message: String) {

                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))

                }

            }).addTo(autoDisposable)
    }

    fun serviceSpinnerListner() {
        spService.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && serviceListArray.size > position - 1) {
                    if (position == 0) {
                        serviceId = "-1"
                    } else {
                        serviceId = serviceListArray.get(position - 1).serviceID.toString()
                        Logger.d("serviceID : ", serviceId)
                    }

                }

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode === RESULT_OK) {
            // Check for the request code, we might be usign multiple startActivityForReslut
            when (requestCode) {
                REQUEST_CONTACT -> {
                    var cursor: Cursor? = null
                    try {
                        val uri: Uri = data?.data!!
                        cursor = requireActivity().getContentResolver().query(
                            uri,
                            arrayOf(CommonDataKinds.Phone.NUMBER),
                            null,
                            null,
                            null
                        )
                        if (cursor != null && cursor.moveToNext()) {
                            val phone = cursor.getString(0)
                            // Do something with phone

                            var selectedContact =
                                phone.replace(" ", "").replace("+91", "").replace("(", "")
                                    .replace("+", "")
                                    .replace(")", "").replace("*", "").replace("#", "")
                            if (selectedContact.length > 10) {
                                selectedContact = selectedContact.substring(1)

                            }

                            edtPhone.setText(selectedContact)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
            Log.d("MainActivity", "Failed to pick contact")
        }
    }

    fun serviceViewClick() {
        view5.setOnClickListener {
            if (itemService.size > 0)
                SearchableDialog(requireContext(),
                    itemService!!,
                    getString(R.string.select_service),
                    { item, _ ->
                        spService.setSelection(item.id.toInt())
                    }).show()
        }
    }
}








