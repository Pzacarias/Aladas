package ar.com.ada.api.aladas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.aladas.entities.Aeropuerto;
import ar.com.ada.api.aladas.models.response.GenericResponse;
import ar.com.ada.api.aladas.services.AeropuertoService;

@RestController
public class AeropuertoController {
    
    @Autowired
    AeropuertoService service;

    @PostMapping("/api/aeropuertos")
    public ResponseEntity<GenericResponse> crear(@RequestBody Aeropuerto aeropuerto){
        
        GenericResponse respuesta = new GenericResponse ();

        if(service.validarAeropuertoExiste(aeropuerto.getAeropuertoId()) == false){

        service.crear(aeropuerto.getAeropuertoId(), aeropuerto.getNombre(), aeropuerto.getCodigoIATA());

        respuesta.isOk = true;
        respuesta.message = "Se creo el aeropuerto correctamente";
        respuesta.id = aeropuerto.getAeropuertoId();

        return ResponseEntity.ok(respuesta);
        }
        
        else {
        respuesta.isOk = false;
        respuesta.message="El aeropuerto que quiere crear ya existe";

        return ResponseEntity.badRequest().body(respuesta);
        }
    }
}
