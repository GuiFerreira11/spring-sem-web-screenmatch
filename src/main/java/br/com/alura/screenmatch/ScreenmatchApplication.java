package br.com.alura.screenmatch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.screenmatch.models.DadosSerie;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

  @Override
  public void run(String... args) throws Exception {
    ConsumoApi consumoApi = new ConsumoApi();
    String json = consumoApi.obterDados("http://www.omdbapi.com/?t=breaking+bad&apikey=e5a99d20");
    ConverteDados conversor = new ConverteDados();
    DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
    System.out.println(dados);
    
  }



}
