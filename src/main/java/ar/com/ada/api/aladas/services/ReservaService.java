package ar.com.ada.api.aladas.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.aladas.entities.Pasajero;
import ar.com.ada.api.aladas.entities.Reserva;
import ar.com.ada.api.aladas.entities.Vuelo;
import ar.com.ada.api.aladas.entities.Reserva.EstadoReservaEnum;
import ar.com.ada.api.aladas.entities.Vuelo.EstadoVueloEnum;
import ar.com.ada.api.aladas.repos.ReservaRepository;

@Service
public class ReservaService {

    @Autowired
    ReservaRepository repo;

    @Autowired
    VueloService vueloService;

    @Autowired
    PasajeroService pasajeroService;

    public Reserva generarReserva(Integer vueloId, Integer pasajeroId) {

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

        return repo.save(reserva);
    }

    public Reserva buscarPorId(Integer id) {
        return repo.findByReservaId(id);
    }

    public enum ValidacionReservaDataEnum {
        OK, ERROR_VUELO_NO_EXISTE, ERROR_VUELO_NO_ABIERTO
    }

    public ValidacionReservaDataEnum validar(Integer vueloId) {
        if (!vueloService.validarVueloExiste(vueloId))
            return ValidacionReservaDataEnum.ERROR_VUELO_NO_EXISTE;

        if (!validarVueloAbierto(vueloId))
            return ValidacionReservaDataEnum.ERROR_VUELO_NO_ABIERTO;

        return ValidacionReservaDataEnum.OK;

    }

    public boolean validarVueloAbierto(Integer id) {
        Vuelo vuelo = vueloService.buscarPorId(id);
        if (vuelo.getEstadoVueloId().equals(EstadoVueloEnum.ABIERTO)) {
            return true;
        }
        return false;

    }

    public List<Reserva> obtenerTodas() {
       return repo.findAll();
    }

    public boolean validarReservaExiste(Integer id) {
        Reserva reserva = repo.findByReservaId(id);
        if (reserva != null) {
            return true;
        } else
            return false;
    }

    public void modificarReserva(Integer id) {
        Reserva reserva = this.buscarPorId(id);
        Vuelo vuelo = vueloService.buscarPorId(id);
        reserva.setVuelo(vuelo);
        reserva.setFechaEmision(new Date());

        Calendar c = Calendar.getInstance();
        c.setTime(reserva.getFechaEmision());
        c.add(Calendar.DATE, 1);

        reserva.setFechaVencimiento(c.getTime());
       
        repo.save(reserva);
    }

    public void eliminarReservaPorId(Integer id) {
        repo.deleteById(id);
    }
    



}
