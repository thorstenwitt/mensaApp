package de.thorstenwitt.mensaapp.businessobject;


import java.util.ArrayList;

public class Mensa {

    private String mensaName;
    private ArrayList<LunchOffer> lunchOffers;

    public Mensa(String mensaName, ArrayList<LunchOffer> lunchOffers) {
        this.mensaName = mensaName;
        this.lunchOffers = lunchOffers;
    }

    public String getMensaName() {
        return mensaName;
    }

    public void setMensaName(String mensaName) {
        this.mensaName = mensaName;
    }

    public ArrayList<LunchOffer> getGerichte() {
        return lunchOffers;
    }

    public void setGerichte(ArrayList<LunchOffer> lunchOffers) {
        lunchOffers = lunchOffers;
    }
}
