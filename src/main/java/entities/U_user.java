package entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "U_User")
@Table(name = "U_User")
public class U_user {

    @Id
    @Column(name = "U_id", unique = true, nullable = false)
    private int U_id;

    @Column(name="U_Name")
    private String U_Name;

}
