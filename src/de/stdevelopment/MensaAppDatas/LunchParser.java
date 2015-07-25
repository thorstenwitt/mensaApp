package de.stdevelopment.MensaAppDatas;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import java.util.LinkedList;
import java.util.Queue;



public class LunchParser {
	
	Queue<String> requestedDays = new LinkedList<String>();
	NumberFormat nf = NumberFormat.getInstance();
	ArrayList<Tagesmenues> gerichteListe = new ArrayList<Tagesmenues>();
	private static LunchParser instance = null;

	private LunchParser() {
		
	}
	public static LunchParser getInstance() {
		if(instance == null) {
			instance = new LunchParser();
		}
		return instance;
	}
	
	public static ArrayList<Tagesmenues> exampleDatas() {
		Mittagsgericht m1 = new Mittagsgericht("Schnitzel",1,2,3);
		Mittagsgericht m2 = new Mittagsgericht("Nudeln",0.5f,1f,1.5f);
		Mittagsgericht m3 = new Mittagsgericht("Wurst",2,4,6);
		String d1 = "Montag 1.1.2001";
		String d2 = "Dienstag 1.2.2002";
		ArrayList<Mittagsgericht> mm1 = new ArrayList<Mittagsgericht>();
		mm1.add(m1);
		mm1.add(m2);
		ArrayList<Mittagsgericht> mm2 = new ArrayList<Mittagsgericht>();
		mm2.add(m3);
		
		Tagesmenues myDay1 = new Tagesmenues(d1, mm1);
		Tagesmenues myDay2 = new Tagesmenues(d2, mm2);
		ArrayList<Tagesmenues> liste = new ArrayList<Tagesmenues>();
		liste.add(myDay1);
		liste.add(myDay2);
		return liste;
		
	}
	 
	public void parse() throws IOException {
		if(!gerichteListe.isEmpty()) {
			gerichteListe = new ArrayList<Tagesmenues>();
		}
		requestedDays.add("");
		while(!requestedDays.isEmpty()) {
			String url="http://studwerk.fh-stralsund.de/speiseplan/speiseplan_hst.php"+requestedDays.poll();
			try {
				Document doc = Jsoup.connect(url).get();
				String day = doc.getElementsByTag("h2").get(0).text();
				day = day.substring(0, day.indexOf("("));
				ArrayList<Mittagsgericht> mittagsgerichte = new ArrayList<Mittagsgericht>(); 
						
				Elements links = doc.getElementsByTag("a");
				if(links.size()>2) {
					requestedDays.offer(links.get(2).attr("href"));
				}

				doc.select("span").remove();
				doc.getElementsByClass("tblkopflinks").remove();
				doc.getElementsByClass("tblkopf").remove();
				doc.select("sup").remove();

				
					for (Element table : doc.select("table")) {
				        for (Element row : table.select("tr")) {
				            Elements tds = row.select("td");
				            if (!tds.isEmpty()) {
								if (tds.html().contains("€")) {
									if (convertString(tds.get(1).text()) >= 0.1) {
										Log.d("App", tds.get(0).text() + ":" + tds.get(1).text() + ":" + tds.get(2).text() + ":" + tds.get(3).text());
										mittagsgerichte.add(new Mittagsgericht(tds.get(0).text(), convertString(tds.get(1).text()), convertString(tds.get(2).text()), convertString(tds.get(3).text())));
									}
								}
							}
						}
				    }
				gerichteListe.add(new Tagesmenues(day, mittagsgerichte));
					
			}
			catch (IOException e) {
				throw e; 
			}
		}
	}
	public ArrayList<Tagesmenues> getLunchData() {
		return gerichteListe;
	}
		    
	
	public float convertString(String td) {
		float l=0;
		String s = td.replaceAll("\\s€", "");
		s = s.replaceAll(",", ".");
		try {
			l = Float.parseFloat(s);
		}
		catch (Exception e) {
			Log.d("App", e.getMessage());
		}
		return l;
	}


}
