package entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "E_EigeneArbeitsblaetter")
public class E_EigeneArbeitsblaetter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "E_id", unique = true, nullable = false)
    private int E_id;

    @Column(name="E_DateiName")
    private String E_DateiName;

    @Column(name="E_Timestamp")
    private LocalDateTime E_Timestamp;


    public int getE_id() {
        return E_id;
    }

    public void setE_id(int e_id) {
        E_id = e_id;
    }

    public String getE_DateiName() {
        return E_DateiName;
    }

    public void setE_DateiName(String e_DateiName) {
        E_DateiName = e_DateiName;
    }

    public LocalDateTime getE_Timestamp() {
        return E_Timestamp;
    }

    public void setE_Timestamp(LocalDateTime e_Timestamp) {
        E_Timestamp = e_Timestamp;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="E_U_id", referencedColumnName = "U_id")
    private U_user u_user;
}
