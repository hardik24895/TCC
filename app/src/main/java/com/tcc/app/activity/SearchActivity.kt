package com.tcc.app.activity


import android.os.Bundle
import com.tcc.app.R
import com.tcc.app.extention.visible
import kotlinx.android.synthetic.main.toolbar_with_back_arrow.*

class SearchActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_search)
        imgBack.visible()
        imgBack.setOnClickListener {
            finish()
        }

        txtTitle.text = getString(R.string.filter)
    }
}