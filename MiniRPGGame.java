package week12;
import java.util.Arrays;
import java.util.Scanner;

public class MiniRPGGame {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GamePlayEngine engine = new GamePlayEngine(sc);

        engine.printGameResult();
    }
}

class GamePlayEngine {
    static final int CHAR_ARG_LENGTH = 4;
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

                        if (currentGame.getCurrentMap().objectPlaceable(newPosition))
                            currentGame.getPlayer().move(newPosition);
                        break;
                    case "attack":
                        break;
                    case "use":
                        break;
                }
            }

            isGameContinue = isGameContinue || monsterMoveQueueIdx < monsterMoveQueue.length;

            if (isGameContinue)
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
            String[] subArray = Arrays.copyOfRange(serializedCharacterInfo, 4 * i, 4 * i + 4);
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

        return new Character(name, position, hp);
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
    	System.out.println("[" + type +"] "+ "Name: "+ character.getName() + ", Position: (" + character.getPosition().x +", "+ character.getPosition().y +")");
    }

    public void printGameResult() {
        // 여기를 채우세요
    	System.out.println("[Player] Name: "+ currentGame.getPlayer().getName() + ", Position: (" + currentGame.getPlayer().getPosition().x +", "+ currentGame.getPlayer().getPosition().y +")");
    	
    	for(int i=0; i< currentGame.getMonsterCount() ;i++) {
    		System.out.println("[Monster] Name: "+ currentGame.getMonster(i).getName() + ", Position: (" + currentGame.getMonster(i).getPosition().x +", "+ currentGame.getMonster(i).getPosition().y +")");
    	}
    }
}

class Game {
    private Map currentMap;
    private Character player;
    private Character[] monsters;

    public Game(Map map, Character player, Character[] monsters) {
        // 여기를 채우세요
    	setCurrentMap(map);
    	setPlayer(player);
    	this.monsters = monsters;
    }

    public void setPlayer(Character player) {
        // 여기를 채우세요
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

class Character {
    private String name;
    private Point position;
    private int hp;

    public Character(String name, Point position, int hp) {
        // 여기를 채우세요
    	this.name = name;
    	this.position = position;
    	this.hp = hp; 
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
}