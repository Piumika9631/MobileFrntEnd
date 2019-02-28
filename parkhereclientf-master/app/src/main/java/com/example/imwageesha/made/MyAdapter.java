package com.example.imwageesha.made;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private List<Vehicle> vehicles;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name,number,type;
        public LinearLayout main;
        public MyViewHolder(LinearLayout v) {
            super(v);
            name = v.findViewById(R.id.name);
            number = v.findViewById(R.id.number);
            type = v.findViewById(R.id.type);
            main = v.findViewById(R.id.main);
        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }



    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(vehicles.get(position).name);
        holder.number.setText(vehicles.get(position).number);
        holder.type.setText(vehicles.get(position).type);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return vehicles.size();
    }

}
