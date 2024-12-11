package com.example.Interface

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ServerApisInterface {

    companion object {
       //const val home_URL = "http://stg-hrms-saas-client-api.xyug.in:3009/" // staging
          const val home_URL = "http://192.168.168.164:3009/"
       //  const val home_URL = "https://hrms-client-api.xyug.in/api/" // Live
    }

    @Headers("Content-Type: application/json")
    @POST("profile/send-otp")
    fun sendotp(@Body body: String): Call<SendOtpModel>

    @Headers("Content-Type: application/json")
    @POST("profile/verify-otp")
    fun verifyOtp(@Body verifyOtpBodyModel: VerifyOtpBodyModel): Call<VerifyOtp>

    @Multipart
    @POST("profile/upload-profile")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part image: MultipartBody.Part): Call<UploadImageModel>

    @Headers("Content-Type: application/json")
    @GET("profile/all-employees")
    fun getAllEmployeess(@Header("Authorization") token: String): Call<List<AllEmployeeModel>>

    @GET("profile/get-singleuser")
    fun getSingleEmployee(@Header("Authorization") token: String): Call<SingleEmployeeModel>

    @GET("profile/bussiness-data")
    fun getBussinessUnit(@Header("Authorization") token: String): Call<BussinessUnitModel>

    @GET("profile/get-employee/{employeenumber}")
    fun getEmployeee(@Header("Authorization") token: String,
        @Path("employeenumber") employeeNumber: String): Call<SingleEmpModel>

    @POST("profile/addjobsummary")
    fun postProfessionalSummary(@Header("Authorization") token: String,@Body professionalbodymodel: ProfessionalSummaryBodyModel): Call<ProfessionalsummaryModel>

        @Multipart
        @POST("profile/addEducation")
        fun postEducationDetails(
            @Header("Authorization") token: String,
            @Part("degree") degree: RequestBody,
            @Part("institution") institution: RequestBody,
            @Part("yearOfPassing") yearOfPassing: RequestBody,
            @Part("specialization") specialization: RequestBody,
            @Part("grade") grade: RequestBody,
            @Part image: MultipartBody.Part?): Call<EducationdetailsModel>

    @Multipart
    @POST("profile/addexperience")
    fun postExperienceDetails(
        @Header("Authorization") token: String,
        @Part("companyName") companyName: RequestBody,
        @Part("role") role: RequestBody,
        @Part("startDate") startDate: RequestBody,
        @Part("endDate") endDate: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("achievments") achievements: RequestBody,
        @Part("jobResponsibility") jobResponsibility: RequestBody,
        @Part("skills") skills: RequestBody): Call<ExpereincedetailsModel>

    @POST("attendence/employee-clockin")
    fun postClockIn(@Header("Authorization") token: String,@Body clockInBodyModel: ClockInBodyModel): Call<ClockInModel>

    @POST("attendence/employee-clockout\n")
    fun postClockOut(@Header("Authorization") token: String): Call<ClockOutModel>

    @GET("attendence/get-attedence")
    fun getAttendenceHistory(@Header("Authorization") token: String): Call<AttendanceHistoryModel>

    @GET("leave/notify/employees")
    fun getNotifyEmp(@Header("Authorization") token: String): Call<NotifyempModel>

    @GET("leave/types")
    fun getLeavetype(@Header("Authorization") token: String): Call<LeavetypeModel>

    @GET("leave/data")
    fun getLeaveavaialbledata(@Header("Authorization") token: String): Call<LeaveavailableModel>

   @GET("leave/history")
    fun getLeaveHistory(@Header("Authorization") token: String): Call<LeaveHistoryModel>

    @POST("leave/apply")
    fun applyleave(@Header("Authorization") token: String,@Body applyleavebodymodel: ApplyleaveBodyModel): Call<ApplyleaveModel>
//old api's

//    @Headers("Content-Type: application/json")
//    @GET("payslip")
//    fun getAllPlaySlips(@Header("Authorization") token: String): Call<ArrayList<PayslipModel>>
//
//    @Headers("Content-Type: application/json")
//    @GET("payslip/{payslip_id}")
//    fun getPayslipPdfDownload(@Header("Authorization") token: String, @Path("payslip_id") pid: String): Call<PayslipDownloadModel>
//
//    @Headers("Content-Type: application/json")
//    @GET("request/payslip/{payslip_id}")
//    fun getPayslipRequest(@Header("Authorization") token: String, @Path("payslip_id") pid: String): Call<PayslipRequestModel>
//
//    @Headers("Content-Type: application/json")
//    @GET("achievements")
//    fun getAllAchievements(@Header("Authorization") token: String): Call<ArrayList<ListModel>>
//
//    @GET("achievements/{achievement_id}")
//    fun getAchievementPdfDownload(@Header("Authorization") token: String, @Path("achievement_id") pid: String): Call<ResponseBody>
//
//    @GET("request/achievements/{achievement_id}")
//    fun getAchievementRequest(@Header("Authorization") token: String, @Path("achievement_id") pid: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @GET("performance")
//    fun getPerformance(@Header("Authorization") token: String): Call<ArrayList<ListModel>>
//
//    @GET("performance/{performance_id}")
//    fun getPerformancePdfDownload(@Header("Authorization") token: String, @Path("performance_id") pid: String): Call<ResponseBody>
//
//    @GET("request/performance/{performance_id}")
//    fun getPerformanceRequest(@Header("Authorization") token: String, @Path("performance_id") pid: String): Call<ListModel>
//
//    @GET("profile")
//    fun getProfileData(@Header("Authorization") token: String): Call<ProfileModel>
//
//    @Headers("Content-Type: application/json")
//    @POST("email/verify")
//    fun emailVerify(@Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @GET("upcomingbirthday")
//    fun getUpcomingBirthday(): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @GET("singleupcomingbirthday")
//    fun getSingleUpcomingBirthday(): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @GET("workanniversary")
//    fun getWorkAnniversary(): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @GET("newemployees")
//    fun getNewEmployees(): Call<ArrayList<ListModel>>
//
//    @GET("singlenewemployee")
//    fun getNewSingleEmployees(): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @GET("holidayslist")
//    fun getHolidays(): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @POST("checkin")
//    fun checkIn(@Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @POST("checkout")
//    fun checkOut(@Body body: String): Call<ListModel>

