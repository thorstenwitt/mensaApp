package de.thorstenwitt.mensaapp.businessobject;


import java.util.List;

public class Mensa {

    private String mensaName;
    private List<LunchOffer> lunchOffers;

    public Mensa(String mensaName, List<LunchOffer> lunchOffers) {
        this.mensaName = mensaName;
        this.lunchOffers = lunchOffers;
    }

    public String getMensaName() {
        return mensaName;
    }

    public void setMensaName(String mensaName) {
        this.mensaName = mensaName;
    }

    public List<LunchOffer> getGerichte() {
        return lunchOffers;
    }

    public void setGerichte(List<LunchOffer> lunchOffers) {
        lunchOffers = lunchOffers;
    }
}
