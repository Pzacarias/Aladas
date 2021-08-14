  
package ar.com.ada.api.aladas.controllers;

import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.aladas.entities.Pasaje;
import ar.com.ada.api.aladas.models.request.InfoPasajeNuevo;
import ar.com.ada.api.aladas.models.response.GenericResponse;
import ar.com.ada.api.aladas.services.PasajeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class PasajeController {

    @Autowired
    PasajeService service;


    @PostMapping("api/pasajes")
    public ResponseEntity<GenericResponse> emitir(@RequestBody InfoPasajeNuevo infoPasajes) {
        
        GenericResponse respuesta = new GenericResponse();

        Pasaje pasaje = service.emitir(infoPasajes.reservaId);

        respuesta.message = "El pasaje ha sido generado correctamente.";
        respuesta.isOk = true;
        respuesta.id = pasaje.getPasajeId();

        return ResponseEntity.ok(respuesta);
    }
    
    
}