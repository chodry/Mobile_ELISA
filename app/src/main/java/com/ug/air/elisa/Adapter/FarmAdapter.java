package com.ug.air.elisa.Adapter;

import static com.ug.air.elisa.Fragments.FarmerList.UUID_SPECIAL;
import static com.ug.air.elisa.Fragments.Survey.SHARED_PREFS_2;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ug.air.elisa.Models.Farm;
import com.ug.air.elisa.Models.Form;
import com.ug.air.elisa.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FarmAdapter extends RecyclerView.Adapter<FarmAdapter.FarmHolder> {

    List<Farm> farmList;
    Context context;
    int row_index = -1;
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor2;

    private FarmAdapter.OnItemClickListener mListener;

    public FarmAdapter(List<Farm> farmList, Context context) {
        this.farmList = farmList;
        this.context = context;
    }

    public void setOnItemClickListener(FarmAdapter.OnItemClickListener listener){
        this.mListener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public FarmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.farm, parent, false);
        FarmAdapter.FarmHolder farmHolder = new FarmAdapter.FarmHolder(view, mListener);
        return farmHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FarmHolder holder, int position) {

        sharedPreferences2 = context.getSharedPreferences(SHARED_PREFS_2, 0);
        editor2 = sharedPreferences2.edit();

        Farm farm = farmList.get(position);
        holder.name.setText(farm.getName());
        holder.location.setText(farm.getLocation());
        holder.date.setText(farm.getDate());
        String uuid = farm.getAnimal();
        String special_uuid = farm.getOption();

//        if (special_uuid.equals(uuid)){
//            holder.imageView.setVisibility(View.VISIBLE);
//        }

//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                row_index = position;
//                notifyDataSetChanged();
//            }
//        });
//
//        if (row_index==position){
//            String uuid = farm.getAnimal();
//            holder.imageView.setVisibility(View.VISIBLE);
//            editor2.putString(UUID_SPECIAL, uuid);
//            editor2.apply();
//        }else {
//            holder.imageView.setVisibility(View.GONE);
//        }

    }

    @Override
    public int getItemCount() {
        return farmList.size();
    }

    public class FarmHolder extends RecyclerView.ViewHolder {

        TextView name, location, date;
        ImageView imageView;
        CardView linearLayout;

        public FarmHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            location = itemView.findViewById(R.id.location);
            date = itemView.findViewById(R.id.date);
            imageView = itemView.findViewById(R.id.ready);
            linearLayout = itemView.findViewById(R.id.farm);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                        if (imageView.getVisibility() == View.VISIBLE){
                            imageView.setVisibility(View.GONE);
                        }else{
                            imageView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

        }
    }
}
