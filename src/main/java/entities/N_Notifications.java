package entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="N_Notifications")
@Table(name="N_Notifications")
public class N_Notifications {
    @Id
    @Column(name="N_id", unique = true, nullable = false)
    private int n_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="N_K_ID", referencedColumnName="K_ID")
    private K_Kalender N_K_ID;

    @Column(name="N_datetime")
    private LocalDateTime n_datetime;
    @Column(name = "N_stunde")
    private int nStunde;

    @Column(name = "N_minute")
    private int nMinute;

    public N_Notifications(int n_id, K_Kalender n_K_ID, LocalDateTime n_datetime, int nStunde, int nMinute) {
        this.n_id = n_id;
        N_K_ID = n_K_ID;
        this.n_datetime = n_datetime;
        this.nStunde = nStunde;
        this.nMinute = nMinute;
    }

    public N_Notifications() {
    }

    public int getN_id() {
        return n_id;
    }

    public void setN_id(int n_id) {
        this.n_id = n_id;
    }

    public K_Kalender getN_K_ID() {
        return N_K_ID;
    }

    public void setN_K_ID(K_Kalender n_K_ID) {
        N_K_ID = n_K_ID;
    }

    public LocalDateTime getN_datetime() {
        return n_datetime;
    }

    public void setN_datetime(LocalDateTime n_datetime) {
        this.n_datetime = n_datetime;
    }

    public int getnStunde() {
        return nStunde;
    }

    public void setnStunde(int nStunde) {
        this.nStunde = nStunde;
    }

    public int getnMinute() {
        return nMinute;
    }

    public void setnMinute(int nMinute) {
        this.nMinute = nMinute;
    }
}