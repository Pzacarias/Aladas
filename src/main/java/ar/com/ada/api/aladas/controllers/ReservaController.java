package ar.com.ada.api.aladas.controllers;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import ar.com.ada.api.aladas.entities.Reserva;
import ar.com.ada.api.aladas.entities.Usuario;
import ar.com.ada.api.aladas.models.request.InfoReservaNueva;
import ar.com.ada.api.aladas.models.response.GenericResponse;
import ar.com.ada.api.aladas.models.response.ReservaGenerationResponse;
import ar.com.ada.api.aladas.services.ReservaService;
import ar.com.ada.api.aladas.services.UsuarioService;

@RestController
public class ReservaController {
    @Autowired
    ReservaService service;

    @Autowired
    UsuarioService usuarioService;

    @PostMapping("/api/reservas")
    public ResponseEntity<ReservaGenerationResponse> generarReserva(@RequestBody InfoReservaNueva infoReserva) {
        ReservaGenerationResponse rta = new ReservaGenerationResponse();

        // Obtengo a quien esta autenticado del otro lado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // De lo que esta autenticado, obtengo su USERNAME
        String username = authentication.getName();

        // Buscar el usuario por username
        Usuario usuario = usuarioService.buscarPorUsername(username);

        // con el usuario, obtengo el pasajero, y con ese, obtengo el Id
        rta = service.generarReservaConLinkDePago(infoReserva.vueloId, usuario.getPasajero().getPasajeroId());

        return ResponseEntity.ok(rta);

    }
}