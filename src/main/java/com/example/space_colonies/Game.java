package com.example.space_colonies;

import com.example.space_colonies.model.Colony;
import com.example.space_colonies.model.Planet;
import com.example.space_colonies.model.Position;
import com.example.space_colonies.model.Resource;
import com.example.space_colonies.model.Spaceship;
import com.example.space_colonies.model.StarMap;
import com.example.space_colonies.model.Technology;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Scanner;

import static java.nio.charset.StandardCharsets.UTF_8;
public class Game {

    static {
        configureConsoleUtf8();
    }

    private static void configureConsoleUtf8() {
        try {
            System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, UTF_8));
            System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.err), true, UTF_8));
        } catch (Exception ignored) {
            // оставляем стандартные потоки
        }
    }

    private static final int MAP_SIZE = 8;
    private static final int WIN_COLONIES = 3;
    private static final int WIN_STATION_LEVEL_SUM = 6;

    private final StarMap starMap;
    private final List<Resource> resources;
    private final List<Planet> planets;
    private final List<Colony> colonies;
    private final List<Technology> technologies;
    private Spaceship flagship;
    private int turnNumber;
    private boolean finished;

    public Game() {
        this.starMap = new StarMap(MAP_SIZE, MAP_SIZE);
        this.resources = new ArrayList<>();
        this.planets = new ArrayList<>();
        this.colonies = new ArrayList<>();
        this.technologies = new ArrayList<>();
        this.turnNumber = 1;
        this.finished = false;
        initializeGame();
    }

    private void initializeGame() {
        resources.clear();
        resources.add(new Resource("Энергия", 800, 5000));
        resources.add(new Resource("Минералы", 400, 8000));
        resources.add(new Resource("Материалы", 350, 4000));
        resources.add(new Resource("Наука", 120, 2000));

        flagship = new Spaceship(
                "Пионер-1",
                new Position(0, 0),
                220,
                200,
                3,
                100);

        technologies.add(new Technology("Автоматизированная добыча", 3, 40, 5, 0));
        technologies.add(new Technology("Гипердвигатель", 3, 35, 0, 1));

        planets.clear();
        colonies.clear();

        addPlanet(new Planet("Кеплер-442b", new Position(2, 1), 72, 55));
        addPlanet(new Planet("Проксима-III", new Position(5, 3), 45, 80));
        addPlanet(new Planet("Тау Кита", new Position(3, 6), 88, 30));
        addPlanet(new Planet("Глизе-581g", new Position(7, 7), 60, 65));
    }

    private void addPlanet(Planet planet) {
        planets.add(planet);
        if (!starMap.placePlanet(planet)) {
            throw new IllegalStateException("Не удалось разместить планету: " + planet.getName());
        }
    }

    private Optional<Resource> resourceByName(String name) {
        return resources.stream().filter(r -> r.getName().equalsIgnoreCase(name)).findFirst();
    }

    private int getEffectiveShipRange() {
        int bonus = technologies.stream().mapToInt(Technology::getTotalShipRangeBonus).sum();
        return flagship.getMovementRange() + bonus;
    }

    private int getTotalMineralTechBonusPercent() {
        return technologies.stream().mapToInt(Technology::getTotalMineralBonusPercent).sum();
    }

    /** Движение корабля и вывод состояния. */
    public void playTurn() {
        System.out.println("=== Ход " + turnNumber + " ===");
        displayGameState();
        Position current = flagship.getPosition();
        Position probe = new Position(Math.min(MAP_SIZE - 1, current.getX() + 1), current.getY());
        if (starMap.isValidPosition(probe) && flagship.moveTo(probe, getEffectiveShipRange())) {
            System.out.println("Корабль переместился на " + flagship.getPosition());
        } else {
            System.out.println("Корабль остался на месте (недостаточно топлива или вне досягаемости).");
        }
        applyEndOfTurnEconomy();
        turnNumber++;
    }

    private void displayGameState() {
        System.out.println("Ресурсы:");
        for (Resource resource : resources) {
            System.out.println("  " + resource);
        }
        System.out.println("Планеты:");
        for (Planet planet : planets) {
            System.out.println("  " + planet);
        }
        System.out.println("Колонии:");
        if (colonies.isEmpty()) {
            System.out.println("  (нет)");
        } else {
            for (Colony colony : colonies) {
                System.out.println("  " + colony);
            }
        }
        System.out.println("Корабль: " + flagship + " | эфф. дальность: " + getEffectiveShipRange());
        System.out.println("Технологии:");
        for (Technology t : technologies) {
            System.out.println("  " + t);
        }
    }

    private void applyEndOfTurnEconomy() {
        int mineralBonus = getTotalMineralTechBonusPercent();
        Optional<Resource> minerals = resourceByName("Минералы");
        Optional<Resource> energy = resourceByName("Энергия");
        Optional<Resource> science = resourceByName("Наука");

        for (Colony colony : colonies) {
            Planet p = colony.getPlanet();
            int rawYield = p.getBaseMineralYieldPerTurn() + colony.getStationMineralBonusPerTurn();
            final int mineralYield = rawYield * (100 + mineralBonus) / 100;
            minerals.ifPresent(m -> m.addAmount(Math.min(mineralYield, m.getMaxAmount() - m.getAmount())));
            energy.ifPresent(e -> e.addAmount(Math.min(25 + colony.getStationLevel() * 5, e.getMaxAmount() - e.getAmount())));
            if (colony.getStationLevel() >= 1) {
                science.ifPresent(s -> s.addAmount(Math.min(8, s.getMaxAmount() - s.getAmount())));
            }
            int growth = colony.getPopulationGrowthPerTurn();
            colony.setPopulation(colony.getPopulation() + growth);
        }
    }

    private boolean tryColonize() {
        Planet here = starMap.getPlanetAt(flagship.getPosition());
        if (here == null) {
            System.out.println("Здесь нет планеты.");
            return false;
        }
        if (here.isColonized()) {
            System.out.println("Планета уже освоена.");
            return false;
        }
        int energyCost = here.getColonizationEnergyCost();
        int materialCost = 280;
        Optional<Resource> energy = resourceByName("Энергия");
        Optional<Resource> materials = resourceByName("Материалы");
        if (energy.isEmpty() || materials.isEmpty()) {
            return false;
        }
        if (!energy.get().consumeAmount(energyCost)) {
            System.out.println("Недостаточно энергии (нужно " + energyCost + ").");
            return false;
        }
        if (!materials.get().consumeAmount(materialCost)) {
            energy.get().addAmount(energyCost);
            System.out.println("Недостаточно материалов (нужно " + materialCost + ").");
            return false;
        }
        String colonyName = "База «" + here.getName() + "»";
        Colony colony = new Colony(colonyName, here, 200);
        if (!here.establishColony(colony)) {
            energy.get().addAmount(energyCost);
            materials.get().addAmount(materialCost);
            System.out.println("Не удалось зарегистрировать колонию.");
            return false;
        }
        colonies.add(colony);
        System.out.println("Колония основана: " + colony);
        return true;
    }

    private boolean tryBuildStation(Scanner scanner) {
        if (colonies.isEmpty()) {
            System.out.println("Нет колоний.");
            return false;
        }
        System.out.println("Выберите колонию (номер):");
        for (int i = 0; i < colonies.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + colonies.get(i));
        }
        int idx;
        try {
            idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод.");
            return false;
        }
        if (idx < 0 || idx >= colonies.size()) {
            System.out.println("Нет такой колонии.");
            return false;
        }
        Colony colony = colonies.get(idx);
        int cost = colony.getNextStationBuildCost();
        if (cost < 0) {
            System.out.println("Станция максимального уровня.");
            return false;
        }
        Optional<Resource> materials = resourceByName("Материалы");
        if (materials.isEmpty() || !materials.get().consumeAmount(cost)) {
            System.out.println("Недостаточно материалов (нужно " + cost + ").");
            return false;
        }
        if (!colony.upgradeStation()) {
            materials.get().addAmount(cost);
            return false;
        }
        System.out.println("Станция улучшена: " + colony);
        return true;
    }

    private boolean tryResearch(Scanner scanner) {
        System.out.println("Выберите технологию (номер):");
        for (int i = 0; i < technologies.size(); i++) {
            Technology t = technologies.get(i);
            int next = t.getNextLevelCost();
            String costStr = next < 0 ? "макс." : String.valueOf(next);
            System.out.println("  " + (i + 1) + ") " + t.getName() + " — следующий уровень: " + costStr + " науки");
        }
        int idx;
        try {
            idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Некорректный ввод.");
            return false;
        }
        if (idx < 0 || idx >= technologies.size()) {
            System.out.println("Нет такой технологии.");
            return false;
        }
        Technology tech = technologies.get(idx);
        int cost = tech.getNextLevelCost();
        if (cost < 0) {
            System.out.println("Уже максимум.");
            return false;
        }
        Optional<Resource> science = resourceByName("Наука");
        if (science.isEmpty() || !science.get().consumeAmount(cost)) {
            System.out.println("Недостаточно науки (нужно " + cost + ").");
            return false;
        }
        if (!tech.researchLevel()) {
            science.get().addAmount(cost);
            return false;
        }
        System.out.println("Исследовано: " + tech);
        return true;
    }

    private void tryMoveShip(Scanner scanner) {
        System.out.println("Введите целевые координаты x y (0.." + (MAP_SIZE - 1) + "):");
        String line = scanner.nextLine().trim();
        String[] parts = line.split("\\s+");
        if (parts.length < 2) {
            System.out.println("Нужно два числа.");
            return;
        }
        try {
            int tx = Integer.parseInt(parts[0]);
            int ty = Integer.parseInt(parts[1]);
            Position target = new Position(tx, ty);
            if (!starMap.isValidPosition(target)) {
                System.out.println("Координаты вне карты.");
                return;
            }
            Position from = flagship.getPosition();
            double dist = from.distanceTo(target);
            int range = getEffectiveShipRange();
            int fuelNeeded = Spaceship.fuelCostForDistance(dist);
            if (dist > range + 1e-9) {
                System.out.println("Слишком далеко: дистанция " + String.format(Locale.US, "%.2f", dist)
                        + ", эфф. дальность " + range);
                return;
            }
            if (flagship.getFuel() < fuelNeeded) {
                System.out.println("Недостаточно топлива: нужно " + fuelNeeded + ", есть " + flagship.getFuel());
                return;
            }
            if (flagship.moveTo(target, range)) {
                System.out.println("Корабль на " + flagship.getPosition());
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private boolean checkVictory() {
        if (colonies.size() >= WIN_COLONIES) {
            return true;
        }
        int sum = colonies.stream().mapToInt(Colony::getStationLevel).sum();
        return sum >= WIN_STATION_LEVEL_SUM;
    }

    public void runConsole() {
        Scanner scanner = new Scanner(System.in, UTF_8);
        System.out.println("Космическая колонизация — консольный режим. Цель: "
                + WIN_COLONIES + " колонии или суммарно " + WIN_STATION_LEVEL_SUM + " уровней станций.");
        while (!finished) {
            System.out.println();
            System.out.println("—— Ход " + turnNumber + " ——");
            displayGameState();
            System.out.println("Команды: 1 — ход корабля | 2 — колонизировать | 3 — улучшить станцию | "
                    + "4 — исследование | 5 — завершить ход | 0 — выход");
            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1" -> tryMoveShip(scanner);
                case "2" -> tryColonize();
                case "3" -> tryBuildStation(scanner);
                case "4" -> tryResearch(scanner);
                case "5" -> {
                    applyEndOfTurnEconomy();
                    turnNumber++;
                    if (checkVictory()) {
                        System.out.println("Победа: вы развили колонии!");
                        finished = true;
                    }
                }
                case "0" -> finished = true;
                default -> System.out.println("Неизвестная команда.");
            }
            if (!flagship.isOperational()) {
                System.out.println("Корабль уничтожен. Игра окончена.");
                finished = true;
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("stdout.encoding", "UTF-8");
        System.setProperty("stderr.encoding", "UTF-8");
        Game game = new Game();
        if (args.length > 0 && "demo".equalsIgnoreCase(args[0])) {
            for (int i = 0; i < 3; i++) {
                game.playTurn();
                System.out.println();
            }
            return;
        }
        game.runConsole();
    }
}
