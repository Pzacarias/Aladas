package ar.com.ada.api.aladas.entities;

import java.util.*;

import javax.persistence.*;

@Entity
@Table (name = "reserva")
public class Reserva {
    
    @Id
    @Column (name = "reserva_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer reservaId;

    @ManyToOne
    @JoinColumn (name = "vuelo_id", referencedColumnName = "vuelo_id")
    private Vuelo vuelo;
    
    @ManyToOne
    @JoinColumn (name = "pasajero_id", referencedColumnName = "pasajero_id")
    private Pasajero pasajero;

    @Column (name = "estado_reserva_id")
    private Integer estadoReservaId;

    @Column (name = "fecha_emision")
    private Date fechaEmision;

    @Column (name = "fecha_vencimiento")
    private Date fechaVencimiento;

    public Integer getReservaId() {
        return reservaId;
    }

    public void setReservaId(Integer reservaId) {
        this.reservaId = reservaId;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public Vuelo getVuelo() {
        return vuelo;
    }

    public void setVuelo(Vuelo vuelo) {
        this.vuelo = vuelo;
    }

    public void setPasajero(Pasajero pasajero) {
        this.pasajero = pasajero;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

}
