package com.example.kumawat.aapnishop;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

//import com.github.wrdlbrnft.sortedlistadapter.SortedListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kumawat on 4/22/2018.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{
    private Context ctx;
    public List<Item> itemList=new ArrayList<>();
    public List<Item> selected_itemList=new ArrayList<>();

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public ItemAdapter(){

    }
    public ItemAdapter(Context ctx, List<Item> itemList, List<Item> selected_itemList) {
        this.ctx = ctx;
        this.itemList = itemList;
        this.selected_itemList = selected_itemList;
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

    public List<Item> addNewItemEntry(Item item) {
        itemList.add(item);
        notifyDataSetChanged();
        return itemList;
    }

    public void setAllClearItem() {
        itemList.clear();
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
