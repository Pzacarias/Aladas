package ar.com.ada.api.aladas.entities;

import java.util.Date;

import javax.persistence.*;

import ar.com.ada.api.aladas.entities.Pais.PaisEnum;
import ar.com.ada.api.aladas.entities.Pais.TipoDocuEnum;

@MappedSuperclass
public abstract class Persona {
    
    private String nombre;

    @Column (name = "tipo_documento_id")
    private Integer tipoDocumentoId;

    private String documento;

    @Column (name = "fecha_nacimiento")
    @Temporal (TemporalType.DATE)
    private Date fechaNacimiento;

    @Column (name = "pais_id")
    private Integer paisId;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoDocuEnum getTipoDocumentoId() {
        return TipoDocuEnum.parse(this.tipoDocumentoId);
    }

    public void setTipoDocumentoId(TipoDocuEnum tipoDocumentoId) {
        this.tipoDocumentoId = tipoDocumentoId.getValue();
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public PaisEnum getPaisId() {
        return PaisEnum.parse(this.paisId);
    }

    public void setPaisId(PaisEnum paisId) {
        this.paisId = paisId.getValue();
    }

    
}
