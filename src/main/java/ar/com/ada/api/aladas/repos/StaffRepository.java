package ar.com.ada.api.aladas.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ar.com.ada.api.aladas.entities.Staff;

@Repository
public interface StaffRepository extends JpaRepository<Staff,Integer> {
    
}
