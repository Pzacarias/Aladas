package ar.com.ada.api.aladas.entities;

import javax.persistence.*;

@Entity
@Table (name = "staff")
public class Staff extends Persona {
    
    @Id
    @Column (name = "staff_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer staffId;

    @OneToOne (mappedBy = "staff", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Usuario usuario;

    public Integer getStaffId() {
        return staffId;
    }

    public void setStaffId(Integer staffId) {
        this.staffId = staffId;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        usuario.setStaff(this);
    }

    

}
