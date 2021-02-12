package com.tcc.app.network

import com.tcc.app.modal.ForgotPasswordModal
import com.tcc.app.modal.GetRoleModal
import com.tcc.app.modal.LeadListModal
import com.tcc.app.modal.LoginModal
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface APIInterface {

    @POST("service/")
    fun login(@Body body: RequestBody): Observable<Response<LoginModal>>

    @POST("service/")
    fun forgotpwd(@Body body: RequestBody): Observable<Response<ForgotPasswordModal>>

    @POST("service/")
    fun getLeadList(@Body body: RequestBody): Observable<Response<LeadListModal>>

    @POST("service/")
    fun getRole(@Body body: RequestBody): Observable<Response<GetRoleModal>>

}
