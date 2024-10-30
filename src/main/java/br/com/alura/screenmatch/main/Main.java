package br.com.alura.screenmatch.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import br.com.alura.screenmatch.models.DadosSerie;
import br.com.alura.screenmatch.models.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

public class Main {

  private Scanner scanner = new Scanner(System.in);
  private ConsumoApi consumo = new ConsumoApi();
  private ConverteDados conversor = new ConverteDados();

  private final String ENDERECO = "http://www.omdbapi.com/?t=";
  private final String API_KEY = "&apikey=e5a99d20";

  public void exibeMenu() {
    System.out.println("Digite o nome da série que deseja procurar:");
    String nomeSerie = scanner.nextLine();
    String json = consumo.obterDados(ENDERECO + nomeSerie.replaceAll(" ", "+") + API_KEY);
    DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
    System.out.println(dadosSerie);

    List<DadosTemporada> listaTemporadas = new ArrayList<>();

    for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
      json = consumo.obterDados(ENDERECO + nomeSerie.replaceAll(" ", "+") + "&season=" + i + "&apikey=e5a99d20");
      DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
      listaTemporadas.add(dadosTemporada);
    }
    // listaTemporadas.forEach(System.out::println);
    listaTemporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
  }
}
