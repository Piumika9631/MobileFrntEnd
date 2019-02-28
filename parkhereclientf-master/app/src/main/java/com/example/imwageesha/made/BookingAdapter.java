package com.example.imwageesha.made;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<com.example.imwageesha.made.BookingAdapter.MyViewHolder>{
        private List<Booking> bookings;



        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder
        public static class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView name,number,vehicle,date,arrival,departure;
            public LinearLayout main;
            public MyViewHolder(LinearLayout v) {
                super(v);
                name = v.findViewById(R.id.name);
                number = v.findViewById(R.id.number);
                vehicle = v.findViewById(R.id.vehicle);
                date = v.findViewById(R.id.date);
                arrival = v.findViewById(R.id.arrival);
                departure = v.findViewById(R.id.departure);
                main = v.findViewById(R.id.main);
            }


        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public BookingAdapter(List<Booking> bookings) {
            this.bookings = bookings;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public com.example.imwageesha.made.BookingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                                     int viewType) {
            // create a new view
            LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.booking_list_item, parent, false);

            com.example.imwageesha.made.BookingAdapter.MyViewHolder vh = new com.example.imwageesha.made.BookingAdapter.MyViewHolder(v);
            return vh;
        }



        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(com.example.imwageesha.made.BookingAdapter.MyViewHolder holder, final int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.name.setText(bookings.get(position).getParkName());
            holder.number.setText(bookings.get(position).getSlotNum());
            holder.vehicle.setText(bookings.get(position).getVehicleNum());
            holder.date.setText(bookings.get(position).getDate());
            holder.arrival.setText(""+bookings.get(position).getArivalTime());
            holder.departure.setText(""+bookings.get(position).getDepatureTime());
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return bookings.size();
        }

}