//    @Headers("Content-Type: application/json")
//    @POST("applyleave")
//    fun applyLeave(@Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @GET("primarydetails/{employee_id}")
//    fun getPrimaryDetails(@Path("employee_id") empId: String): Call<ArrayList<Primarydetails>>
//
//    @Headers("Content-Type: application/json")
//    @GET("contactdetails/{employee_id}")
//    fun getContactDetails(@Path("employee_id") empId: String): Call<ArrayList<Contactdetails>>
//
//    @Headers("Content-Type: application/json")
//    @GET("employee/{employee_id}")
//    fun getProfileAllData(@Path("employee_id") empId: String): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @PUT("primarydetails/{employee_id}")
//    fun updatePrimaryDetails(@Path("employee_id") empId: String, @Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @PUT("contactdetails/{employee_id}")
//    fun updateContactDetails(@Path("employee_id") empId: String, @Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @PUT("addressdetails/{employee_id}")
//    fun updateAddressDetails(@Path("employee_id") empId: String, @Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @PUT("relation/{employee_id}")
//    fun updateRelationDetails(@Path("employee_id") empId: String, @Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @PUT("workexperience/{employee_id}")
//    fun updateExperienceDetails(@Path("employee_id") empId: String, @Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @PUT("education/{employee_id}")
//    fun updateEducationDetails(@Path("employee_id") empId: String, @Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @PUT("professionalsummary/{employee_id}")
//    fun updateProfessionalSummaryDetails(@Path("employee_id") empId: String, @Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @POST("compoffrequest")
//    fun requestCompOff(@Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @POST("regularizereq")
//    fun regularize(@Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @GET("allemployees")
//    fun getAllEmployees(): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @GET("teamemployeeleaves")
//    fun getAllEmployeesMyTeam(@Header("Authorization") token: String): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @GET("teamemployeeleaves")
//    fun getAllEmployeesLeavesTeam(@Header("Authorization") token: String): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @GET("leavestatus/{employee_id}")
//    fun getLeaveDetails(@Path("employee_id") empId: String): Call<ArrayList<ListModel>>
//

}//    @Headers("Content-Type: application/json")
//    @GET("attendancelogs/{employee_id}")
//    fun getAttendanceData(@Path("employee_id") empId: String): Call<ArrayList<AttendanceData>>
//
//    @GET("attendancelog/{employee_id}")
//    fun getSingleAttendanceData(@Path("employee_id") empId: String): Call<AttendanceData>
//
//    @Headers("Content-Type: application/json")
//    @POST("selecteddaylog/{employee_id}")
//    fun getWeekDayAttendanceData(@Path("employee_id") empId: String, @Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @GET("weeklogs/{employee_id}")
//    fun getOneWeekAttendanceData(@Path("employee_id") empId: String): Call<ArrayList<AttendanceData>>
//
//    @Headers("Content-Type: application/json")
//    @GET("attendancelogs/{employee_id}")
//    fun getLastWeekAttendanceData(@Path("employee_id") empId: String): Call<ArrayList<AttendanceData>>
//
//    @Headers("Content-Type: application/json")
//    @GET("annualleaves/{employee_id}")
//    fun getAnnualLeaveData(@Path("employee_id") empId: String): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @GET("getcompoff/{employee_id}")
//    fun getCompOffData(@Path("employee_id") empId: String): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @PUT("cancel/compoff/{employee_id}")
//    fun cancelCompOffData(@Path("employee_id") empId: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @GET("checkin/{employee_id}")
//    fun getCheckingStatus(@Path("employee_id") empId: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @GET("payslip/{payslip_id}")
//    fun getAllPlaySlipsSingle(@Header("Authorization") token: String, @Path("payslip_id") payslipId: String): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @GET("payslips/{employee_id}")
//    fun getPlaySlipsDownload(@Path("employee_id") empId: String, @Path("pdfurl") pdfurl: String): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @GET("announcements")
//    fun getAnnouncements(): Call<ArrayList<ListModel>>
//
//    @Headers("Content-Type: application/json")
//    @POST("announcements")
//    fun postAnnouncements(@Body body: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @PUT("announcements/{employee_id}")
//    fun putAnnouncements(@Path("employee_id") empId: String): Call<ListModel>
//
//    @Headers("Content-Type: application/json")
//    @DELETE("announcements/{employee_id}")
//    fun deleteAnnouncements(@Path("employee_id") empId: String): Call<ListModel>
