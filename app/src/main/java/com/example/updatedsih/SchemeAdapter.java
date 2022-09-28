package com.example.updatedsih;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import io.reactivex.rxjava3.internal.operators.mixed.SingleFlatMapObservable;

public class SchemeAdapter extends RecyclerView.Adapter<SchemeAdapter.MyViewHolder> {

    private ArrayList<String> schemelist;
    private ArrayList<String> descriptionlist;
    private RecyclerViewListner listner;

    public SchemeAdapter(ArrayList<String> schemelist,ArrayList<String> descriptionlist, RecyclerViewListner listner) {
        this.schemelist = schemelist;
        this.descriptionlist = descriptionlist;
        this.listner = listner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scheme_layout_file,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       holder.t_schemename.setText(schemelist.get(position));
       holder.index.setText(String.valueOf(position+1));
       holder.description.setText(descriptionlist.get(position));
       holder.info.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
              if (holder.description.getVisibility()==View.VISIBLE){
                  holder.description.setVisibility(View.GONE);
              }else{
                  holder.description.setVisibility(View.VISIBLE);
              }
           }
       });
    }

    @Override
    public int getItemCount() {
        return schemelist.size();
    }

    public interface RecyclerViewListner{
        void onClick(View v, int position);

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
         TextView t_schemename,index,description;
         ImageView info;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);
            t_schemename=(TextView) itemView.findViewById(R.id.t_schemename);
            info= itemView.findViewById(R.id.imageButton);
            description=(TextView) itemView.findViewById(R.id.t_description);
            index=(TextView) itemView.findViewById(R.id.index);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            listner.onClick(view,getAdapterPosition());
        }
    }
}
