package com.ticketing.skyloop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ticketing.skyloop.Activity.SeatListActivity;
import com.ticketing.skyloop.Model.Flight;
import com.ticketing.skyloop.databinding.ViewholderFlightBinding;

import java.util.ArrayList;

public class FlightAdapter extends RecyclerView.Adapter<FlightAdapter.Viewholder> {
    private final ArrayList<Flight> flights;
    private Context context;

    public FlightAdapter(ArrayList<Flight> flights) {
        this.flights = flights;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderFlightBinding binding = ViewholderFlightBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Flight flight = flights.get(position);
        Glide.with(context)
                .load(flight.getAirlineLogo())
                .into(holder.binding.logo);

        holder.binding.fromTxt.setText(flight.getFrom());
        holder.binding.fromShortTxt.setText(flight.getFromShort());
        holder.binding.toTxt.setText(flight.getTo());
        holder.binding.toShortTxt.setText(flight.getToShort());
        holder.binding.arrivalTxt.setText(flight.getArriveTime());
        holder.binding.classTxt.setText(flight.getClassSeat());
        holder.binding.priceTxt.setText("Rp"+flight.getPrice()+"'-");

        holder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(context, SeatListActivity.class);
            intent.putExtra("flight",flight);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return flights.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private final ViewholderFlightBinding binding;

        public Viewholder(ViewholderFlightBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
