package com.tcc.app.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.tcc.app.R
import com.tcc.app.activity.AddQuotationActivity
import com.tcc.app.activity.DocumentListActivity
import com.tcc.app.adapter.SiteListAdapter
import com.tcc.app.extention.invisible
import com.tcc.app.extention.visible
import com.tcc.app.modal.LeadItem
import com.tcc.app.modal.SiteListItem
import com.tcc.app.utils.Constant
import kotlinx.android.synthetic.main.reclerview_only.*


class SiteTabFragment() : BaseFragment(), SiteListAdapter.OnItemSelected {


    private var list: MutableList<SiteListItem> = mutableListOf()

    constructor(list1: MutableList<SiteListItem>) : this() {
        list = list1
    }

    var adapter: SiteListAdapter? = null

    var page: Int = 1
    var hasNextPage: Boolean = true
    var leadItem: LeadItem? = null

//    companion object {
//
//        var name: String = ""
//        fun getInstance(bundle: Bundle): SiteTabFragment {
//            val fragment = SiteTabFragment()
//            fragment.arguments = bundle
//
//            return fragment
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.reclerview_only, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //refreshData()


        // getBundleData()
        /* if (leadItem == null && customerId == -1 && visitorId == -1) {
             setHomeScreenTitle(requireActivity(), getString(R.string.nav_site))
         }*/

        /*recyclerView.setLoadMoreListener(object : LoadMoreListener {
            override fun onLoadMore() {
                if (hasNextPage && !recyclerView.isLoading) {
                    progressbar.visible()
                    getSiteList(++page)
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            name = ""
            list.clear()
            hasNextPage = true
            recyclerView.isLoading = true
            adapter?.notifyDataSetChanged()
            getSiteList(page)
        }*/
    }


    /*   private fun getBundleData() {
           val bundle = arguments
           if (bundle != null) {
               leadItem = bundle.getSerializable(Constant.DATA) as LeadItem
               visitorId = leadItem?.visitorID?.toInt()

           }
       }*/

    fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = SiteListAdapter(requireContext(), list, this)
        recyclerView.adapter = adapter
        adapter!!.notifyDataSetChanged()
        refreshData()
        Log.d("TAG", "setupRecyclerView: " + list.size)
    }

    override fun onItemSelect(position: Int, data: SiteListItem) {
        val i = Intent(requireContext(), AddQuotationActivity::class.java)
        i.putExtra(Constant.DATA, data)
        if (leadItem != null) i.putExtra(Constant.DATA1, leadItem)
        startActivity(i)
        Animatoo.animateCard(requireContext())

    }

    override fun onDocumentClick(position: Int, data: SiteListItem) {
        val i = Intent(requireContext(), DocumentListActivity::class.java)
        i.putExtra(Constant.DATA, data)
        startActivity(i)
        Animatoo.animateCard(requireContext())
    }


    private fun refreshData() {
        recyclerView.setLoadedCompleted()
        // adapter?.notifyDataSetChanged()

        if (list.size > 0) {
            imgNodata.invisible()
            recyclerView.visible()
        } else {
            imgNodata.visible()
            imgNodata.setImageResource(R.drawable.nodata)
            recyclerView.invisible()
        }
    }

    override fun onResume() {
        setupRecyclerView()
        super.onResume()
    }
//
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.home, menu)
//        val add = menu.findItem(R.id.action_add)
//        add.setVisible(false)
//        val filter = menu.findItem(R.id.action_filter)
//        filter.setVisible(true)
//        super.onCreateOptionsMenu(menu, inflater)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//
//            R.id.action_filter -> {
//                val intent = Intent(context, SearchActivity::class.java)
//                intent.putExtra(Constant.DATA, Constant.SITE)
//                startActivity(intent)
//                Animatoo.animateCard(context)
//                return true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true)
//    }
//
//    override fun onDestroy() {
//        name = ""
//        super.onDestroy()
//    }
//
//    override fun onDestroyView() {
//        name = ""
//        super.onDestroyView()
//    }
//
//    override fun onAttach(context: Context) {
//        name = ""
//        super.onAttach(context)
//    }
//
//    override fun onPause() {
//        name = ""
//        super.onPause()
//    }

}