package com.miscos.staffhandler.employer_java.remote;




import com.miscos.staffhandler.employer_java.remote.model.GetJsonResponse;
import com.miscos.staffhandler.employer_java.remote.model.NormalResponse;
import com.miscos.staffhandler.employer_java.remote.model.breaktime.BreakTimeResponse;
import com.miscos.staffhandler.employer_java.remote.model.departmentlist.DepartmentListResponse;
import com.miscos.staffhandler.employer_java.remote.model.designationlist.DesignationResponse;
import com.miscos.staffhandler.employer_java.remote.model.designationlist.employeedesignationwise.EmployeesDesignationWiseResponse;
import com.miscos.staffhandler.employer_java.remote.model.fetchdataform2.FetchResponseForm2;
import com.miscos.staffhandler.employer_java.remote.model.inoutdata.InOutResponse;
import com.miscos.staffhandler.employer_java.remote.model.inoutdata.oldinoutdata.OldInOutResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {

    @FormUrlEncoded
    @POST("index.php/register_2/register_2")
    Call<NormalResponse> postForm2(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("mode") String mode,
            @Field("contact") String contact,
            @Field("business_type") String business_type,
            @Field("company_name") String company_name,
            @Field("no_of_emp") String no_of_emp,
            @Field("attendance_type") String attendance_type,
            @Field("email_id") String email_id,
            @Field("office_gps_location") String office_gps_location,
            @Field("office_address") String office_address,
            @Field("geo_permitted_distance") String geo_permitted_distance,
            @Field("employer_id") String employer_id,
            @Field("qr_type") String qr_type);

    @FormUrlEncoded
    @POST("index.php/register_3/register_3")
    Call<FetchResponseForm2> fetchDataForm2(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id,
            @Field("year") String year);

    @FormUrlEncoded
    @POST("index.php/register_3/register_3")
    Call<NormalResponse> form3PaymentAPI(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id,
            @Field("licenses_count") String licenses_count,
            @Field("pay_amount") String pay_amount,
            @Field("order_id") String order_id,
            @Field("txn_id") String txn_id,
            @Field("txn_status") String txn_status
    );

    @FormUrlEncoded
    @POST("index.php/login_qrcode/login_qrcode")
    Call<NormalResponse> generateQrCode(
            @Field("mode") String mode,
            @Field("qr_type") String qr_type,
            @Field("username") String username,
            @Field("password") String password,
            @Field("qrcode_no") String qrcode_no);

    @POST("index.php/Qr_config/qr_config")
    @FormUrlEncoded
    Call<NormalResponse> setQrConfig(
            @Field("mode") String mode,
            @Field("contact") String contact,
            @Field("employer_id") String employer_id);

    @POST("index.php/register_1/register_1")
    @FormUrlEncoded
    Call<NormalResponse> postForm1(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("mode") String mode,
            @Field("contact") String contact,
            @Field("business_type") String business_type,
            @Field("company_name") String company_name,
            @Field("age") String age,
            @Field("gender") String gender,
            @Field("email_id") String email_id,
            @Field("office_gps_location") String office_gps_location,
            @Field("office_address") String office_address,
            @Field("optional_address") String optional_Address);


    @FormUrlEncoded
    @POST("index.php/register_3/register_3")
    Call<NormalResponse> postForm3(
            @Field("employer_id") String employerId,
            @Field("mode") String update,
            @Field("week_off") String s,
            @Field("in_timing") String in_timing,
            @Field("out_timing") String out_timing,
            @Field("pre_start_timing") String pre_start_timing,
            @Field("post_close_timing") String post_close_timing,
            @Field("adhar_verification") String adhar_verification,
            @Field("enable_save_feedback_employee") String enable_save_feedback_employee,
            @Field("enable_employee_activity_message") String enable_employee_activity_message,
            @Field("employee_no_format") String employee_no_format,
            @Field("want_enter_employee_no_manually") String want_enter_employee_no_manually,
            @Field("adhar_verification_feedback") String adhar_verification_feedback,
            @Field("adhar_licence_flag") String adhar_license_flag,
            @Field("office_break_from_time") String office_break_from_time,
            @Field("office_break_to_time") String office_break_to_time,
            @Field("break_duration") String break_duration

    );


    @Multipart
    @POST("index.php/register_3/register_3")
    Call<NormalResponse> addHoliday(@Part("mode") RequestBody mode,
                                    @Part("name") RequestBody fullName,
                                    @Part("from_date") RequestBody from,
                                    @Part("to_date") RequestBody to,
                                    @Part("employer_id") RequestBody id,
                                    @Part("holiday_for") RequestBody holiday_for,
                                    @Part("holiday_type") RequestBody type,
                                    @Part("message") RequestBody msg,
                                    @Part MultipartBody.Part image);


    @Multipart
    @POST("index.php/register_3/register_3")
    Call<NormalResponse> editHoliday(@Part("mode") RequestBody mode,
                                     @Part("name") RequestBody fullName,
                                     @Part("from_date") RequestBody from,
                                     @Part("to_date") RequestBody to,
                                     @Part("employer_id") RequestBody id,
                                     @Part("holiday_for") RequestBody holiday_for,
                                     @Part("holiday_type") RequestBody type,
                                     @Part("holiday_id") RequestBody holi_id,
                                     @Part("message") RequestBody msg,
                                     @Part MultipartBody.Part image);
    @Multipart
    @POST("index.php/register_3/register_3")
    Call<NormalResponse> editHolidayWithoutImage(@Part("mode") RequestBody mode,
                                                 @Part("name") RequestBody fullName,
                                                 @Part("from_date") RequestBody from,
                                                 @Part("to_date") RequestBody to,
                                                 @Part("employer_id") RequestBody id,
                                                 @Part("holiday_for") RequestBody holiday_for,
                                                 @Part("holiday_type") RequestBody type,
                                                 @Part("holiday_id") RequestBody holi_id,
                                                 @Part("message") RequestBody msg);

    @FormUrlEncoded
    @POST("index.php/register_3/register_3")
    Call<GetJsonResponse> remove_holiday(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id,
            @Field("holiday_id") String holiday_id,
            @Field("from_date") String from_date);

    //added by saif

    @FormUrlEncoded
    @POST("index.php/Department_management/department_management")
    Call<DepartmentListResponse> getDepartmentList(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id);

    @FormUrlEncoded
    @POST("http://miscosproducts.com/mini_staff_handler/index.php/Designation_management/designation_management")
    Call<List<DesignationResponse>> getDepartmentDesignationList(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id);

    @FormUrlEncoded
    @POST("http://miscosproducts.com/mini_staff_handler/index.php/Department_management/department_management")
    Call<List<EmployeesDesignationWiseResponse>> getEmployeesDesignationWise(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id);

    @FormUrlEncoded
    @POST("http://miscosproducts.com/mini_staff_handler/index.php/Department_management/department_management")
    Call<List<NormalResponse>> addDepartment(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id,
            @Field("dept_name") String dept_name);

    @FormUrlEncoded
    @POST("http://miscosproducts.com/mini_staff_handler/index.php/Designation_management/designation_management")
    Call<List<NormalResponse>> addDesignation(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id,
            @Field("dept_id") String dept_id,
            @Field("designation_name") String designation_name);

    @FormUrlEncoded
    @POST("http://miscosproducts.com/mini_staff_handler/index.php/Department_management/department_management")
    Call<List<NormalResponse>> removeDepartment(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id,
            @Field("dept_id") String dept_id);

    @FormUrlEncoded
    @POST("http://miscosproducts.com/mini_staff_handler/index.php/Designation_management/designation_management")
    Call<List<NormalResponse>> removeDesignation(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id,
            @Field("dept_id") String dept_id,
            @Field("desi_id") String desi_id);



    @FormUrlEncoded
    @POST("http://miscosproducts.com/mini_staff_handler/index.php/Employee_daily_in_out_data/employee_daily_in_out_data")
    Call<List<InOutResponse>> getInOutData(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id,
            @Field("employee_id") String employee_id);

    @FormUrlEncoded
    @POST("http://miscosproducts.com/mini_staff_handler/index.php/Employee_daily_in_out_data/employee_daily_in_out_data")
    Call<List<NormalResponse>> addInOutData(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id,
            @Field("employee_id") String employee_id,
            @Field("line_no") String line_no,
            @Field("current_date") String current_date,
            @Field("status") String status,
            @Field("reason") String reason);

    @FormUrlEncoded
    @POST("http://miscosproducts.com/mini_staff_handler/index.php/Employee_daily_in_out_data/employee_daily_in_out_data")
    Call<List<OldInOutResponse>> getOldInOutData(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id,
            @Field("employee_id") String employee_id,
            @Field("from_date") String from_date,
            @Field("to_date") String to_date);

    @FormUrlEncoded
    @POST("http://miscosproducts.com/mini_staff_handler/index.php/Employee_daily_in_out_data/employee_daily_in_out_data")
    Call<List<NormalResponse>> saveBreakTime(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id,
            @Field("employee_id") String employee_id,
            @Field("operation_type") String operation_type,
            @Field("break_time_str") String break_time_str);

    @FormUrlEncoded
    @POST("http://miscosproducts.com/mini_staff_handler/index.php/Employee_daily_in_out_data/employee_daily_in_out_data")
    Call<List<BreakTimeResponse>> getBreakTimingData(
            @Field("mode") String mode,
            @Field("employer_id") String employer_id,
            @Field("employee_id") String employee_id,
            @Field("operation_type") String operation_type);
    @FormUrlEncoded
    @POST("http://miscosproducts.com/mini_staff_handler/index.php/Late_arrival_and_early_deperature/late_arrival_and_early_deperature")
    Call<List<NormalResponse>> saveLateArrivalEarlyDeparture(
            @Field("mode") String mode,
            @Field("late_maximun_permitted_dealy_in_min") String late_maximun_permitted_dealy_in_min,
            @Field("late_maximum_permitted_dealys_in_month_cnt") String late_maximum_permitted_dealys_in_month_cnt,
            @Field("late_penalty_every_min") String late_penalty_every_min,
            @Field("late_penalty_every_min_in_rupees") String late_penalty_every_min_in_rupees,
            @Field("half_day_marking_after_arrived_in_min") String half_day_marking_after_arrived_in_min,
            @Field("permitted_deperature_in_min") String permitted_deperature_in_min,
            @Field("maximum_permitted_departure_in_month_cnt") String maximum_permitted_departure_in_month_cnt,
            @Field("penalty_every_deperature_min") String penalty_every_deperature_min,
            @Field("penalty_every_deperature_in_ruppes") String penalty_every_deperature_in_ruppes,
            @Field("half_day_marking_in_early_departure_in_min") String half_day_marking_in_early_departure_in_min,
            @Field("send_notification_to_employee") String send_notification_to_employee,
            @Field("send_notification_to") String send_notification_to,
            @Field("auto_reflect_in_salary_calculation") String auto_reflect_in_salary_calculation,
            @Field("employer_id") String employer_id);
}
