package com.example.updatedsih;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
public  class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder_states>{

    private ArrayList<String> statelist;
    private RecyclerViewListner listner;

    public StateAdapter(ArrayList<String> statelist, RecyclerViewListner listner) {
        this.statelist = statelist;
        this.listner = listner;
    }

    @NonNull
    @Override
    public MyViewHolder_states onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_layout_file,parent,false);
       return new MyViewHolder_states(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder_states holder, int position) {
        holder.t_statename.setText(statelist.get(position));
        holder.index.setText(String.valueOf(position+1));
    }


    @Override
    public int getItemCount() {
        return statelist.size();
    }

    public interface RecyclerViewListner{
        void  onClick(View v, int position);

    }

    public class MyViewHolder_states extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView t_statename,index;
        public MyViewHolder_states(@NonNull View itemView) {
            super(itemView);
            t_statename=itemView.findViewById(R.id.t_statename);
            index=itemView.findViewById(R.id.index);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listner.onClick(view,getAbsoluteAdapterPosition());
        }
    }
}