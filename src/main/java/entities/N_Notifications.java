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
}