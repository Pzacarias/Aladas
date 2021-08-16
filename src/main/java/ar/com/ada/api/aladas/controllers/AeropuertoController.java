package ar.com.ada.api.aladas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.com.ada.api.aladas.entities.Aeropuerto;
import ar.com.ada.api.aladas.models.request.InfoAeropuertoNuevo;
import ar.com.ada.api.aladas.models.response.GenericResponse;
import ar.com.ada.api.aladas.services.AeropuertoService;
import ar.com.ada.api.aladas.services.AeropuertoService.ValidacionAeropuertoDataEnum;
import ar.com.ada.api.aladas.services.AeropuertoService.ValidacionModificacionAeropuertoEnum;

@RestController
public class AeropuertoController {

    @Autowired
    AeropuertoService service;

    @PostMapping("/api/aeropuertos")
    public ResponseEntity<GenericResponse> crear(@RequestBody Aeropuerto aeropuerto) {

        GenericResponse respuesta = new GenericResponse();

        ValidacionAeropuertoDataEnum resultado = service.validar(aeropuerto);

        if (resultado == ValidacionAeropuertoDataEnum.OK) {
            service.crear(aeropuerto.getAeropuertoId(), aeropuerto.getNombre(), aeropuerto.getCodigoIATA());

            respuesta.isOk = true;
            respuesta.message = "Se creo el aeropuerto correctamente";
            respuesta.id = aeropuerto.getAeropuertoId();

            return ResponseEntity.ok(respuesta);
        }

        else {
            respuesta.isOk = false;
            respuesta.message = "Error(" + resultado.toString() + ")";

            return ResponseEntity.badRequest().body(respuesta);
        }
    }

    @GetMapping("/api/aeropuertos")
    public ResponseEntity<List<Aeropuerto>> traerAeropuertos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/api/aeropuertos/{codigoIATA}")
    public ResponseEntity<?> traerAeropuertoPorCodigoIATA(@PathVariable String codigoIATA) {
        GenericResponse respuesta = new GenericResponse();
        if (!service.validarTraerPorCodigoIata(codigoIATA)) {
            respuesta.isOk = false;
            respuesta.message = "El codigo ingresado no es correcto.";
            return ResponseEntity.badRequest().body(respuesta);
        }
        return ResponseEntity.ok(service.buscarPorCodigoIATA(codigoIATA));
    }

    @GetMapping("api/aeropuertos/id/{id}")
    public ResponseEntity<?> traerAeropueroPorId(@PathVariable Integer id) {
        GenericResponse respuesta = new GenericResponse();
        if (!service.validarAeropuertoExiste(id)) {
            respuesta.isOk = false;
            respuesta.message = "El número de Id ingresado no es correcto.";
            return ResponseEntity.badRequest().body(respuesta);
        }
        return ResponseEntity.ok(service.buscarPorAeropuertoId(id));
    }

    @PutMapping("/aeropuertos/{id}")
    public ResponseEntity<GenericResponse> modificar(@PathVariable Integer id,
            @RequestBody InfoAeropuertoNuevo infoAeropuerto) {

        GenericResponse respuesta = new GenericResponse();

        ValidacionModificacionAeropuertoEnum resultado = service.validarModificarAeropuertoPorId(id,
                infoAeropuerto.nombreNuevo, infoAeropuerto.nuevoCodigoIATA);

        if (resultado == ValidacionModificacionAeropuertoEnum.OK) {
            service.modificarAeropuerto(id, infoAeropuerto.nombreNuevo, infoAeropuerto.nuevoCodigoIATA);

            respuesta.isOk = true;
            respuesta.message = "Se actualizó el aeropuerto correctamente.";

            return ResponseEntity.ok(respuesta);
        }

        else {
            respuesta.isOk = false;
            respuesta.message = "Error(" + resultado.toString() + ")";

            return ResponseEntity.badRequest().body(respuesta);
        }
    }


    @DeleteMapping ("/api/aeropuertos/{id}")
    public ResponseEntity<GenericResponse> eliminar(@PathVariable Integer id){
       
        GenericResponse respuesta = new GenericResponse();
        if (service.validarAeropuertoExiste(id)) {
            service.eliminarAeropuertoPorId(id);
            respuesta.isOk = true;
            respuesta.message = "El aeropuerto ha sido eliminado correctamente.";
            return ResponseEntity.ok(respuesta);
            
        }
        else {
            respuesta.isOk = false;
            respuesta.message = "El número de Id ingresado no es correcto.";
            return ResponseEntity.badRequest().body(respuesta);
          
        }
        
        
    }
}
