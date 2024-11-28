package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.models.Categoria;

public record SerieDTO(
    Long id,
    String titulo,
    Integer totalTemporadas,
    Double avaliacao,
    Categoria genero,
    String urlPoster,
    String sinopse,
    String atores) {}
