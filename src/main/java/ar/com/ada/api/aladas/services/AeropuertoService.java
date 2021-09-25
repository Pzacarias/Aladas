package ar.com.ada.api.aladas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.aladas.entities.Aeropuerto;
import ar.com.ada.api.aladas.repos.AeropuertoRepository;

@Service
public class AeropuertoService extends Servicio<Aeropuerto, Integer>  {




    public List<Aeropuerto> obtenerTodos() {

        return repo.findAll();

    }

    public Aeropuerto buscarPorCodigoIATA(String codigoIATA) {
        return ((AeropuertoRepository)repo).findByCodigoIATA(codigoIATA);
    }

    public boolean validarCodigoIATA(Aeropuerto aeropuerto) {

        // SI viene algo diferente de 3, que salga.
        if (aeropuerto.getCodigoIATA().length() != 3)
            return false;

        String codigoIATA = aeropuerto.getCodigoIATA();

        // " AP"
        for (int i = 0; i < codigoIATA.length(); i++) {
            char c = codigoIATA.charAt(i);

            if (!(c >= 'A' && c <= 'Z'))
                return false;

        }

        return true;
    }

    public boolean validarAeropuertoExiste(Integer aeropuertoId) {
        Aeropuerto aeropuerto = ((AeropuertoRepository)repo).findByAeropuertoId(aeropuertoId);
        if (aeropuerto != null) {
            return true;
        } else
            return false;

    }

    public enum ValidacionAeropuertoDataEnum {
        OK, ERROR_AEROPUERTO_YA_EXISTE, ERROR_CODIGO_IATA
    }

    public ValidacionAeropuertoDataEnum validar(Aeropuerto aeropuerto) {

        if (validarAeropuertoExiste(aeropuerto.getAeropuertoId()))
            return ValidacionAeropuertoDataEnum.ERROR_AEROPUERTO_YA_EXISTE;

        if (!validarCodigoIATA(aeropuerto))
            return ValidacionAeropuertoDataEnum.ERROR_CODIGO_IATA;

        return ValidacionAeropuertoDataEnum.OK;
    }

    

}