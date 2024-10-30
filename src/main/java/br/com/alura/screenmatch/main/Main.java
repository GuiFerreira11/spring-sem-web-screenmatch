package br.com.alura.screenmatch.main;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.screenmatch.models.DadosSerie;
import br.com.alura.screenmatch.models.DadosTemporada;
import br.com.alura.screenmatch.models.Episodio;
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
    System.out.println();

    String json = consumo.obterDados(ENDERECO + nomeSerie.replaceAll(" ", "+") + API_KEY);

    System.out.println("Dados da série");
    DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);
    System.out.println();

    System.out.println(dadosSerie);

    List<DadosTemporada> listaTemporadas = new ArrayList<>();

    for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
      json = consumo.obterDados(ENDERECO + nomeSerie.replaceAll(" ", "+") + "&season=" + i + API_KEY);
      DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
      listaTemporadas.add(dadosTemporada);
    }

    List<Episodio> episodios = listaTemporadas.stream()
        .flatMap(t -> t.episodios().stream()
            .map(e -> new Episodio(t.numero(), e)))
        .collect(Collectors.toList());

    System.out.println("Os 5 melhores episódios da série:");
    episodios.stream()
        .sorted(Comparator.comparingDouble(Episodio::getAvaliacao).reversed())
        .limit(5)
        .forEach(System.out::println);
    System.out.println();

    System.out.println("A partir de qual ano você deseja assistir aos episódios?");
    int anoFiltro = scanner.nextInt();
    scanner.nextLine();
    LocalDate dataFiltro = LocalDate.of(anoFiltro, 1, 1);
    DateTimeFormatter dataFormater = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    episodios.stream()
      .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataFiltro))
      .forEach(e -> System.out.println(
        "Temporada: " + e.getTemporada() +
        ". Episódio: " + e.getTitulo() +
        ". Data de Lançamento: " + e.getDataLancamento().format(dataFormater)
      ));

  }
}
