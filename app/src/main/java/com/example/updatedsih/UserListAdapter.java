package com.example.updatedsih;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder_User> {

    ArrayList<Map> UserList= new ArrayList<>();

    public UserListAdapter(ArrayList<Map> userList) {
        this.UserList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder_User onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist_layout_file,parent,false);

        return new MyViewHolder_User(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder_User holder, int position) {
      holder.userid.setText(UserList.get(position).get("HolderUserid").toString());
      holder.assetcount.setText(String.valueOf(UserList.get(position).get("No_of_assets")));
      holder.assetamount.setText(String.valueOf((long)UserList.get(position).get("Capital_invested")));
        holder.assetname.setText(UserList.get(position).get("Assets").toString());
        holder.village.setText(UserList.get(position).get("Village").toString());
        holder.state.setText(UserList.get(position).get("State").toString());
        holder.district.setText(UserList.get(position).get("District").toString());
        holder.scheme.setText(UserList.get(position).get("Scheme").toString());
        holder.numberoffarmers.setText(String.valueOf((long)UserList.get(position).get("Farmers")));
        holder.latitude.setText(String.valueOf((double)UserList.get(position).get("Latitude")));
        holder.longitude.setText(String.valueOf((double)UserList.get(position).get("Longitude")));
        holder.milk.setText(String.valueOf((long)UserList.get(position).get("Milk")));
    }

    @Override
    public int getItemCount() {
        return UserList.size();
    }

    public class MyViewHolder_User extends RecyclerView.ViewHolder {
        TextView userid,assetcount,assetamount,assetname,village,district,state,scheme,numberoffarmers,latitude,longitude,milk;
        public MyViewHolder_User(@NonNull View itemView) {
            super(itemView);
            userid=itemView.findViewById(R.id.userid);
            assetcount=itemView.findViewById(R.id.assetsnumber);
            assetamount=itemView.findViewById(R.id.capital_invested);
            assetname=itemView.findViewById(R.id.assetsname);
            village=itemView.findViewById(R.id.village);
            district=itemView.findViewById(R.id.district);
            state=itemView.findViewById(R.id.state);
            scheme=itemView.findViewById(R.id.scheme);
            numberoffarmers=itemView.findViewById(R.id.farmers);
            latitude=itemView.findViewById(R.id.latitude);
            longitude=itemView.findViewById(R.id.longitude);
            milk=itemView.findViewById(R.id.milk);

        }
    }
}
