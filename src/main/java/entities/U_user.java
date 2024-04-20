package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "U_User")
@Table(name = "U_User")
public class U_user {

    @Id
    @Column(name = "U_id", unique = true, nullable = false)
    private String U_id;

    @Column(name="U_Name")
    private String U_Name;

    @Column(name="U_Passwort")
    private char U_Passwort;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="U_E_id", referencedColumnName = "E_id")
    private List<E_EigeneArbeitsblätter> e_eigeneArbeitsblätters=new ArrayList<E_EigeneArbeitsblätter>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="U_K_id", referencedColumnName = "K_ID")
    private List<K_Kalender> k_kalenders=new ArrayList<K_Kalender>();

}
