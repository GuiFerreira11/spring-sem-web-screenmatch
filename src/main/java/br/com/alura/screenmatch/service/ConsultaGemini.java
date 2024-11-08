package br.com.alura.screenmatch.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.googleai.GoogleAiGeminiChatModel;
import io.github.cdimascio.dotenv.Dotenv;

public class ConsultaGemini {
  
  public static String obterTraducao(String texto) {

    Dotenv dotenv = Dotenv.load();
    String apiKey = dotenv.get("API_KEY_GEMINI");
    ChatLanguageModel gemini = GoogleAiGeminiChatModel.builder()
        .apiKey(apiKey)
        .modelName("gemini-1.5-flash")
        .build();

    String response = gemini.generate("Traduza para português o texto: " + texto);
    return response;
  }
}
