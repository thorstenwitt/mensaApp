package de.thorstenwitt.mensaapp.parser;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.List;
import java.util.Locale;


import de.thorstenwitt.mensaapp.businessobject.Lunch;
import de.thorstenwitt.mensaapp.businessobject.LunchOffer;
import de.thorstenwitt.mensaapp.businessobject.Mensa;


public class LunchParser {

	private static LunchParser instance = null;
	ArrayList<Mensa> mensaList = new ArrayList<>();
	DecimalFormat df = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.GERMANY);


	private LunchParser() {

	}
	public static LunchParser getInstance() {
		if(instance == null) {
			instance = new LunchParser();
		}
		return instance;
	}
	

	public void parse() throws IOException {
		mensaList = new ArrayList<>();

		URL mensaUrl = new URL("http://studwerk.fh-stralsund.de/speiseplan/speiseplan_xml.php");
		URLConnection connection = mensaUrl.openConnection();
		org.jsoup.nodes.Document mensaDoc = Jsoup.parse(connection.getInputStream(),"UTF-8","http://studwerk.fh-stralsund.de/speiseplan/speiseplan_xml.php");

		int i=0;
		for (Element mensaElement: mensaDoc.children().select("mensa > name")) {

			String mensaName = mensaElement.text();
			mensaList.add(new Mensa(mensaName, new ArrayList<LunchOffer>()));

			for (Element lunchOffer: mensaElement.parent().children().select("theke > gericht")) {
				Lunch lunch;
				String date = lunchOffer.children().select("datum").text();
				String lunchOfferName = "";
				String foodAdditives = "";
				try {
					float studentPrice = df.parse(lunchOffer.children().select("preisStudent").text()).floatValue();
					float employeePrice = df.parse(lunchOffer.children().select("preisMitarbeiter").text()).floatValue();
					float guestPrice = df.parse(lunchOffer.children().select("preisGast").text()).floatValue();
					for(Element name: lunchOffer.children().select("name")) {
						if(name.parent().tagName().equals("gericht")) {
							lunchOfferName = name.text();
						}
						else {
							foodAdditives+=name.text()+" ";
						}
					}

					lunch = new Lunch(lunchOfferName, studentPrice, employeePrice, guestPrice);
					boolean lunchOfferNotExists = true;
					for(LunchOffer existentLunchOffers: mensaList.get(i).getLunchOffers()) {
						if(existentLunchOffers.getMydate().equals(date)) {
							existentLunchOffers.getLunchList().add(lunch);
							lunchOfferNotExists=false;
							break;
						}
					}
					if (lunchOfferNotExists) {
						ArrayList<Lunch> lunchList = new ArrayList<>();
						lunchList.add(lunch);
						LunchOffer lunchOfferDay = new LunchOffer(date, lunchList);
						mensaList.get(i).getLunchOffers().add(lunchOfferDay);
					}

				}
				catch (ParseException e) {
					System.out.println(e.getMessage());
				}

			}
			i++;
		}
	}
	public ArrayList<LunchOffer> getLunchDataForStralsund() {
		for(Mensa mensa: mensaList) {
			if (mensa.getMensaName().equals("Mensa Stralsund")) {
				return mensa.getLunchOffers();
			}
		}
		return null;
	}

	public List<Mensa> getLunchDataForAllMensas() {
		return mensaList;
	}
	


}
