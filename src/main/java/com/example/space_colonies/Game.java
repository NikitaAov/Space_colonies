package com.example.space_colonies;

import com.example.space_colonies.model.Colonizable;
import com.example.space_colonies.model.Habitable;
import com.example.space_colonies.model.HyperdriveTech;
import com.example.space_colonies.model.Mining;
import com.example.space_colonies.model.MiningAutomationTech;
import com.example.space_colonies.model.Planet;
import com.example.space_colonies.model.Position;
import com.example.space_colonies.model.Research;
import com.example.space_colonies.model.Resource;
import com.example.space_colonies.model.SpaceObject;
import com.example.space_colonies.model.SpaceObjectFactory;
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
        }
    }

    private static final int MAP_SIZE = 8;
    private static final int WIN_COLONIES = 3;
    private static final int WIN_STATION_LEVEL_SUM = 6;

    private final StarMap starMap;
    private final List<Resource> resources;
    private final List<SpaceObject> spaceObjects;
    private final List<Colonizable> colonies;
    private final List<Technology> technologies;
    private final GameManager gameManager;
    private Spaceship flagship;
    private int turnNumber;
    private boolean finished;

    public Game() {
        this.starMap = new StarMap(MAP_SIZE, MAP_SIZE);
        this.resources = new ArrayList<>();
        this.spaceObjects = new ArrayList<>();
        this.colonies = new ArrayList<>();
        this.technologies = new ArrayList<>();
        this.gameManager = new GameManager();
        this.turnNumber = 1;
        this.finished = false;
        initializeGame();
        registerInterfaceEntities();
    }

    /** Регистрация объектов в менеджере по интерфейсам (лаб. 3). */
    private void registerInterfaceEntities() {
        gameManager.addMovable(flagship);
        gameManager.addCombatable(flagship);
        for (SpaceObject object : spaceObjects) {
            if (object instanceof Planet planet) {
                gameManager.addProducible(planet);
            }
        }
        for (Technology technology : technologies) {
            gameManager.addUpgradeableTechnology(technology);
        }
        for (Resource resource : resources) {
            gameManager.addTradeable(resource);
        }
    }

    private void initializeGame() {
        resources.clear();
        resources.add(new Resource("Энергия", 800, 5000, 1));
        resources.add(new Resource("Минералы", 400, 8000, 2));
        resources.add(new Resource("Материалы", 350, 4000, 2));
        resources.add(new Resource("Наука", 120, 2000, 3));

        flagship = new Spaceship(
                "Пионер-1",
                new Position(0, 0),
                220,
                200,
                3,
                100);

        technologies.add(new MiningAutomationTech());
        technologies.add(new HyperdriveTech());

        spaceObjects.clear();
        colonies.clear();

        addSpaceObject(new Planet("Кеплер-442b", new Position(2, 1), 72, 55));
        addSpaceObject(new Planet("Проксима-III", new Position(5, 3), 45, 80));
        addSpaceObject(new Planet("Тау Кита", new Position(3, 6), 88, 30));
        addSpaceObject(new Planet("Глизе-581g", new Position(7, 7), 60, 65));
        addSpaceObject(SpaceObjectFactory.createAsteroid("Пояс η", new Position(1, 4), 3, 70));
        addSpaceObject(SpaceObjectFactory.createStation("Станция «Рубикон»", new Position(4, 2), 2));
    }

    private void addSpaceObject(SpaceObject object) {
        if (!starMap.placeSpaceObject(object)) {
            throw new IllegalStateException("Не удалось разместить объект: " + object.getName());
        }
        spaceObjects.add(object);
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

    /** Полиморфное обновление объектов карты и движение корабля. */
    public void playTurn() {
        System.out.println("=== Ход " + turnNumber + " ===");
        for (SpaceObject object : spaceObjects) {
            object.update();
        }
        displayGameState();
        Position current = flagship.getPosition();
        Position probe = new Position(Math.min(MAP_SIZE - 1, current.getX() + 1), current.getY());
        if (starMap.isValidPosition(probe) && flagship.moveTo(probe, getEffectiveShipRange())) {
            System.out.println("Корабль переместился на " + flagship.getPosition());
        } else {
            System.out.println("Корабль остался на месте (недостаточно топлива или вне досягаемости).");
        }
        int planetYieldSum = gameManager.processAllProduction();
        System.out.println("[GameManager] сумма базовых ставок добычи планет: " + planetYieldSum
                + " | торговая ценность запасов: " + gameManager.getTotalTradeValue());
        gameManager.displayCombatSnapshot();
        applyEndOfTurnEconomy();
        turnNumber++;
    }

    private void displayGameState() {
        System.out.println("Ресурсы:");
        for (Resource resource : resources) {
            System.out.println("  " + resource);
        }
        System.out.println("Объекты на карте:");
        for (SpaceObject object : spaceObjects) {
            System.out.println("  " + object.getDescription());
        }
        System.out.println("Колонии:");
        if (colonies.isEmpty()) {
            System.out.println("  (нет)");
        } else {
            for (Colonizable colony : colonies) {
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

        for (Colonizable colony : colonies) {
            Planet p = colony.getPlanet();
            int rawYield = p.getBaseMineralYieldPerTurn() + colony.getStationMineralBonusPerTurn();
            final int mineralYield = rawYield * (100 + mineralBonus) / 100;
            minerals.ifPresent(m -> m.addAmount(Math.min(mineralYield, m.getMaxAmount() - m.getAmount())));
            energy.ifPresent(e -> e.addAmount(Math.min(25 + colony.getStationLevel() * 5, e.getMaxAmount() - e.getAmount())));
            if (colony.getStationLevel() >= 1) {
                int sciBonus = colony.getSciencePerTurnIfStation();
                int addSci = sciBonus > 0 ? sciBonus : 8;
                science.ifPresent(s -> s.addAmount(Math.min(addSci, s.getMaxAmount() - s.getAmount())));
            }
            int growth = colony.getPopulationGrowthPerTurn();
            colony.setPopulation(colony.getPopulation() + growth);
        }
    }

    private boolean tryColonize(Scanner scanner) {
        Planet here = starMap.getPlanetAt(flagship.getPosition());
        if (here == null) {
            System.out.println("Здесь нет планеты.");
            return false;
        }
        if (here.isColonized()) {
            System.out.println("Планета уже освоена.");
            return false;
        }
        System.out.println("Тип колонии: 1 — Обитаемая, 2 — Добывающая, 3 — Исследовательская");
        String typeLine = scanner.nextLine().trim();
        Colonizable colony;
        String colonyName = "База «" + here.getName() + "»";
        switch (typeLine) {
            case "1" -> colony = new Habitable(colonyName, here, 200);
            case "2" -> colony = new Mining(colonyName, here, 200);
            case "3" -> colony = new Research(colonyName, here, 200);
            default -> {
                System.out.println("Некорректный тип, по умолчанию — обитаемая.");
                colony = new Habitable(colonyName, here, 200);
            }
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
        Colonizable colony = colonies.get(idx);
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
        int sum = colonies.stream().mapToInt(Colonizable::getStationLevel).sum();
        return sum >= WIN_STATION_LEVEL_SUM;
    }

    public void runConsole() {
        Scanner scanner = new Scanner(System.in, UTF_8);
        System.out.println("Космическая колонизация — консольный режим. Цель: "
                + WIN_COLONIES + " колонии или суммарно " + WIN_STATION_LEVEL_SUM + " уровней станций.");
        while (!finished) {
            System.out.println();
            System.out.println("—— Ход " + turnNumber + " ——");
            for (SpaceObject object : spaceObjects) {
                object.update();
            }
            displayGameState();
            System.out.println("Команды: 1 — ход корабля | 2 — колонизировать | 3 — улучшить станцию | "
                    + "4 — исследование | 5 — завершить ход | 0 — выход");
            String cmd = scanner.nextLine().trim();
            switch (cmd) {
                case "1" -> tryMoveShip(scanner);
                case "2" -> tryColonize(scanner);
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
