package in.nj.nearby.common.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import in.nj.nearby.R;

/**
 * Created by hp on 30-11-2017.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.MyViewHolder> implements Filterable {

    List<String> itemList;
    List<String> filteredItemist;
    Set<String> checkedItems;

    public SearchListAdapter(List<String> itemList,Set<String> checkedItems){
        this.itemList = itemList;
        this.checkedItems = checkedItems;
        filteredItemist = itemList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.item.setText(filteredItemist.get(position));
        if(checkedItems.contains(filteredItemist.get(position))){
            holder.item.setChecked(true);
        }else holder.item.setChecked(false);
        holder.item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    checkedItems.add(filteredItemist.get(position));
                }else {
                    if(checkedItems.contains(filteredItemist.get(position))){
                        checkedItems.remove(filteredItemist.get(position));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredItemist.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String input = charSequence.toString();
                if(input.isEmpty()){
                    filteredItemist = itemList;
                }else {
                    List<String> temp = new ArrayList<>();
                    for (String s : itemList){
                        if(s.toLowerCase().contains(input.trim())){
                            temp.add(s);
                        }
                    }
                    filteredItemist = temp;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredItemist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredItemist = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public CheckBox item;

        public MyViewHolder(View view){
            super(view);
            item = (CheckBox)view.findViewById(R.id.item_checkBox);

        }

    }
}
