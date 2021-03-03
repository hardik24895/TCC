package com.tcc.app.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.tcc.app.R
import com.tcc.app.activity.LeadDetailActivity
import com.tcc.app.adapter.AutoImageSliderAdapter
import com.tcc.app.adapter.HomeServiceAdapter
import com.tcc.app.extention.invisible
import com.tcc.app.extention.setHomeScreenTitle
import com.tcc.app.extention.showAlert
import com.tcc.app.extention.visible
import com.tcc.app.interfaces.LoadMoreListener
import com.tcc.app.modal.*
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
    var adapterLead: HomeServiceAdapter? = null
    var list: ArrayList<HomeCounterDataItem> = ArrayList()
    var leadList: ArrayList<DashBoardLeadDataItem> = ArrayList()
    lateinit var cityNameList: ArrayList<String>
    lateinit var cityListArray: ArrayList<CityDataItem>
    var adapterCity: ArrayAdapter<String>? = null
    var itens: List<SearchableItem>? = null
    var autoImageSliderAdapter: AutoImageSliderAdapter? = null
    lateinit var parent: View
    var page: Int = 1
    var hasNextPage: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        parent = inflater.inflate(R.layout.fragment_home, container, false)
        return parent
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHomeScreenTitle(requireActivity(), getString(R.string.menu_home))
        cityListArray = ArrayList()
        cityNameList = ArrayList()
        getCityList()
        setuprvHomeCounterMarchant()
        // recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getDashBoardLead(++page)
                }
            }
        })




        rbDaly.isChecked = true

        //getDashBoardCount(rbDaly.text.toString())

        spCity.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parents: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position != -1 && cityListArray.size > position) {
                    session.storeDataByKey(
                        SessionManager.KEY_CITY_ID,
                        cityListArray.get(position).cityID.toString()
                    )
                    val selectedId: Int = rg.getCheckedRadioButtonId()
                    val rbType = parent.findViewById<View>(selectedId) as? RadioButton
                    getDashBoardCount(rbType?.text.toString())
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


        rg.setOnCheckedChangeListener { group, checkedId -> // checkedId is the RadioButton selected
            val rb = parent.findViewById(checkedId) as RadioButton
            getDashBoardCount(rb.text.toString())
            leadList.clear()
            getDashBoardLead(page)
        }

    }


    fun setSlider() {
        autoImageSliderAdapter = AutoImageSliderAdapter(mContext!!, list, this)
        rvHomeCounter.setSliderAdapter(autoImageSliderAdapter!!)
        rvHomeCounter.setIndicatorAnimation(IndicatorAnimationType.SLIDE)
        rvHomeCounter.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION)
        rvHomeCounter.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH)
        rvHomeCounter.setIndicatorUnselectedColor(Color.WHITE)
        rvHomeCounter.setScrollTimeInSec(4)
        rvHomeCounter.startAutoCycle()
    }

    fun setuprvHomeCounterMarchant() {

        val layoutManager1 =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager1
        adapterLead = HomeServiceAdapter(requireContext(), leadList, this)
        recyclerView.adapter = adapterLead

    }

    override fun onItemSelect(position: Int, data: String) {

    }

    override fun onResume() {
        page = 1
        leadList.clear()
        hasNextPage = true
        setuprvHomeCounterMarchant()
        recyclerView.isLoading = true
        getDashBoardLead(page)
        super.onResume()

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
                        if (response.data.get(items).cityID.toString()
                                .equals(session.getDataByKey(SessionManager.KEY_CITY_ID))
                        ) {
                            SelecetdCityPostion = items
                        }
                    }
                    itens = myList

                    adapterCity =
                        ArrayAdapter(requireContext(), R.layout.custom_spinner_item, cityNameList)
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

    fun getDashBoardCount(filter: String) {
        var result = ""
        list.clear()

        try {
            val jsonBody = JSONObject()
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            jsonBody.put("FilterType", filter)
            jsonBody.put("UserID", session.user.data?.userID)

            result = Networking.setParentJsonData(Constant.METHOD_GET_DASHBOARD, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(requireContext())
            .getServices()
            .getDashBoardCount(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<HomeCounterModal>() {
                override fun onSuccess(response: HomeCounterModal) {
                    list.addAll(response.data)
                    setSlider()

                }

                override fun onFailed(code: Int, message: String) {

                    showAlert(message)

                }

            }).addTo(autoDisposable)
    }

    fun getDashBoardLead(page: Int) {
        var result = ""
        val selectedId: Int = rg.getCheckedRadioButtonId()
        val rbType = parent.findViewById<View>(selectedId) as? RadioButton
        try {
            val jsonBody = JSONObject()
            jsonBody.put("PageSize", Constant.PAGE_SIZE)
            jsonBody.put("CurrentPage", page)
            jsonBody.put("CityID", session.getDataByKey(SessionManager.KEY_CITY_ID))
            jsonBody.put("FilterType", rbType?.text.toString())
            jsonBody.put("UserID", session.user.data?.userID.toString())

            result = Networking.setParentJsonData(Constant.METHOD_GET_DASHBOARD_LEAD, jsonBody)

        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(requireContext())
            .getServices()
            .getDashBoardLead(Networking.wrapParams(result))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<DashBoardLeadModal>() {
                override fun onSuccess(response: DashBoardLeadModal) {
                    if (leadList.size > 0) {
                        progressbar.invisible()
                    }
                    leadList.addAll(response.data)
                    adapterLead?.notifyItemRangeInserted(
                        leadList.size.minus(response.data.size),
                        leadList.size
                    )
                    hasNextPage = leadList.size < response.rowcount!!

                   refreshData(getString(R.string.no_data_found))
                }

                override fun onFailed(code: Int, message: String) {
                    if (leadList.size > 0) {
                        progressbar.invisible()
                    }
                    showAlert(message)
                    refreshData(message)
                }

            }).addTo(autoDisposable)
    }

    private fun refreshData(msg: String?) {
        recyclerView.setLoadedCompleted()
        adapterLead?.notifyDataSetChanged()

        if (leadList.size > 0) {
            tvInfo.invisible()
            txtService.visible()
            recyclerView.visible()
        } else {
            tvInfo.text = msg
            tvInfo.invisible()
            txtService.invisible()
            recyclerView.invisible()
        }
    }

    override fun onItemSelect(position: Int, data: DashBoardLeadDataItem) {
        var item = LeadItem(
            data.mobileNo,
            data.status,
            data.visitorID,
            data.emailID,
            data.address,
            data.rowcount,
            data.stateID,
            data.leadType,
            data.cityName,
            data.customerID,
            data.name,
            data.pinCode,
            data.rno,
            data.cityID,
            data.userID
        )
        val intent = Intent(context, LeadDetailActivity::class.java)
        intent.putExtra(Constant.DATA, item)
        startActivity(intent)
        Animatoo.animateCard(context)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        var filter = menu.findItem(R.id.action_filter)
        filter?.setVisible(false)
        var add = menu.findItem(R.id.action_add)
        add?.setVisible(false)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                return true
            }
            R.id.action_filter -> {
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

}