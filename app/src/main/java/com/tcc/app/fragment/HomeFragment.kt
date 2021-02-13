package com.tcc.app.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.tcc.app.R
import com.tcc.app.adapter.AutoImageSliderAdapter
import com.tcc.app.adapter.HomeServiceAdapter
import com.tcc.app.extention.setHomeScreenTitle
import com.tcc.app.extention.showAlert
import com.tcc.app.modal.CityDataItem
import com.tcc.app.modal.CityListModel
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import com.tcc.app.utils.SessionManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException
import org.json.JSONObject
import tech.hibk.searchablespinnerlibrary.SearchableDialog
import tech.hibk.searchablespinnerlibrary.SearchableItem

class HomeFragment : BaseFragment(), AutoImageSliderAdapter.OnItemSelected,
        HomeServiceAdapter.OnItemSelected {
    var adapter1: HomeServiceAdapter? = null
    lateinit var chipArray: ArrayList<String>
    lateinit var cityNameList: ArrayList<String>
    lateinit var cityListArray: ArrayList<CityDataItem>
    var adapterCity: ArrayAdapter<String>? = null
    var itens: List<SearchableItem>? = null
    var autoImageSliderAdapter: AutoImageSliderAdapter? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHomeScreenTitle(requireActivity(), getString(R.string.menu_home))
        chipArray = ArrayList()
        cityListArray = ArrayList()
        cityNameList = ArrayList()
        getCityList()
        setChipList()
        setuprvHomeCounterMarchant()


        spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {
                if (position != -1 && cityListArray.size > position) {
                    session.storeDataByKey(SessionManager.KEY_CITY_ID, cityListArray.get(position).cityID.toString())
                }

            }
        }

        view2.setOnClickListener {

            SearchableDialog(requireContext(),
                    itens!!,
                    getString(R.string.select_city),
                    { item, _ ->
                        spCity.setSelection(item.id.toInt())
                    }).show()
        }
    }

    private fun setChipList() {
        chipArray.add("Visitors")
        chipArray.add("Active Sites")
        chipArray.add("Customers")
        chipArray.add("Collections")
        chipArray.add("Follow Ups")

    }

    fun setuprvHomeCounterMarchant() {

        autoImageSliderAdapter = AutoImageSliderAdapter(mContext!!, chipArray, this)
        rvHomeCounter.setSliderAdapter(autoImageSliderAdapter!!)
        rvHomeCounter.setIndicatorAnimation(IndicatorAnimationType.SLIDE)
        rvHomeCounter.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        rvHomeCounter.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH)
        rvHomeCounter.setIndicatorUnselectedColor(Color.WHITE)
        rvHomeCounter.setScrollTimeInSec(4)
        rvHomeCounter.startAutoCycle()


        val layoutManager1 = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rvDeepCleaing.layoutManager = layoutManager1
        adapter1 = HomeServiceAdapter(requireContext(), chipArray, this)
        rvDeepCleaing.adapter = adapter1

    }

    override fun onItemSelect(position: Int, data: String) {

    }

    fun getCityList() {
        var result = ""
        var SelecetdCityPostion = -1
        try {
            val jsonBody = JSONObject()
            jsonBody.put("StateID", "-1")

            result = Networking.setParentJsonData(Constant.METHOD_CITY_LIST, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
                .with(requireContext())
                .getServices()
                .getCityList(Networking.wrapParams(result))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : CallbackObserver<CityListModel>() {
                    override fun onSuccess(response: CityListModel) {
                        cityListArray.addAll(response.data)
                        var myList: MutableList<SearchableItem> = mutableListOf()
                        for (items in response.data.indices) {
                            cityNameList.add(response.data.get(items).cityName.toString())
                            myList.add(SearchableItem(items.toLong(), cityNameList.get(items)))
                            if (response.data.get(items).cityID.toString().equals(session.getDataByKey(SessionManager.KEY_CITY_ID))) {
                                SelecetdCityPostion = items
                            }
                        }
                        itens = myList

                        adapterCity = ArrayAdapter(requireContext(), R.layout.custom_spinner_item, cityNameList)
                        spCity.setAdapter(adapterCity)
                        if (!session.getDataByKey(SessionManager.KEY_CITY_ID).equals("")) {
                            spCity.setSelection(SelecetdCityPostion)
                        }

                    }

                    override fun onFailed(code: Int, message: String) {

                        showAlert(message)

                    }

                }).addTo(autoDisposable)
    }

}