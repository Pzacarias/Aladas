package ar.com.ada.api.aladas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ar.com.ada.api.aladas.entities.Aeropuerto;
import ar.com.ada.api.aladas.entities.Vuelo;
import ar.com.ada.api.aladas.models.response.GenericResponse;
import ar.com.ada.api.aladas.services.AeropuertoService;
import ar.com.ada.api.aladas.services.VueloService;
import ar.com.ada.api.aladas.models.request.EstadoVueloRequest;

import static ar.com.ada.api.aladas.services.VueloService.ValidacionVueloDataEnum;

import java.util.List;

@RestController
public class VueloController {

    private VueloService service;

    private AeropuertoService aeropuertoService;

    public VueloController(VueloService service, AeropuertoService aeropuertoService) {
        this.service = service;
        this.aeropuertoService = aeropuertoService;
    }

    @PostMapping("/api/vuelos")
    public ResponseEntity<GenericResponse> postCrearVuelo(@RequestBody Vuelo vuelo) {
        GenericResponse respuesta = new GenericResponse();

        ValidacionVueloDataEnum resultadoValidacion = service.validar(vuelo);
        if (resultadoValidacion == ValidacionVueloDataEnum.OK) {
            service.crear(vuelo);

            respuesta.isOk = true;
            respuesta.id = vuelo.getVueloId();
            respuesta.message = "Vuelo creado correctamente";

            return ResponseEntity.ok(respuesta);
        } else {

            respuesta.isOk = false;
            respuesta.message = "Error(" + resultadoValidacion.toString() + ")";

            return ResponseEntity.badRequest().body(respuesta);
        }

    }

    @PutMapping("/api/vuelos/{id}/estados")
    public ResponseEntity<GenericResponse> putActualizarEstadoVuelo(@PathVariable Integer id,
            @RequestBody EstadoVueloRequest estadoVuelo) {

        GenericResponse r = new GenericResponse();

        Vuelo vuelo = service.buscarPorId(id);
        vuelo.setEstadoVueloId(estadoVuelo.estado);
        service.actualizar(vuelo);

        r.isOk = true;
        r.message = "El estado ha sido actualizado";
        
        return ResponseEntity.ok(r);
    }

    @GetMapping("/api/vuelos/abiertos")
    public ResponseEntity<List<Vuelo>> getVuelosAbiertos(){
        
        return ResponseEntity.ok(service.traerVuelosAbiertos());
    }

    @GetMapping("/api/vuelos")
    public ResponseEntity<List<Vuelo>> traerAeropuertos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("api/vuelos/{id}")
    public ResponseEntity<?> traerAeropueroPorId(@PathVariable Integer id) {
        GenericResponse respuesta = new GenericResponse();
        if (!service.validarTraerPorId(id)) {
            respuesta.isOk = false;
            respuesta.message = "El número de Id del vuelo ingresado no es correcto.";
            return ResponseEntity.badRequest().body(respuesta);
        }
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @DeleteMapping ("/api/vuelos/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id){
       
        GenericResponse respuesta = new GenericResponse();
        if (service.validarVueloExiste(id)) {
            service.eliminarVueloPorId(id);
            respuesta.isOk = true;
            respuesta.message = "El vuelo ha sido eliminado correctamente.";
            return ResponseEntity.ok(respuesta);
            
        }
        else {
            respuesta.isOk = false;
            respuesta.message = "El número de Id del vuelo ingresado no es correcto.";
            return ResponseEntity.badRequest().body(respuesta);
          
        }
        
        
    }

}