package de.portfolio.backend.data;

import java.math.BigDecimal;

public class Material {
    private String name;
    private BigDecimal preisProPaket;
    private double qmProPaket;

    public Material(String name, double preis, double qmProPaket) {
        this.name = name;
        this.preisProPaket = BigDecimal.valueOf(preis);
        this.qmProPaket = qmProPaket;
    }

    public String getName() { return name; }
    public BigDecimal getPreisProPaket() { return preisProPaket; }
    public double getQmProPaket() { return qmProPaket; }

    @Override
    public String toString() {
        return name;
    }
}
