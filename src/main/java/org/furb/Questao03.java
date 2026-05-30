package org.furb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Questao03 {

    private final Set<String> targetStates = Set.of("MT", "RJ", "ES", "SP", "SC", "RS", "PR", "MS");

    private final List<RadioStation> stations = List.of(
        new RadioStation("Kum", Set.of("SP", "SC", "RS")),
        new RadioStation("Kdois", Set.of("RJ", "SP", "MT")),
        new RadioStation("Ktres", Set.of("ES", "SC", "PR")),
        new RadioStation("Kquatro", Set.of("SC", "RS")),
        new RadioStation("Kcinco", Set.of("PR", "MS"))
    );

    public static void main(String[] args) {
        new Questao03();
    }

    public Questao03() {
        init();
    }

    private void init() {
        List<RadioStation> greedySolution = resolveGreedy(stations, targetStates);

        printResult("Solução gulosa", greedySolution);

        System.out.println("\nO algoritmo guloso escolhe, a cada etapa, a estação que cobre o maior número de estados ainda não cobertos.");
    }

    private List<RadioStation> resolveGreedy(List<RadioStation> stations, Set<String> targetStates) {
        Set<String> uncoveredStates = new HashSet<>(targetStates);
        List<RadioStation> selectedStations = new ArrayList<>();

        while (!uncoveredStates.isEmpty()) {
            RadioStation bestStation = null;
            Set<String> bestCoverage = Set.of();

            for (RadioStation station : stations) {
                Set<String> coveredStates = new HashSet<>(station.coverage());
                coveredStates.retainAll(uncoveredStates);

                if (coveredStates.size() > bestCoverage.size()) {
                    bestStation = station;
                    bestCoverage = coveredStates;
                }
            }

            if (bestStation == null) {
                break;
            }

            selectedStations.add(bestStation);
            uncoveredStates.removeAll(bestCoverage);
        }

        return List.copyOf(selectedStations);
    }

    private void printResult(String title, List<RadioStation> result) {
        Set<String> coveredStates = new HashSet<>();

        System.out.println("\n" + title + ":");

        System.out.println("Estações selecionadas: " +
            result.stream()
                .map(RadioStation::name)
                .toList()
        );

        for (RadioStation station : result) {
            coveredStates.addAll(station.coverage());

            System.out.println(station.name() + " - Cobertura: " + station.coverage());
        }

        coveredStates.retainAll(targetStates);

        System.out.println("Estados cobertos: " + coveredStates);
        System.out.println("Quantidade de estações contratadas: " + result.size());
    }

    private record RadioStation(String name, Set<String> coverage) {
    }
}