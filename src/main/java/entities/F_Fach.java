package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "F_Fach")
@Table(name = "F_Fach")
public class F_Fach {
    @Id
    @Column(name = "F_Bez", unique = true, nullable = false)
    private String F_Bez;

    @Column(name="F_Name")
    private String F_Name;

    public F_Fach(String f_Bez, String f_Name) {
        F_Bez = f_Bez;
        F_Name = f_Name;
    }

    public F_Fach() {
    }

    public String getF_Bez() {
        return F_Bez;
    }

    public void setF_Bez(String f_Bez) {
        F_Bez = f_Bez;
    }

    public String getF_Name() {
        return F_Name;
    }

    public void setF_Name(String f_Name) {
        F_Name = f_Name;
    }
}
