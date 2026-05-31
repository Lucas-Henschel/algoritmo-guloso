package org.furb;

import org.furb.helpers.questao02.Place;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Questao02 {
    private static final int AVAILABLE_DAYS = 7;

    private final List<Place> places = List.of(
        new Place("Berna", 10, 3),
        new Place("Mônaco", 8, 2),
        new Place("Luxemburgo", 7, 2),
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
        List<Place> greedySolution = resolveGreedy(places);
        List<Place> optimalSolution = resolveExact(places);

        printResult("Solução gulosa", greedySolution);
        printResult("Solução ótima", optimalSolution);

        boolean greedyIsOptimal = calculateTotalValue(greedySolution) == calculateTotalValue(optimalSolution);
        System.out.println("\nO algoritmo guloso retorna a solução ótima? " + (greedyIsOptimal ? "Sim" : "Não"));
    }

    private List<Place> resolveGreedy(List<Place> places) {
        List<Place> sortedPlaces = new ArrayList<>(places);

        sortedPlaces.sort(Comparator.comparingDouble(Place::getValuePerDay).reversed());

        List<Place> selectedPlaces = new ArrayList<>();
        int usedDays = 0;

        for (Place place : sortedPlaces) {
            if (usedDays + place.getDurationDays() <= AVAILABLE_DAYS) {
                selectedPlaces.add(place);
                usedDays += place.getDurationDays();
            }
        }

        return List.copyOf(selectedPlaces);
    }

    private List<Place> resolveExact(List<Place> places) {
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

                    if (currentDays > AVAILABLE_DAYS) {
                        break;
                    }

                    currentValue += place.getValue();
                    selectedPlaces.add(place);
                }
            }

            if (currentDays <= AVAILABLE_DAYS && currentValue > bestValue) {
                bestValue = currentValue;
                bestResult = List.copyOf(selectedPlaces);
            }
        }

        return bestResult;
    }

    private void printResult(String title, List<Place> result) {
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
        System.out.println("Dias restantes: " + (AVAILABLE_DAYS - totalDays));
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