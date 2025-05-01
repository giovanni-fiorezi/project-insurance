package br.com.project.insurance.repository;

import br.com.project.insurance.entity.Parcela;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParcelaRepository extends JpaRepository<Parcela, Integer> {
}
