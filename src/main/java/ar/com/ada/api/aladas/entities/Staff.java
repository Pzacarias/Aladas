package ar.com.ada.api.aladas.entities;

import javax.persistence.*;

@Entity
@Table (name = "staff")
public class Staff extends Persona {
    
    @Id
    @Column (name = "staff_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer staffId;

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

}
