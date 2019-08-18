package com.example.kumawat.aapnishop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Kumawat on 4/24/2018.
 */

class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>{
    private Context ctx;
    private List<Transaction> transactionList;
    public TransactionAdapter(Context ctx, List<Transaction> transactionList) {
        this.ctx = ctx;
        this.transactionList = transactionList;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_history_item, parent, false);
        return new TransactionAdapter.TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        //getting the product of the specified position
        Transaction transaction = transactionList.get(position);

        //binding the data with the viewholder views
        holder.tr_amount.setText(String.valueOf(transaction.getTrAmount()));
        holder.tr_details.setText(transaction.getTrDetails());
        holder.tr_type.setText(transaction.getTrType());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    public void setAllClearItem() {
        transactionList.clear();
        notifyDataSetChanged();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView tr_amount, tr_details, tr_type;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            tr_amount = itemView.findViewById(R.id.transaction_amount);
            tr_details = itemView.findViewById(R.id.transaction_details);
            tr_type = itemView.findViewById(R.id.transaction_type);
        }
    }
}
