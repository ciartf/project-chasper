package de.tubs.ibr.dtn.chat;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import constant.GeneralData;
import constant.GlobalData;
import dao.MessageDestination;
import dataaccess.MessageDestinationDataAccess;
import de.tubs.ibr.dtn.chat.service.ChatService;
import de.tubs.ibr.dtn.chat.service.Utils;
import tool.Tool;

/**
 * Created by danu on 6/9/17.
 */

public class AddDestinationActivity extends FragmentActivity {
    Button btnAddDest;
    EditText txtAddDest;
    Context context;
    public static ListView listView;
    List<MessageDestination> messageDestinationList;
    private static DestinationListAdapter adapter;
    public static TextView txtPleaseInsert;

    private static ChatService mService = null;
    private boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ((ChatService.LocalBinder)service).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_destination_fragment);
        btnAddDest = (Button) findViewById(R.id.button_ddd_destination);
        txtAddDest = (EditText) findViewById(R.id.text_ddd_destination);
        listView=(ListView)findViewById(R.id.list_destination);
        txtPleaseInsert = (TextView) findViewById(R.id.text_please_insert);
        context = getApplicationContext();

        txtPleaseInsert.setVisibility(View.GONE);

        messageDestinationList = MessageDestinationDataAccess.getAll(context);
        if(messageDestinationList == null){
            messageDestinationList = new ArrayList<MessageDestination>();
            listView.setVisibility(View.GONE);
            txtPleaseInsert.setVisibility(View.VISIBLE);
        }

        btnAddDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDestination = txtAddDest.getText().toString();
                String uuid = Tool.getUUID();
                MessageDestination messageDestination = new MessageDestination();
                messageDestination.setUuid_destination(uuid);
                messageDestination.setDestination(newDestination);
                Long buddyId = addRoster(newDestination);
                messageDestination.setBuddy_id(buddyId.toString());
                if(GeneralData.getBuddyEndpointMap() == null){
                    GeneralData.setBuddyEndpointMap(new HashMap<String, Long>());
                }
                GeneralData.getBuddyEndpointMap().put(newDestination, buddyId);
                messageDestinationList.add(messageDestination);
                MessageDestinationDataAccess.addOrReplace(context, messageDestination);
                if(listView.getVisibility() == View.GONE){
                    listView.setVisibility(View.VISIBLE);
                    txtPleaseInsert.setVisibility(View.GONE);

                }
                txtAddDest.setText("");
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

                Toast.makeText(context, "Data saved", Toast.LENGTH_SHORT).show();
            }
        });


        adapter= new DestinationListAdapter(messageDestinationList, context);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!mBound) {
            this.bindService(new Intent(this, ChatService.class), mConnection, Context.BIND_AUTO_CREATE);
            mBound = true;
        }
    }

    @Override
    public void onDestroy() {
        // unbind from service
        if (mBound) {
            this.unbindService(mConnection);
            mBound = false;
        }

        super.onDestroy();
    }

    public static void deleteRoster(Long buddyId){
        if (mService != null) {
            mService.getRoster().removeBuddy(buddyId);
        }
    }

    public static Long addRoster(String buddyEndpoint){
        long buddyId = -1l;
        if (mService != null) {
            buddyEndpoint = "dtn://"+buddyEndpoint+".dtn/chat";
            buddyId = mService.getRoster().updatePresence(buddyEndpoint, new Date(), null, buddyEndpoint, null, null, null, null, 0L);
        }
        if(buddyId > -1){
            return buddyId;
        }else{
            return null;
        }
    }
}
