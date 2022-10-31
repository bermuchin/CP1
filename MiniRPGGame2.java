package week11;

import java.util.Arrays;
import java.util.Scanner;

public class MiniRPGGame2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GamePlayEngine engine = new GamePlayEngine(sc);

        engine.printGameResult();
    }
}

class GamePlayEngine {
    static final int CHAR_ARG_LENGTH = 7;
    static final int MAP_ARG_MIN_LENGTH = 2;

    static class MonsterMove {
        public int monsterId;
        public Point position;
    }

    Game currentGame;

    MonsterMove[] monsterMoveQueue;
    int monsterMoveQueueIdx = 0;

    public GamePlayEngine(Scanner sc) throws IllegalArgumentException {
        String[] mapInfo = sc.nextLine().split(" ");
        String[] playerInfo = sc.nextLine().split(" ");
        String[] monsterInfo = sc.nextLine().split(" ");
        String[] monsterMovesInfo = sc.nextLine().split(" ");

        Map map = initializeMap(mapInfo);
        Character player = initializeCharacter(playerInfo);
        Character[] monsters = initializeCharacters(monsterInfo);
        monsterMoveQueue = initializeMonsterMoves(monsterMovesInfo);

        currentGame = new Game(map, player, monsters);

        doGameLoop(sc);
    }

    private MonsterMove[] initializeMonsterMoves(String[] monsterMovesInfo) {
        if (monsterMovesInfo.length % 3 != 0)
            throw new IllegalArgumentException("Malformed monster move info");

        int queueSize = monsterMovesInfo.length / 3;
        MonsterMove[] newMonsterMoveQueue = new MonsterMove[queueSize];

        for (int i = 0; i < queueSize; ++i) {
            MonsterMove move = new MonsterMove();
            move.monsterId = Integer.parseInt(monsterMovesInfo[3 * i]);

            move.position = new Point();
            move.position.x = Integer.parseInt(monsterMovesInfo[3 * i + 1]);
            move.position.y = Integer.parseInt(monsterMovesInfo[3 * i + 2]);

            newMonsterMoveQueue[i] = move;
        }

        return newMonsterMoveQueue;
    }

    private void popMonsterMoveQueue(MonsterMove move) {
        if (move.monsterId >= currentGame.getMonsterCount() || move.monsterId == -1)
            return;

        currentGame.getMonster(move.monsterId)
                   .move(move.position);
    }

    private void logMove(Point position) {
        // 여기를 채우세요
    	System.out.println("[SYSTEM] Player moved to ("+ currentGame.getPlayer().getPosition().x +", " + 
        currentGame.getPlayer().getPosition().y+")");
    }

    private void logAttack(Character target, Weapon weapon, int damage) {
        // 여기를 채우세요
    	System.out.println("[SYSTEM] Player attacked "+ target.getName() +" with "+
        weapon.getName()+", "+damage+ " of damage has been applied");
    	
    	target.applyDamage(damage);
    	
    	if(target.isAlive()==false) {System.out.printf("[SYSTEM] "+target.getName()+" is now dead!\n");}
    }

    private void doGameLoop(Scanner sc) {
        boolean isGameContinue = true;

        while (isGameContinue) {
            isGameContinue = sc.hasNextLine();

            if (isGameContinue) {
                String[] controls = sc.nextLine().split(" ");

                if (controls.length == 0) {
                    continue;
                }

                switch (controls[0]) {
                    case "move":
                        Point newPosition = new Point();
                        newPosition.x = Integer.parseInt(controls[1]);
                        newPosition.y = Integer.parseInt(controls[2]);

                        if (currentGame.getCurrentMap().objectPlaceable(newPosition)) {
                            currentGame.getPlayer().move(newPosition);
                            logMove(newPosition);
                        }

                        break;
                    case "attack":
                        int target = Integer.parseInt(controls[1]);
                        Character monster = currentGame.getMonster(target);
                        int damage = currentGame.getPlayer().attack(monster);
                        Weapon weapon = currentGame.getPlayer().getWeapon();

                        logAttack(monster, weapon, damage);

                        break;
                    case "use":
                        break;
                }
            }

            isGameContinue = isGameContinue || monsterMoveQueueIdx < monsterMoveQueue.length;

            if (isGameContinue && monsterMoveQueueIdx < monsterMoveQueue.length)
                popMonsterMoveQueue(monsterMoveQueue[monsterMoveQueueIdx++]);
        }
    }

