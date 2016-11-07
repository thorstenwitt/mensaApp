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
	ArrayList<FoodSelection> foodSelectionList = new ArrayList<FoodSelection>();
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
	
	public static ArrayList<FoodSelection> exampleDatas() {
		MiddayMeal m1 = new MiddayMeal("Schnitzel",1,2,3);
		MiddayMeal m2 = new MiddayMeal("Nudeln",0.5f,1f,1.5f);
		MiddayMeal m3 = new MiddayMeal("Wurst",2,4,6);
		String d1 = "Montag 1.1.2001";
		String d2 = "Dienstag 1.2.2002";
		ArrayList<MiddayMeal> mm1 = new ArrayList<MiddayMeal>();
		mm1.add(m1);
		mm1.add(m2);
		ArrayList<MiddayMeal> mm2 = new ArrayList<MiddayMeal>();
		mm2.add(m3);
		
		FoodSelection myDay1 = new FoodSelection(d1, mm1);
		FoodSelection myDay2 = new FoodSelection(d2, mm2);
		ArrayList<FoodSelection> liste = new ArrayList<FoodSelection>();
		liste.add(myDay1);
		liste.add(myDay2);
		return liste;
		
	}
	 
	public void parse() throws IOException {
		if(!foodSelectionList.isEmpty()) {
			foodSelectionList = new ArrayList<FoodSelection>();
		}
		requestedDays.add("");
		while(!requestedDays.isEmpty()) {
			String url="http://studwerk.fh-stralsund.de/speiseplan/speiseplan_hst.php"+requestedDays.poll();
			try {
				Document doc = Jsoup.connect(url).get();
				String day = doc.getElementsByTag("h2").get(0).text();
				day = day.substring(0, day.indexOf(" ("));
				ArrayList<MiddayMeal> middayMealList = new ArrayList<MiddayMeal>();
						
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
										middayMealList.add(new MiddayMeal(tds.get(0).text(), convertString(tds.get(1).text()), convertString(tds.get(2).text()), convertString(tds.get(3).text())));
									}
								}
							}
						}
				    }
				foodSelectionList.add(new FoodSelection(day, middayMealList));
					
			}
			catch (IOException e) {
				throw e; 
			}
		}
	}
	public ArrayList<FoodSelection> getLunchData() {
		return foodSelectionList;
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
