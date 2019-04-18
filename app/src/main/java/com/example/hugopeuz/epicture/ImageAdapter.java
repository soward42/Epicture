package com.example.hugopeuz.epicture;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.AdapterView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    private List<String> imagesLinks;
    private LayoutInflater inflater;

    public ImageAdapter(Context cont, List<String> Links) {
        context = cont;
        imagesLinks = Links;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imagesLinks.size();
    }

    @Override
    public Object getItem(int position) {
        return imagesLinks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gallery_item, parent, false);
        }
        Glide.with(context)
                .load(imagesLinks.get(position))
                .into((ImageView) convertView);
        return convertView;
    }
}
