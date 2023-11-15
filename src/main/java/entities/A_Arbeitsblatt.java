package entities;

import javax.persistence.*;

@Entity (name = "A_Arbeitsblatt")
@Table (name = "A_Arbeitsblatt")
public class A_Arbeitsblatt {
    @Id
    @Column(name = "A_ID", unique = true, nullable = false)
    private Integer A_ID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="A_KL_Bez", referencedColumnName ="KL_Bez")
    private KL_Klasse A_KL_Bez;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="A_F_Bez", referencedColumnName ="F_Bez")
    private F_Fach f_Bez;

    @Column(name = "A_Name")
    private String A_Name;

    public A_Arbeitsblatt(Integer a_ID, KL_Klasse a_KL_Bez, F_Fach f_Bez, String a_Name) {
        A_ID = a_ID;
        A_KL_Bez = a_KL_Bez;
        this.f_Bez = f_Bez;
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

    public KL_Klasse getA_KL_Bez() {
        return A_KL_Bez;
    }

    public void setA_KL_Bez(KL_Klasse a_KL_Bez) {
        A_KL_Bez = a_KL_Bez;
    }

    public F_Fach getF_Bez() {
        return f_Bez;
    }

    public void setF_Bez(F_Fach f_Bez) {
        this.f_Bez = f_Bez;
    }

    public String getA_Name() {
        return A_Name;
    }

    public void setA_Name(String a_Name) {
        A_Name = a_Name;
    }
}
