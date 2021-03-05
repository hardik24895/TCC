package com.tcc.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tcc.app.R
import com.tcc.app.activity.ChangePasswordActivity
import com.tcc.app.activity.InformationActivity
import com.tcc.app.activity.LoginActivity
import com.tcc.app.activity.NotificationActivity
import com.tcc.app.dialog.LogoutDailog
import com.tcc.app.extention.goToActivity
import com.tcc.app.extention.goToActivityAndClearTask
import com.tcc.app.extention.replaceFragment
import com.tcc.app.extention.setHomeScreenTitle
import com.tcc.app.utils.Constant
import com.tcc.app.utils.Constant.CMS_URL
import com.tcc.app.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingsFragment : BaseFragment() {

    lateinit var intent: Intent
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_setting, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHomeScreenTitle(requireActivity(), getString(R.string.nav_setting))
        intent = Intent(requireContext(), InformationActivity::class.java)

        relayNotification.setOnClickListener { goToActivity<NotificationActivity>() }
        relayAboutus.setOnClickListener {
            intent.putExtra("Title", "About Us")
            intent.putExtra("Desc", CMS_URL + "AboutUS")
            startActivity(intent)
        }
        relayPrivacy.setOnClickListener {
            intent.putExtra("Title", "Privacy Policy")
            intent.putExtra("Desc", CMS_URL + "PrivacyPolicy")
            startActivity(intent)
        }
        relayTerms.setOnClickListener {
            intent.putExtra("Title", "Terms And Condition")
            intent.putExtra("Desc", CMS_URL + "TermandCondition")
            startActivity(intent)
        }

        relayTickets.setOnClickListener {
            replaceFragment(TicketListFragment(), R.id.nav_host_fragment)
        }

        relayPenalty.setOnClickListener {
            replaceFragment(PenaltyFragment(), R.id.nav_host_fragment)
        }

        relayPwd.setOnClickListener { goToActivity<ChangePasswordActivity>() }
        relayLogout.setOnClickListener {
            val dialog = LogoutDailog.newInstance(
                requireContext(),
                object : LogoutDailog.onItemClick {
                    override fun onItemCLicked() {
                        //   val mobile=  session.getDataByKey(Constant.MOBILE)
                        //    val code=  session.getDataByKey(Constant.PHONE_CODE)
                        session.clearSession()
                        session.storeDataByKey(SessionManager.IsFirst, false)

                        goToActivityAndClearTask<LoginActivity>()
                    }
                })
            val bundle = Bundle()
            bundle.putString(Constant.TITLE, this.getString(R.string.app_name))
            bundle.putString(Constant.TEXT, this.getString(R.string.msg_logout))
            dialog.arguments = bundle
            dialog.show(childFragmentManager, "YesNO")
        }

        switchPushNotion.isChecked = session.getDataByKeyBoolean(Constant.ISCHECKED, false)

        switchPushNotion.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                session.storeDataByKey(Constant.ISCHECKED, true)
            } else {
                session.storeDataByKey(Constant.ISCHECKED, false)
            }
        }

    }
}