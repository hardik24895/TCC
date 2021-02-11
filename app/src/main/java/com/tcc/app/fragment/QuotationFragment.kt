package com.tcc.app.fragment

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.tcc.app.Adapter.QuotationAdapter
import com.tcc.app.R
import kotlinx.android.synthetic.main.fragment_quotation.*
import kotlinx.android.synthetic.main.reclerview_swipelayout.*


class QuotationFragment : BaseFragment(), QuotationAdapter.OnItemSelected {

    var adapter: QuotationAdapter? = null
    lateinit var chipArray: ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_quotation, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chipArray = ArrayList()
        setChipList()
        txtAccepted.isSelected = true
        setupRecyclerViewMarchant(true)
        txtAccepted.setOnClickListener {
            txtAccepted.isSelected = true
            txtRejected.isSelected = false
            txtOther.isSelected = false
            setupRecyclerViewMarchant(true)
        }
        txtRejected.setOnClickListener {
            txtAccepted.isSelected = false
            txtRejected.isSelected = true
            txtOther.isSelected = false
            setupRecyclerViewMarchant(false)
        }
        txtOther.setOnClickListener {
            txtAccepted.isSelected = false
            txtRejected.isSelected = false
            txtOther.isSelected = true
            setupRecyclerViewMarchant(false)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                //   goToActivity<AddEmployeeActivity>()

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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


    }

    fun setupRecyclerViewMarchant(isAccept: Boolean) {

        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager
        adapter = QuotationAdapter(requireContext(), chipArray, isAccept, this)
        recyclerView.adapter = adapter

    }

    override fun onItemSelect(position: Int, data: String) {

    }

}