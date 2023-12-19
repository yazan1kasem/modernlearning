package entities;



import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name="N_Notifications")
@Table(name="N_Notifications")
public class N_Notifications {
    @Id
    @Column(name="N_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int n_id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="N_K_ID", referencedColumnName="K_ID")
    private K_Kalender N_K_ID;

    @Column(name="N_Vorzeit")
    private LocalDateTime N_Vorzeit;

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

    public LocalDateTime getN_Vorzeit() {
        return N_Vorzeit;
    }

    public void setN_Vorzeit(LocalDateTime n_Vorzeit) {
        N_Vorzeit = n_Vorzeit;
    }
}