package entities;
import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "KF_Klassefach")
@Table(name = "KF_Klassefach")
public class KF_Klassefach  {
    @Id
    @Column(name = "KF_BEZ", unique = true, nullable = false)
    private String KF_BEZ;

    @Column(name = "KF_KL_Bez")
    private String KF_KL_Bez;

    @Column(name="KF_F_Bez")
    private String KF_F_Bez;

    public KF_Klassefach(String KF_BEZ, String KF_KL_Bez, String KF_F_Bez) {
        this.KF_BEZ = KF_BEZ;
        this.KF_KL_Bez = KF_KL_Bez;
        this.KF_F_Bez = KF_F_Bez;
    }

    public KF_Klassefach() {
    }

    public String getKF_BEZ() {
        return KF_BEZ;
    }

    public void setKF_BEZ(String KF_BEZ) {
        this.KF_BEZ = KF_BEZ;
    }

    public String getKF_KL_Bez() {
        return KF_KL_Bez;
    }

    public void setKF_KL_Bez(String KF_KL_Bez) {
        this.KF_KL_Bez = KF_KL_Bez;
    }

    public String getKF_F_Bez() {
        return KF_F_Bez;
    }

    public void setKF_F_Bez(String KF_F_Bez) {
        this.KF_F_Bez = KF_F_Bez;
    }
}
