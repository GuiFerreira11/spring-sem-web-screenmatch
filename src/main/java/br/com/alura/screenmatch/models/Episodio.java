package br.com.alura.screenmatch.models;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "episodios")
public class Episodio {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Integer temporada;
  private String titulo;
  private Integer numeroEpisodio;
  private double avaliacao;
  private LocalDate dataLancamento;
  @ManyToOne
  private Serie serie;

  public Episodio() {
  }

  public Episodio(Integer temporada, DadosEpisodio dadosEpisodio) {
    this.temporada = temporada;
    this.titulo = dadosEpisodio.titulo();
    this.numeroEpisodio = dadosEpisodio.numeroEpisodio();
    try {
      this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
    } catch (NumberFormatException e) {
      this.avaliacao = 0.0;
    }
    try {
      this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
    } catch (DateTimeParseException e) {
      this.dataLancamento = null;
    }
  }

  @Override
  public String toString() {
    return "Temporada=" + temporada + ", Titulo=" + titulo + ", Numero do Episodio=" + numeroEpisodio
        + ", Avaliacao=" + avaliacao + ", Data de Lançamento=" + dataLancamento;
  }

  public Integer getTemporada() {
    return temporada;
  }

  public String getTitulo() {
    return titulo;
  }

  public Integer getNumeroEpisodio() {
    return numeroEpisodio;
  }

  public double getAvaliacao() {
    return avaliacao;
  }

  public LocalDate getDataLancamento() {
    return dataLancamento;
  }

  public Long getId() {
    return id;
  }

  public Serie getSerie() {
    return serie;
  }

  public void setSerie(Serie serie) {
    this.serie = serie;
  }

}
