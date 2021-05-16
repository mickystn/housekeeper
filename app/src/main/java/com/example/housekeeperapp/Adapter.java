package com.example.housekeeperapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    public Adapter(Context context, ArrayList<User> list) {
        this.context = context;
        List = list;
    }
    private OnRecyclerViewListener listener;
    public interface OnRecyclerViewListener{
        void OnItemClick(int position);
    }
    public void OnRecyclerViewListener(OnRecyclerViewListener listener){
        this.listener = listener;

    }
    Context context;
    ArrayList<User> List;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = List.get(position);
        holder.Name.setText(user.getName());
        holder.Address.setText(user.getAddress());
        holder.Phone.setText(user.getPhone());
    }

    @Override
    public int getItemCount() {
        return List.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Name,Address,Phone;

        public MyViewHolder(@NonNull View itemView,OnRecyclerViewListener listener) {
            super(itemView);
            Name=itemView.findViewById(R.id.tvName);
            Address=itemView.findViewById(R.id.tvAddress);
            Phone = itemView.findViewById(R.id.tvPhone);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null&&getAdapterPosition()!=RecyclerView.NO_POSITION){
                        listener.OnItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
