package org.furb;

import org.furb.helpers.questao02.Place;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Questao02 {
    private static final int AVAILABLE_DAYS = 7;
    private final List<Place> places = List.of(
        new Place("Paris", 10, 3),
        new Place("Roma", 8, 2),
        new Place("Londres", 7, 2),
        new Place("Berlim", 6, 1),
        new Place("Amsterdã", 5, 1)
    );

    public static void main(String[] args) {
        new Questao02();
    }

    public Questao02() {
        init();
    }

    private void init() {
        List<Place> greedySolution = resolveGreedy(places, AVAILABLE_DAYS);
        List<Place> optimalSolution = resolveExact(places, AVAILABLE_DAYS);

        printResult("Solução gulosa", greedySolution, AVAILABLE_DAYS);
        printResult("Solução ótima", optimalSolution, AVAILABLE_DAYS);

        boolean greedyIsOptimal = calculateTotalValue(greedySolution) == calculateTotalValue(optimalSolution);

        System.out.println("O algoritmo guloso retorna a solução ótima? " + (greedyIsOptimal ? "Sim" : "Não"));
        System.out.println("O algoritmo guloso busca o ótimo local, mas não garante o ótimo global.");
    }

    private List<Place> resolveGreedy(List<Place> places, int availableDays) {
        List<Place> sortedPlaces = new ArrayList<>(places);

        sortedPlaces.sort(Comparator.comparingDouble(Place::getValuePerDay).reversed());

        List<Place> selectedPlaces = new ArrayList<>();
        int usedDays = 0;

        for (Place place : sortedPlaces) {
            if (usedDays + place.getDurationDays() <= availableDays) {
                selectedPlaces.add(place);
                usedDays += place.getDurationDays();
            }
        }

        return List.copyOf(selectedPlaces);
    }

    private List<Place> resolveExact(List<Place> places, int availableDays) {
        List<Place> bestResult = List.of();
        int bestValue = 0;
        int totalCombinations = 1 << places.size();

        for (int mask = 0; mask < totalCombinations; mask++) {
            List<Place> selectedPlaces = new ArrayList<>();
            int currentDays = 0;
            int currentValue = 0;

            for (int index = 0; index < places.size(); index++) {
                boolean placeIncluded = (mask & (1 << index)) != 0;

                if (placeIncluded) {
                    Place place = places.get(index);
                    currentDays += place.getDurationDays();

                    if (currentDays > availableDays) {
                        break;
                    }

                    currentValue += place.getValue();
                    selectedPlaces.add(place);
                }
            }

            if (currentDays <= availableDays && currentValue > bestValue) {
                bestValue = currentValue;
                bestResult = List.copyOf(selectedPlaces);
            }
        }

        return bestResult;
    }

    private void printResult(String title, List<Place> result, int availableDays) {
        int totalValue = calculateTotalValue(result);
        int totalDays = calculateTotalDays(result);

        System.out.println("\n" + title + ":");

        System.out.println("Locais selecionados: " +
            result.stream()
                .map(place -> place.getName()
                    + "(" + place.getValue()
                    + " pontos, " + place.getDurationDays()
                    + " dias)")
                .toList()
        );

        System.out.println("Total de dias utilizados: " + totalDays);
        System.out.println("Dias restantes: " + (availableDays - totalDays));
        System.out.println("Pontuação total obtida: " + totalValue);
    }

    private int calculateTotalValue(List<Place> places) {
        return places.stream()
            .mapToInt(Place::getValue)
            .sum();
    }

    private int calculateTotalDays(List<Place> places) {
        return places.stream()
            .mapToInt(Place::getDurationDays)
            .sum();
    }
}