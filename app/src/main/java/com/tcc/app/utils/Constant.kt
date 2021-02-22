package com.tcc.app.utils

object Constant {
    const val VISITOR_ID = "visitor_id"
    const val CUSTOMER_ID = "customer_id"
    const val BASE_URL = "http://societyfy.in/tcc_new/"
    const val API_URL = "${BASE_URL}api/"
    const val EMP_PROFILE = "${BASE_URL}assets/uploads/user/"
    const val PDF_INVOICE_URL = "${BASE_URL}assets/uploads/invoice/"
    const val PDF_QUOTATION_URL = "${BASE_URL}assets/uploads/estimation/"

    const val OVERTIME = "overtime"
    const val LATEFINE = "latefine"
    const val PAGE_SIZE = 10

    const val TITLE = "title"

    const val TEN_MILISEC = 600000

    const val TEXT = "text"

    // Common Params
    const val METHOD = "method"
    const val BODY = "body"
    const val MESSAGE = "message"
    const val ERROR = "error"
    const val ROW_COUNT = "rowCount"
    const val DATA = "data"
    const val DATA1 = "data1"


    // ---Server Date Time--//
    const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"


    const val MOBILE = "mobile"
    const val UNAUTHORIZED = "unauthorized"


    //----- Lead Type-----
    const val HOT = "Hot"
    const val WARM = "Warm"
    const val COLD = "Cold"
    const val SILENT = "Silent"


    //--- Method Name-----

    const val METHOD_LOGIN = "checkLogin"
    const val METHOD_FORGOTPWD = "forgotPassword"
    const val METHOD_LEADLIST = "getVisitor"
    const val METHOD_ROLE = "getRole"
    const val METHOD_EMPLOYEE_LIST = "getEmployee"
    const val METHOD_TRAINING_LIST = "getEmployeeTraining"
    const val METHOD_UNIFORM_LIST = "getEmployeeUniform"
    const val METHOD_ROOM_LIST = "getEmployeeRoom"
    const val METHOD_CITY_LIST = "getCities"
    const val METHOD_USERTYPE_LIST = "getUsertype"
    const val METHOD_QUOTATION_LIST = "getQuotation"
    const val METHOD_SITE_LIST = "getSites"
    const val METHOD_PAYMENT_LIST = "getPayment"
    const val METHOD_CUSTOMER_LIST = "getCustomer"
    const val METHOD_CHANGE_PWD = "changePassword"
    const val METHOD_ADD_ROOM = "addEmployeeRoom"
    const val METHOD_TRAINING_DATE_TIME = "getTrainingDateTime"
    const val METHOD_UNIFORM_SPINNER_LIST = "getUniformType"
    const val METHOD_ADD_TRAINING = "addEmployeeTraining"
    const val METHOD_ADD_UNIFORM = "addEmployeeUniform"
    const val METHOD_STATE_LIST = "getStates"
    const val METHOD_ADD_LEAD = "addVisitor"
    const val METHOD_ADD_SITE = "addSites"
    const val METHOD_EDIT_LEAD = "editVisitor"
    const val METHOD_COMPANY_LIST = "getCompany"
    const val METHOD_SERVICE_LIST = "getService"
    const val METHOD_CONFIG = "getConfig"
    const val METHOD_ADD_QUOTATIOON = "addQuotation"
    const val METHOD_GET_REASON = "getReason"
    const val METHOD_REJECT_REASON = "convertToQuotationReject"
    const val METHOD_ACCEPT_REASON = "convertToQuotationAccept"
    const val METHOD_QUESTION = "getQuestion"
    const val METHOD_ADD_INVOICE = "addInvoice"
    const val METHOD_GET_INVOICE = "getInvoice"
    const val METHOD_ADD_PAYMENT = "addPayment"
    const val METHOD_TEAM_DEFINITION_LIST = "getTeamDefination"
    const val METHOD_AVAILABLE_EMPLOYEE_LIST = "getAvailableEmployee"
    const val METHOD_ADD_TEAM_DEFINITION = "addTeamDefination"
    const val METHOD_GETCUSTOMER_ATTEDANCE = "getAttendance"
    const val METHOD_ADD_ATTEDANCE_LIST = "getaddAttendaneEmployee"
    const val METHOD_GET_INVOICE_ATTEDANCE_LIST = "getInvoiceAttendance"
    const val METHOD_GET_SLARYY = "getSalary"
    const val METHOD_GET_TICKET = "getTicket"
}