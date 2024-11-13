package br.com.alura.screenmatch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.screenmatch.models.Categoria;
import br.com.alura.screenmatch.models.Serie;

public interface SerieRepository extends JpaRepository<Serie, Long> {
  Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

  List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, double avaliacao);

  List<Serie> findTop5ByOrderByAvaliacaoDesc();

  List<Serie> findByGenero(Categoria categoria);

  List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int Temporadas, double avaliacao);
}
