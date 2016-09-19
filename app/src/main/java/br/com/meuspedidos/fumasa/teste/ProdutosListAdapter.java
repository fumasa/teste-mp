package br.com.meuspedidos.fumasa.teste;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ProdutosListAdapter extends BaseAdapter {
    private ArrayList<Produto> listData;
    private LayoutInflater layoutInflater;
    private ImageLoader loader;

    public ProdutosListAdapter(Context aContext, ArrayList<Produto> listData, ImageLoader loader) {
        this.listData = listData;
        this.loader = loader;
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
            convertView = layoutInflater.inflate(R.layout.product_row_layout, null);
            holder = new ViewHolder();
            holder.nameView = (TextView) convertView.findViewById(R.id.name);
            holder.priceView = (TextView) convertView.findViewById(R.id.price);
            holder.photoView = (ImageView) convertView.findViewById(R.id.photo);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (listData.get(position) != null) {
            holder.nameView.setText(listData.get(position).getName());
            holder.priceView.setText("R$ " + listData.get(position).getPrice());
            //holder.photoView.setImageURI(Uri.parse(listData.get(position).getPhoto()));

            this.loader.DisplayImage(listData.get(position).getPhoto(), holder.photoView);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView nameView;
        TextView priceView;
        ImageView photoView;
    }
}