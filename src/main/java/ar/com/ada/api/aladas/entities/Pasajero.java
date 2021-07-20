package ar.com.ada.api.aladas.entities;

import java.util.*;

import javax.persistence.*;

@Entity
@Table(name = "pasajero")
public class Pasajero extends Persona {

    @Id
    @Column(name = "pasajero_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pasajeroId;

    @OneToMany(mappedBy = "pasajero", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList<>();

    public Integer getPasajeroId() {
        return pasajeroId;
    }

    public void setPasajeroId(Integer pasajeroId) {
        this.pasajeroId = pasajeroId;
    }

    public void agregarReserva(Reserva reserva){
        this.reservas.add(reserva);
        reserva.setPasajero(this);
    }
}
