package br.com.project.insurance.repository;

import br.com.project.insurance.entity.Apolice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApoliceRepository extends JpaRepository<Apolice, Integer> {
}
