package ar.com.ada.api.aladas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ReservaResponse> generarReserva(@RequestBody InfoReservaNueva infoReserva) {
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
            rta.message = "Error(" + resultado.toString() + ")";

            return ResponseEntity.badRequest().body(rta);
        }

    }

}
