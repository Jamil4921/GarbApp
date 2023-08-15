package be.ehb.garbapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import be.ehb.garbapp.GarbUser;
import be.ehb.garbapp.R;
import be.ehb.garbapp.Report;

public class GarbUserAdapter extends RecyclerView.Adapter<GarbUserAdapter.ViewHolder> {



    private ArrayList<GarbUser> garbUserList;

    public GarbUserAdapter (){
        garbUserList = new ArrayList<>();
    }

    public void addItems(List<GarbUser> garbUserList){
        this.garbUserList = new ArrayList<>(garbUserList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_rankingUserName, tv_ranking_totalPoints;
        ImageView ranking_imageView;
        public ViewHolder(View view){
            super(view);

            tv_rankingUserName = view.findViewById(R.id.ranking_username);
            tv_ranking_totalPoints = view.findViewById(R.id.ranking_totalPoints);
            ranking_imageView = view.findViewById(R.id.rank_imageView);
        }
    }

    @NonNull
    @Override
    public GarbUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_garbusers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GarbUser garbUser = garbUserList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(garbUser.getProfilePictureUrl())
                .into(holder.ranking_imageView);

        //holder.title.setText(reports.getTitle());
        holder.tv_rankingUserName.setText(garbUser.getName());
        holder.tv_ranking_totalPoints.setText(String.valueOf(garbUser.getTotalPoints()));

    }

    @Override
    public int getItemCount() {
        return garbUserList.size();
    }
}
