package com.example.kumawat.aapnishop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

//import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * Created by Kumawat on 4/22/2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{
    private Context ctx;
    private List<Item> itemList;

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    /*public interface Listener {
            void onExampleModelClicked(Item item);
        }
        private final Listener mListener;

        public ItemAdapter(Context context, Comparator<Item> comparator, Listener listener) {
            super(context, Item.class, comparator);
            mListener = listener;
        }

        @NonNull
        @Override
        protected ViewHolder<? extends Item> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
            final ItemWordBinding binding = ItemWordBinding.inflate(inflater, parent, false);
            return new WordViewHolder(binding, mListener);
        }

        public class ItemViewHolder extends SortedListAdapter.ViewHolder<Item> {

            private final ItemWordBinding mBinding;

            public ItemViewHolder(ItemWordBinding binding, ItemAdapter.Listener listener) {
                super(binding.getRoot());
                binding.setListener(listener);

                mBinding = binding;
            }

            @Override
            protected void performBind(@NonNull Item item) {

            }
        }
        */
    public ItemAdapter(){

    }
    public ItemAdapter(Context ctx, List<Item> itemList) {
        this.ctx = ctx;
        this.itemList = itemList;
        //mFilter = new CustomFilter(ItemAdapter.this);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_new_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        //getting the product of the specified position
        Item item = itemList.get(position);

        //binding the data with the viewholder views
        holder.product_name.setText(item.getpName());
        holder.product_details.setText(item.getpDetails());
        holder.purchase_price.setText(String.valueOf(item.getPurPrice()));
        holder.customer_price.setText(String.valueOf(item.getCustPrice()));

        //holder.imageView.setImageDrawable(ctx.getResources().getDrawable(item.getImage()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setFilter(ArrayList<Item> newItemList){
        itemList = new ArrayList<>();
        itemList.addAll(newItemList);
        notifyDataSetChanged();
    }

    public void addNewItemEntry(Item item) {
        itemList.add(item);
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView product_name, product_details, purchase_price, customer_price;
        ImageView imageView;
        public ItemViewHolder(View itemView) {
            super(itemView);

            product_name = itemView.findViewById(R.id.item_name);
            product_details = itemView.findViewById(R.id.item_details);
            purchase_price = itemView.findViewById(R.id.item_pur_price);
            customer_price = itemView.findViewById(R.id.item_cust_price);
            imageView = itemView.findViewById(R.id.item_imageView);
        }
    }
    public class CustomFilter extends Filter {
        private ItemAdapter mAdapter;
        private CustomFilter(ItemAdapter mAdapter) {
            super();
            this.mAdapter = mAdapter;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            itemList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                itemList.addAll(itemList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Item item : itemList) {
                    if (item.getpName().toLowerCase().startsWith(filterPattern)) {
                        itemList.add(item);
                    }
                }
            }
            System.out.println("Count Number " + itemList.size());
            results.values = itemList;
            results.count = itemList.size();
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            System.out.println("Count Number 2 " + ((List<Item>) results.values).size());
            this.mAdapter.notifyDataSetChanged();
        }
    }
}
