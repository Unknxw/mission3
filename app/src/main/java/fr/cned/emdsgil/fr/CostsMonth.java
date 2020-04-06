package fr.cned.emdsgil.fr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class CostsMonth implements Serializable {

    private int month;
    private int years;
    private int state = 0;
    private Integer km = 0;
    private Integer overnight = 0;
    private Integer repas = 0;
    private final List<FraisHf> costs = new ArrayList<>();

    /**
     * @param years
     * @param month
     */
    CostsMonth(Integer years, int month) {
        this.years = years;
        this.month = month;
    }

    /**
     * @param price
     * @param motif
     * @param day
     */
    void addCosts(float price, String motif, int day) {
        costs.add(new FraisHf(price, motif, day));
    }

    public void supprFraisHf(Integer index) {
        costs.remove(index);
    }

    public Integer getMois() {
        return month;
    }

    public void setMois(Integer mois) {
        this.month = mois;
    }

    public Integer getyears() {
        return years;
    }

    public void setyears(Integer years) {
        this.years = years;
    }

    Integer getState() {
        return state;
    }

    void setState(Integer state) {
        this.state = state;
    }

    Integer getKm() {
        return km;
    }

    void setKm(Integer km) {
        this.km = km;
    }

    Integer getOverNight() {
        return overnight;
    }

    void setOverNight(Integer overnight) {
        this.overnight = overnight;
    }

    Integer getRepas() {
        return repas;
    }

    void setRepas(Integer repas) {
        this.repas = repas;
    }

    List<FraisHf> getCosts() {
        return costs;
    }

}
