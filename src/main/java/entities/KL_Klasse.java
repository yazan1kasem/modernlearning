package entities;

import javax.persistence.*;


@Entity(name = "KL_Klasse")
@Table(name = "KL_Klasse")
public class KL_Klasse {
    @Id
    @Column(name = "KL_Bez", unique = true, nullable = false)
    private String KL_Bez;
    @Column(name = "KL_Title")
    private String KL_Title;

    public KL_Klasse(String KL_Bez, String KL_Title) {
        this.KL_Bez = KL_Bez;
        this.KL_Title = KL_Title;
    }

    public KL_Klasse() {}

    public String getKL_Bez() {
        return KL_Bez;
    }

    public void setKL_Bez(String KL_Bez) {
        this.KL_Bez = KL_Bez;
    }

    public String getKL_Title() {
        return KL_Title;
    }

    public void setKL_Title(String KL_Title) {
        this.KL_Title = KL_Title;
    }
}
