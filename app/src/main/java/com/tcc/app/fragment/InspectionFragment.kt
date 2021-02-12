package com.tcc.app.fragment

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.R
import com.tcc.app.activity.AddInspectionActivity
import com.tcc.app.activity.InspectionDetailActivity
import com.tcc.app.adapter.InspectionAdapter
import com.tcc.app.extention.goToActivity
import com.tcc.app.extention.setHomeScreenTitle
import kotlinx.android.synthetic.main.reclerview_swipelayout.*


class InspectionFragment : BaseFragment(), InspectionAdapter.OnItemSelected {

    var adapter: InspectionAdapter? = null
    lateinit var chipArray: ArrayList<String>
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.reclerview_swipelayout, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chipArray = ArrayList()
        setChipList()
        setHomeScreenTitle(requireActivity(), getString(R.string.nav_inspection))

    }

    private fun setChipList() {
        chipArray.add("Hair")
        chipArray.add("Massage")
        chipArray.add("Nail")
        chipArray.add("Spa")
        chipArray.add("Barber")
        chipArray.add("Training")
        chipArray.add("Makeup")
        chipArray.add("Hair Removel")
        chipArray.add("All")

        setupRecyclerViewMarchant()
    }

    fun setupRecyclerViewMarchant() {

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = InspectionAdapter(requireContext(), chipArray, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: String) {
        goToActivity<InspectionDetailActivity>()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                goToActivity<AddInspectionActivity>()
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