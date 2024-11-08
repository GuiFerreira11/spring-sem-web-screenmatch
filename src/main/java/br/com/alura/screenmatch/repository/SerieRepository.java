package br.com.alura.screenmatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.screenmatch.models.Serie;

public interface SerieRepository extends JpaRepository<Serie, Long> {
}
