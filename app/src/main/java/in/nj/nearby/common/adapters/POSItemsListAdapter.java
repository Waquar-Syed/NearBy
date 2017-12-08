package in.nj.nearby.common.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.nj.nearby.R;
import in.nj.nearby.model.POSModel;

/**
 * Created by nitesh on 07-12-2017.
 */

public class POSItemsListAdapter extends RecyclerView.Adapter<POSItemsListAdapter.MyViewHolder>{

    private List<POSModel> posModelList;
    private Context context;

    public POSItemsListAdapter(List<POSModel> posModelList,Context context) {
        this.posModelList = posModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pos_items,parent,false);
        return new POSItemsListAdapter.MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        POSModel posModel = posModelList.get(position);
        holder.merchant.setText(posModel.getTitle());
        holder.category.setText(posModel.getCategory());
        holder.address.setText(posModel.getAddress());
        holder.offers.setText(posModel.getOffers());
        holder.navigate.setOnClickListener(posModel.getOnNavigationClickListener());
        holder.share.setOnClickListener(posModel.getOnShareClickListener());
        holder.call.setOnClickListener(posModel.getOnCallClickListener());

        String catagory = posModel.getCategory();
        if(catagory.toLowerCase().contains("elect")){
            holder.bg.setBackground(context.getResources().getDrawable(R.drawable.electronics));
        }else
        if(catagory.toLowerCase().contains("trav")){
            holder.bg.setBackground(context.getResources().getDrawable(R.drawable.travle));
        }else
        if(catagory.toLowerCase().contains("clot")){
            holder.bg.setBackground(context.getResources().getDrawable(R.drawable.cloths));
        }else
        if(catagory.toLowerCase().contains("din")){
            holder.bg.setBackground(context.getResources().getDrawable(R.drawable.dining));
        }
    }

    @Override
    public int getItemCount() {
        return posModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView merchant,address,offers,category;
        Button navigate,share,call;
        LinearLayout bg;

        public MyViewHolder(View view){
            super(view);
            merchant = (TextView)view.findViewById(R.id.merchant);
            address = (TextView)view.findViewById(R.id.address);
            offers = (TextView)view.findViewById(R.id.offer);
            category = (TextView)view.findViewById(R.id.category);

            navigate = (Button)view.findViewById(R.id.navigate);
            share= (Button)view.findViewById(R.id.share);
            call = (Button)view.findViewById(R.id.call);

            bg = (LinearLayout)view.findViewById(R.id.bg_image);
        }

    }
}
