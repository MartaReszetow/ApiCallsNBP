import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Scanner;

public class Main {

    public final static HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static void main(String[] args) {
// _____________________________ generowanie linku w zaeżności od rodzaju tabeli _____________________________________________
        // "A" i "C"        http://api.nbp.pl/api/exchangerates/rates/{table}/{currency}/{endDate}/?format=json
        // "B"              http://api.nbp.pl/api/exchangerates/tables/B/{startDate}/{endDate}/?format=json
        String link = "http://api.nbp.pl/api/exchangerates";


        Scanner scan = new Scanner(System.in);
        String tableType;
        String currencyChoice;
        String startData;
        String endData;
        String format = "/?format=json";

// ______ zapytania do użytkowinika o wprowadzenie danych i wstępna weryfikacja ich poprawności (rodzaj tabeli/waluta)__________
        System.out.println("Podaj rodzaj tabeli (A/B/C)");
        tableType = scan.nextLine().toUpperCase();
        if (!tableType.equals("A") && !tableType.equals("B") && !tableType.equals("C")) {
            System.err.println("Błędnie podany rodzaj tabeli. Spróbuj ponownie");
            tableType = scan.nextLine().toUpperCase();
        }
        System.out.println("Podaj walutę, dla której chcesz spradzić statystyki:");
        currencyChoice = scan.nextLine().toUpperCase();
        try {
            if (!(Currency.valueOf(currencyChoice)).toString().equals(currencyChoice)) {
                // jeżeli nie ma takiej waluty w dostępnych opcjach w enumie Currency to obsluguje wyjatek i
                // prosi użyytkownika o ponowne wpisanie waluty
            }
        } catch (IllegalArgumentException eeeexc) {
            System.err.println("Błędnie podana waluta. Spróbuj ponownie.");
            currencyChoice = scan.nextLine().toUpperCase();

        }

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

            try {
//____________________________________ zacięgnięcie odpowiedzi do zapytania______________________________________________________

                HttpResponse<String> resp = HTTP_CLIENT.send(request,
                        HttpResponse.BodyHandlers.ofString());

                String responseBody = resp.body(); // cialo odpowiedzi - jest w formacie json

//_________________________________________ odczytanie tresci z jsona ____________________________________________________________

                ObjectMapper objectMapper = new ObjectMapper();

                // dla tabel(A i C) wynikiem będzie tabela a gdzieś głębiej ratesy
                // dla tabeli B mamy listę


                TableTypeA importedDataA = null;
                List<TableTypeB> importedDataB = null;
                TableTypeC importedDataC = null;


                if (tableType.equalsIgnoreCase("A")) {
                    importedDataA = objectMapper.readValue(responseBody,
                            TableTypeA.class);
                    //     String printRates = importedDataA.toString();
                    //     System.out.println(printRates);

                }

                if (tableType.equalsIgnoreCase("B")) {
                    importedDataB = objectMapper.readValue(responseBody,
                            new TypeReference<List<TableTypeB>>() {
                            });
                    //    importedDataB.forEach(System.out::println);
                }

                if (tableType.equalsIgnoreCase("C")) {
                    importedDataC = objectMapper.readValue(responseBody,
                            TableTypeC.class);
                    //    String printRates = importedDataC.toString();
                    //    System.out.println(printRates);
                }


                String menu = "Co chcesz zrobić ?:" + "\n" +
                        "*** jeśli chcesz obliczyć średnią kursu dla podanego przedziału napisz: \"SREDNIA\" " + "\n" +
                        "*** jeśli chcesz obliczyć odchylenie maxymalne  napisz:  \"ODCHYLENIE\" " + "\n" +
                        "*** jeśi chcesz obliczyć max i min wartości kursów napisz: \"MINMAX\"  " + "\n" +
                        "*** jeśli chcesz zakończyć program napisz: \"STOP\"";


                System.out.println(menu);
                String komenda = scan.nextLine().toUpperCase();
                while (!komenda.equalsIgnoreCase("STOP")) {


                    // ___________________ SREDNIA ______________________________

                    if (komenda.equals("SREDNIA") && tableType.equals("A")) {
                        getAverageForTableA(importedDataA);
                    } else if (komenda.equals("SREDNIA") && tableType.equals("B")) {
                        System.err.println("brak możliwości policzenia średniej dla tej tabeli");
                    } else if (komenda.equals("SREDNIA") && tableType.equals("C")) {
                        getAverageForTableC(importedDataC);
                    }

                    // ___________________ ODCHYLENIE ______________________________

                    if (komenda.equals("ODCHYLENIE") && tableType.equals("A")) {
                        getDeviationForTableA(importedDataA);
                    } else if (komenda.equals("ODCHYLENIE") && tableType.equals("B")) {
                        System.err.println("brak możliwości policzenia odchyenia dla tej tabeli");
                    }
                    if (komenda.equals("ODCHYLENIE") && tableType.equals("C")) {
                        getDeviationForTableC(importedDataC);
                    }

                    // ___________________ MIN i MAX ______________________________

                    if (komenda.equals("MINMAX") && tableType.equals("A")) {
                        getMinMaxForTableA(importedDataA);
                    } else if (komenda.equals("MINMAX") && tableType.equals("B")) {
                        System.err.println("brak możliwości policzenia min i max dla tej tabeli");
                    }
                    if (komenda.equals("MINMAX") && tableType.equals("C")) {
                        getMinMaxForTableC(importedDataC);
                    }


                    System.out.println("Podaj nową komendę");
                    komenda = scan.nextLine().toUpperCase();
                }
            } catch (MismatchedInputException exc) {
                System.err.println("Błędnie wprowadzony przedział czasowy. Odpal ponownie program i spróbuj z innymi datami. ");
            }
        } catch (NullPointerException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

// ________________________________________________METODY_______________________________________________________________

// --------------- metoda obiczająca SREDNIA w zależności od wybranej tabeli (A/C)-----------------------

    private static void getAverageForTableA(TableTypeA importedData) {
        OptionalDouble averageA = importedData.getRatesA().stream().mapToDouble(RateA::getMid).average();
        if (averageA.isPresent()) {
            System.out.println("średni kurs dla podanego przedziału czasu wynosi:" + averageA.getAsDouble());
        } else {
            System.err.println("Nie można obliczyć średniej dla zadanego przedziału czasu");
        }
    }

    private static void getAverageForTableC(TableTypeC importedData) {
        OptionalDouble averageBID = importedData.getRatesC().stream().mapToDouble(RateC::getBid).average();
        OptionalDouble averageASK = importedData.getRatesC().stream().mapToDouble(RateC::getAsk).average();

        if (averageBID.isPresent() && averageASK.isPresent()) {
            System.out.println("średni kurs sprzedaży(BID):" + averageBID.getAsDouble() + "  średni kurs kupna(ASK): " + averageASK.getAsDouble());
        } else {
            System.err.println("Nie można obliczyć średniej dla zadanego przedziału czasu");
        }
    }

    // -----------------------metoda obiczająca ODCHYLENIE w zależności od wybranej tabeli (A/C)----------------------

    private static void getDeviationForTableA(TableTypeA importedDataA) {
        OptionalDouble max = importedDataA.getRatesA().stream().mapToDouble(RateA::getMid).max();
        OptionalDouble min = importedDataA.getRatesA().stream().mapToDouble(RateA::getMid).min();

        if (max.isPresent() && min.isPresent()) {


            Double odchylenie = max.getAsDouble() - min.getAsDouble();
            System.out.println("różnica między maksymalną i minimalną wartością (odchylenie) wynosiła w danym oksesie: " + odchylenie);
        }
    }

    private static void getDeviationForTableC(TableTypeC importedDataC) {
        OptionalDouble maxBid = importedDataC.getRatesC().stream().mapToDouble(RateC::getBid).max();
        OptionalDouble minBid = importedDataC.getRatesC().stream().mapToDouble(RateC::getBid).min();

        OptionalDouble maxAsk = importedDataC.getRatesC().stream().mapToDouble(RateC::getAsk).max();
        OptionalDouble minAsk = importedDataC.getRatesC().stream().mapToDouble(RateC::getAsk).min();

        if (maxBid.isPresent() && minBid.isPresent() && maxAsk.isPresent() && minAsk.isPresent()) {
            Double odchylenieBid = maxBid.getAsDouble() - minBid.getAsDouble();
            Double odchylenieAsk = maxAsk.getAsDouble() - minAsk.getAsDouble();
            System.out.println("różnica między maksymalną i minimalną wartością (odchylenie) wynosiła w danym oksesie: \n" +
                    +odchylenieBid + " - dla sprzedaży waluty (BID) \n" +
                    odchylenieAsk + " - da kupna waluty (ASK)");
        }
    }


    // -------------------metoda obiczająca MIN i MAX w zależności od wybranej tabeli (A/C)----------------

    private static void getMinMaxForTableA(TableTypeA importedDataA) {
        OptionalDouble max = importedDataA.getRatesA().stream().mapToDouble(RateA::getMid).max();
        OptionalDouble min = importedDataA.getRatesA().stream().mapToDouble(RateA::getMid).min();

        if (max.isPresent() && min.isPresent()) {
            System.out.println("Dla podanego przedziału czasu wartości Min i Max wynosiły odpowiednio: \n"
                    + "MIN: " + min.getAsDouble() + "\nMAX: " + max.getAsDouble());
        }
    }

    private static void getMinMaxForTableC(TableTypeC importedDataC) {
        OptionalDouble maxBid = importedDataC.getRatesC().stream().mapToDouble(RateC::getBid).max();
        OptionalDouble minBid = importedDataC.getRatesC().stream().mapToDouble(RateC::getBid).min();

        OptionalDouble maxAsk = importedDataC.getRatesC().stream().mapToDouble(RateC::getAsk).max();
        OptionalDouble minAsk = importedDataC.getRatesC().stream().mapToDouble(RateC::getAsk).min();

        if (maxBid.isPresent() && minBid.isPresent() && maxAsk.isPresent() && minAsk.isPresent()) {
            System.out.println("Dla podanego przedziału czasu wartości Min i Max wynosiły odpowiednio: \n" +
                    "dla sprzedaży (BID): \n"
                    + "- MIN: " + minBid.getAsDouble() + "\n- MAX: " + maxBid.getAsDouble() +
                    "\n dla kupna(ASK): \n"
                    + "- MIN: " + minAsk.getAsDouble() + "\n- MAX: " + maxAsk.getAsDouble()
            );
        }
    }
}
