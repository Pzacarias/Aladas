
package ar.com.ada.api.aladas.controllers;

import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.aladas.entities.Pasaje;
import ar.com.ada.api.aladas.models.request.InfoPasajeNuevo;
import ar.com.ada.api.aladas.models.response.GenericResponse;
import ar.com.ada.api.aladas.models.response.PasajeResponse;
import ar.com.ada.api.aladas.services.PasajeService;
import ar.com.ada.api.aladas.services.ReservaService;
import ar.com.ada.api.aladas.services.PasajeService.ValidacionPasajeDataEnum;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PasajeController {

    @Autowired
    PasajeService service;

    @Autowired
    ReservaService resService;

    @PostMapping("api/pasajes")
    public ResponseEntity<?> emitir(@RequestBody InfoPasajeNuevo infoPasajes) {

        PasajeResponse respuesta = new PasajeResponse();

        ValidacionPasajeDataEnum resultado = service.validar(infoPasajes.reservaId);

        if (resultado == ValidacionPasajeDataEnum.OK) {

            Pasaje pasaje = service.emitir(infoPasajes.reservaId);

            respuesta.pasajeId = pasaje.getPasajeId();
            respuesta.fechaDeEmision = pasaje.getFechaEmision();
            respuesta.reservaId = pasaje.getReserva().getReservaId();
            respuesta.vueloId = pasaje.getReserva().getVuelo().getVueloId();
            respuesta.infoDePago = "PAGADO";
            respuesta.message = "El pasaje ha sido modificado correctamente.";

            return ResponseEntity.ok(respuesta);

        } else{

            GenericResponse rta = new GenericResponse();
            rta.isOk = false;
            rta.message = "Error(" + resultado.toString() + ")";

            return ResponseEntity.badRequest().body(rta);
        }
    }

    @GetMapping("/api/pasajes")
    public ResponseEntity<List<Pasaje>> traerPasajes() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("api/pasajes/{id}")
    public ResponseEntity<?> traerPasajePorId(@PathVariable Integer id) {
        GenericResponse respuesta = new GenericResponse();
        if (!service.validarPasajeExiste(id)) {
            respuesta.isOk = false;
            respuesta.message = "El número de Id ingresado no es correcto.";
            return ResponseEntity.badRequest().body(respuesta);
        }
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/api/pasajes/{id}")
    public ResponseEntity<?> modificar(@PathVariable Integer id, @RequestBody InfoPasajeNuevo infoNueva) {

        PasajeResponse respuesta = new PasajeResponse();

        ValidacionPasajeDataEnum resultado = service.validar(infoNueva.reservaId);

        if (resultado == ValidacionPasajeDataEnum.OK) {

            Pasaje pasaje = service.modificarPasaje(id, infoNueva.reservaId);
            
                respuesta.pasajeId = pasaje.getPasajeId();
                respuesta.fechaDeEmision = pasaje.getFechaEmision();
                respuesta.reservaId = pasaje.getReserva().getReservaId();
                respuesta.vueloId = pasaje.getReserva().getVuelo().getVueloId();
                respuesta.infoDePago = "PAGADO";
                respuesta.message = "El pasaje ha sido modificado correctamente.";

                return ResponseEntity.ok(respuesta);
            
        }else {
            GenericResponse rta = new GenericResponse();
            rta.isOk = false;
            rta.message = "Error(" + resultado.toString() + ")";

            return ResponseEntity.badRequest().body(rta);
        }
    }

    @DeleteMapping("/api/pasajes/{id}")
    public ResponseEntity<GenericResponse> eliminar(@PathVariable Integer id) {

        GenericResponse respuesta = new GenericResponse();
        if (!service.validarPasajeExiste(id)) {
            service.eliminarPasajePorId(id);
            respuesta.isOk = true;
            respuesta.message = "El aeropuerto ha sido eliminado correctamente.";
            return ResponseEntity.ok(respuesta);

        } else {
            respuesta.isOk = false;
            respuesta.message = "El número de Id ingresado no es correcto.";
            return ResponseEntity.badRequest().body(respuesta);

        }

    }

}