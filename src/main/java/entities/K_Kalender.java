package entities;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "K_Kalender")
public class K_Kalender {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "K_ID", unique = true, nullable = false)
    private int K_ID;

    @Column(name = "K_Title")
    private String K_Title;
    @Column(name = "K_Beschreibung")
    private String K_Beschreibung;
    @Column(name = "K_vonDatum ")
    private LocalDateTime K_vonDatum;
    @Column(name = "K_bisDatum ")
    private LocalDateTime K_bisDatum;

    @ManyToOne
    @JoinColumn(name="K_U_id", referencedColumnName = "U_ID")
    private U_user user;

    public K_Kalender(int k_ID, String k_Title, String k_Beschreibung, LocalDateTime k_vonDatum, LocalDateTime k_bisDatum, U_user user) {
        K_ID = k_ID;
        K_Title = k_Title;
        K_Beschreibung = k_Beschreibung;
        K_vonDatum = k_vonDatum;
        K_bisDatum = k_bisDatum;
        this.user = user;
    }

    public K_Kalender() {
    }

    public int getK_ID() {
        return K_ID;
    }

    public String getK_Title() {
        return K_Title;
    }

    public void setK_Title(String k_Title) {
        K_Title = k_Title;
    }

    public String getK_Beschreibung() {
        return K_Beschreibung;
    }

    public void setK_Beschreibung(String k_Beschreibung) {
        K_Beschreibung = k_Beschreibung;
    }

    public LocalDateTime getK_vonDatum() {
        return K_vonDatum;
    }

    public void setK_vonDatum(LocalDateTime k_vonDatum) {
        K_vonDatum = k_vonDatum;
    }

    public LocalDateTime getK_bisDatum() {
        return K_bisDatum;
    }

    public void setK_bisDatum(LocalDateTime k_bisDatum) {
        K_bisDatum = k_bisDatum;
    }

    public void setK_ID(int k_ID) {
        K_ID = k_ID;
    }

    public U_user getUser() {
        return user;
    }

    public void setUser(U_user user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "K_Kalender{" +
                "K_ID=" + K_ID +
                ", K_Title='" + K_Title + '\'' +
                ", K_Beschreibung='" + K_Beschreibung + '\'' +
                ", K_vonDatum=" + K_vonDatum +
                ", K_bisDatum=" + K_bisDatum +
                '}';
    }


}
