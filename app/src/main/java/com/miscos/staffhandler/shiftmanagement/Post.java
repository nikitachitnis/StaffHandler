package com.miscos.staffhandler.shiftmanagement;

import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForAdmin;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForAuthority;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForEmployee;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForShift;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForSpecialTiming;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForWeekOffEmployees;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForWifi;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonDataForWorkingOnHoliday;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonResponse;
import com.miscos.staffhandler.shiftmanagement.models.HolidayList;
import com.miscos.staffhandler.shiftmanagement.models.Roster;


import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
/*Developed under Miscos
 * Developed by Karan
 * 15-09-2020
 * */
public interface Post {

    @FormUrlEncoded
    @POST("Shift_management/shift_management")
    Call<GetJsonDataForShift> getShiftData(@Field("mode") String mode, @Field("employer_id") String employer_id);

    @FormUrlEncoded
    @POST("Employee_list/employee_list")
    Call<GetJsonDataForEmployee> getEmployeeList(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("shift_no") String shift_no, @Field("type") String type, @Field("operation_type") String operation_type);

    @FormUrlEncoded
    @POST("Shift_management/shift_management")
    Call<GetJsonResponse> setShiftData(@Field("mode") String mode, @Field("employer_id") String employer_id,
                                       @Field("shift_1_in_time") String shift_1_in_time,
                                       @Field("shift_1_out_time") String shift_1_out_time,
                                       @Field("shift_1_pre") String shift_1_pre,
                                       @Field("shift_1_post") String shift_1_post,
                                       @Field("shift_2_in_time") String shift_2_in_time,
                                       @Field("shift_2_out_time") String shift_2_out_time,
                                       @Field("shift_2_pre") String shift_2_pre,
                                       @Field("shift_2_post") String shift_2_post,
                                       @Field("shift_3_in_time") String shift_3_in_time,
                                       @Field("shift_3_out_time") String shift_3_out_time,
                                       @Field("shift_3_pre") String shift_3_pre,
                                       @Field("shift_3_post") String shift_3_post,
                                       @Field("noOfShift") String noOfShift,
                                       @Field("shift_1_name") String shiftname1,
                                       @Field("shift_2_name") String shiftname2,
                                       @Field("shift_3_name") String shiftname3,
                                        @Field("shift_1_break_from_time") String shift_1_break_from_time,
                                        @Field("shift_1_break_to_time") String shift_1_break_to_time,
                                        @Field("shift_2_break_from_time") String shift_2_break_from_time,
                                        @Field("shift_2_break_to_time") String shift_2_break_to_time,
                                        @Field("shift_3_break_from_time") String shift_3_break_from_time,
                                       @Field("shift_3_break_to_time") String shift_3_break_to_time,
                                       @Field("shift_1_duration") String shift_1_duration,
                                       @Field("shift_2_duration") String shift_2_duration,
                                       @Field("shift_3_duration") String shift_3_duration,
                                       @Field("shift_send_notification_alert_for_break") String shift_send_notification_alert_for_break,
                                       @Field("generate_voice_alarm") String generate_voice_alarm);


    @FormUrlEncoded
    @POST("Shift_management/shift_management")
    Call<GetJsonResponse> removeShift(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("old_shift_no") String old_shift_no, @Field("new_shift_no") String new_shift_no);

    @FormUrlEncoded
    @POST("Special_timing_holiday_management/special_timing_holiday_management")
    Call<GetJsonDataForSpecialTiming> getSpecialTimingData(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("employee_id") String employee_id);

    @FormUrlEncoded
    @POST("Special_timing_holiday_management/special_timing_holiday_management")
    Call<GetJsonResponse> removeSpecialTimingData(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("employee_id") String employee_id, @Field("action") String action);

    @FormUrlEncoded
    @POST("Special_timing_holiday_management/special_timing_holiday_management")
    Call<GetJsonResponse> setSpecialTimingData(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("employee_id") String employee_id, @Field("in_time") String in_time, @Field("out_time") String out_time, @Field("pre_time") String pre,
                                               @Field("post_time") String post, @Field("holiday") String holiday,
                                               @Field("applicable_from_date") String applied_on,
                                               @Field("in_time2") String in_time2,
                                               @Field("out_time2") String out_time2,
                                               @Field("pre_time2") String pre_time2,
                                               @Field("post_time2") String post_time2,
                                               @Field("holiday2") String holiday2);

    @FormUrlEncoded
    @POST("employee_working_on_holiday/employee_working_on_holiday")
    Call<GetJsonDataForWorkingOnHoliday> getEmployeeListForHoilday(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("shift_no") String shift_no, @Field("holiday_date") String holiday_date, @Field("employee_type") String employee_type, @Field("week_off") String week_off);

    @FormUrlEncoded
    @POST("employee_working_on_holiday/employee_working_on_holiday")
    Call<GetJsonResponse> setEmployeeListForHoilday(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("employee_ids") String employee_ids, @Field("shift_no") String shift_no, @Field("holiday_date") String holiday_date);

    @FormUrlEncoded
    @POST("Authority_management/authority_management")
    Call<GetJsonDataForAdmin> getAdminList(@Field("mode") String mode, @Field("employer_id") String employer_id);

    @FormUrlEncoded
    @POST("Authority_management/authority_management")
    Call<GetJsonResponse> setAdmin(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("employee_id") String employee_id);

    @FormUrlEncoded
    @POST("Authority_management/authority_management")
    Call<GetJsonResponse> setAuthority(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("employee_id") String employee_id, @Field("authorities") String authorities);

    @FormUrlEncoded
    @POST("Authority_management/authority_management")
    Call<GetJsonDataForAuthority> getAuthority(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("employee_id") String employee_id);

    @FormUrlEncoded
    @POST("Roster_management/roster_management")
    Call<Roster> getRoster(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("employee_id") String employee_id);

    @FormUrlEncoded
    @POST("Roster_management/roster_management")
    Call<GetJsonResponse> setRoster(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("employee_id") String employee_id, @Field("old_list") String old_list, @Field("new_list") String new_list, @Field("date") String date, @Field("shift_no") String shift_no);

    @FormUrlEncoded
    @POST("employee_working_on_holiday/employee_working_on_holiday")
    Call<HolidayList> getHolidayList(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("date") String date, @Field("holiday_type") String holiday_type);

    @FormUrlEncoded
    @POST("Week_off_employee_list/week_off_employee_list")
    Call<GetJsonDataForWeekOffEmployees> getWeekOffEmployeesListForHoliday(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("date") String date, @Field("employee_type") String employee_type);

    @FormUrlEncoded
    @POST("Authority_management/authority_management")
    Call<GetJsonResponse> removeAdmin(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("employee_id") String employee_id);

    @FormUrlEncoded
    @POST("Wifi_management/wifi_management")
    Call<GetJsonResponse> setWifi(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("wifi_name") String wifi_name, @Field("gps_with_wifi") String gps_with_wifi , @Field("alert_on_holiday") String alert_on_holiday, @Field("absent_leave_deduction") String absent_leave_deduction,
                                  @Field("salary_management") String sal_management,@Field("leave_management") String leave_mngmnt,@Field("show_salary_detail") String sal_report);

    @FormUrlEncoded
    @POST("Wifi_management/wifi_management")
    Call<GetJsonDataForWifi> getWifi(@Field("mode") String mode, @Field("employer_id") String employer_id);

    @FormUrlEncoded
    @POST("Wifi_management/wifi_management")
    Call<GetJsonResponse> removeWifi(@Field("mode") String mode, @Field("employer_id") String employer_id, @Field("wifi_name") String wifi_name);

}
