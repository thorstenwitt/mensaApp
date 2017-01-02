package de.thorstenwitt.mensaapp.common.businessobject;


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

    public ArrayList<LunchOffer> getLunchOffers() {
        return lunchOffers;
    }

    public void setLunchOffers(ArrayList<LunchOffer> lunchOffers) {
        lunchOffers = lunchOffers;
    }
}
