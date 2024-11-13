package br.com.alura.screenmatch.models;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

import br.com.alura.screenmatch.service.ConsultaGemini;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "series")
public class Serie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(unique = true)
  private String titulo;
  private Integer totalTemporadas;
  private Double avaliacao;
  @Enumerated(EnumType.STRING)
  private Categoria genero;
  private String urlPoster;
  private String sinopse;
  private String atores;
  @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<Episodio> episodios = new ArrayList<>();

  public Serie() {
  }

  public Serie(DadosSerie dadosSerie) {
    this.titulo = dadosSerie.titulo();
    this.totalTemporadas = dadosSerie.totalTemporadas();
    this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
    this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
    this.urlPoster = dadosSerie.urlPoster();
    this.sinopse = ConsultaGemini.obterTraducao(dadosSerie.sinopse().trim());
    this.atores = dadosSerie.atores();
  }

  @Override
  public String toString() {
    return "Genero = " + genero + ", Titulo = " + titulo + ", Total de Temporadas = " + totalTemporadas
        + ", Avaliacao = " + avaliacao + ", Poster = " + urlPoster + ", Sinopse = " + sinopse + ", Atores = " + atores
        + ", Episódios =" + episodios;
  }

  public String getTitulo() {
    return titulo;
  }

  public Integer getTotalTemporadas() {
    return totalTemporadas;
  }

  public Double getAvaliacao() {
    return avaliacao;
  }

  public Categoria getGenero() {
    return genero;
  }

  public String getUrlPoster() {
    return urlPoster;
  }

  public String getSinopse() {
    return sinopse;
  }

  public String getAtores() {
    return atores;
  }

  public long getId() {
    return id;
  }

  public List<Episodio> getEpisodios() {
    return episodios;
  }

  public void setEpisodios(List<Episodio> episodios) {
    episodios.forEach(e -> e.setSerie(this));
    this.episodios = episodios;
  }

}
