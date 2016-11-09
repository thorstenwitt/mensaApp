package de.thorstenwitt.mensaapp.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import de.thorstenwitt.mensaapp.businessobject.*;


public class NewLunchParser {


    public static void main(String[] args) throws Exception {
        parseLunchData();
    }


    public static List<Mensa> parseLunchData() throws Exception {
        List<Mensa> mensaList = new ArrayList<>();
        URL mensaUrl = new URL("http://studwerk.fh-stralsund.de/speiseplan/speiseplan_xml.php");
        URLConnection connection = mensaUrl.openConnection();

        DecimalFormat df = (DecimalFormat) DecimalFormat.getNumberInstance(Locale.GERMANY);

        org.jsoup.nodes.Document mensaDoc = Jsoup.parse(connection.getInputStream(),"UTF-8","http://studwerk.fh-stralsund.de/speiseplan/speiseplan_xml.php");
        Elements elements = mensaDoc.getElementsByTag("mensa");

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
                    for(LunchOffer existentLunchOffers: mensaList.get(i).getGerichte()) {
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
                        mensaList.get(i).getGerichte().add(lunchOfferDay);
                    }

                }
                catch (ParseException e) {
                    System.out.println(e.getMessage());
                }

            }
            i++;
        }

        return null;

    }

}
