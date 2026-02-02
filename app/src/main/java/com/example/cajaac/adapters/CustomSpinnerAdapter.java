package com.example.cajaac.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.example.cajaac.R;

public class CustomSpinnerAdapter extends BaseAdapter {
    private Context context;
    private String[] items;
    private LayoutInflater inflater;

    public CustomSpinnerAdapter(Context context, String[] items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.spinner_layout, parent, false);
        }

        TextView textView = view.findViewById(R.id.tvSpinnerText);
        ImageView arrow = view.findViewById(R.id.ivSpinnerArrow);

        textView.setText(items[position]);

        // Configurar color de la flecha usando tint
        arrow.setImageTintList(ContextCompat.getColorStateList(context, R.color.text_65));

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.spinner_item, parent, false);
        }

        TextView textView = (TextView) view;
        textView.setText(items[position]);

        return view;
    }
}