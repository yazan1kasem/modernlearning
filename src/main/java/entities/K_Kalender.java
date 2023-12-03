package entities;
import javax.persistence.*;
import java.util.Date;

@Entity(name ="K_Kalender")
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
    @Column(name = "K_vonZeitMinute")
    private int K_vonZeitMinute;
    @Column(name = "K_vonZeitStunde")
    private int K_vonZeitStunde;
    @Column(name = "K_bisZeitMinute")
    private int K_bisZeitMinute;
    @Column(name = "K_bisZeitStunde")
    private int K_bisZeitStunde;

    public K_Kalender(String k_Title, String k_Beschreibung, int k_vonZeitMinute, int k_vonZeitStunde, int k_bisZeitMinute, int k_bisZeitStunde) {

        K_Title = k_Title;
        K_Beschreibung = k_Beschreibung;
        K_vonZeitMinute = k_vonZeitMinute;
        K_vonZeitStunde = k_vonZeitStunde;
        K_bisZeitMinute = k_bisZeitMinute;
        K_bisZeitStunde = k_bisZeitStunde;
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

    public int getK_vonZeitMinute() {
        return K_vonZeitMinute;
    }

    public void setK_vonZeitMinute(int k_vonZeitMinute) {
        K_vonZeitMinute = k_vonZeitMinute;
    }

    public int getK_vonZeitStunde() {
        return K_vonZeitStunde;
    }

    public void setK_vonZeitStunde(int k_vonZeitStunde) {
        K_vonZeitStunde = k_vonZeitStunde;
    }

    public int getK_bisZeitMinute() {
        return K_bisZeitMinute;
    }

    public void setK_bisZeitMinute(int k_bisZeitMinute) {
        K_bisZeitMinute = k_bisZeitMinute;
    }

    public int getK_bisZeitStunde() {
        return K_bisZeitStunde;
    }

    public void setK_bisZeitStunde(int k_bisZeitStunde) {
        K_bisZeitStunde = k_bisZeitStunde;
    }
}
