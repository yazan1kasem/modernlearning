package entities;
import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "KF_Klassefach")
@Table(name = "KF_Klassefach")
public class KF_Klassefach implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KF_KL_Bez",referencedColumnName = "KL_Bez")
    private KL_Klasse KF_KL_Bez;
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "KL_F_Bez", referencedColumnName="F_Bez")
    private F_Fach KL_F_Bez;

    public KF_Klassefach(KL_Klasse KF_KL_Bez, F_Fach KL_F_Bez) {
        this.KF_KL_Bez = KF_KL_Bez;
        this.KL_F_Bez = KL_F_Bez;
    }

    public KF_Klassefach() {
    }

    public KL_Klasse getKF_KL_Bez() {
        return KF_KL_Bez;
    }

    public void setKF_KL_Bez(KL_Klasse KF_KL_Bez) {
        this.KF_KL_Bez = KF_KL_Bez;
    }

    public F_Fach getKL_F_Bez() {
        return KL_F_Bez;
    }

    public void setKL_F_Bez(F_Fach KL_F_Bez) {
        this.KL_F_Bez = KL_F_Bez;
    }
}
