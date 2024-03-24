package zadanie2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<Lekarz> lekarze = readLekarzeFromFile("src/zadanie2/lekarze.txt");
        List<Pacjent> pacjenci = readPacjenciFromFile("src/zadanie2/pacjenci.txt");
        List<Wizyta> wizyty = readWizytyFromFile("src/zadanie2/wizyty.txt");

//        // Znajdź lekarza, który miał najwięcej wizyt
        Lekarz lekarzZNajwiecejWizyt = znajdzLekarzaZNajwiecejWizyt(lekarze, wizyty);
        System.out.println("Lekarz z największą liczbą wizyt: " + lekarzZNajwiecejWizyt.getImie() + " " + lekarzZNajwiecejWizyt.getNazwisko());

//        // Znajdź pacjenta, który miał najwięcej wizyt
        int patientId = findPatientWithMostVisits(wizyty);
        System.out.println("Patient with the most visits: " + patientId);
        // Która specjalizacja cieszy się największym powodzeniem?
        String najpopularniejszaSpecjalnosc = znajdzNajpopularniejszaSpecjalnosc(lekarze, wizyty);
        System.out.println("Najpopularniejsza specjalizacja: " + najpopularniejszaSpecjalnosc);

        // Którego roku było najwięcej wizyt?
        int rokZNajwiecejWizyt = znajdzRokZNajwiecejWizyt(wizyty);
        System.out.println("Rok z największą liczbą wizyt: " + rokZNajwiecejWizyt);

        // Wypisz top 5 najstarszych lekarzy
        List<Lekarz> najstarsiLekarze = top5NajstarszychLekarzy(lekarze);
        System.out.println("Top 5 najstarszych lekarzy:");
        for (Lekarz lekarz : najstarsiLekarze) {
            System.out.println(lekarz.getImie() + " " + lekarz.getNazwisko() + ", data urodzenia: " + lekarz.getDataUrodzenia());
        }

        // Wypisz top 5 lekarzy, którzy mieli najwięcej wizyt
        List<Lekarz> top5Lekarzy = top5LekarzyZNajwiekszaLiczbaWizyt(lekarze, wizyty);
        System.out.println("Top 5 lekarzy z największą liczbą wizyt:");
        for (Lekarz lekarz : top5Lekarzy) {
            System.out.println(lekarz.getImie() + " " + lekarz.getNazwisko());
        }
        // Zwróć pacjentów, którzy byli u minimum 5 różnych lekarzy
        List<Pacjent> pacjenciU5RoznychLekarzy = znajdzPacjentowU5RoznychLekarzy(pacjenci, wizyty);
        System.out.println("Pacjenci, którzy byli u co najmniej 5 różnych lekarzy:");
        for (Pacjent pacjent : pacjenciU5RoznychLekarzy) {
            System.out.println(pacjent);
        }

        // Zwróć lekarzy, którzy przyjęli tylko jednego pacjenta
        List<Integer> lekarzeZJednymPacjentem = znajdzLekarzyZJednymPacjentem(wizyty);
        System.out.println("Lekarze z tylko jednym pacjentem:");
        for (Integer idLekarza : lekarzeZJednymPacjentem) {
            System.out.println("ID lekarza: " + idLekarza);
        }

    }

    private static List<Lekarz> readLekarzeFromFile(String fileName) {
        List<Lekarz> lekarze = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                if (tokens.length == 7) {
                    int id = Integer.parseInt(tokens[0]);
                    String nazwisko = tokens[1];
                    String imie = tokens[2];
                    String specjalnosc = tokens[3];
                    Date dataUrodzenia = new SimpleDateFormat("yyyy-MM-dd").parse(tokens[4]);
                    String nip = tokens[5];
                    String pesel = tokens[6];

                    Lekarz lekarz = new Lekarz(id, nazwisko, imie, specjalnosc, dataUrodzenia, nip, pesel);
                    lekarze.add(lekarz);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return lekarze;
    }

    private static List<Pacjent> readPacjenciFromFile(String fileName) {
        List<Pacjent> pacjenci = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                if (tokens.length == 4) {
                    int id = Integer.parseInt(tokens[0]);
                    String nazwisko = tokens[1];
                    String imie = tokens[2];
                    String pesel = tokens[3];
                    Date dataUrodzenia = new SimpleDateFormat("yyyy-MM-dd").parse(tokens[4]);

                    Pacjent pacjent = new Pacjent(id, nazwisko, imie, pesel, dataUrodzenia);
                    pacjenci.add(pacjent);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return pacjenci;
    }

    private static List<Wizyta> readWizytyFromFile(String fileName) {
        List<Wizyta> wizyty = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] tokens = line.split("\t");
                if (tokens.length == 3) {
                    int idLekarza = Integer.parseInt(tokens[0]);
                    int idPacjenta = Integer.parseInt(tokens[1]);
                    Date dataWizyty = new SimpleDateFormat("yyyy-MM-dd").parse(tokens[2]);

                    Wizyta wizyta = new Wizyta(idLekarza, idPacjenta, dataWizyty);
                    wizyty.add(wizyta);
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return wizyty;
    }

    private static String znajdzNajpopularniejszaSpecjalnosc(List<Lekarz> lekarze, List<Wizyta> wizyty) {
        Map<String, Integer> liczbaWizytNaSpecjalnosc = new HashMap<>();

        for (Wizyta wizyta : wizyty) {
            int idLekarza = wizyta.getIdLekarza();
            Lekarz lekarz = lekarze.stream().filter(l -> l.getId() == idLekarza).findFirst().orElse(null);

            if (lekarz != null) {
                String specjalnosc = lekarz.getSpecjalnosc();
                liczbaWizytNaSpecjalnosc.put(specjalnosc, liczbaWizytNaSpecjalnosc.getOrDefault(specjalnosc, 0) + 1);
            }
        }

        int maxLiczbaWizyt = 0;
        String najpopularniejszaSpecjalnosc = null;

        for (Map.Entry<String, Integer> entry : liczbaWizytNaSpecjalnosc.entrySet()) {
            String specjalnosc = entry.getKey();
            int liczbaWizyt = entry.getValue();

            if (liczbaWizyt > maxLiczbaWizyt) {
                maxLiczbaWizyt = liczbaWizyt;
                najpopularniejszaSpecjalnosc = specjalnosc;
            }
        }

        return najpopularniejszaSpecjalnosc;
    }

    private static int znajdzRokZNajwiecejWizyt(List<Wizyta> wizyty) {
        Map<Integer, Integer> liczbaWizytNaRok = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        for (Wizyta wizyta : wizyty) {
            calendar.setTime(wizyta.getDataWizyty());
            int rok = calendar.get(Calendar.YEAR);
            liczbaWizytNaRok.put(rok, liczbaWizytNaRok.getOrDefault(rok, 0) + 1);
        }

        int maxLiczbaWizyt = 0;
        int rokZNajwiecejWizyt = -1;

        for (Map.Entry<Integer, Integer> entry : liczbaWizytNaRok.entrySet()) {
            int rok = entry.getKey();
            int liczbaWizyt = entry.getValue();

            if (liczbaWizyt > maxLiczbaWizyt) {
                maxLiczbaWizyt = liczbaWizyt;
                rokZNajwiecejWizyt = rok;
            }
        }

        return rokZNajwiecejWizyt;
    }

    private static Lekarz znajdzLekarzaZNajwiecejWizyt(List<Lekarz> lekarze, List<Wizyta> wizyty) {
        Map<Integer, Integer> liczbaWizytNaLekarza = new HashMap<>();

        for (Wizyta wizyta : wizyty) {
            int idLekarza = wizyta.getIdLekarza();
            if (!liczbaWizytNaLekarza.containsKey(idLekarza)) {
                liczbaWizytNaLekarza.put(idLekarza, 0);
            }
            liczbaWizytNaLekarza.put(idLekarza, liczbaWizytNaLekarza.get(idLekarza) + 1);
        }

        int maxLiczbaWizyt = 0;
        int idLekarzaZNajwiecejWizyt = -1;

        for (Map.Entry<Integer, Integer> entry : liczbaWizytNaLekarza.entrySet()) {
            if (entry.getValue() > maxLiczbaWizyt) {
                maxLiczbaWizyt = entry.getValue();
                idLekarzaZNajwiecejWizyt = entry.getKey();
            }
        }

        for (Lekarz lekarz : lekarze) {
            if (lekarz.getId() == idLekarzaZNajwiecejWizyt) {
                return lekarz;
            }
        }

        return null;
    }


    public static int findPatientWithMostVisits(List<Wizyta> visits) {
        Map<Integer, Integer> visitCountMap = new HashMap<>();


        for (Wizyta visit : visits) {
            int currentCount = visitCountMap.getOrDefault(visit.getIdPacjenta(), 0);
            visitCountMap.put(visit.getIdPacjenta(), currentCount + 1);
        }


        int maxVisits = 0;
        int patientIdWithMostVisits = -1;
        for (Map.Entry<Integer, Integer> entry : visitCountMap.entrySet()) {
            if (entry.getValue() > maxVisits) {
                maxVisits = entry.getValue();
                patientIdWithMostVisits = entry.getKey();
            }
        }

        return patientIdWithMostVisits;
    }


    private static List<Lekarz> top5LekarzyZNajwiekszaLiczbaWizyt(List<Lekarz> lekarze, List<Wizyta> wizyty) {
        Map<Integer, Integer> lekarzLiczbaWizyt = new HashMap<>();
        for (Wizyta wizyta : wizyty) {
            lekarzLiczbaWizyt.put(wizyta.getIdLekarza(), lekarzLiczbaWizyt.getOrDefault(wizyta.getIdLekarza(), 0) + 1);
        }

        List<Map.Entry<Integer, Integer>> lista = new ArrayList<>(lekarzLiczbaWizyt.entrySet());
        lista.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        List<Lekarz> top5 = new ArrayList<>();
        for (int i = 0; i < Math.min(5, lista.size()); i++) {
            Map.Entry<Integer, Integer> wpis = lista.get(i);
            for (Lekarz lekarz : lekarze) {
                if (lekarz.getId() == wpis.getKey()) {
                    top5.add(lekarz);
                    break;
                }
            }
        }
        return top5;
    }


    private static List<Lekarz> top5NajstarszychLekarzy(List<Lekarz> lekarze) {

        List<Lekarz> kopiaLekarzy = new ArrayList<>(lekarze);

        Collections.sort(kopiaLekarzy, (Lekarz l1, Lekarz l2) -> l1.getDataUrodzenia().compareTo(l2.getDataUrodzenia()));


        return kopiaLekarzy.subList(0, Math.min(5, kopiaLekarzy.size()));
    }

    public static List<Integer> znajdzLekarzyZJednymPacjentem(List<Wizyta> wizyty) {
        Map<Integer, Integer> mapLekarze = new HashMap<>();
        for (Wizyta wizyta : wizyty) {
            int idLekarza = wizyta.getIdLekarza();
            mapLekarze.put(idLekarza, mapLekarze.getOrDefault(idLekarza, 0) + 1);
        }

       // System.out.println("Mapa lekarzy po zliczeniu wizyt: " + mapLekarze);

        List<Integer> lekarzeZJednymPacjentem = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : mapLekarze.entrySet()) {
            if (entry.getValue() == 1) {
                lekarzeZJednymPacjentem.add(entry.getKey());
            }
        }

      //  System.out.println("Lista lekarzy z jednym pacjentem: " + lekarzeZJednymPacjentem);

        return lekarzeZJednymPacjentem;
    }
    private static List<Pacjent> znajdzPacjentowU5RoznychLekarzy(List<Pacjent> pacjenci, List<Wizyta> wizyty) {
        Map<Integer, Set<Integer>> pacjentDoLekarzy = new HashMap<>();
        for (Wizyta wizyta : wizyty) {
            pacjentDoLekarzy.computeIfAbsent(wizyta.getIdPacjenta(), k -> new HashSet<>()).add(wizyta.getIdLekarza());
        }


       // pacjentDoLekarzy.forEach((idPacjenta, lekarze) -> System.out.println("Pacjent ID: " + idPacjenta + ", liczba odwiedzonych lekarzy: " + lekarze.size()));

        List<Pacjent> wynik = new ArrayList<>();
        for (Pacjent pacjent : pacjenci) {
            Set<Integer> lekarze = pacjentDoLekarzy.get(pacjent.getId());
            if (lekarze != null && lekarze.size() >= 5) {
                wynik.add(pacjent);
            }
        }

        return wynik;
    }

}








