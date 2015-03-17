package com.example.fuerthwiki;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

@SuppressLint("InflateParams")
public class ItemAdapter extends ArrayAdapter<Item> {

	private ArrayList<Item> objects;
	public ItemAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
		super(context, textViewResourceId, objects);
		this.objects = objects;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent){

		View v = convertView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.list_item, null);
		}
		if (objects.get(position) != null) {
			TextView tt = (TextView) v.findViewById(R.id.toptext);
			TextView ttd = (TextView) v.findViewById(R.id.toptextdata);
			TextView mt = (TextView) v.findViewById(R.id.middletext);
			TextView mtd = (TextView) v.findViewById(R.id.middletextdata);
			if (tt != null){
				tt.setText("Name: ");
			}
			if (ttd != null){
				ttd.setText(objects.get(position).getName());
			}
			if (mt != null){
				mt.setText("Bereits geschossene Bilder: ");
			}
			if (mtd != null)
			{
				mtd.setText(objects.get(position).getCount());
			}
		}
		return v;
	}
}
