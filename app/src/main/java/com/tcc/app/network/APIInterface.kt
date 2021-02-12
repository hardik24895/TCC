package com.tcc.app.network

import com.tcc.app.modal.*
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

    @POST("service/")
    fun getEmployeeList(@Body body: RequestBody): Observable<Response<EmployeeListModel>>

    @POST("service/")
    fun getTrainingList(@Body body: RequestBody): Observable<Response<TrainingListModel>>

    @POST("service/")
    fun getUniformList(@Body body: RequestBody): Observable<Response<UniformListModel>>

    @POST("service/")
    fun getRoomAllocationList(@Body body: RequestBody): Observable<Response<RoomAllocationListModel>>

    @POST("service/")
    fun getCityList(@Body body: RequestBody): Observable<Response<CityListModel>>
    @POST("service/")
    fun getQuationList(@Body body: RequestBody): Observable<Response<QuotationListModal>>

    @POST("service/")
    fun getSiteList(@Body body: RequestBody): Observable<Response<SiteListModal>>

    @POST("service/")
    fun getCustomerList(@Body body: RequestBody): Observable<Response<CustomerListModal>>

    @POST("service/")
    fun changePassword(@Body body: RequestBody): Observable<Response<ChangePasswordModal>>
}
