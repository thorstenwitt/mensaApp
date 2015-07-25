package de.stdevelopment.MensaAppDatas;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;



@SuppressLint("NewApi")
public class MainActivity extends ListActivity {
	ShowDialogAsyncTask sd = new ShowDialogAsyncTask();
	//LunchParser pm = new LunchParser();
	public ArrayList<String> ls = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	
	boolean done=false;
	
	
	  
	  public class ShowDialogAsyncTask extends AsyncTask<Void, Integer, Void>{
	    	 
	        int progress_status;
	         
	        @Override
	     protected void onPreExecute() {
	      // update the UI immediately after the task is executed
	      super.onPreExecute();
	       
	     /*  Toast.makeText(download.this,
	               "Invoke onPreExecute()", Toast.LENGTH_SHORT).show();
	    */
	       progress_status = 0;
	       //txt_percentage.setText("downloading 0%");
	       
	     }
	         
	     @Override
	     protected Void doInBackground(Void... params) {
	      

	    	 
	      return null;
	     }
	     
	     @Override
	     protected void onProgressUpdate(Integer... values) {
	      super.onProgressUpdate(values);
	       
	      //progressBar.setProgress(values[0]);
	      //txt_percentage.setText("downloading " +values[0]+"%");
	       
	     }
	      
	     @Override
	     protected void onPostExecute(Void result) {
	      super.onPostExecute(result);
	      done = true;
	      adapter.addAll(ls);
			    
	/*       Toast.makeText(MainActivity.this,
	               "Invoke onPostExecute()", Toast.LENGTH_SHORT).show();
	  */      
	       //txt_percentage.setText("download complete");
	       //btn_start.setEnabled(true);
	     }
	 }	
	  public void onCreate(Bundle icicle) {
			super.onCreate(icicle);
			sd.execute();
			 
			while(done) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		     adapter = new ArrayAdapter<String>(this,
			        android.R.layout.simple_list_item_1, ls);
		    
		    setListAdapter(adapter);
		    ListView lv = getListView();

		     	    
		        // listening to single list item on click
		        lv.setOnItemClickListener(new OnItemClickListener() {
		          public void onItemClick(AdapterView<?> parent, View view,
		              int position, long id) {
		 
		              // selected item
		              String product = ((TextView) view).getText().toString();
		              
		              
		              Log.d("OnClick", product);
		            //  Toast.makeText(getApplicationContext(), pm.returnPreis(product), Toast.LENGTH_SHORT).show();
		              
		              // Launching new Activity on selecting single List Item
		 
		          }
		        });
		     

		  }
} 

