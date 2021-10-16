package ar.com.ada.api.aladas.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.aladas.entities.Pasajero;
import ar.com.ada.api.aladas.entities.Reserva;
import ar.com.ada.api.aladas.entities.Vuelo;
import ar.com.ada.api.aladas.entities.Reserva.EstadoReservaEnum;
import ar.com.ada.api.aladas.models.response.ReservaGenerationResponse;
import ar.com.ada.api.aladas.repos.ReservaRepository;
import ar.com.ada.api.aladas.sistema.payments.MercadoPagoService;

@Service
public class ReservaService {

    @Autowired
    ReservaRepository repo;

    @Autowired
    VueloService vueloService;

    @Autowired
    PasajeroService pasajeroService;

    @Autowired
    MercadoPagoService mercadoPagoService;

    public Integer generarReserva(Integer vueloId, Integer pasajeroId) {

        Reserva reserva = new Reserva();

        Vuelo vuelo = vueloService.buscarPorId(vueloId);

        reserva.setFechaEmision(new Date());

        Calendar c = Calendar.getInstance();
        c.setTime(reserva.getFechaEmision());
        c.add(Calendar.DATE, 1);

        reserva.setFechaVencimiento(c.getTime());

        reserva.setEstadoReservaId(EstadoReservaEnum.CREADA);

        Pasajero pasajero = pasajeroService.buscarPorId(pasajeroId);

        // Relaciones bidireccionales.
        pasajero.agregarReserva(reserva);
        vuelo.agregarReserva(reserva);

        repo.save(reserva);
        return reserva.getReservaId();
    }

    public ReservaGenerationResponse generarReservaConLinkDePago(Integer vueloId, Integer pasajeroId) {

        ReservaGenerationResponse r = new ReservaGenerationResponse();
        //1ro generar la reserva:
        Integer reservaId = generarReserva(vueloId, pasajeroId);

        //2do decirle  MP que genere una preferencia de pago.
        Reserva reserva = repo.findByReservaId(reservaId);
        r = mercadoPagoService.generarPreferenciaParaReserva(reserva);

        return r;
    }

    public Reserva buscarPorId(Integer id) {
        return repo.findByReservaId(id);
    }

}
