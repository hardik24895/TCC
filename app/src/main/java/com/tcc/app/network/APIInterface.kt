package com.tcc.app.network

import com.tcc.app.adapter.InspectionListModel
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
    fun AddInvoice(@Body body: RequestBody): Observable<Response<CommonAddModal>>


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

    @POST("service/")
    fun getRejectReasonList(@Body body: RequestBody): Observable<Response<RejectReasonListModel>>

    @POST("service/")
    fun QuationReason(@Body body: RequestBody): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun InspectionQuestionList(@Body body: RequestBody): Observable<Response<InspectionQuestionListModel>>

    @POST("service/")
    fun getInvoiceList(@Body body: RequestBody): Observable<Response<InvoiceListModal>>

    @POST("service/")
    fun PaymetList(@Body body: RequestBody): Observable<Response<PaymentListModel>>

    @POST("service/")
    fun AddPayment(@Body body: RequestBody): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun AddSite(@Body body: RequestBody): Observable<Response<SiteListModal>>

    @POST("service/")
    fun getCustomerAttendanceList(@Body body: RequestBody): Observable<Response<CustomerAttendanceListModal>>

    @POST("service/")
    fun getTeamDefinationList(@Body body: RequestBody): Observable<Response<TeamDefinitionListModel>>

    @POST("service/")
    fun getAvailableEmployeeList(@Body body: RequestBody): Observable<Response<AvailableEmployeeListModel>>

    @POST("service/")
    fun AddTeamDefinition(@Body body: RequestBody): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun getAttendenceList(@Body body: RequestBody): Observable<Response<AttendanceListModel>>


    @POST("service/")
    fun AddAttendence(@Body body: RequestBody): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun getLeadReminder(@Body body: RequestBody): Observable<Response<LeadReminderListModal>>

    @POST("service/")
    fun getAllEmpList(@Body body: RequestBody): Observable<Response<AllEmpAttendanceListModel>>

    @POST("service/")
    fun getEmpListWiseAttendance(@Body body: RequestBody): Observable<Response<EmployeeAttendanceListModel>>

    @POST("service/")
    fun getInvoiceAttedanceList(@Body body: RequestBody): Observable<Response<InVoiceAttendanceListModal>>

    @POST("service/")
    fun getSalaryList(@Body body: RequestBody): Observable<Response<SalaryListModal>>

    @POST("service/")
    fun addSalary(@Body body: RequestBody): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun getUserSalaryList(@Body body: RequestBody): Observable<Response<GetUserSalaryDetail>>

    @POST("service/")
    fun getTicketList(@Body body: RequestBody): Observable<Response<TicketListMdal>>


    @Multipart
    @POST("service/")
    fun AddTicket(
        @Part ImageData: MultipartBody.Part,
        @Part("method") method: RequestBody,
        @Part("UserID") UserID: RequestBody,
        @Part("Title") Title: RequestBody,
        @Part("Description") Description: RequestBody,
        @Part("Priority") Priority: RequestBody,
        @Part("CityID") CityID: RequestBody
    ): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun getDashBoardCount(@Body body: RequestBody): Observable<Response<HomeCounterModal>>

    @POST("service/")
    fun getDashBoardLead(@Body body: RequestBody): Observable<Response<DashBoardLeadModal>>

    @POST("service/")
    fun getLeadFollowupList(@Body body: RequestBody): Observable<Response<LeadFollowUpListModal>>

    @POST("service/")
    fun getInspectionList(@Body body: RequestBody): Observable<Response<InspectionListModel>>

    @POST("service/")
    fun getUserbyTypeList(@Body body: RequestBody): Observable<Response<GetUserByType>>

    @POST("service/")
    fun AddInspection(@Body body: RequestBody): Observable<Response<CommonAddModal>>

    @Multipart
    @POST("service/")
    fun AddInspectionImage(
        @Part ImageData: MultipartBody.Part,
        @Part("method") method: RequestBody,
        @Part("InspectionID") FirstName: RequestBody
    ): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun getPaneltyList(@Body body: RequestBody): Observable<Response<PenaltyListModel>>


    @POST("service/")
    fun AddCheckIn(@Body body: RequestBody): Observable<Response<CommonAddModal>>


    @POST("service/")
    fun AddCheckOut(@Body body: RequestBody): Observable<Response<CommonAddModal>>

    @Multipart
    @POST("service/")
    fun AddCustomerSiteDocument(
        @Part ImageData: MultipartBody.Part,
        @Part("method") method: RequestBody,
        @Part("UserID") UserID: RequestBody,
        @Part("Title") Title: RequestBody,
        @Part("SitesID") Description: RequestBody
    ): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun getDocumentList(@Body body: RequestBody): Observable<Response<DocumentListModal>>

    @Multipart
    @POST("service/")
    fun EditCustomerSiteDocument(
        @Part ImageData: MultipartBody.Part,
        @Part("method") method: RequestBody,
        @Part("UserID") UserID: RequestBody,
        @Part("Title") Title: RequestBody,
        @Part("SitesID") Description: RequestBody,
        @Part("CustomerSitesDocumentID") DocumentID: RequestBody
    ): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun deleteDocument(@Body body: RequestBody): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun addPanelty(@Body body: RequestBody): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun getCheckInOutList(@Body body: RequestBody): Observable<Response<CheckInOutListModel>>

    @POST("service/")
    fun getGloablAttendanceList(@Body body: RequestBody): Observable<Response<GlobalEmployeeAttedanceListModal>>

    @POST("service/")
    fun getAdvanceList(@Body body: RequestBody): Observable<Response<AdvanceListModal>>


    @POST("service/")
    fun getProcessList(@Body body: RequestBody): Observable<Response<ProcessListModal>>

    @POST("service/")
    fun SendMail(@Body body: RequestBody): Observable<Response<CommonAddModal>>


    @POST("service/")
    fun SendMessage(@Body body: RequestBody): Observable<Response<CommonAddModal>>

    @POST("service/")
    fun getNotification(@Body body: RequestBody): Observable<Response<NotificationListModel>>


}
