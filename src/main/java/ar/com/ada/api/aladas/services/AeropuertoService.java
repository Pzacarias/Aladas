package ar.com.ada.api.aladas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.aladas.entities.Aeropuerto;
import ar.com.ada.api.aladas.repos.AeropuertoRepository;

@Service
public class AeropuertoService {

    @Autowired
    private AeropuertoRepository repo;

    public void crear(Integer aeropuertoId, String nombre, String codigoIATA) {

        Aeropuerto aeropuerto = new Aeropuerto();
        aeropuerto.setAeropuertoId(aeropuertoId);
        aeropuerto.setNombre(nombre);
        aeropuerto.setCodigoIATA(codigoIATA);

        repo.save(aeropuerto);
    }

    public List<Aeropuerto> obtenerTodos() {
        return repo.findAll();

    }

    public Aeropuerto buscarPorCodigoIATA(String codigoIATA) {
        return repo.findByCodigoIATA(codigoIATA);
    }

    public boolean validarIATA(String codigoIATA) {
        if (codigoIATA.length() != 3)
            return false;

        for (int i = 0; i < codigoIATA.length(); i++) {
            char c = codigoIATA.charAt(i);

            if (!(c >= 'A' && c <= 'Z'))
                return false;

        }
        return true;
    }
    
    public boolean validarAeropuertoExiste(Integer aeropuertoId) {
        Aeropuerto aeropuerto = repo.findByAeropuertoId(aeropuertoId);
        if (aeropuerto != null) {
            return true;
        } else
            return false;

    }

    public enum ValidacionAeropuertoDataEnum {
        OK, ERROR_AEROPUERTO_YA_EXISTE, ERROR_CODIGO_IATA,
    }

    public ValidacionAeropuertoDataEnum validar(Aeropuerto aeropuerto) {

        if (validarAeropuertoExiste(aeropuerto.getAeropuertoId()))
            return ValidacionAeropuertoDataEnum.ERROR_AEROPUERTO_YA_EXISTE;

        if (!validarIATA(aeropuerto.getCodigoIATA()))
            return ValidacionAeropuertoDataEnum.ERROR_CODIGO_IATA;

        return ValidacionAeropuertoDataEnum.OK;
    }

    public boolean validarTraerPorCodigoIata(String codigoIATA) {
        if (this.buscarPorCodigoIATA(codigoIATA) == null) {
            return false;
        }
        return true;
    }

    public boolean validarTraerPorId(Integer id) {
        if (repo.findByAeropuertoId(id) == null) {
            return false;
        }
        return true;
    }

    public Aeropuerto buscarPorAeropuertoId(Integer id) {
        return repo.findByAeropuertoId(id);
    }

    public void modificarAeropuerto(Integer id, String nombreNuevo, String nuevoCodigoIATA) {
        Aeropuerto aeropuerto = this.buscarPorAeropuertoId(id);
        aeropuerto.setCodigoIATA(nuevoCodigoIATA);
        aeropuerto.setNombre(nombreNuevo);
        repo.save(aeropuerto);
    }

    public ValidacionModificacionAeropuertoEnum validarModificarAeropuertoPorId(Integer id, String nombreNuevo,
            String nuevoCodigoIATA) {
        if (nombreNuevo .length() == 0) {
            return ValidacionModificacionAeropuertoEnum.ERROR_NOMBRE_VACIO;
        }
        if (!validarIATA(nuevoCodigoIATA))
            return ValidacionModificacionAeropuertoEnum.ERROR_CODIGO_IATA;

        return ValidacionModificacionAeropuertoEnum.OK;
    }


    public enum ValidacionModificacionAeropuertoEnum {
        ERROR_NOMBRE_VACIO, ERROR_CODIGO_IATA, OK
    }

    public void eliminarAeropuertoPorId(Integer id) {
        repo.deleteById(id);
    }
}