package com.tcc.app.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tcc.app.R
import com.tcc.app.activity.*
import com.tcc.app.dialog.LogoutDailog
import com.tcc.app.extention.goToActivity
import com.tcc.app.extention.goToActivityAndClearTask
import com.tcc.app.extention.setHomeScreenTitle
import com.tcc.app.utils.Constant
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


        relayNotification.setOnClickListener { goToActivity<NotificationActivity>() }
        relayAboutus.setOnClickListener {
            intent = Intent(requireContext(), InformationActivity::class.java)
            intent.putExtra(Constant.TITLE, "2")
            intent.putExtra("Desc", "AboutUS")
            startActivity(intent)
        }
        relayPrivacy.setOnClickListener {
            intent = Intent(requireContext(), InformationActivity::class.java)
            intent.putExtra(Constant.TITLE, "3")
            intent.putExtra("Desc", "PrivacyPolicy")
            startActivity(intent)
        }
        relayTerms.setOnClickListener {
            intent = Intent(requireContext(), InformationActivity::class.java)
            intent.putExtra(Constant.TITLE, "1")
            intent.putExtra("Desc", "TermandCondition")
            startActivity(intent)
        }

        relayTickets.setOnClickListener {

            intent = Intent(requireContext(), TicketActivity::class.java)
            startActivity(intent)
        }

        relayPenalty.setOnClickListener {

            intent = Intent(requireContext(), PenaltyActivity::class.java)
            startActivity(intent)
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