package com.ug.air.elisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ug.air.elisa.Models.Image;
import com.ug.air.elisa.R;

import java.util.List;

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.CameraViewHolder> {

    List<Image> imagesList;
    private OnItemClickListener mListener;
    Context context;

    public CameraAdapter(List<Image> imagesList, Context context) {
        this.imagesList = imagesList;
        this.context = context;
    }

    public interface OnItemClickListener {
        void onShowClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public CameraViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.symptom, parent, false);
        CameraViewHolder cameraViewHolder = new CameraViewHolder(view, mListener);
        return cameraViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CameraViewHolder holder, int position) {
        Image image = imagesList.get(position);
        holder.textView.setText(image.getSymptom());
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class CameraViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public CameraViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.symptom);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onShowClick(position);
                        }
                    }
                }
            });
        }
    }
}
