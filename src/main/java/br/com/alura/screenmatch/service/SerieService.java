package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodioDTO;
import br.com.alura.screenmatch.dto.SerieDTO;
import br.com.alura.screenmatch.models.Categoria;
import br.com.alura.screenmatch.models.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SerieService {

  @Autowired private SerieRepository repository;

  public List<SerieDTO> obterTodasAsSeries() {
    return convertData(repository.findAll());
  }

  public List<SerieDTO> obterTop5Series() {
    return convertData(repository.findTop5ByOrderByAvaliacaoDesc());
  }

  public List<SerieDTO> obterLancamentos() {
    return convertData(repository.encontrarEpisodiosMaisRecentes());
  }

  public SerieDTO obterPorId(Long id) {
    Optional<Serie> serie = repository.findById(id);
    if (serie.isPresent()) {
      Serie s = serie.get();
      return new SerieDTO(
          s.getId(),
          s.getTitulo(),
          s.getTotalTemporadas(),
          s.getAvaliacao(),
          s.getGenero(),
          s.getUrlPoster(),
          s.getSinopse(),
          s.getAtores());
    }
    return null;
  }

  public List<EpisodioDTO> obterTodasTemporadas(Long id) {
    Optional<Serie> serie = repository.findById(id);
    if (serie.isPresent()) {
      Serie s = serie.get();
      return s.getEpisodios().stream()
          .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio()))
          .collect(Collectors.toList());
    }
    return null;
  }

  public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
    return repository.obterEpisodiosPorTemporada(id, numero).stream()
        .map(e -> new EpisodioDTO(e.getTemporada(), e.getTitulo(), e.getNumeroEpisodio()))
        .collect(Collectors.toList());
  }

  public List<SerieDTO> obterSeriesPoCategoria(String categoriaSelecionada) {
    Categoria categoria = Categoria.fromPortugues(categoriaSelecionada);
    return convertData(repository.findByGenero(categoria));
  }

  private List<SerieDTO> convertData(List<Serie> series) {
    return series.stream()
        .map(
            s ->
                new SerieDTO(
                    s.getId(),
                    s.getTitulo(),
                    s.getTotalTemporadas(),
                    s.getAvaliacao(),
                    s.getGenero(),
                    s.getUrlPoster(),
                    s.getSinopse(),
                    s.getAtores()))
        .collect(Collectors.toList());
  }
}
