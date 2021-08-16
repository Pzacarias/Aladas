package ar.com.ada.api.aladas.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.aladas.entities.*;
import ar.com.ada.api.aladas.entities.Vuelo.EstadoVueloEnum;
import ar.com.ada.api.aladas.repos.VueloRepository;

@Service
public class VueloService {

    @Autowired
    private VueloRepository repo;

    @Autowired
    private AeropuertoService aeroService;

    public void crear(Vuelo vuelo) {

        vuelo.setEstadoVueloId(EstadoVueloEnum.GENERADO);
        repo.save(vuelo);

    }

    public Vuelo crear(Date fecha, Integer capacidad, String aeropuertoOrigenIATA, String aeropuertoDestinoIATA,
            BigDecimal precio, String codigoMoneda) {

        Vuelo vuelo = new Vuelo();
        vuelo.setFecha(fecha);
        vuelo.setCapacidad(capacidad);
        vuelo.setEstadoVueloId(EstadoVueloEnum.GENERADO);

        Aeropuerto aeropuertoOrigen = aeroService.buscarPorCodigoIATA(aeropuertoOrigenIATA);

        Aeropuerto aeropuertoDestino = aeroService.buscarPorCodigoIATA(aeropuertoDestinoIATA);

        vuelo.setAeropuertoOrigen(aeropuertoOrigen.getAeropuertoId());
        vuelo.setAeropuertoDestino(aeropuertoDestino.getAeropuertoId());

        vuelo.setPrecio(precio);
        vuelo.setCodigoMoneda(codigoMoneda);

        repo.save(vuelo);

        return vuelo;

    }

    public ValidacionVueloDataEnum validar(Vuelo vuelo) {

        if (!validarPrecio(vuelo))
            return ValidacionVueloDataEnum.ERROR_PRECIO;

        if (!validarAeropuertoOrigenDiffDestino(vuelo))
            return ValidacionVueloDataEnum.ERROR_AEROPUERTOS_IGUALES;

        if (!validarAeropuertoExiste(vuelo.getAeropuertoOrigen()))
            return ValidacionVueloDataEnum.ERROR_AEROPUERTO_ORIGEN_NO_EXISTE;

        if (!validarAeropuertoExiste(vuelo.getAeropuertoDestino()))
            return ValidacionVueloDataEnum.ERROR_AEROPUERTO_DESTINO_NO_EXISTE;

        if (!validarCapacidadMinima(vuelo))
            return ValidacionVueloDataEnum.ERROR_CAPACIDAD_MINIMA;

        if (!validarCapacidadMaxima(vuelo))
            return ValidacionVueloDataEnum.ERROR_CAPACIDAD_MAXIMA;
        
        
        return ValidacionVueloDataEnum.OK;

    }

    public boolean validarAeropuertoExiste(Integer id) {
        if (aeroService.buscarPorAeropuertoId(id) != null) {
            return true;
        } else
            return false;

    }

    public boolean validarPrecio(Vuelo vuelo) {

        if (vuelo.getPrecio() == null)
            return false;

        if (vuelo.getPrecio().doubleValue() > 0)
            return true;
        return false;

    }

    public boolean validarCapacidadMinima(Vuelo vuelo) {

        // Boeing 737 

        if (vuelo.getCapacidad() < 85) {
            return false;
        }
        return true;
    }

    public boolean validarCapacidadMaxima(Vuelo vuelo) {

        // Airbus 380 con clase Business y First Class

        if (vuelo.getCapacidad() > 500) {
            return false;
        }
        return true;
    }

    public boolean validarAeropuertoOrigenDiffDestino(Vuelo vuelo) {

        return vuelo.getAeropuertoDestino().intValue() != vuelo.getAeropuertoOrigen().intValue();

    }

    public enum ValidacionVueloDataEnum {
        OK, ERROR_PRECIO, ERROR_AEROPUERTO_ORIGEN_NO_EXISTE, ERROR_AEROPUERTO_DESTINO_NO_EXISTE, ERROR_FECHA,
        ERROR_MONEDA, ERROR_CAPACIDAD_MINIMA, ERROR_CAPACIDAD_MAXIMA, ERROR_AEROPUERTOS_IGUALES, ERROR_GENERAL

    }

    public Vuelo buscarPorId(Integer id) {
        return repo.findByVueloId(id);

    }

    public void actualizar(Vuelo vuelo) {
        repo.save(vuelo);
    }

    public List<Vuelo> traerVuelosAbiertos() {
        return repo.findByEstadoVueloId(EstadoVueloEnum.ABIERTO.getValue());
    }

    public List<Vuelo> obtenerTodos() {
        return repo.findAll();
    }

    public boolean validarVueloExiste(Integer id) {
        if (buscarPorId(id) != null) {
            return true;
        } else
            return false;

    }

    public void eliminarVueloPorId(Integer id) {
        repo.deleteById(id);
    }

    public Vuelo modificarVuelo(Date fecha, Integer capacidad, Integer aeropuertoOrigen, Integer aeropuertoDestino,
            BigDecimal precio, String codigoMoneda, Integer id) {
        Vuelo vuelo = buscarPorId(id);
        vuelo.setFecha(fecha);
        vuelo.setCapacidad(capacidad);
        vuelo.setAeropuertoOrigen(aeropuertoOrigen);
        vuelo.setAeropuertoDestino(aeropuertoDestino);
        vuelo.setPrecio(precio);
        vuelo.setCodigoMoneda(codigoMoneda);

        return repo.save(vuelo);

    }
}