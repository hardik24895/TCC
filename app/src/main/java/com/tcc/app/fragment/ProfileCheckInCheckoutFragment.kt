package com.tcc.app.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.R
import com.tcc.app.adapter.CheckInOutAdapter
import com.tcc.app.extention.invisible
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.visible
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.CheckInOutDataItem
import com.tcc.app.modal.CheckInOutListModel
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
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


    var latitude: String? = ""
    var longitude: String? = ""

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

        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION
            )
        } else {
            locationManager =
                requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                //Write Function To enable gps
                gpsEnable()
            } else {
                //GPS is already On then
                getLocation()
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
            val LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val LocationNetwork =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            val LocationPassive =
                locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
            if (LocationGps != null) {
                val lat = LocationGps.latitude
                val longi = LocationGps.longitude
                latitude = lat.toString()
                longitude = longi.toString()
            } else if (LocationNetwork != null) {
                val lat = LocationNetwork.latitude
                val longi = LocationNetwork.longitude
                latitude = lat.toString()
                longitude = longi.toString()
            } else if (LocationPassive != null) {
                val lat = LocationPassive.latitude
                val longi = LocationPassive.longitude
                latitude = lat.toString()
                longitude = longi.toString()
            } else {
                Toast.makeText(activity, "Can't Get Your Location", Toast.LENGTH_SHORT).show()
            }
            showAlert("$latitude    $longitude")

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
}