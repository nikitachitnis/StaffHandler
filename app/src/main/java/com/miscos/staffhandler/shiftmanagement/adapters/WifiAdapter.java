package com.miscos.staffhandler.shiftmanagement.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.miscos.staffhandler.R;
import com.miscos.staffhandler.employee.helper.PreferenceManager;
import com.miscos.staffhandler.shiftmanagement.Api;
import com.miscos.staffhandler.shiftmanagement.fragments.WifiManagement;
import com.miscos.staffhandler.shiftmanagement.models.GetJsonResponse;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.MyViewHolder1> {

    Context context;
    List<String> employeeArray;
    List<Boolean> checkList;
    OnButtonClick onButtonClick;
    int pos = -1;
    String wifiName = "";
    PreferenceManager preferenceManager;
    ProgressDialog progressBar;

    public void ClickListener(OnButtonClick onButtonClick) {
        this.onButtonClick = onButtonClick;
    }

    public WifiAdapter(Context context, List<String> projectsArray, List<Boolean> checkList) {
        this.employeeArray = projectsArray;
        this.context = context;
        this.checkList = checkList;
    }

    public WifiAdapter(Context context, List<String> projectsArray) {
        this.employeeArray = projectsArray;
        this.context = context;
    }


    @NonNull
    @Override
    public WifiAdapter.MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.employee_recycler_item, parent, false);

        return new WifiAdapter.MyViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final WifiAdapter.MyViewHolder1 holder, final int i) {
        preferenceManager = new PreferenceManager(context);
        holder.textView.setText(employeeArray.get(i));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkList != null) {
                    checkList.set(i, !checkList.get(i));
                    holder.textView.setBackgroundColor(checkList.get(i) ? context.getResources().getColor(R.color.colorPrimary) : Color.WHITE);
                }
                else {
                    holder.textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final Dialog dialog = new Dialog(context);
// Include dialog.xml file
                            dialog.setContentView(R.layout.dialog_exit);
                            dialog.show();

                            TextView heading = dialog.findViewById(R.id.tv_quit_learning);
                            TextView description = dialog.findViewById(R.id.tv_description);
                            Button btnNo = (Button) dialog.findViewById(R.id.btn_no);
                            Button btnYes = (Button) dialog.findViewById(R.id.btn_yes);
                            heading.setText("Remove Wifi");
                            description.setText("Do you want to remove wifi "+holder.textView.getText().toString() +"?");
                            btnYes.setText("Yes");
                            btnNo.setText("No");
                            btnNo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            btnYes.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                   ((WifiManagement)context).progressDialog.show();
                                    (Api.getClient()).removeWifi("remove_wifi",preferenceManager.getStringPreference(PreferenceManager.KEY_EMPLOYERID),holder.textView.getText().toString()).enqueue(new Callback<GetJsonResponse>() {
                                        @Override
                                        public void onResponse(Call<GetJsonResponse> call, Response<GetJsonResponse> response) {
                                            try{
                                                ((WifiManagement)context).progressDialog.dismiss();
                                                ((WifiManagement)context).tvNoData.setVisibility(View.VISIBLE);
                                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                                employeeArray.remove(i);
                                                notifyDataSetChanged();
                                            }
                                            catch (Exception e){
                                                e.printStackTrace();
                                                ((WifiManagement)context).progressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<GetJsonResponse> call, Throwable t) {

                                        }
                                    });
                                }
                            });

                        }
                    });
                }
                if (onButtonClick != null) {
                    onButtonClick.addButtonClick(holder.textView.getText().toString(), checkList.get(i));
                }
            }
        });
        // holder.bind();
    }

    @Override
    public int getItemCount() {
        return employeeArray.size();
    }


    public class MyViewHolder1 extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder1(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textview);
        }

        void bind() {

            if (pos == -1) {
                textView.setBackgroundColor(context.getResources().getColor(R.color.white));
            } else {
                if (pos == getAdapterPosition()) {
                    textView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    textView.setTextColor(context.getResources().getColor(R.color.black));
                } else {
                    textView.setBackgroundColor(context.getResources().getColor(R.color.white));
                }
            }

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("Adapter Position", getAdapterPosition() + "");
                    textView.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    textView.setTextColor(context.getResources().getColor(R.color.black));
                    if (pos != getAdapterPosition()) {
                        notifyItemChanged(pos);
                        pos = getAdapterPosition();
                    }
                    //onButtonClick.addButtonClick(textView.getText().toString());
                }
            });
        }


    }

    public interface OnButtonClick {
        void addButtonClick(String wifi, boolean check);
    }

    public void updateList(List<String> list) {
        employeeArray = list;
        notifyDataSetChanged();
    }

}
