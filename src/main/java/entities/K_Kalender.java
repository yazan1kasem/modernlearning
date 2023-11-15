package entities;
import javax.persistence.*;
import java.util.Date;

@Entity(name ="K_Kalender")
@Table(name = "K_Kalender")
public class K_Kalender {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "K_ID", unique = true, nullable = false)
    private int K_ID;

    @Column(name = "K_Title")
    private String K_Title;
    @Column(name = "K_Beschreibung")
    private String K_Beschreibung;
    @Column(name = "K_Termin")
    private Date K_Termin;
    @Column(name = "K_Uhrzeit")
    private String K_Uhrzeit;

    public K_Kalender(int k_ID, String k_Title, String k_Beschreibung, Date k_Termin, String k_Uhrzeit) {
        K_ID = k_ID;
        K_Title = k_Title;
        K_Beschreibung = k_Beschreibung;
        K_Termin = k_Termin;
        K_Uhrzeit = k_Uhrzeit;
    }

    public K_Kalender() {
    }

    public int getK_ID() {
        return K_ID;
    }

    public void setK_ID(int k_ID) {
        K_ID = k_ID;
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

    public Date getK_Termin() {
        return K_Termin;
    }

    public void setK_Termin(Date k_Termin) {
        K_Termin = k_Termin;
    }

    public String getK_Uhrzeit() {
        return K_Uhrzeit;
    }

    public void setK_Uhrzeit(String k_Uhrzeit) {
        K_Uhrzeit = k_Uhrzeit;
    }
}
