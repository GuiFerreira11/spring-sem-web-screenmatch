package br.com.alura.screenmatch.main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.screenmatch.models.Categoria;
import br.com.alura.screenmatch.models.DadosSerie;
import br.com.alura.screenmatch.models.DadosTemporada;
import br.com.alura.screenmatch.models.Episodio;
import br.com.alura.screenmatch.models.Serie;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {

  private Scanner scanner = new Scanner(System.in);
  private ConsumoApi consumo = new ConsumoApi();
  private ConverteDados conversor = new ConverteDados();

  private Dotenv dotenv = Dotenv.load();

  private final String ENDERECO = "http://www.omdbapi.com/?t=";
  private final String API_SECRET = dotenv.get("API_KEY_OMDB");
  private final String API_KEY = "&apikey=" + API_SECRET;

  private SerieRepository repository;

  private List<Serie> series = new ArrayList<>();

  public Main(SerieRepository repository) {
    this.repository = repository;
  }

  public void exibeMenu() {

    int opcao = -1;

    while (opcao != 0) {

      String menu = """
          1 - Buscar séries
          2 - Buscar eposódios
          3 - Listar séries buscadas
          4 - Buscar série por título
          5 - Buscar série por ator
          6 - Top 5 séries
          7 - Buscar séries por categoria
          8 - Filtrar séries

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
        case 4:
          buscarSeriePorTitulo();
          break;
        case 5:
          buscarSeriePorAtor();
          break;
        case 6:
          buscarTop5Series();
          break;
        case 7:
          buscarPorCategoria();
          break;
        case 8:
          buscarSeriePorTemporadaEAvaliacao();
          break;
        case 0:
          System.out.println("Saindo ...");
          break;
        default:
          System.out.println("Opção inválida");
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
    Serie serie = new Serie(dados);
    repository.save(serie);
    System.out.println(serie);
  }

  private void buscarEpisodiosPorSerie() {
    listarSeriesBuscas();
    System.out.println("Escolha uma série para obter mais informações:");
    String nomeSerie = scanner.nextLine();
    Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

    if (serie.isPresent()) {

      Serie serieEncontrada = serie.get();
      List<DadosTemporada> temporadas = new ArrayList<>();

      for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
        String json = consumo.obterDados(
            ENDERECO + serieEncontrada.getTitulo().toLowerCase().replaceAll(" ", "+") + "&season=" + i + API_KEY);
        System.out.println(json);
        DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
        temporadas.add(dadosTemporada);
      }
      temporadas.forEach(System.out::println);

      List<Episodio> episodios = temporadas.stream()
          .flatMap(t -> t.episodios().stream()
              .map(e -> new Episodio(t.numero(), e)))
          .collect(Collectors.toList());

      serieEncontrada.setEpisodios(episodios);
      repository.save(serieEncontrada);
    } else {
      System.out.println("Serie não encontrada!");
    }
  }

  private void listarSeriesBuscas() {
    series = repository.findAll();
    series.stream()
        .sorted(Comparator.comparing(Serie::getGenero))
        .forEach(System.out::println);
  }

  private void buscarSeriePorTitulo() {
    System.out.println("Escolha uma série para obter mais informações:");
    String nomeSerie = scanner.nextLine();
    Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

    if (serie.isPresent()) {
      System.out.println("Dados da série: " + serie.get());
    } else {
      System.out.println("Serie não encontrada!");
    }
  }

  private void buscarSeriePorAtor() {
    System.out.println("Digite o nome do ator que deseja procurar");
    String nomeAtor = scanner.nextLine();
    System.out.println("Avaliação minima:");
    double avaliacaoSelecionada = scanner.nextDouble();
    List<Serie> series = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor,
        avaliacaoSelecionada);
    System.out.println("Séries encontradas com " + nomeAtor);
    series.forEach(s -> System.out.println(s.getTitulo() + " - avaliação: " + s.getAvaliacao()));
  }

  private void buscarTop5Series() {
    List<Serie> topSeries = repository.findTop5ByOrderByAvaliacaoDesc();
    topSeries.forEach(s -> System.out.println(s.getTitulo() + " - avaliação: " + s.getAvaliacao()));
  }

  private void buscarPorCategoria() {
    System.out.println("Qual a catagoria/gênero que deseja buscar?");
    String categoriaBuscada = scanner.nextLine();
    Categoria categoria = Categoria.fromPortugues(categoriaBuscada);
    List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
    System.out.println("Séries da categoria " + categoriaBuscada);
    seriesPorCategoria.forEach(s -> System.out.println(s.getTitulo() + " - avaliação: " + s.getAvaliacao()));
  }

  private void buscarSeriePorTemporadaEAvaliacao() {
    System.out.println("Qual o limíte de temporadas da série?");
    int limiteTemporadas = scanner.nextInt();
    scanner.nextLine();
    System.out.println("Qual a avaliação mínima?");
    double avaliacaoMinima = scanner.nextDouble();
    scanner.nextLine();
    List<Serie> seriesFiltrada = repository
        .findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(limiteTemporadas, avaliacaoMinima);
    System.out.println("Series filtradas:");
    seriesFiltrada.forEach(s -> System.out.println(s.getTitulo() + " - avaliação: " + s.getAvaliacao()));
  }
}
