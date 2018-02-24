package de.tubs.ibr.dtn.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import constant.GeneralData;
import dao.MessageDestination;
import dataaccess.MessageDestinationDataAccess;

import static android.view.View.GONE;

/**
 * Created by danu on 6/10/17.
 */

public class DestinationListAdapter extends ArrayAdapter<MessageDestination> {

    private List<MessageDestination> dataSet;
    Context mContext;

    public DestinationListAdapter(List<MessageDestination> data, Context context){
        super(context, R.layout.destination_list_item, data);
        this.dataSet = data;
        this.mContext=context;
    }

    // View lookup cache
    private static class ViewHolder {
        TextView txtDest;
        Button btnRemoveDest;
    }

    private int lastPosition = -1;

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final MessageDestination messageDestination = getItem(position);

        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.destination_list_item, parent, false);
            viewHolder.txtDest = (TextView) convertView.findViewById(R.id.text_destination);
            viewHolder.btnRemoveDest = (Button) convertView.findViewById(R.id.button_remove_destination);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtDest.setText(messageDestination.getDestination());
        viewHolder.btnRemoveDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDestinationDataAccess.deleteOne(mContext, messageDestination);
                dataSet.remove(position);
                if(messageDestination.getBuddy_id()!= null){
                    AddDestinationActivity.deleteRoster(Long.parseLong(messageDestination.getBuddy_id()));
                    GeneralData.getBuddyEndpointMap().remove(Long.parseLong(messageDestination.getBuddy_id()));
                }
                notifyDataSetChanged();
                if(dataSet.size() == 0){
                    AddDestinationActivity.listView.setVisibility(GONE);
                    AddDestinationActivity.txtPleaseInsert.setVisibility(View.VISIBLE);
                }

                Toast.makeText(mContext, "Data deleted", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
