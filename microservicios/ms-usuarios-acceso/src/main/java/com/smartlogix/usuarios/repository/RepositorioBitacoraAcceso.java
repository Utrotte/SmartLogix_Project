package com.smartlogix.usuarios.repository;

import com.smartlogix.usuarios.model.BitacoraAcceso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioBitacoraAcceso extends JpaRepository<BitacoraAcceso, Long> {
}
