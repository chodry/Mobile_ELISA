package com.ug.air.elisa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ug.air.elisa.Models.Form;
import com.ug.air.elisa.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.FormHolder> {

    List<Form> formList;
    Context context;

    private OnItemClickListener mListener;

    public FormAdapter(List<Form> formList, Context context) {
        this.formList = formList;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public FormHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.form, parent, false);
        FormHolder formHolder = new FormHolder(view, mListener);
        return formHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FormHolder holder, int position) {
        Form form = formList.get(position);
        holder.name.setText(form.getName());
        holder.location.setText(form.getLocation());
        String ani = form.getAnimal();
        if (ani.equals("cattle")){
            holder.circleImageView.setImageResource(R.drawable.cow);
        }else if (ani.equals("farm")){
            holder.circleImageView.setImageResource(R.drawable.livestock);
        }
        else {
            holder.circleImageView.setImageResource(R.drawable.pig);
        }
        holder.date.setText(form.getDate());
    }

    @Override
    public int getItemCount() {
        return formList.size();
    }

    public class FormHolder extends RecyclerView.ViewHolder {

        TextView name, location, date;
        CircleImageView circleImageView;

        public FormHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.location);
            date = itemView.findViewById(R.id.date);
            circleImageView = itemView.findViewById(R.id.animal);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}
