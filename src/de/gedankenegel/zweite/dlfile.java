package de.gedankenegel.zweite;

import android.annotation.SuppressLint;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import android.util.Log;




public class dlfile {
	
	public void DownloadFromUrl(String fileURL, String fileName) {  //this is the downloader method
	       try {
	               URL url = new URL( fileURL); 
	               File file = new File(fileName);
	               file.createNewFile();
	               long startTime = System.currentTimeMillis();
	               Log.d("DownloadManager", "download begining");
	               Log.d("DownloadManager", "download url:" + url);
	               Log.d("DownloadManager", "downloaded file name:" + fileName);
	               /* Open a connection to that URL. */
	               URLConnection ucon = url.openConnection();

	               /*
	                * Define InputStreams to read from the URLConnection.
	                */
	               InputStream is = ucon.getInputStream();

	               /*
	                * Read bytes to the Buffer until there is nothing more to read(-1) and write on the fly in the file.
	                */
	               FileOutputStream fos = new FileOutputStream(file);
	               final int BUFFER_SIZE = 23 * 1024;
	               BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
	               byte[] baf = new byte[BUFFER_SIZE];
	               int actual = 0;
	               while (actual != -1) {
	                   fos.write(baf, 0, actual);
	                   actual = bis.read(baf, 0, BUFFER_SIZE);
	               }

	               fos.close();
	               Log.d("DownloadManager", "download ready in"
	                               + ((System.currentTimeMillis() - startTime) / 1000)
	                               + " sec");

	       } catch (IOException e) {
	               Log.d("DownloadManager", "Error: " + e);
	       }

	}
	

      
 
}

