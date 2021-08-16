package ar.com.ada.api.aladas.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import ar.com.ada.api.aladas.entities.Reserva;
import ar.com.ada.api.aladas.entities.Usuario;
import ar.com.ada.api.aladas.models.request.InfoReservaNueva;
import ar.com.ada.api.aladas.models.response.GenericResponse;
import ar.com.ada.api.aladas.models.response.ReservaResponse;
import ar.com.ada.api.aladas.services.ReservaService;
import ar.com.ada.api.aladas.services.UsuarioService;
import ar.com.ada.api.aladas.services.ReservaService.ValidacionReservaDataEnum;

@RestController
public class ReservaController {

    @Autowired
    ReservaService service;

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/api/reservas")
    public ResponseEntity<?> generarReserva(@RequestBody InfoReservaNueva infoReserva) {
        ReservaResponse rta = new ReservaResponse();
        

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Usuario usuario = usuarioService.buscarPorUsername(username);

        ValidacionReservaDataEnum resultado = service.validar(infoReserva.vueloId);

        if (resultado == ValidacionReservaDataEnum.OK) {

            Reserva reserva = service.generarReserva(infoReserva.vueloId, usuario.getPasajero().getPasajeroId());

            rta.id = reserva.getReservaId();
            rta.fechaDeEmision = reserva.getFechaEmision();
            rta.fechaDeVencimiento = reserva.getFechaVencimiento();
            rta.message = "Reserva creada con éxito.";

            return ResponseEntity.ok(rta);

        } else {
            GenericResponse respuesta = new GenericResponse();
            respuesta.isOk = false;
            respuesta.message = "Error(" + resultado.toString() + ")";

            return ResponseEntity.badRequest().body(respuesta);
        }

    }

    @GetMapping("/api/reservas")
    public ResponseEntity<List<Reserva>> traerReservas() {
        return ResponseEntity.ok(service.obtenerTodas());
    }

    @GetMapping("api/reservas/{id}")
    public ResponseEntity<?> traerReservasPorId(@PathVariable Integer id) {
        GenericResponse respuesta = new GenericResponse();
        if (!service.validarReservaExiste(id)) {
            respuesta.isOk = false;
            respuesta.message = "El número de Id ingresado no es correcto.";
            return ResponseEntity.badRequest().body(respuesta);
        }
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PutMapping("/reservas/{id}")
    public ResponseEntity<?> modificar(@PathVariable Integer id,
            @RequestBody InfoReservaNueva infoNueva) {

        ReservaResponse respuesta = new ReservaResponse();
      
        ValidacionReservaDataEnum resultado = service.validar(infoNueva.vueloId);

        if (resultado == ValidacionReservaDataEnum.OK) {
            
            Reserva reserva = service.modificarReserva(id);

            respuesta.id = reserva.getReservaId();
            respuesta.fechaDeEmision = reserva.getFechaEmision();
            respuesta.fechaDeVencimiento = reserva.getFechaVencimiento();
            respuesta.message = "El vuelo de la reserva ha sido modificado correctamente.";

            return ResponseEntity.ok(respuesta);
        }

        else {
            GenericResponse rta = new GenericResponse();
            rta.isOk = false;
            rta.message = "Error(" + resultado.toString() + ")";

            return ResponseEntity.badRequest().body(rta);
        }
    }


    @DeleteMapping ("/api/reservas/{id}")
    public ResponseEntity<GenericResponse> eliminar(@PathVariable Integer id){
       
        GenericResponse respuesta = new GenericResponse();
        if (service.validarReservaExiste(id)) {
            service.eliminarReservaPorId(id);
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
