package ar.com.ada.api.aladas.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table (name = "vuelo")
public class Vuelo {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name = "vuelo_id")
    private Integer vueloId;

    private Date fecha;

    @Column (name = "estado_vuelo_id")
    private Integer estadoVueloId;

    private Integer capacidad;

    @Column (name = "aeropuerto_origen")
    private Integer aeropuertoOrigen;

    @Column (name = "aeropuerto_destino")
    private Integer aeropuertoDestino;

    private BigDecimal precio;

    @Column (name = "codigo_moneda")
    private String codigoMoneda;



    @OneToMany(mappedBy = "pasajero", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reserva> reservas = new ArrayList <>();

    public void agregarReserva (Reserva reserva){
        this.reservas.add(reserva);
        reserva.setVuelo(this);
    }

}
