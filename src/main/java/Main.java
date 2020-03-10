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
        // "A" i "C"        http://api.nbp.pl/api/exchangerates/rates/{table}/{currency}/{startDate}/{endDate}/?format=json
        // "B"              http://api.nbp.pl/api/exchangerates/tables/B/{startDate}/{endDate}/?format=json
        String link = "http://api.nbp.pl/api/exchangerates";


        Scanner scan = new Scanner(System.in);
        String tableType;
        String currencyChoice;
        String startData;
        String endData;
        String format = "/?format=json";


        System.out.println("Podaj rodzaj tabeli (A/B/C)");
        tableType = scan.nextLine().toUpperCase();
        System.out.println("Podaj walutę, dla której chcesz spradzić statystyki:");
        currencyChoice = scan.nextLine().toUpperCase();
        System.out.println("Podaj datę od której chcesz rozpocząć sprawdzanie kursów walut w formacie(RRRR-MM-DD): ");
        startData = scan.nextLine();
        System.out.println("Podaj datę od której chcesz rozpocząć sprawdzanie kursów walut w formacie(RRRR-MM-DD): ");
        endData = scan.nextLine();

        if (tableType.equals("A") || tableType.equals("C")) {
            link += "/rates/" + tableType + "/" + currencyChoice + "/" + startData + "/" + endData + format;
        } else if (tableType.equals("B")) {
            link += "/tables/B/" + startData + "/" + endData + format;
        }
        System.out.println(link);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(link))
                .build();

        try {

            // zacięgnięcie odpowiedzi do zapytania
            HttpResponse<String> resp = HTTP_CLIENT.send(request,
                    HttpResponse.BodyHandlers.ofString());

            String responseBody = resp.body(); // cialo odpowiedzi - jest w formacie json

        // odczytanie tresci z jsona

            ObjectMapper objectMapper = new ObjectMapper();

            // wynikiem będzie lista więc musimy zmapować - NewTypeReference


           if(tableType.equals("A")) {
               List<tableTypeA> importedData = objectMapper.readValue(responseBody,
                       new TypeReference<List<tableTypeA>>() {
                       });
               importedData.forEach(System.out::println);

           }

            if(tableType.equals("B")) {
                List<tableTypeB> importedData = objectMapper.readValue(responseBody,
                        new TypeReference<List<tableTypeB>>() {
                        });
                importedData.forEach(System.out::println);

            }

            if(tableType.equals("C")) {
                List<tableTypeC> importedData = objectMapper.readValue(responseBody,
                        new TypeReference<List<tableTypeC>>() {
                        });

                importedData.forEach(System.out::println);

            }








        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

}
