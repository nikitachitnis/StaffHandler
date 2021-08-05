package com.miscos.staffhandler.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.miscos.staffhandler.Model.ReportModel;
import com.miscos.staffhandler.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
/*Developed under Miscos
 * Developed by Nikhil
 * 01-06-2020
 * */
public class EmployeeReportAdapter extends RecyclerView.Adapter<EmployeeReportAdapter.ReportHolder> {

    private List<ReportModel> reportModelArrayList = new ArrayList<>();
    private Context context;
    private String employeeName;

    public EmployeeReportAdapter(Context context, List<ReportModel> reportModels) {
        this.context = context;
        this.reportModelArrayList = reportModels;
    }
    @NonNull
    @Override
    public ReportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext()).inflate(R.layout.report_view, parent, false);

        return new ReportHolder(view);
    }
    @Override
    public void onBindViewHolder(@NotNull final ReportHolder holder, int position) {
        ReportModel reportModel = reportModelArrayList.get(position);
        holder.tvName.setText(reportModel.getEmployee_name());
        holder.dayIn.setText(reportModel.getDay_in());
        holder.dayOut.setText(reportModel.getDay_out());
        holder.tvDate.setText(reportModel.getDate());

    }
    @Override
    public int getItemCount() {
        return reportModelArrayList.size();
    }

    public static class ReportHolder extends RecyclerView.ViewHolder{
        View itemView;
        TextView dayIn,dayOut,tvName,tvDate;
        public ReportHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvName = itemView.findViewById(R.id.tvName);
            dayIn = itemView.findViewById(R.id.tvDayIn);
            dayOut = itemView.findViewById(R.id.tvDayout);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
