package com.tcc.app.fragment

import android.os.Bundle
import android.view.*
import com.bumptech.glide.Glide
import com.tcc.app.R
import com.tcc.app.extention.*
import com.tcc.app.modal.LoginModal
import com.tcc.app.network.CallbackObserver
import com.tcc.app.network.Networking
import com.tcc.app.network.addTo
import com.tcc.app.utils.Constant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONException
import org.json.JSONObject


class ProfileFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHomeScreenTitle(requireActivity(), getString(R.string.menu_profile))


        edtFirstName.setText(session.user.data?.firstName)
        edtLastName.setText(session.user.data?.lastName)
        edtContact.setText(session.user.data?.mobileNo)
        edtEmail.setText(session.user.data?.emailID)
        edtAddress.setText(session.user.data?.address)
        Glide.with(this)
            .load(Constant.EMP_PROFILE + session.user.data?.photoURL)
            .apply(
                com.bumptech.glide.request.RequestOptions().centerCrop()
                    .placeholder(com.tcc.app.R.drawable.ic_profile)
            )
            .into(imgProfile)

        btnSubmit.setOnClickListener {
            validation()
        }
    }


    fun validation() {

        when {

            edtFirstName.isEmpty() -> {
                root.showSnackBar("Enter First Name")
                edtFirstName.requestFocus()
            }
            edtLastName.isEmpty() -> {
                root.showSnackBar("Enter Last Name")
                edtLastName.requestFocus()
            }

            edtContact.isEmpty() -> {
                root.showSnackBar("Enter Mobile No.")
                edtContact.requestFocus()
            }
            edtContact.getValue().length < 10 -> {
                root.showSnackBar("Enter Valid Mobile No.")
                edtContact.requestFocus()
            }

            edtAddress.isEmpty() -> {
                edtAddress.requestFocus()
                root.showSnackBar("Enter Address")
            }

            else -> {
                UpdateProfile()
            }

        }
    }


    override fun onDestroy() {
        super.onDestroy()

        setHomeScreenTitle(requireActivity(), getString(R.string.menu_home))
    }

    fun UpdateProfile() {
        showProgressbar()
        var result = ""
        try {
            val jsonBody = JSONObject()
            jsonBody.put("UserID", session.user.data?.userID)
            jsonBody.put("FirstName", edtFirstName.getValue())
            jsonBody.put("LastName", edtLastName.getValue())
            jsonBody.put("MobileNo", edtContact.getValue())
            jsonBody.put("Address", edtAddress.getValue())
            jsonBody.put("UsertypeID", session.user.data?.usertypeID)
            if (session.user.data?.salary.equals(""))
                jsonBody.put("Salary", "0")
            else
                jsonBody.put("Salary", session.user.data?.salary)

            result = Networking.setParentJsonData(
                Constant.METHOD_EDIT_PROFILE,
                jsonBody
            )
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        Networking
            .with(requireContext())
            .getServices()
            .login(Networking.wrapParams(result))//wrapParams Wraps parameters in to Request body Json format
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : CallbackObserver<LoginModal>() {
                override fun onSuccess(response: LoginModal) {
                    val data = response.data
                    hideProgressbar()
                    if (data != null) {
                        if (response.error == 200) {
                            session.user = response
                            root.showSnackBar(response.message.toString())

                        } else {
                            showAlert(response.message.toString())
                        }

                    } else {
                        showAlert(response.message.toString())
                    }
                }

                override fun onFailed(code: Int, message: String) {
                    // showAlert(message)
                    showAlert(getString(R.string.show_server_error))
                    hideProgressbar()
                }

            }).addTo(autoDisposable)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_add).isVisible = false
        menu.findItem(R.id.action_filter).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home, menu)

        val filter = menu.findItem(R.id.action_filter)
        filter.setVisible(false)

        val add = menu.findItem(R.id.action_add)
        add.setVisible(false)
    }
}