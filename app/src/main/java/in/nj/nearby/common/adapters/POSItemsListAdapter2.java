package in.nj.nearby.common.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import in.nj.nearby.R;
import in.nj.nearby.model.POSModel;
import in.nj.nearby.views.OffersListActivity;

/**
 * Created by nitesh on 07-12-2017.
 */

public class POSItemsListAdapter2 extends RecyclerView.Adapter<POSItemsListAdapter2.MyViewHolder>{

    private OffersListActivity.OnClickListener onClickListener;
    private List<POSModel> posModelList;
    private Context context;

    public POSItemsListAdapter2(List<POSModel> posModelList, Context context) {
        this.posModelList = posModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_pos_items_two,parent,false);
        return new POSItemsListAdapter2.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        POSModel posModel = posModelList.get(position);
        holder.merchant.setText(posModel.getTitle());
        holder.category.setText(posModel.getCategory());
        holder.address.setText(posModel.getAddress());
        holder.rewardsMultiplier.setText((posModel.getRewardMultiplier()+"X"));
    }

    @Override
    public int getItemCount() {
        return posModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView merchant,address,category,rewardsMultiplier;

        public MyViewHolder(View view){
            super(view);
            view.setOnClickListener(this);
            merchant = (TextView)view.findViewById(R.id.title);
            address = (TextView)view.findViewById(R.id.address);
            category = (TextView)view.findViewById(R.id.category);
            rewardsMultiplier = (TextView)view.findViewById(R.id.rewardMultiplier);
        }

        @Override
        public void onClick(View view) {
            onClickListener.onClick(view,getAdapterPosition());
        }
    }

    public void setOnClickListener(OffersListActivity.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public List<POSModel> getPosModelList() {
        return posModelList;
    }
}
