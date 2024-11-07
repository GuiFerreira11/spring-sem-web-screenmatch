package br.com.alura.screenmatch.main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.screenmatch.models.DadosSerie;
import br.com.alura.screenmatch.models.DadosTemporada;
import br.com.alura.screenmatch.models.Serie;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

public class Main {

  private Scanner scanner = new Scanner(System.in);
  private ConsumoApi consumo = new ConsumoApi();
  private ConverteDados conversor = new ConverteDados();

  private final String ENDERECO = "http://www.omdbapi.com/?t=";
  private final String API_KEY = "&apikey=e5a99d20";

  private List<DadosSerie> dadosSeries = new ArrayList<>();

  public void exibeMenu() {

    int opcao = -1;

    while (opcao != 0) {

      String menu = """
          1 - Buscar séries
          2 - Buscar eposódios
          3 - Listar séries buscadas

          0 - sair
          """;

      System.out.println(menu);

      opcao = scanner.nextInt();
      scanner.nextLine();

      switch (opcao) {
        case 1:
          buscarSerieWeb();
          break;
        case 2:
          buscarEpisodiosPorSerie();
          break;
        case 3:
          listarSeriesBuscas();
          break;
        case 0:
          System.out.println("Saindo ...");
          break;
        default:
          System.out.println("Opção inválida");
          ;
      }
    }
  }

  private DadosSerie getDadosSerie() {
    System.out.println("Digite o nome da série que deseja buscar");
    String nomeSerie = scanner.nextLine();
    String json = consumo.obterDados(ENDERECO + nomeSerie.replaceAll(" ", "+") + API_KEY);
    DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
    return dados;
  }

  private void buscarSerieWeb() {
    DadosSerie dados = getDadosSerie();
    dadosSeries.add(dados);
    System.out.println(dados);
  }

  private void buscarEpisodiosPorSerie() {
    DadosSerie dados = getDadosSerie();
    List<DadosTemporada> temporadas = new ArrayList<>();

    for (int i = 1; i <= dados.totalTemporadas(); i++) {
      String json = consumo.obterDados(ENDERECO + dados.titulo().replaceAll(" ", "+") + "&seasons=" + i + API_KEY);
      DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
      temporadas.add(dadosTemporada);
    }

    temporadas.forEach(System.out::println);
  }

  private void listarSeriesBuscas() {
    List<Serie> series = new ArrayList<>();
    series = dadosSeries.stream()
      .map(d -> new Serie(d))
      .collect(Collectors.toList());
    series.stream()
      .sorted(Comparator.comparing(Serie::getGenero))
      .forEach(System.out::println);
  }
}
