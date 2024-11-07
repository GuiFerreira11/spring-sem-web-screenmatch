package br.com.alura.screenmatch.models;

import java.util.OptionalDouble;

public class Serie {
  private String titulo;
  private Integer totalTemporadas;
  private Double avaliacao;
  private Categoria genero;
  private String urlPoster;
  private String sinopse;
  private String atores;

  public Serie(DadosSerie dadosSerie) {
    this.titulo = dadosSerie.titulo();
    this.totalTemporadas = dadosSerie.totalTemporadas();
    this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
    this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
    this.urlPoster = dadosSerie.urlPoster();
    this.sinopse = dadosSerie.sinopse();
    this.atores = dadosSerie.atores();
  }

  @Override
  public String toString() {
    return "Genero = " + genero + ", Titulo = " + titulo + ", Total de Temporadas = " + totalTemporadas
        + ", Avaliacao = " + avaliacao + ", Poster = " + urlPoster + ", Sinopse = " + sinopse + ", Atores = " + atores;
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

}
