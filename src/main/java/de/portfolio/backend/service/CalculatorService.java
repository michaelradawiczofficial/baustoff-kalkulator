package de.portfolio.backend.service;

import de.portfolio.backend.data.Material;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Service // Sorgt dafür, dass Spring Boot diese Klasse autoatisch lädt.
public class CalculatorService {

    private static final double VERSCHNITT_FAKTOR =1.10;

    public CalculationResult berechneBedarf(Material material, double benoetigteQm) {

        // 10 % Verschnitt aufschlagen
        double qmMitVerschnitt = benoetigteQm * VERSCHNITT_FAKTOR;

        // Wieviele Pakete werden benötigt?
        // Bedarf durch Paketinhalt, immer aufrunden
        int pakete = (int) Math.ceil(qmMitVerschnitt / material.getQmProPaket());

        // Gesamtpreis berechnen
        // Preis * Anzahl (BigDecimal für genaue Geldbeträge)
        BigDecimal gesamtPreis = material.getPreisProPaket()
            .multiply(BigDecimal.valueOf(pakete))
            .setScale(2, RoundingMode.HALF_UP); // Auf 2 Nachkommastellen runden

        // Ergebnis zurück
        return new CalculationResult(pakete, gesamtPreis, qmMitVerschnitt);
        
    }

    // "Record" kurze Klasse nur für Daten.
    // Spart Schreibarbeit (Getter/Setter/Konstruktor/)
    public record CalculationResult(
        int anzahlPakete,
        BigDecimal gesamtPreis,
        double qmInklVerschnitt
    ) {}
}
