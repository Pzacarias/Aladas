
package ar.com.ada.api.aladas.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.aladas.entities.Pasaje;
import ar.com.ada.api.aladas.entities.Reserva;
import ar.com.ada.api.aladas.entities.Reserva.EstadoReservaEnum;
import ar.com.ada.api.aladas.repos.PasajeRepository;

@Service
public class PasajeService {

    @Autowired
    PasajeRepository repo;

    @Autowired
    ReservaService resService;

    @Autowired
    VueloService vueloService;

    public Pasaje emitir(Integer reservaId) {

        Pasaje pasaje = new Pasaje();
        pasaje.setFechaEmision(new Date());
        pasaje.setInfoPago("PAGADO");
        Reserva reserva = resService.buscarPorId(reservaId);
        reserva.setEstadoReservaId(EstadoReservaEnum.EMITIDA);
        reserva.setPasaje(pasaje);

        Integer nuevaCapacidad = reserva.getVuelo().getCapacidad() - 1;
        reserva.getVuelo().setCapacidad(nuevaCapacidad);

        vueloService.actualizar(reserva.getVuelo());
        // reservaService.actualizar(reserva);
        // pasajeService.actualizar(pasa);

        return pasaje;
    }

    public boolean validadCapacidadDisponible(Integer capacidad) {
        if (capacidad <= 0) {
            return false;
        } else
            return true;
    }

   
    public enum ValidacionPasajeDataEnum {
        OK, ERROR_CAPACIDAD_MAXIMA_ALCANZADA, ERROR_RESERVA_NO_EXISTE, ERROR_RESERVA_YA_TIENE_UN_PASAJE
    }

    public ValidacionPasajeDataEnum validar(Integer reservaId) {
        Reserva reserva = resService.buscarPorId(reservaId);

        if (!resService.validarReservaExiste(reservaId))
            return ValidacionPasajeDataEnum.ERROR_RESERVA_NO_EXISTE;

        if (!validadCapacidadDisponible(reserva.getVuelo().getCapacidad()))
            return ValidacionPasajeDataEnum.ERROR_CAPACIDAD_MAXIMA_ALCANZADA;

        if(!validarReservaYaTieneUnPasajeAsignado(reservaId))
            return ValidacionPasajeDataEnum.ERROR_RESERVA_YA_TIENE_UN_PASAJE;

        return ValidacionPasajeDataEnum.OK;

    }

    public boolean validarReservaYaTieneUnPasajeAsignado (Integer id){
        Reserva reserva = resService.buscarPorId(id);

        if (reserva.getPasaje() != null) {
            return true;
        } else
            return false;

    }

    public List<Pasaje> obtenerTodos() {
        return repo.findAll();
    }

    public Pasaje buscarPorId(Integer id) {
        return repo.findByPasajeId(id);
    }

    public boolean validarPasajeExiste(Integer id) {
        Pasaje pasaje = repo.findByPasajeId(id);
        if (pasaje != null) {
            return true;
        } else
            return false;
    }

    public void eliminarPasajePorId(Integer id) {
        repo.deleteById(id);
    }

    public Pasaje modificarPasaje(Integer id, Integer reservaId) {
        Pasaje pasaje = buscarPorId(id);
        pasaje.setFechaEmision(new Date());

        Reserva reserva = resService.buscarPorId(reservaId);
        reserva.setEstadoReservaId(EstadoReservaEnum.EMITIDA);
        reserva.setPasaje(pasaje);
        Integer nuevaCapacidad = reserva.getVuelo().getCapacidad() - 1;

        if (validadCapacidadDisponible(reserva.getVuelo().getCapacidad())) {
            reserva.getVuelo().setCapacidad(nuevaCapacidad);

        } else {
            reserva.getVuelo().setCapacidad(null);
        }

        vueloService.actualizar(reserva.getVuelo());
        return repo.save(pasaje);
    }
}
