package ar.com.ada.api.aladas.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.com.ada.api.aladas.entities.*;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

}