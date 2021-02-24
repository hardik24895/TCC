package com.tcc.app.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
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
import com.tcc.app.utils.GPSTracker
import com.tcc.app.utils.SessionManager
import com.tcc.app.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_checkinout.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*
import org.json.JSONException
import org.json.JSONObject


class ProfileCheckInCheckoutFragment : BaseFragment(), CheckInOutAdapter.OnItemSelected {
    private val REQUEST_LOCATION = 1
    private val list: MutableList<CheckInOutDataItem> = mutableListOf()
    var page: Int = 1
    var hasNextPage: Boolean = true
    var adapter: CheckInOutAdapter? = null

    private lateinit var locationManager: LocationManager

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

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getCheckinoutList(page)
        }


        if (session.getDataByKey(SessionManager.KEY_CHECKIN_ID).equals("")) {
            btnCHeckout.text = "Check In"
        } else {
            btnCHeckout.text = "Check Out"
        }
        btnCHeckout.setOnClickListener {

            if (session.getDataByKey(SessionManager.KEY_CHECKIN_ID).equals("")) {
                CheckInApi()
            } else {
                CheckOutApi()
            }

        }

    }

    private fun checkLocationPermission() {

        askPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) {
            // locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (Utils.displayGpsStatus(requireContext())) {

                gpsEnable()
            } else {
                //GPS is already On then
                getLocation()
            }
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

    private fun gpsEnable() {
        val builder = AlertDialog.Builder(requireActivity())
        builder.setMessage("Enable GPS")
            .setCancelable(false)
            .setPositiveButton("YES")
            { dialog, which ->
                requireActivity().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }

        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = CheckInOutAdapter(requireContext(), list, this)
        recyclerView.adapter = adapter

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

                    refreshData(getString(R.string.no_data_found))
                }

                override fun onFailed(code: Int, message: String) {
                    if (list.size > 0) {
                        progressbar.invisible()
                    }
                    showAlert(message)
                    refreshData(message)
                }

            }).addTo(autoDisposable)
    }

    private fun refreshData(msg: String?) {
        recyclerView.setLoadedCompleted()
        swipeRefreshLayout.isRefreshing = false
        adapter?.notifyDataSetChanged()

        if (list.size > 0) {
            tvInfo.invisible()
            recyclerView.visible()
        } else {
            tvInfo.text = msg
            tvInfo.visible()
            recyclerView.invisible()
        }
    }

    override fun onResume() {
        page = 1
        list.clear()
        checkLocationPermission()
        hasNextPage = true
        swipeRefreshLayout.isRefreshing = true
        setupRecyclerView()
        recyclerView.isLoading = true
        getCheckinoutList(page)
        super.onResume()
    }

    override fun onItemSelect(position: Int, data: CheckInOutDataItem) {

    }


    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
        } else {
            var gpsTraker = GPSTracker(requireContext())
            gpsTraker.latitude
            gpsTraker.longitude

            showAlert("${gpsTraker.latitude}  ${gpsTraker.longitude}")

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        checkLocationPermission()
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        checkLocationPermission()
    }


    fun CheckInApi() {
        showProgressbar()
        var result = ""
        try {
            val jsonBody = JSONObject()

            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("Checkintime", getCurrentDateTime())
            jsonBody.put("Inlatitude", "")
            jsonBody.put("Inlongitude", "")
            jsonBody.put("InAddress", "")


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


    fun CheckOutApi() {
        showProgressbar()
        var result = ""
        try {
            val jsonBody = JSONObject()

            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("Checkouttime", getCurrentDateTime())
            jsonBody.put("Outlatitude", "")
            jsonBody.put("Outlongitude", "")
            jsonBody.put("CheckincheckoutID", session.getDataByKey(SessionManager.KEY_CHECKIN_ID))
            jsonBody.put("OutAddress", "")


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
                        session.storeDataByKey(
                            SessionManager.KEY_CHECKIN_ID, ""
                        )
                        btnCHeckout.text = "Check In"
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