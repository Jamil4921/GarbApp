package be.ehb.garbapp.Adapter;

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

import be.ehb.garbapp.R;
import be.ehb.garbapp.Report;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {


    private ArrayList<Report> reportsList;

    public ReportAdapter (){
        reportsList= new ArrayList<>();
    }

    public void addItems(List<Report> reportList){
        this.reportsList = new ArrayList<>(reportList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, description, repostUserName, approved, createdPost, address;
        ImageView image_picture;
        public ViewHolder(View view){
            super(view);

            title = view.findViewById(R.id.rv_title);
            description = view.findViewById(R.id.rv_description);
            repostUserName = view.findViewById(R.id.rv_repostUserName);
            approved = view.findViewById(R.id.rv_approved);
            createdPost = view.findViewById(R.id.rv_createdPost);
            address = view.findViewById(R.id.rv_address);
            image_picture = view.findViewById(R.id.rv_imageView_picture);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_reports, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){


        Report reports = reportsList.get(position);

        Glide.with(holder.itemView.getContext())
                .load(reports.getPictureUrl())
                .into(holder.image_picture);
        holder.title.setText(reports.getTitle());
        holder.description.setText(reports.getDescription());
        holder.repostUserName.setText(reports.getRepostUserName());
        holder.address.setText(reports.getAddress());
        if (reports.isApproved()) {
            holder.approved.setText("Approved");
        } else {
            holder.approved.setText("Pending");
        }

    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }
}
