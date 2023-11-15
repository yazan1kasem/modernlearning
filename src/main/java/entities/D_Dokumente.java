package entities;

import javax.persistence.*;

@Entity
@Table(name = "D_Dokumente")
public class D_Dokumente {
    @Id
    @Column(name = "D_ID", unique = true, nullable = false)
    private int D_ID;

    @Column(name = "D_Name")
    private String D_Name;

    public D_Dokumente(int d_ID, String d_Name) {
        D_ID = d_ID;
        D_Name = d_Name;
    }
    public D_Dokumente() {

    }

    public int getD_ID() {
        return D_ID;
    }

    public void setD_ID(int d_ID) {
        D_ID = d_ID;
    }

    public String getD_Name() {
        return D_Name;
    }

    public void setD_Name(String d_Name) {
        D_Name = d_Name;
    }
}
