package br.com.meuspedidos.fumasa.teste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CategoriasListAdapter extends BaseAdapter {
    private ArrayList<Categoria> listData;
    private LayoutInflater layoutInflater;

    public CategoriasListAdapter(Context aContext, ArrayList<Categoria> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.category_row_layout, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (listData.get(position) != null) {
            holder.nameView.setText(listData.get(position).getName());
        }
        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
    }
}