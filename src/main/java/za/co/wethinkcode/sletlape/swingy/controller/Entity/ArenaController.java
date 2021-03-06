package za.co.wethinkcode.sletlape.swingy.controller.Entity;

import za.co.wethinkcode.sletlape.swingy.controller.Battle;
import za.co.wethinkcode.sletlape.swingy.controller.ValidationControl.PlayerValidation;
import za.co.wethinkcode.sletlape.swingy.enums.EDirection;
import za.co.wethinkcode.sletlape.swingy.enums.EHeroClass;
import za.co.wethinkcode.sletlape.swingy.factory.HeroFactory;
import lombok.Getter;
import za.co.wethinkcode.sletlape.swingy.model.LivingElements.Hero;
import za.co.wethinkcode.sletlape.swingy.model.LivingElements.LiveEntity;
import za.co.wethinkcode.sletlape.swingy.model.mapElements.Arena;
import za.co.wethinkcode.sletlape.swingy.persistence.IRepository;
import za.co.wethinkcode.sletlape.swingy.persistence.RepositoryImpl;
import za.co.wethinkcode.sletlape.swingy.utils.Formulas;
import za.co.wethinkcode.sletlape.swingy.view.cli.Cli;

import javax.validation.ConstraintViolation;
import java.awt.*;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

@Getter
public class ArenaController {
    Arena arena;
    PlayerController playerController = new PlayerController();
    MapController mapController = new MapController();
    Battle battle;
    IRepository repository;

    public ArenaController(Arena arena) {
        this.arena = arena;
        repository = new RepositoryImpl<Hero>();
        battle = new Battle(arena.getGameMessages());
    }

    public void move(EDirection direction) {
        if (!arena.isInFight() && !arena.isGameOver()) {
            arena.setWasInfight(false);
            arena.getGameMessages().clearMessages();
            Point newPoint = new Point(arena.getHero().getPoint());
            arena.getHero().setLastPoint(arena.getHero().getPoint());

            if (isWithinBoundaries(direction)) {
                switch (direction) {
                    case UP:
                        newPoint.y--;
                        break;
                    case DOWN:
                        newPoint.y++;
                        break;
                    case LEFT:
                        newPoint.x--;
                        break;
                    case RIGHT:
                        newPoint.x++;
                        break;
                }
            } else {
                repository.update(arena.getHero());
                arena.setGameOver(true);
            }

            mapController.removeObject(arena.getHero().getPoint());
            arena.getHero().setPoint(newPoint);

            if (isColliding(newPoint))
                arena.setInFight(true);
            else
                mapController.addObject(arena.getHero());
            gainMovePoints();
        }
    }



    protected void gainMovePoints() {
        int movePoints = this.getArena().getHero().getXp()+15;
        this.getArena().getHero().setXp(movePoints);
    }

    private boolean isColliding(Point newPoint) {
        return mapController.containsEnemy(newPoint);
    }

    private boolean isWithinBoundaries(EDirection direction) {
        Point playerPosition = arena.getHero().getPoint();
        int mapSize = arena.getWorldMap().getSize();
        int newPosition;

        if (direction == EDirection.UP || direction == EDirection.DOWN) {
            newPosition = playerPosition.y + direction.getIncrement();
            return newPosition >= 0 && newPosition < mapSize;
        }
        else {
            newPosition = playerPosition.x + direction.getIncrement();
            return newPosition >= 0 && newPosition < mapSize;
        }
    }

    public void createHero(EHeroClass heroClass) {
        Hero hero = HeroFactory.newHero(heroClass);

        arena.setHero(hero);
        playerController.registerHero(hero);
        mapController.initializeMap(arena.getWorldMap(), hero.getLevel());
        mapController.addObject(hero);
    }

    public void fight() {
        arena.getGameMessages().clearMessages();
        if (this.arena.isInFight()){
            Point heroPoint = arena.getHero().getPoint();
            LiveEntity  villain = mapController.getObject(heroPoint);
            LiveEntity winner = battle.fight(arena.getHero(), villain);

            if (winner == arena.getHero()) {
                mapController.addObject(winner);
                arena.getHero().setXp(arena.getHero().getXp()+(10*arena.getHero().getLevel()));
                arena.setInFight(false);
                playerController.levelUp();
            }
            else {
                arena.setGameOver(true);
            }
            arena.setWasInfight(true);
            repository.update(arena.getHero());
        }
    }

    public void run(){
        arena.getGameMessages().clearMessages();
        Random rndm = new Random();
        int success = rndm.nextInt(2);

        if (success == 1){
            System.out.println("You are running away");
            arena.setInFight(false);
            backToLastPoint();
        }else {
            Cli.displayTooSlowForEnemyMsg();
            System.out.println("Cannot run away, you have to fight");
            fight();
        }
    }

    protected void backToLastPoint(){
        playerController.returnToLastPoint();
        mapController.addObject(arena.getHero());
    }

    public boolean isPlayerValid() {
        return arena.isValidPlayerName();
    }

    public void setPlayerName(String name) {
        playerController.setName(name);
        Set<ConstraintViolation<LiveEntity>> violations = PlayerValidation.validEntity(arena.getHero());

        if (violations.isEmpty()) {
            arena.setValidPlayerName(true);
            repository.create(arena.getHero());
        }
        else {
            for (ConstraintViolation<LiveEntity> violation: violations) {
                System.out.println(violation.getMessage());
            }
        }
    }

    public Collection getAllProfiles() {
        return repository.getAll();
    }

    public void loadProfile(Hero profile) {
        resetHP(profile);
        this.arena.setHero(profile);
        //set the size of the arena
        int mapSize = Formulas.getMapSize(profile.getLevel());
        this.arena.getWorldMap().setSize(mapSize);

        playerController.registerHero(profile);
        mapController.initializeMap(arena.getWorldMap(), profile.getLevel());
        mapController.addObject(profile);
    }

    private void resetHP(Hero profile) {
        profile.setHp(100);
    }
}
