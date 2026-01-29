package de.portfolio.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon; // schöne Icons
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout; // Layout nebeneinander
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Route;
import de.portfolio.backend.data.Material;
import de.portfolio.backend.service.CalculatorService;

import java.util.List;

@Route("") // Definiert die Hauptseite der Anwendung
public class MainView extends VerticalLayout {
    
    // Service zum Rechnen wird durch Dependency Injection gegeben.
    private final CalculatorService service;

    // Definieren der UI-Komponenten
    private ComboBox<Material> materialAuswahl = new ComboBox<>("Material wählen");
    private NumberField flaecheInput = new NumberField("Benötigte Fläche (m²)");

    // Buttons definieren
    private Button berechneButton = new Button("Kosten berechnen");
    private Button resetButton = new Button("Zurücksetzen", VaadinIcon.TRASH.create());

    private Span ergebnisText = new Span(); //Text-Container

    // Kosntruktor
    public MainView(CalculatorService service) {
        this.service = service;

        // Grundeinstellungen der UI
        setSizeFull(); // Nutzen des kompletten Bildschirms
        setAlignItems(Alignment.CENTER); // Alle Elemente mittig zentriet
        setJustifyContentMode(JustifyContentMode.CENTER);

        // Überschrift
        H1 titel = new H1("🛠️ Baustoff-Rechner");

        configureMaterialLoader();

        // Einstellung Eingabefeld
        flaecheInput.setMin(0.1); // Keine negativen Flächen
        flaecheInput.setStepButtonsVisible(true);
        flaecheInput.setHelperText("Inkl. Wände und Nischen");

        // Button schön machen + Klick-Logik anhängen
        berechneButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        resetButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR); // Button wird rot, wenn man drüber fährt (LUMO_ERROR)


        berechneButton.addClickListener(event-> berechneErgebnis());
        resetButton.addClickListener(event -> resetFormular()); 

        // Button-Layout (Horizontal), Buttons landen in einem Container, damit sie nebeneinander stehen
        HorizontalLayout buttonLayout = new HorizontalLayout(berechneButton, resetButton);
        buttonLayout.setSpacing(true); // Kleiner Abstand zwischen den Button

        // Alles auf Bildschirm in Reihenfolge ausgeben.
        add(titel, materialAuswahl, flaecheInput, buttonLayout, ergebnisText);
    }

    // Hilfsmethode: Lädt Dumme-Daten in die Box
    private void configureMaterialLoader() {

        List<Material> materialien = List.of(
            new Material("Eichenparkett Premium", 45.50, 2.5),
            new Material("Vinylboden Grau", 29.99, 3.2),
            new Material("Wandfliese Metro", 19.50, 1.5),
            new Material("Dämmwolle Rockwool", 12.90, 5.0)
        );

        materialAuswahl.setItems(materialien);

        materialAuswahl.setItemLabelGenerator(m ->
            m.getName() + " (" + m.getQmProPaket() + " m²/Paket)"
        );
    }

    // Logik, wenn der Button geklickt wird
    private void berechneErgebnis() {
        Material material = materialAuswahl.getValue();
        Double flaeche = flaecheInput.getValue();

        // Validierung: Hat der Nutzer alles richtig eingegeben?
        if (material == null) {
            Notification.show("Bitte wählen Sie ein Material aus!")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
        }
        if (flaeche == null || flaeche <= 0) {
            Notification.show("Bitte geben Sie eine gültige Fläche ein!")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
        }

        // Service rechnet (UI von Logik getrennt.)
        var ergebnis = service.berechneBedarf(material, flaeche);

        String resultText = String.format("Sie benötigen %d Paket (für %.2f m² inkl. Verschnitt). Gesamtpreis: %s €",
            ergebnis.anzahlPakete(),
            ergebnis.qmInklVerschnitt(),
            ergebnis.gesamtPreis().toString()); // toSTring() weil es BigDecimal ist

        ergebnisText.setText(resultText);
        ergebnisText.getStyle().set("font-weight", "bold"); // Fett gedruckt
        ergebnisText.getStyle().set("color", "green");      // Grün

        Notification.show("Berechnung erfolgreich!", 2000, Notification.Position.BOTTOM_END);

    }

    // Logik zum Zurücksetzen des Formulars
    private void resetFormular() {
        materialAuswahl.clear(); // Leert Auswahl
        flaecheInput.clear(); // Leert die Zahl
        ergebnisText.setText(""); // Löscht den Ergebnis-Text

        // Focus wieder auf das erste Feld setzten (UX)

        materialAuswahl.focus();

        Notification.show("Formular zurückgesetzt.", 1000, Notification.Position.BOTTOM_START);
    }
}
