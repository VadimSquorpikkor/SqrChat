package com.squorpikkor.sqrchat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<ChatMessage> {
    public MessageAdapter(@NonNull Context context, int resource, List<ChatMessage> messages) {
        super(context, resource, messages);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.item_list, parent, false);
        }
        ImageView imgView = convertView.findViewById(R.id.photo);
        TextView text = convertView.findViewById(R.id.text);
        TextView name = convertView.findViewById(R.id.name);

        ChatMessage message = getItem(position);

        //сообщение может быть 2-х типов: текст или картинка

        //если getImgUrl возвращает null, значит сообщение -- это текст, иначе -- значит это картинка
        boolean isText = message.getImgUrl()==null;
        if (isText) {
            text.setVisibility(View.VISIBLE);
            imgView.setVisibility(View.GONE);
            text.setText(message.getText());
        } else {
            text.setVisibility(View.GONE);
            imgView.setVisibility(View.VISIBLE);
            Glide.with(imgView.getContext())
                    .load(message.getImgUrl())
                    .into(imgView);
        }

        name.setText(message.getName());

        return convertView;
    }
}

