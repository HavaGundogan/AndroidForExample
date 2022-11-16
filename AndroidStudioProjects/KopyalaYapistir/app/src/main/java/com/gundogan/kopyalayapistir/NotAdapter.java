package com.gundogan.kopyalayapistir;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiuimobileproject.databinding.RecyclerRowBinding;

import java.util.ArrayList;
public class NotAdapter extends RecyclerView.Adapter<NotAdapter.NotHolder>{
    ArrayList<NotConcructer> notConcructerArrayList;
    public NotAdapter(ArrayList<NotConcructer> notConcructerArrayList){
        this.notConcructerArrayList= notConcructerArrayList;
    }


    @Override
    public NotHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NotHolder(recyclerRowBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull NotHolder holder, int position) {

        holder.binding.recylerViewid.setText(notConcructerArrayList.get(position).notum);
    }

    @Override
    public int getItemCount() {
        return notConcructerArrayList.size();
    }

    public  class NotHolder extends RecyclerView.ViewHolder{

        private RecyclerRowBinding binding;
        public NotHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}