package de.tubs.ibr.dtn.chat;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;


import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.util.Strings;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import constant.GeneralData;
import constant.GlobalData;
import dao.MessageDestination;
import dataaccess.MessageDestinationDataAccess;
import dataaccess.UserDataAccess;
import de.tubs.ibr.dtn.chat.service.ChatService;
import de.tubs.ibr.dtn.chat.service.Utils;
import mqtt.PahoMQTT;

import static android.content.Context.ACTIVITY_SERVICE;

public class RosterFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    boolean mDualPane;
    Long mBuddyId = null;
	
    private final static int LOADER_ID = 1;
    
	@SuppressWarnings("unused")
	private final String TAG = "RosterFragment";
	
	private RosterAdapter mAdapter = null;

	private ChatService mService = null;
	private boolean mBound = false;
	
	private MenuItem mMenuShowOffline = null;

	String buffer;
	public static Long defaultBuddyId;
	
	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((ChatService.LocalBinder)service).getService();
			switch (mService.getServiceError()) {
			case NO_ERROR:
				//adding from me
				//add buddy in roster for attaching message history display
				//if user logged as mqtt static relay(bypass user)
				if(GeneralData.getUserId().equalsIgnoreCase(GlobalData.mqttUser)){
//					defaultBuddyId = mService.getRoster().updatePresence(GlobalData.buddyEndpoint, new Date(), null, GlobalData.buddyEndpoint, null, null, null, null, 0L);
					updateRoster(mService, getContext());
				}

				break;
				
			case SERVICE_NOT_FOUND:
				Utils.showInstallServiceDialog(getActivity());
				break;
				
			case PERMISSION_NOT_GRANTED:
				Utils.showReinstallDialog(getActivity());
				break;
			}
			
			// load roster
			getLoaderManager().initLoader(LOADER_ID, null, RosterFragment.this);
		}

		public void onServiceDisconnected(ComponentName name) {
			getLoaderManager().destroyLoader(LOADER_ID);
			mService = null;
		}
	};

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//adding from me
		//received message listener for MQTT
		// if user logged as mqtt static relay (bypas user)
		if(GeneralData.getUserId().equalsIgnoreCase(GlobalData.mqttUser)){
			PahoMQTT mqttClient = new PahoMQTT();
			mqttClient.initPahoMQTT(getContext());
			mqttClient.setOnActionListener(new IMqttMessageListener() {
				@Override
				public void messageArrived(String topic, MqttMessage message) throws Exception {
					buffer = new String(message.getPayload());
//				System.out.println("Message: " + topic + " : " + new String(message.getPayload()));
					if(GeneralData.getBuddyEndpointMap() != null && GeneralData.getBuddyEndpointMap().size() != 0){
						for (HashMap.Entry<String, Long> entry : GeneralData.getBuddyEndpointMap().entrySet()) {
							Long buddyId = entry.getValue();
							forwardMessage(buffer, buddyId);
						}
					}
				}
			});
		}
	}



	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.buddy_menu, menu);
		inflater.inflate(R.menu.logout_menu, menu);

		//adding from me
		if(GeneralData.getUserId().equalsIgnoreCase(GlobalData.mqttUser)) {
			inflater.inflate(R.menu.system_menu, menu);
		}

	    if (0 != (getActivity().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
	    	inflater.inflate(R.menu.debug_menu, menu);
	    }
	    
	    mMenuShowOffline = menu.findItem(R.id.itemHideOffline);
	    
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
	    mMenuShowOffline.setChecked(prefs.getBoolean("hideOffline", false));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	    case R.id.itemPreferences:
	    {
			// Launch Preference activity
			Intent i = new Intent(getActivity(), Preferences.class);
			startActivity(i);
	        return true;
	    }
	    
	    case R.id.itemHideOffline:
	    {
		    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
		    boolean previousState = prefs.getBoolean("hideOffline", false);
		    mMenuShowOffline.setChecked(!previousState);
		    prefs.edit().putBoolean("hideOffline", !previousState).commit();
	    	return true;
	    }
	    
	    case R.id.itemDebugNotification:
	    	if (mService != null)
				mService.startDebug(ChatService.Debug.NOTIFICATION);
	    	return true;
	    	
	    case R.id.itemDebugBuddyAdd:
	    	if (mService != null)
				mService.startDebug(ChatService.Debug.BUDDY_ADD);
	    	return true;
	    	
	    case R.id.itemDebugSendPresence:
            if (mService != null)
                mService.startDebug(ChatService.Debug.SEND_PRESENCE);
	        return true;
	        
	    case R.id.itemDebugUnregister:
            if (mService != null)
                mService.startDebug(ChatService.Debug.UNREGISTER);
            getActivity().finish();
	        return true;

		//adding from me
		case R.id.addDestination:
			if (mService != null) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddDestinationActivity.class);
                startActivity(intent);
			}
			return true;
		case R.id.logout:
			if (mService != null) {
				//still not stable cause background service crash
				clearData();
				goToLogin();
			}
			return true;
    
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// enable options menu
		setHasOptionsMenu(true);
		
		// enable context menu
		registerForContextMenu(getListView());
		
        // Create an empty adapter we will use to display the loaded data.
        mAdapter = new RosterAdapter(getActivity(), null, new RosterAdapter.ColumnsMap());
        setListAdapter(mAdapter);
		
        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View messagesFrame = getActivity().findViewById(R.id.messages);
        mDualPane = messagesFrame != null && messagesFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null) {
            // Restore last state for checked position.
        	mBuddyId = savedInstanceState.getLong(ChatService.EXTRA_BUDDY_ID, -1);
        	if (mBuddyId.equals(-1L)) mBuddyId = null;
        } else {
        	Intent i = getActivity().getIntent();
        	if (i != null)
        	{
        		Bundle extras = i.getExtras();
            	if (extras != null) {
            		mBuddyId = extras.getLong(ChatService.EXTRA_BUDDY_ID, -1);
            		if (mBuddyId.equals(-1L)) mBuddyId = null;
            	}
        	}
        }

        if (mDualPane) {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        
        // Make sure our UI is in the correct state.
        if (mBuddyId != null) showMessages(mBuddyId);

        // Start out with a progress indicator.
        setListShown(false);
	}
	  
    public void showMessages(Long buddyId) {
    	mBuddyId = buddyId;
    	
        if (mDualPane) {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
    		if (mAdapter != null)
    			mAdapter.setSelected(buddyId);
    		
            // Check what fragment is currently shown, replace if needed.
            MessageFragment messages = (MessageFragment)
                    getFragmentManager().findFragmentById(R.id.messages);
            if (messages == null || messages.getBuddyId() != buddyId) {
                // Make new fragment to show this selection.
            	messages = MessageFragment.newInstance(buddyId);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.messages, messages);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        } else {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), MessageActivity.class);
            intent.putExtra(ChatService.EXTRA_BUDDY_ID, buddyId);
            startActivity(intent);
        }
    }
    
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mBuddyId != null)
			outState.putLong(ChatService.EXTRA_BUDDY_ID, mBuddyId);
		else
			outState.remove(ChatService.EXTRA_BUDDY_ID);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// get buddy of position
		RosterItem ritem = (RosterItem)v;
		if (ritem != null)
			showMessages(ritem.getBuddyId());
	}
    
	@Override
	public void onResume() {
		super.onResume();
		
		// Establish a connection with the service.  We use an explicit
		// class name because we want a specific service implementation that
		// we know will be running in our own process (and thus won't be
		// supporting component replacement by other applications).
		// supporting component replacement by other applications).
		if (!mBound) {
			getActivity().bindService(new Intent(getActivity(), ChatService.class), mConnection, Context.BIND_AUTO_CREATE);
			mBound = true;
		}

	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroy() {
		// unbind from service
		if (mBound) {
			getActivity().unbindService(mConnection);
			getLoaderManager().destroyLoader(LOADER_ID);
			mBound = false;
		}
		
		super.onDestroy();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.buddycontext_menu, menu);
		
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
		RosterItem ritem = (RosterItem)info.targetView;
		
		if (ritem.isPinned()) {
			menu.removeItem(R.id.itemPin);
		} else {
			menu.removeItem(R.id.itemUnpin);
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		if (info.targetView instanceof RosterItem) {
			RosterItem ritem = (RosterItem)info.targetView;

			switch (item.getItemId())
			{
			case R.id.itemDelete:
				if (mService != null) {
					mService.getRoster().removeBuddy(ritem.getBuddyId());
				}
				return true;
			case R.id.itemPin:
				if (mService != null) {
					mService.getRoster().setPinned(ritem.getBuddyId(), true);
				}
				return true;
			case R.id.itemUnpin:
				if (mService != null) {
					mService.getRoster().setPinned(ritem.getBuddyId(), false);
				}
				return true;
			default:
				return super.onContextItemSelected(item);
			}
		}
		
		return super.onContextItemSelected(item);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new RosterLoader(getActivity(), mService);
	}
	
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        mAdapter.swapCursor(data);
        
        // The list should now be shown.
        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(true);
        }
	}
	
	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        mAdapter.swapCursor(null);
	}

	//adding from me
	//forward message to sending message service
	public void forwardMessage(String message, Long buddyId){
        //adding from me
        //check for wifi auto connection
        if(!LoginActivity.wiFiConnection.isConnected()){
            LoginActivity.wiFiConnection.connect();
        }

		final Intent intent = new Intent(getActivity(), ChatService.class);
		intent.setAction(ChatService.ACTION_SEND_MESSAGE);
//		intent.putExtra(ChatService.EXTRA_BUDDY_ID, defaultBuddyId);
		intent.putExtra(ChatService.EXTRA_BUDDY_ID, buddyId);
		intent.putExtra(ChatService.EXTRA_TEXT_BODY, message);
		getActivity().startService(intent);
	}

	//adding from me
	public void clearData(){
		//clear data
//		if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
//			((ActivityManager)getContext().getSystemService(ACTIVITY_SERVICE))
//					.clearApplicationUserData(); // note: it has a return value!
//		} else {
//			// use old hacky way, which can be removed
//			// once minSdkVersion goes above 19 in a few years.
//		}

		//stop background process
		List<ApplicationInfo> packages;
		PackageManager pm;
		pm = getContext().getPackageManager();
		//get a list of installed apps.
		packages = pm.getInstalledApplications(0);

		ActivityManager mActivityManager = (ActivityManager)getContext().getSystemService(Context.ACTIVITY_SERVICE);

		for (ApplicationInfo packageInfo : packages) {
			if(packageInfo.packageName.equals("de.tubs.ibr.dtn.chat")) {
				mActivityManager.killBackgroundProcesses(packageInfo.packageName);
			}
		}
	}

	//adding from me
	public void goToLogin(){
		UserDataAccess.clean(getContext());
		GeneralData.setPresenceHackFlag(false);
		GeneralData.setToken(null);
		GeneralData.setPassword(null);
		GeneralData.setUserId(null);
		GeneralData.setUuidUser(null);
		Intent intent = new Intent(getContext(), LoginActivity.class);
		startActivity(intent);
	}

	//adding from me
	public static void updateRoster(ChatService service, Context context){
		GeneralData.setBuddyEndpointMap(new HashMap<String, Long>());
		List<MessageDestination> messageDestinationList = MessageDestinationDataAccess.getAll(context);
		if(messageDestinationList != null){
			if(messageDestinationList.size() != 0){
				for(MessageDestination messageDestination : messageDestinationList){
					Long buddyId;
					String buddyEndpoint = messageDestination.getDestination();
                    buddyEndpoint = "dtn://"+buddyEndpoint+".dtn/chat";
					buddyId = service.getRoster().updatePresence(buddyEndpoint, new Date(), null, buddyEndpoint, null, null, null, null, 0L);
					GeneralData.getBuddyEndpointMap().put(buddyEndpoint, buddyId);
					messageDestination.setBuddy_id(buddyId.toString());
					MessageDestinationDataAccess.addOrReplace(context, messageDestination);
				}
			}
		}
	}
}
