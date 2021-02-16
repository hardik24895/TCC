package com.tcc.app.network

import com.tcc.app.modal.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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

    @POST("service/")
    fun getStateList(@Body body: RequestBody): Observable<Response<StateListModal>>

    @POST("service/")
    fun addLead(@Body body: RequestBody): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun getTrainingData(@Body body: RequestBody): Observable<Response<TrainingSpinnerListModel>>

    @POST("service/")
    fun AddTrainingData(@Body body: RequestBody): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun AddUniformData(@Body body: RequestBody): Observable<Response<UniformSpinnerListModel>>

    @POST("service/")
    fun getUserTypeList(@Body body: RequestBody): Observable<Response<UserTypeListModel>>

    @POST("service/")
    fun getConfigData(@Body body: RequestBody): Observable<Response<ConfigDataModel>>


    @POST("service/")
    fun AddQuotationData(@Body body: RequestBody): Observable<Response<CommonAddModal>>


    @POST("service/")
    fun getServiceList(@Body body: RequestBody): Observable<Response<ServiceListModel>>

    @Multipart
    @POST("service/")
    fun AddEmployeeApi(
        @Part ImageData: MultipartBody.Part,
        @Part DocumentImageData: MultipartBody.Part,
        @Part OfferletterData: MultipartBody.Part,
        @Part("method") method: RequestBody,
        @Part("FirstName") FirstName: RequestBody,
        @Part("LastName") LastName: RequestBody,
        @Part("EmailID") EmailID: RequestBody,
        @Part("Password") Password: RequestBody,
        @Part("MobileNo") MobileNo: RequestBody,
        @Part("Address") Address: RequestBody,
        @Part("UsertypeID") UsertypeID: RequestBody,
        @Part("Salary") Salary: RequestBody,
        @Part("JoiningDate") JoiningDate: RequestBody,
        @Part("WorkingHours") WorkingHours: RequestBody,
        @Part("BankName") BankName: RequestBody,
        @Part("BranchName") BranchName: RequestBody,
        @Part("AccountNo") AccountNo: RequestBody,
        @Part("IFSCCode") IFSCCode: RequestBody,
        @Part("GSTNo") GSTNo: RequestBody,
        @Part("CityID") CityID: RequestBody
    ): Observable<Response<CommonAddModal>>


    @POST("service/")
    fun getCompanyList(@Body body: RequestBody): Observable<Response<CompanyListModal>>
}
