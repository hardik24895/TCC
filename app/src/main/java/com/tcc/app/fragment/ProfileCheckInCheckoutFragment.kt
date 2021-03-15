package com.tcc.app.fragment

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.tcc.app.R
import com.tcc.app.adapter.CheckInOutAdapter
import com.tcc.app.extention.*
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.CheckInOutDataItem
import com.tcc.app.modal.CheckInOutListModel
import com.tcc.app.modal.CommonAddModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.GpsTracker
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.TimeStamp.getTimeFromCheckInOUtTime
import com.tcc.app.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_checkinout.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import org.json.JSONException
import org.json.JSONObject


class ProfileCheckInCheckoutFragment : BaseFragment(), CheckInOutAdapter.OnItemSelected,
    LocationListener {
    private val REQUEST_LOCATION = 1
    private val list: MutableList<CheckInOutDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true
    var adapter: CheckInOutAdapter? = null

    private var locationManager: LocationManager? = null


    var latitude: Double? = 0.0
    var longitude: Double? = 0.0
    var isBtnClick: Boolean = false
    var address: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_checkinout, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getCheckinoutList(++page)
                }
            }
        })

        txtDate.append(" : ${getCurrentDate()}")

        isBtnClick = false
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getCheckinoutList(page)
        }

//        if (session.getDataByKey(SessionManager.KEY_CHECKIN_ID).equals("")) {
//            btnCHeckout.text = "Check In"
//        } else {
//            btnCHeckout.text = "Check Out"
//        }
        btnCHeckout.setOnClickListener {
            isBtnClick = true
            checkPermission()
        }
        checkPermission()
    }


    fun setCurrentLocation() {
        if (Utils.displayGpsStatus(requireContext())) {
            getCurrentLocation()
        } else {
            alertbox("Gps Status", "Your Device's GPS is Disable")
        }
    }

    /*----------Method to create an AlertBox ------------- */
    protected fun alertbox(title: String?, mymessage: String?) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Your Device's GPS is Disable")
            .setCancelable(false)
            .setTitle("** Gps Status **")
            .setPositiveButton("Gps On",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        // finish the current activity
                        // AlertBoxAdvance.this.finish();
                        val myIntent = Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        )
                        startActivity(myIntent)
                        dialog.cancel()
                    }
                })
            .setNegativeButton("Cancel",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, id: Int) {
                        // cancel the dialog box
                        dialog.cancel()
                    }
                })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun checkPermission() {
        askPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) {
            //getLocation()
            setCurrentLocation()
            //Request location updates:
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


    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = CheckInOutAdapter(requireContext(), list, this)
        recyclerView.adapter = adapter

    }

    fun callCheckInOutAPi() {
        page = 1
        list.clear()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getCheckinoutList(page)
    }


    fun getCheckinoutList(page: Int) {
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("UserID", session.user.data?.userID)
            result = Networking.setParentJsonData(
                Constant.METHOD_GET_CHECK_IN_OUT,
                jsonBody
            )

        } catch (e: JSONException) {
            e.printStackTrace()
        }

        Networking
            .with(requireContext())
            .getServices()
            .getCheckInOutList(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CheckInOutListModel>() {
                override fun onSuccess(response: CheckInOutListModel) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    swipeRefreshLayout.isRefreshing = false
                    list.addAll(response.data)
                    adapter?.notifyItemRangeInserted(
                        list.size.minus(response.data.size),
                        list.size
                    )
                    hasNextPage = list.size < response.rowcount!!

                    if (response.checkintime.equals("") && response.checkouttime.equals("")) {
                        txtCheckInTime.invisible()
                        txtCheckOutTime.invisible()
                        btnCHeckout.visible()
                        btnCHeckout.text = "Check In"
                    } else if (!response.checkintime.equals("") && response.checkouttime.equals("")) {
                        txtCheckInTime.text =
                            getTimeFromCheckInOUtTime(response.checkintime.toString())
                        txtCheckInTime.visible()
                        txtCheckOutTime.invisible()
                        btnCHeckout.visible()
                        btnCHeckout.text = "Check Out"
                    } else {
                        txtCheckInTime.text =
                            getTimeFromCheckInOUtTime(response.checkintime.toString())
                        txtCheckOutTime.text =
                            getTimeFromCheckInOUtTime(response.checkouttime.toString())
                        btnCHeckout.invisible()
                        txtCheckInTime.visible()
                        txtCheckOutTime.visible()
                    }

                    refreshData(getString(R.string.no_data_found), 1)
                }

                override fun onFailed(code: Int, message: String) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                    refreshData(message, code)
                }

            }).addTo(autoDisposable)
    }

    private fun refreshData(msg: String?, code: Int) {
        recyclerView.setLoadedCompleted()
        swipeRefreshLayout.isRefreshing = false
        adapter?.notifyDataSetChanged()

        if (list.size > 0) {
            imgNodata.invisible()
            recyclerView.visible()
        } else {
            imgNodata.visible()
            if (code == 0)
                imgNodata.setImageResource(R.drawable.no_internet_bg)
            else
                imgNodata.setImageResource(R.drawable.nodata)
            recyclerView.invisible()
        }
    }

    override fun onResume() {
        callCheckInOutAPi()
        super.onResume()
    }

    override fun onItemSelect(position: Int, data: CheckInOutDataItem) {

    }


    fun getCurrentLocation() {

        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // getLocation();
        var gpsTracker = GpsTracker(requireContext())
        if (gpsTracker.canGetLocation()) {
            latitude = gpsTracker.getLatitude()
            longitude = gpsTracker.getLongitude()
            address = gpsTracker.getAddress()

            if (latitude != 0.0 && longitude != 0.0) {
                if (isBtnClick) {
                    if (session.getDataByKey(SessionManager.KEY_CHECKIN_ID).equals("")) {
                        CheckInApi()
                    } else {
                        CheckOutApi()
                    }
                }

            } else {
                showAlert(latitude.toString() + " " + longitude.toString())
                Toast.makeText(requireContext(), "Getting Location...", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            gpsTracker.showSettingsAlert()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        checkPermission()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        checkPermission()
    }

    override fun onLocationChanged(location: Location) {
        showAlert(location.latitude.toString() + " " + location.longitude.toString())
    }

    fun CheckInApi() {
        showProgressbar()
        var currentTime = getCurrentDateTime()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("Checkintime", currentTime)
            jsonBody.put("Inlatitude", latitude)
            jsonBody.put("Inlongitude", longitude)
            jsonBody.put("InAddress", address)
            result = Networking.setParentJsonData(
                Constant.METHOD_ADD_CHECK_IN,
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
                        session.storeDataByKey(
                            SessionManager.KEY_CHECKIN_ID,
                            response.data.get(0).iD.toString()
                        )

                        btnCHeckout.text = "Check Out"
                        callCheckInOutAPi()


                    } else {
                        showAlert(response.message.toString())
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                }
            }).addTo(autoDisposable)
    }

    fun CheckOutApi() {
        showProgressbar()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("Checkouttime", getCurrentDateTime())
            jsonBody.put("Outlatitude", latitude)
            jsonBody.put("Outlongitude", longitude)
            jsonBody.put("CheckincheckoutID", session.getDataByKey(SessionManager.KEY_CHECKIN_ID))
            jsonBody.put("OutAddress", address)
            result = Networking.setParentJsonData(
                Constant.METHOD_ADD_CHECK_OUT,
                jsonBody
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(requireContext())
            .getServices()
            .AddCheckOut(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<CommonAddModal>() {
                override fun onSuccess(response: CommonAddModal) {
                    hideProgressbar()
                    if (response.error == 200) {
                        root.showSnackBar(response.message.toString())
                        session.storeDataByKey(SessionManager.KEY_CHECKIN_ID, "")
                        btnCHeckout.text = "Check In"
                        callCheckInOutAPi()
                    } else {
                        showAlert(response.message.toString())
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    hideProgressbar()
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                }
            }).addTo(autoDisposable)
    }
}