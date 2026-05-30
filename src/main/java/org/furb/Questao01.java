package org.furb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.furb.helpers.questao01.CargoItem;
import org.furb.helpers.questao01.Solution;

public class Questao01 {
    private static final int TRUCK_CAPACITY = 100;
    private final List<CargoItem> items = List.of(
        new CargoItem(1, 40),
        new CargoItem(2, 30),
        new CargoItem(3, 25),
        new CargoItem(4, 20),
        new CargoItem(5, 15)
    );

    public static void main(String[] args) {
        new Questao01();
    }

    public Questao01() {
        init();
    }

    private void init() {
        Solution greedySolution = resolveGreedy(items, TRUCK_CAPACITY);
        Solution optimalSolution = resolveExact(items, TRUCK_CAPACITY);

        printResult("Solução gulosa", greedySolution, TRUCK_CAPACITY);
        printResult("Solução ótima", optimalSolution, TRUCK_CAPACITY);

        boolean greedyIsOptimal = greedySolution.totalVolume() == optimalSolution.totalVolume();
        System.out.println("\nO algoritmo guloso retorna a solução ótima? " + (greedyIsOptimal ? "Sim" : "Não"));
    }

    private Solution resolveGreedy(List<CargoItem> items, int capacity) {
        List<CargoItem> sortedItems = new ArrayList<>(items);
        sortedItems.sort(Comparator.comparingInt(CargoItem::volume).reversed());

        List<CargoItem> selectedItems = new ArrayList<>();
        int currentVolume = 0;

        for (CargoItem item : sortedItems) {
            if (currentVolume + item.volume() <= capacity) {
                selectedItems.add(item);
                currentVolume += item.volume();
            }
        }

        return new Solution(List.copyOf(selectedItems), currentVolume);
    }

    private Solution resolveExact(List<CargoItem> items, int capacity) {
        Solution bestResult = new Solution(List.of(), 0);
        int totalCombinations = 1 << items.size();

        for (int mask = 0; mask < totalCombinations; mask++) {
            List<CargoItem> selectedItems = new ArrayList<>();
            int currentVolume = 0;

            for (int index = 0; index < items.size(); index++) {
                boolean itemIncluded = (mask & (1 << index)) != 0;

                if (itemIncluded) {
                    CargoItem item = items.get(index);
                    currentVolume += item.volume();

                    if (currentVolume > capacity) {
                        break;
                    }

                    selectedItems.add(item);
                }
            }

            if (currentVolume <= capacity && currentVolume > bestResult.totalVolume()) {
                bestResult = new Solution(List.copyOf(selectedItems), currentVolume);
            }
        }

        return bestResult;
    }

    private void printResult(String title, Solution result, int capacity) {
        System.out.println("\n" + title + ":");

        System.out.println("Itens selecionados: " +
            result.selectedItems().stream()
            .map(item -> item.id() + "(" + item.volume() + "L)")
            .toList()
        );

        System.out.println("Volume total: " + result.totalVolume() + "L");
        System.out.println("Capacidade restante: " + (capacity - result.totalVolume()) + "L");
    }
}