    private Character[] initializeCharacters(String[] serializedCharacterInfo) {
        if (serializedCharacterInfo == null || serializedCharacterInfo.length == 0)
            return null;

        if (serializedCharacterInfo.length % CHAR_ARG_LENGTH > 0)
            throw new IllegalArgumentException("Malformed length info");

        int charCount = serializedCharacterInfo.length / CHAR_ARG_LENGTH;
        Character[] characters = new Character[charCount];

        for (int i = 0; i < charCount; ++i) {
            String[] subArray = Arrays.copyOfRange(serializedCharacterInfo, CHAR_ARG_LENGTH * i, CHAR_ARG_LENGTH * i + CHAR_ARG_LENGTH);
            characters[i] = initializeCharacter(subArray);
        }

        return characters;
    }

    private Character initializeCharacter(String[] characterInfo) {
        if (characterInfo.length != CHAR_ARG_LENGTH)
            throw new IllegalArgumentException("Malformed length info");

        String name = characterInfo[0];

        Point position = new Point();
        position.x = Integer.parseInt(characterInfo[1]);
        position.y = Integer.parseInt(characterInfo[2]);

        int hp = Integer.parseInt(characterInfo[3]);

        Weapon weapon = null;
        String wName = characterInfo[4];

        if (!"-1".equals(wName)) {
            int wDamage = Integer.parseInt(characterInfo[5]);
            int wRange = Integer.parseInt(characterInfo[6]);

            weapon = new Weapon(wName, wDamage, wRange);
        }

        return new Character(name, position, hp, weapon);
    }

    private Map initializeMap(String[] mapInfo) {
        if (mapInfo.length < 2 || mapInfo.length % 2 != 0)
            throw new IllegalArgumentException("Malformed map info");

        Size size = new Size();
        size.width  = Integer.parseInt(mapInfo[0]);
        size.height = Integer.parseInt(mapInfo[1]);

        int blockTilesCount = (mapInfo.length - 2) / 2;
        Point[] blockTiles = null;

        if (blockTilesCount > 0) {
            blockTiles = new Point[blockTilesCount];

            for (int i = 0; i < blockTilesCount; ++i) {
                int x = Integer.parseInt(mapInfo[2 + i * 2]);
                int y = Integer.parseInt(mapInfo[3 + i * 2]);

                Point tile = new Point();
                tile.x = x;
                tile.y = y;

                blockTiles[i] = tile;
            }
        }

        return new Map(size, blockTiles);
    }

    public void printCharacterState(String type, Character character) {
        // 여기를 채우세요
    	System.out.println("[" + type +"] "+ "Name: "+ character.getName()+", HP: "+ character.getHp()+", Position: ("+
        character.getPosition().x +", "+ character.getPosition().y +")");
    }

    public void printGameResult() {
        // 여기를 채우세요
    	System.out.println("[Player] Name: "+ currentGame.getPlayer().getName() + ", HP: "+ currentGame.getPlayer().getHp()+
    			", Position: (" + currentGame.getPlayer().getPosition().x+", "+currentGame.getPlayer().getPosition().y+")");
    	
    	System.out.println("-> with weapon "+ currentGame.getPlayer().getWeapon().getName()+
    			" (damage "+currentGame.getPlayer().getWeapon().getDamage()+")");
    	
    	for(int i=0; i< currentGame.getMonsterCount() ;i++) {
    	System.out.println("[Monster] Name: "+currentGame.getMonster(i).getName()+", HP: "+currentGame.getMonster(i).getHp()+
    			", Position: ("+ currentGame.getMonster(i).getPosition().x+", "+currentGame.getMonster(i).getPosition().y+")");
    	}
    	
    }
}

