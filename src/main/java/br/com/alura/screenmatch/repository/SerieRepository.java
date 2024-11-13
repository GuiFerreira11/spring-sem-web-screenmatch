package br.com.alura.screenmatch.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.alura.screenmatch.models.Categoria;
import br.com.alura.screenmatch.models.Episodio;
import br.com.alura.screenmatch.models.Serie;

public interface SerieRepository extends JpaRepository<Serie, Long> {
  Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

  List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, double avaliacao);

  List<Serie> findTop5ByOrderByAvaliacaoDesc();

  List<Serie> findByGenero(Categoria categoria);

  List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int temporadas, double avaliacao);

  @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :temporadas AND s.avaliacao >= :avaliacao")
  List<Serie> seriesPorTemporadaEAvaliacao(int temporadas, double avaliacao);

  @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nomeEpisodio%")
  List<Episodio> episodioPorNome(String nomeEpisodio);

  @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
  List<Episodio> topEpisodiosPorSerie(Serie serie);

  @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
  List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);
}
