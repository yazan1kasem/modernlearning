package entities;

import javax.persistence.*;

@Entity (name = "A_Arbeitsblatt")
@Table (name = "A_Arbeitsblatt")
public class A_Arbeitsblatt {
    @Id
    @Column(name = "A_ID", unique = true, nullable = false)
    private Integer A_ID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="A_KF_Bez", referencedColumnName ="KF_Bez")
    private KF_Klassefach A_KF_Bez;

    @Column(name = "A_Name")
    private String A_Name;

    public A_Arbeitsblatt(Integer a_ID, KF_Klassefach a_KF_Bez, String a_Name) {
        A_ID = a_ID;
        A_KF_Bez = a_KF_Bez;
        A_Name = a_Name;
    }

    public A_Arbeitsblatt() {
    }

    public Integer getA_ID() {
        return A_ID;
    }

    public void setA_ID(Integer a_ID) {
        A_ID = a_ID;
    }

    public KF_Klassefach getA_KF_Bez() {
        return A_KF_Bez;
    }

    public void setA_KF_Bez(KF_Klassefach a_KF_Bez) {
        A_KF_Bez = a_KF_Bez;
    }

    public String getA_Name() {
        return A_Name;
    }

    public void setA_Name(String a_Name) {
        A_Name = a_Name;
    }
}
