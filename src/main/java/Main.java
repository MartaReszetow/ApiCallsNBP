import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;

public class Main {

    public final static HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static void main(String[] args) {
        // zapytanie do linku
        // http://api.nbp.pl/api/exchangerates/rates/TABLE_TYPE/CURRENCY_CHOISE/START_DATA/END_DATA/?format=json
        String link = "http://api.nbp.pl/api/exchangerates/rates";


        Scanner scan = new Scanner(System.in);
        String tableType;
        String currencyChoice;
        String startData;
        String endData;
        String format = "/?format=json";


        System.out.println("Podaj rodzaj tabeli (A/B/C)");
        tableType = scan.nextLine();
        System.out.println("Podaj walutę, dla której chcesz spradzić statystyki:");
        currencyChoice = scan.nextLine().toUpperCase();
        System.out.println("Podaj datę od której chcesz rozpocząć sprawdzanie kursów walut w formacie(RRRR-MM-DD): ");
        startData = scan.nextLine();
        System.out.println("Podaj datę od której chcesz rozpocząć sprawdzanie kursów walut w formacie(RRRR-MM-DD): ");
        endData = scan.nextLine();


        link += "/" + tableType + "/" + currencyChoice + "/" + startData + "/" + endData + format;
        System.out.println(link);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(link))
                .build();

        System.out.println(link);


        try {

            // zacięgnięcie odpowiedzi do zapytania
            HttpResponse<String> resp = HTTP_CLIENT.send(request,
                    HttpResponse.BodyHandlers.ofString());

            String responseBody = resp.body(); // cialo odpowiedzi - jest w formacie json

            // deklaruje object mappera żeby odzytać treść (stworzyłam schemat poboieranych danych w NOtowanieCenyZłota)

            ObjectMapper objectMapper = new ObjectMapper();

            // wynikiem będzie lista więc musimy zmapować - NewTypeReference
            List<CurrencyData> notowania = objectMapper.readValue(responseBody,
                    new TypeReference<List<CurrencyData>>() {
                    });
            notowania.forEach(System.out::println);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

}
