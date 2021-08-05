package com.miscos.staffhandler.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.miscos.staffhandler.R;

import java.util.ArrayList;
import java.util.List;

public class TextAdapter extends ArrayAdapter<String>  implements Filterable {

    Context context;
    int resource, textViewResourceId;
    List<String> items, tempItems, suggestions;

    public TextAdapter(Context context, int resource, int textViewResourceId, List<String> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;

        this.items = items;
        tempItems = items; // this makes the difference.
        suggestions = new ArrayList<String>();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_autotextview, parent, false);
        }
        String people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.lbl_name);
            if (lblName != null)
                lblName.setText(people);
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((String) resultValue);
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (!constraint.toString().isEmpty())
            {
                ArrayList<String> strings=new ArrayList<>();
                for (String people : tempItems) {
                    if (people.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        strings.add(people);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = strings;
                filterResults.count = strings.size();
                return filterResults;
            } else {

                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            suggestions = (ArrayList<String>) results.values;
            if (results.count > 0)
            {
                clear();
                for (String people : suggestions)
                {
                    add(people);


                }
                notifyDataSetChanged();
            }
            else
            {
                notifyDataSetInvalidated();
            }



        }
    };
}