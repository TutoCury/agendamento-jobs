package br.com.tuto.agendamento.job;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, String> {

    List<Job> findByLigadoIsTrueAndRodandoIsFalse();

}
