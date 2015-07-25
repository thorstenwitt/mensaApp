package de.stdevelopment.MensaAppDatas;

import java.io.File;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;


public class openFile extends Activity {

	public void openPdf (String s) {
		
	
	 File pdfFile = new File(s); 
     if(pdfFile.exists()) 
     {
         Uri path = Uri.fromFile(pdfFile); 
         Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
         pdfIntent.setDataAndType(path, "application/pdf");
         pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

         try
         {
             startActivity(pdfIntent);
         }
         catch(ActivityNotFoundException e)
         {
             Toast.makeText(openFile.this, "No Application available to view pdf", Toast.LENGTH_LONG).show(); 
         }
     }

	}

	

}
