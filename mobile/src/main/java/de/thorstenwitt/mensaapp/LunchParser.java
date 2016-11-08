package de.thorstenwitt.mensaapp;

import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;



public class LunchParser {
	
	Queue<String> requestedDays = new LinkedList<String>();
	NumberFormat nf = NumberFormat.getInstance();
	ArrayList<LunchOffer> lunchOfferList = new ArrayList<LunchOffer>();
	DateFormat df = new SimpleDateFormat("EEEE, dd.MM.yyyy");
	private static LunchParser instance = null;

	private LunchParser() {
		
	}
	public static LunchParser getInstance() {
		if(instance == null) {
			instance = new LunchParser();
		}
		return instance;
	}
	
	public static ArrayList<LunchOffer> exampleDatas() {
		Lunch m1 = new Lunch("Schnitzel",1,2,3);
		Lunch m2 = new Lunch("Nudeln",0.5f,1f,1.5f);
		Lunch m3 = new Lunch("Wurst",2,4,6);
		String d1 = "Montag 1.1.2001";
		String d2 = "Dienstag 1.2.2002";
		ArrayList<Lunch> mm1 = new ArrayList<Lunch>();
		mm1.add(m1);
		mm1.add(m2);
		ArrayList<Lunch> mm2 = new ArrayList<Lunch>();
		mm2.add(m3);
		
		LunchOffer myDay1 = new LunchOffer(d1, mm1);
		LunchOffer myDay2 = new LunchOffer(d2, mm2);
		ArrayList<LunchOffer> liste = new ArrayList<LunchOffer>();
		liste.add(myDay1);
		liste.add(myDay2);
		return liste;
		
	}
	 
	public void parse() throws IOException {
		if(!lunchOfferList.isEmpty()) {
			lunchOfferList = new ArrayList<LunchOffer>();
		}
		requestedDays.add("");
		while(!requestedDays.isEmpty()) {
			String url="http://studwerk.fh-stralsund.de/speiseplan/speiseplan_hst.php"+requestedDays.poll();
			try {
				Document doc = Jsoup.connect(url).get();
				String day = doc.getElementsByTag("h2").get(0).text();
				day = day.substring(0, day.indexOf(" ("));
				ArrayList<Lunch> lunchList = new ArrayList<Lunch>();
						
				Elements links = doc.getElementsByTag("a");
				for (Element e: links) {
					String strDate = e.text();
					try {
						Date linkDate = new SimpleDateFormat("EEEE, dd.MM.yy").parse(strDate);
						Date currentParsedDate = df.parse(day);
						if(currentParsedDate.before(linkDate)) {
							requestedDays.offer(e.attr("href"));
						}
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
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
										lunchList.add(new Lunch(tds.get(0).text(), convertString(tds.get(1).text()), convertString(tds.get(2).text()), convertString(tds.get(3).text())));
									}
								}
							}
						}
				    }
				lunchOfferList.add(new LunchOffer(day, lunchList));
					
			}
			catch (IOException e) {
				throw e; 
			}
		}
	}
	public ArrayList<LunchOffer> getLunchData() {
		return lunchOfferList;
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
