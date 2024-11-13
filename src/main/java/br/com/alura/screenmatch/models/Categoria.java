package br.com.alura.screenmatch.models;

public enum Categoria {
  ACAO("Action", "Ação"),
  ROMANCE("Romance", "Romance"),
  COMEDIA("Comedy", "Comédia"),
  DRAMA("Drama", "Drama"),
  CRIME("Crime", "Crime");

  private String categoriaOmbd;
  private String categoriaPortugues;

  Categoria(String categoriaOmbd, String categoriaPortugues) {
    this.categoriaOmbd = categoriaOmbd;
    this.categoriaPortugues = categoriaPortugues;
  }

  public static Categoria fromString(String text) {
    for (Categoria categoria : Categoria.values()) {
      if (categoria.categoriaOmbd.equalsIgnoreCase(text)) {
        return categoria;
      }
    }
    throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
  }

  public static Categoria fromPortugues(String text) {
    for (Categoria categoria : Categoria.values()) {
      if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
        return categoria;
      }
    }
    throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
  }
}