class Game {
    private Map currentMap;
    private Character player;
    private Character[] monsters;

    public Game(Map map, Character player, Character[] monsters) {
        // 여기를 채우세요
    	setPlayer(player);
    	setCurrentMap(map);
    	this.monsters = monsters;
    }

    public void setPlayer(Character player) {
        this.player = player;
    }

    public void setCurrentMap(Map currentMap) {
        // 여기를 채우세요
    	this.currentMap = currentMap;
    }

    public Character getMonster(int i) {
        // 여기를 채우세요
    	return monsters[i];
    }

    public int getMonsterCount() {
        // 여기를 채우세요
    	return monsters.length;
    }

    public Character getPlayer() {
        // 여기를 채우세요
    	return player;
    }

    public Map getCurrentMap() {
        // 여기를 채우세요
    	return currentMap;
    }
}

class Point {
    public int x;
    public int y;

    public boolean equals(Point point) {
        if (point == null) return false;
        else if (this == point) return true;

        return point.x == this.x && point.y == this.y;
    }
}

class Size {
    public int width;
    public int height;

    public boolean equals(Size size) {
        if (size == null) return false;
        else if (this == size) return true;

        return size.width == this.width && size.height == this.height;
    }
}

class Map {
    private final Size    size;
    private final Point[] blockTiles;

    public Map(Size size, Point[] blockTiles) {
        // 여기를 채우세요
    	this.size = size;
    	this.blockTiles = blockTiles;
    }

    public boolean objectPlaceable(Point point) {
        return (blockTiles == null || Arrays.asList(blockTiles).contains(point)) &&
               (point.x <= size.width && point.y <= size.height);
    }
}

class Weapon {
    private final String name;
    private final int damage;
    private final int range;

    public Weapon(String name, int damage, int range) {
        // 여기를 채우세요
    	this.name = name;
    	this.damage = damage;
    	this.range = range;
    }

    public String getName() {
        // 여기를 채우세요
    	return name;
    }

    public int getDamage() {
        // 여기를 채우세요
    	return damage;
    }

    public int getRange() {
        // 여기를 채우세요
    	return range;
    }
}

class Character {
    private String name;
    private Point position;
    private int hp;

    private Weapon weapon;

    public Character(String name, Point position, int hp, Weapon weapon) {
        // 여기를 채우세요
    	this.name = name;
    	this.position = position;
    	this.hp = hp;
    	this.weapon = weapon;
    }

    public int getHp() {
        // 여기를 채우세요
    	return hp;
    }

    public int applyDamage(int damage) {
        // 여기를 채우세요
    	this.hp = hp - damage;
    	return hp;
    	
    }

    public String getName() {
        // 여기를 채우세요
    	return name;
    }

    public Point getPosition() {
        // 여기를 채우세요
    	return position;
    }

    public void move(Point point) {
        // 여기를 채우세요
    	this.position = point;
    }

    public int attack(Character target) {
        // 여기를 채우세요
    	if (range(target) <= weapon.getRange()) {
    		if(target.hp >= weapon.getDamage()) return weapon.getDamage();
    		else return target.hp;
    	}else return 0;
    }

    public int range(Character target) {
        // 여기를 채우세요
    	int a = (target.position.x - getPosition().x)*(target.position.x - getPosition().x)+
    			(target.position.y - getPosition().y)*(target.position.y - getPosition().y);
    	return (int)Math.sqrt(a);
    }

    public boolean isAlive() {
        // 여기를 채우세요
    	if (hp==0) return false;
    	else return true;
    }

    public Weapon getWeapon() {
        // 여기를 채우세요
    	
    	return weapon;
    }
}
