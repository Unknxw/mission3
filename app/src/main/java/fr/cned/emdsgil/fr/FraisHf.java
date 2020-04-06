package fr.cned.emdsgil.fr;

import java.io.Serializable;

/**
 * Classe métier contenant la description d'un frais hors forfait
 */
class FraisHf implements Serializable {

    private final float price;
    private final String motif;
    private final Integer day;

    /**
     * @param price
     * @param motif
     * @param day
     */
    FraisHf(Float price, String motif, int day) {
        this.price = price;
        this.motif = motif;
        this.day = day;
    }

    Float getPrice() {
        return price;
    }

    String getMotif() {

        return motif;
    }

    Integer getDay() {
        return day;
    }

}
