import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by scott on 4/11/17.
 */
public class MoveCalculator {
    private static GameBoard gameboard;
    private static GameRules gamerules;
    private int maxCoord = 50;

    public MoveCalculator(GameBoard gb, GameRules gr) {
        gameboard = gb;
        gamerules = gr;
    }

    public int possibleTilePlacementNum(boolean isPlayer1, Tile tileToBePlayed) {
        ArrayList<Point[]>[] tilePlacementArray =  getTilePlacementArrayList(isPlayer1, tileToBePlayed);
        return (tilePlacementArray[0].size() +
                tilePlacementArray[1].size() +
                tilePlacementArray[2].size() +
                tilePlacementArray[3].size() +
                tilePlacementArray[4].size() +
                tilePlacementArray[5].size());
    }

    public int possibleNewSettlementPlacementNum(Player player) {
        ArrayList<Point> villagerPlacementArray = getNewSettlementArrayList(player);
        return villagerPlacementArray.size();
    }


    public int possibleTigerPlacementNum(boolean isPlayer1, Player player) {
        ArrayList<Point> tigerPlacementArray = getTigerPlacementArrayList(isPlayer1, player);
        return tigerPlacementArray.size();
    }

    public int possibleTotoroPlacementNum(boolean isPlayer1, Player player) {
        ArrayList<Point> totoroPlacementArray = getTotoroPlacementArrayList(isPlayer1, player);
        return totoroPlacementArray.size();
    }

    public int possibleSettlementExpandNum(boolean isPlayer1, Player player) {
        HashMap<Character, ArrayList<Point>> expansionPlacementMap = getExpandSettlementHashMap(isPlayer1, player);
        int num = 0;
        // J = jungle, R = rocky, L = lake, G = grasslands, V = volcano
        char terrainTypes[] = {'J', 'R', 'L', 'G'};
        for (int i = 0; i < terrainTypes.length; i++) {
            num += expansionPlacementMap.get(terrainTypes[i]).size();
        }
        return num;
    }

    public int possibleNukeNum(boolean isPlayer1, Tile tileToBePlayed) {
        ArrayList<Point[]>[] tileNukeArray =  getNukeArrayList(isPlayer1, tileToBePlayed);
        return (tileNukeArray[0].size() +
                tileNukeArray[1].size() +
                tileNukeArray[2].size() +
                tileNukeArray[3].size() +
                tileNukeArray[4].size() +
                tileNukeArray[5].size());
    }

    public ArrayList<Point[]>[] getTilePlacementArrayList(boolean isPlayer1, Tile tileToBePlayed) {
        Point[] hexLocations;
        ArrayList<Point[]> possiblePlacementsArrayList[] = new ArrayList[6];
        //initialize ArrayList elements
        for (int i = 0; i < 6; i++) {
            possiblePlacementsArrayList[i] = new ArrayList<Point[]>();
        }

        //cycle through y coords
        for (int volcanoLocX = 0; volcanoLocX < maxCoord; volcanoLocX++) {
            //cycle through x coords
            for (int volcanoLocY = 0; volcanoLocY < maxCoord; volcanoLocY++) {
                //cycle through orientations
                for(int orient = 0; orient < 6; orient++) {
                    //Positive volcanoCoords
                    hexLocations = getHexLocationsFromOrient(orient, volcanoLocX, volcanoLocY);
                    tryToAddTilePlacementLocation(isPlayer1, tileToBePlayed, orient, hexLocations, possiblePlacementsArrayList);

                    //Negative volcanoCoordX
                    hexLocations = getHexLocationsFromOrient(orient, volcanoLocX * -1, volcanoLocY);
                    tryToAddTilePlacementLocation(isPlayer1, tileToBePlayed, orient, hexLocations, possiblePlacementsArrayList);

                    //Negative volcanoCoordY
                    hexLocations = getHexLocationsFromOrient(orient, volcanoLocX, volcanoLocY * -1);
                    tryToAddTilePlacementLocation(isPlayer1, tileToBePlayed, orient, hexLocations, possiblePlacementsArrayList);

                    //Negative volcanoCoordX and volcanoCoordY
                    hexLocations = getHexLocationsFromOrient(orient, volcanoLocX * -1, volcanoLocY * -1);
                    tryToAddTilePlacementLocation(isPlayer1, tileToBePlayed, orient, hexLocations, possiblePlacementsArrayList);
                }
            }
        }
        return possiblePlacementsArrayList;
    }

    public ArrayList<Point[]>[] getNukeArrayList(boolean isPlayer1, Tile tileToBePlayed) {
        Point[] hexLocations;
        ArrayList<Point[]> possibleNukeArrayList[] = new ArrayList[6];
        //initialize ArrayList elements
        for (int i = 0; i < 6; i++) {
            possibleNukeArrayList[i] = new ArrayList<Point[]>();
        }

        //cycle through y coords
        for (int volcanoLocX = 0; volcanoLocX < maxCoord; volcanoLocX++) {
            //cycle through x coords
            for (int volcanoLocY = 0; volcanoLocY < maxCoord; volcanoLocY++) {
                //cycle through orientations
                for(int orient = 0; orient < 6; orient++) {
                    //Positive volcanoCoords
                    hexLocations = getHexLocationsFromOrient(orient, volcanoLocX, volcanoLocY);
                    tryToAddNukeLocation(isPlayer1, tileToBePlayed, orient, hexLocations, possibleNukeArrayList);

                    //Negative volcanoCoordX
                    hexLocations = getHexLocationsFromOrient(orient, volcanoLocX * -1, volcanoLocY);
                    tryToAddNukeLocation(isPlayer1, tileToBePlayed, orient, hexLocations, possibleNukeArrayList);

                    //Negative volcanoCoordY
                    hexLocations = getHexLocationsFromOrient(orient, volcanoLocX, volcanoLocY * -1);
                    tryToAddNukeLocation(isPlayer1, tileToBePlayed, orient, hexLocations, possibleNukeArrayList);

                    //Negative volcanoCoordX and volcanoCoordY
                    hexLocations = getHexLocationsFromOrient(orient, volcanoLocX * -1, volcanoLocY * -1);
                    tryToAddNukeLocation(isPlayer1, tileToBePlayed, orient, hexLocations, possibleNukeArrayList);
                }
            }
        }
        return possibleNukeArrayList;
    }

    public ArrayList<Point> getNewSettlementArrayList(Player player) {
        Point hexLocation;
        ArrayList<Point> possiblePlacementsArrayList = new ArrayList<>();

        //if player doesn't have any villagers then return 0
        if (player.getvillagersRemaining() == 0) {
            return possiblePlacementsArrayList;
        }

        //cycle through y coords
        for (int coordX = 0; coordX < maxCoord; coordX++) {
            //cycle through x coords
            for (int coordY = 0; coordY < maxCoord; coordY++) {
                //Positive coords
                hexLocation = new Point(coordX, coordY);
                tryToAddNewSettlementLocation(player, hexLocation, possiblePlacementsArrayList);

                //Negative xCoord
                hexLocation = new Point(coordX * -1, coordY);
                tryToAddNewSettlementLocation(player, hexLocation, possiblePlacementsArrayList);

                //Negative yCoord
                hexLocation = new Point(coordX, coordY * -1);
                tryToAddNewSettlementLocation(player, hexLocation, possiblePlacementsArrayList);

                //Negative xCoord and yCoord
                hexLocation = new Point(coordX * -1, coordY * -1);
                tryToAddNewSettlementLocation(player, hexLocation, possiblePlacementsArrayList);
            }
        }
        return possiblePlacementsArrayList;
    }

    public HashMap<Character, ArrayList<Point>> getExpandSettlementHashMap(boolean isPlayer1, Player player) {
        Point hexLocation;
        HashMap<Character, ArrayList<Point>> possiblePlacementsHashMap = new HashMap<>();
        //if player doesn't have any villagers then return empty list
        if (player.getvillagersRemaining() == 0) {
            return possiblePlacementsHashMap;
        }
        //build hashmap with terrainTypes
        // J = jungle, R = rocky, L = lake, G = grasslands, V = volcano
        char terrainTypes[] = {'J', 'R', 'L', 'G'};
        for (int i = 0; i < terrainTypes.length; i++) {
            ArrayList<Point> newList = new ArrayList<>();
            possiblePlacementsHashMap.put(terrainTypes[i], newList);
        }

        //cycle through y coords
        for (int coordX = 0; coordX < maxCoord; coordX++) {
            //cycle through x coords
            for (int coordY = 0; coordY < maxCoord; coordY++) {
                //Positive coords
                hexLocation = new Point(coordX, coordY);
                tryToAddExpandSettlementLocation(isPlayer1, player, hexLocation, possiblePlacementsHashMap);

                //Negative xCoord
                hexLocation = new Point(coordX * -1, coordY);
                tryToAddExpandSettlementLocation(isPlayer1, player, hexLocation, possiblePlacementsHashMap);

                //Negative yCoord
                hexLocation = new Point(coordX, coordY * -1);
                tryToAddExpandSettlementLocation(isPlayer1, player, hexLocation, possiblePlacementsHashMap);

                //Negative xCoord and yCoord
                hexLocation = new Point(coordX * -1, coordY * -1);
                tryToAddExpandSettlementLocation(isPlayer1, player, hexLocation, possiblePlacementsHashMap);
            }
        }
        return possiblePlacementsHashMap;

    }

    public ArrayList<Point> getTigerPlacementArrayList(boolean isPlayer1, Player player) {
        Point hexLocation;
        ArrayList<Point> possiblePlacementsArrayList = new ArrayList<>();

        //if player doesn't have any tigers then return empty list
        if (player.gettigersRemaining() == 0) {
            return possiblePlacementsArrayList;
        }

        //cycle through y coords
        for (int coordX = 0; coordX < maxCoord; coordX++) {
            //cycle through x coords
            for (int coordY = 0; coordY < maxCoord; coordY++) {
                //Positive coords
                hexLocation = new Point(coordX, coordY);
                tryToAddTiger(isPlayer1, player, hexLocation, possiblePlacementsArrayList);

                //Negative xCoord
                hexLocation = new Point(coordX * -1, coordY);
                tryToAddTiger(isPlayer1, player, hexLocation, possiblePlacementsArrayList);

                //Negative yCoord
                hexLocation = new Point(coordX, coordY * -1);
                tryToAddTiger(isPlayer1, player, hexLocation, possiblePlacementsArrayList);

                //Negative xCoord and yCoord
                hexLocation = new Point(coordX * -1, coordY * -1);
                tryToAddTiger(isPlayer1, player, hexLocation, possiblePlacementsArrayList);
            }
        }
        return possiblePlacementsArrayList;
    }

    public ArrayList<Point> getTotoroPlacementArrayList(boolean isPlayer1, Player player) {
        Point hexLocation;
        ArrayList<Point> possiblePlacementsArrayList = new ArrayList<>();

        //if player doesn't have any totoros then return empty list
        if (player.gettotorosRemaining() == 0) {
            return possiblePlacementsArrayList;
        }

        //cycle through y coords
        for (int coordX = 0; coordX < maxCoord; coordX++) {
            //cycle through x coords
            for (int coordY = 0; coordY < maxCoord; coordY++) {
                //Positive coords
                hexLocation = new Point(coordX, coordY);
                tryToAddTotoro(isPlayer1, player, hexLocation, possiblePlacementsArrayList);

                //Negative xCoord
                hexLocation = new Point(coordX * -1, coordY);
                tryToAddTotoro(isPlayer1, player, hexLocation, possiblePlacementsArrayList);

                //Negative yCoord
                hexLocation = new Point(coordX, coordY * -1);
                tryToAddTotoro(isPlayer1, player, hexLocation, possiblePlacementsArrayList);

                //Negative xCoord and yCoord
                hexLocation = new Point(coordX * -1, coordY * -1);
                tryToAddTotoro(isPlayer1, player, hexLocation, possiblePlacementsArrayList);
            }
        }
        return possiblePlacementsArrayList;
    }

    private Point[] getHexLocationsFromOrient(int orientNum, int volcanoX, int volcanoY) {
        switch (orientNum) {
            case 0: return new Point[] {new Point(volcanoX, volcanoY - 1),
                    new Point(volcanoX + 1, volcanoY - 1),
                    new Point(volcanoX, volcanoY)};

            case 1: return new Point[] {new Point(volcanoX + 1, volcanoY - 1),
                    new Point(volcanoX + 1, volcanoY),
                    new Point(volcanoX, volcanoY)};

            case 2: return new Point[] {new Point(volcanoX + 1, volcanoY),
                    new Point(volcanoX, volcanoY + 1),
                    new Point(volcanoX, volcanoY)};

            case 3: return new Point[] {new Point(volcanoX, volcanoY + 1),
                    new Point(volcanoX - 1, volcanoY + 1),
                    new Point(volcanoX, volcanoY)};

            case 4: return new Point[] {new Point(volcanoX - 1, volcanoY + 1),
                    new Point(volcanoX - 1, volcanoY),
                    new Point(volcanoX, volcanoY)};

            case 5: return new Point[] {new Point(volcanoX - 1, volcanoY),
                    new Point(volcanoX, volcanoY - 1),
                    new Point(volcanoX, volcanoY)};
            default: break;
        }
        return null;
    }

    private void tryToAddTilePlacementLocation(boolean isPlayer1, Tile tile, int orient, Point[] hexLocations, ArrayList<Point[]>[] currentArray) {
        //for some reason can not place tile over two volcanoes so I'm preventing it by checking the hexlocations
        /*int volcanoCount = 0;
        for (int i = 0; i < hexLocations.length; i++) {
            Hex interestedHex = gameboard.getHexAtPointP(hexLocations[i]);
            if (interestedHex == null) {
                continue;
            }
            if (interestedHex.getTerrain() == 'V') {
                volcanoCount += 1;
            }
        }

        if (volcanoCount > 1) {
            return;
        }*/

        //check if hex is not null
        //int numOfOppVillagers = 0;
        for (int i = 0; i < hexLocations.length; i++) {
            Hex interestedHex = gameboard.getHexAtPointP(hexLocations[i]);
            if(interestedHex != null) {
                return;
            }
            /*if ((interestedHex.getPiece() == Pieces.P1_TIGER || interestedHex.getPiece() == Pieces.P1_TOTORO || interestedHex.getPiece() == Pieces.P1_VILLAGER)) {
                return;
            }
            if (interestedHex.getPiece() == Pieces.P2_VILLAGER) {
                numOfOppVillagers += 1;
            }
            //else if (!isPlayer1 && (interestedHex.getPiece() == Pieces.P2_TIGER || interestedHex.getPiece() == Pieces.P2_TOTORO || interestedHex.getPiece() == Pieces.P2_VILLAGER)) {
            //    return;
            //}*/
        }
        //if(numOfOppVillagers == 1) {
        //    return;
        //}

        //check if location is already contained
        boolean locationContained = false;
        for (int i = 0; i < currentArray[orient].size(); i++) {
            Point[] currentPointArray = currentArray[orient].get(i);
            if ((currentPointArray[2].getX() == hexLocations[2].getX()) && (currentPointArray[2].getY() == hexLocations[2].getY())) {
                locationContained = true;
            } else {

            }
        }
        if(locationContained == false) {
            try {
                gamerules.TryToAddTile(tile, hexLocations);
                currentArray[orient].add(hexLocations);
            } catch (GameRulesException e) {
            }
        }
    }

    private void tryToAddNukeLocation(boolean isPlayer1, Tile tile, int orient, Point[] hexLocations, ArrayList<Point[]>[] currentArray) {
        //for some reason can not place tile over two volcanoes so I'm preventing it by checking the hexlocations
        int volcanoCount = 0;
        for (int i = 0; i < hexLocations.length; i++) {
            Hex interestedHex = gameboard.getHexAtPointP(hexLocations[i]);
            if (interestedHex == null) {
                return;
            }
            if (interestedHex.getTerrain() == 'V') {
                volcanoCount += 1;
            }
        }

        if (volcanoCount > 1) {
            return;
        }

        //check if hex contains opponents villages and players villagers
        int numOfOppVillagers = 0;
        for (int i = 0; i < hexLocations.length; i++) {
            Hex interestedHex = gameboard.getHexAtPointP(hexLocations[i]);
            if(interestedHex == null) {
                return;
            }
            if (isPlayer1 && (interestedHex.getPiece() == Pieces.P1_TIGER || interestedHex.getPiece() == Pieces.P1_TOTORO || interestedHex.getPiece() == Pieces.P1_VILLAGER)) {
                return;
            }
            else if (!isPlayer1 && (interestedHex.getPiece() == Pieces.P1_TIGER || interestedHex.getPiece() == Pieces.P1_TOTORO || interestedHex.getPiece() == Pieces.P1_VILLAGER)) {
                return;
            }
            if (isPlayer1 && interestedHex.getPiece() == Pieces.P2_VILLAGER) {
                numOfOppVillagers += 1;
            }
            if (!isPlayer1 && interestedHex.getPiece() == Pieces.P1_VILLAGER) {
                numOfOppVillagers += 1;
            }
        }
        if(numOfOppVillagers < 2) {
            return;
        }

        //check if location is already contained
        boolean locationContained = false;
        for (int i = 0; i < currentArray[orient].size(); i++) {
            Point[] currentPointArray = currentArray[orient].get(i);
            if ((currentPointArray[2].getX() == hexLocations[2].getX()) && (currentPointArray[2].getY() == hexLocations[2].getY())) {
                locationContained = true;
            } else {

            }
        }
        if(locationContained == false) {
            try {
                gamerules.TryToAddTile(tile, hexLocations);
                currentArray[orient].add(hexLocations);
            } catch (GameRulesException e) {
            }
        }
    }

    private void tryToAddNewSettlementLocation(Player player, Point hexLocation, ArrayList<Point> currentList) {
        if (currentList.contains(hexLocation) || gameboard.getHexAtPointP(hexLocation) == null) {
            return;
        }
        if (gameboard.getHexAtPointP(hexLocation).getPiece() != Pieces.NONE) {
            return;
        }
        if (gameboard.getHexAtPointP(hexLocation).getLevel() > 1) {
            return;
        }
        try {
            gamerules.tryToBuildNewSettlement(player, hexLocation);
            currentList.add(hexLocation);
        } catch(GameRulesException e) {}
    }

    private void tryToAddExpandSettlementLocation(boolean isPlayer1, Player player, Point hexLocation, HashMap<Character, ArrayList<Point>> currentMap) {
        //check if intended hex is null
        Hex intendedHex = gameboard.getHexAtPointP(hexLocation);
        if (intendedHex == null) {
            return;
        }

        //check if intendedHex contains player's settlement
        Pieces piece = intendedHex.getPiece();
        if(isPlayer1 && !(piece == Pieces.P1_VILLAGER || piece == Pieces.P1_TOTORO || piece == Pieces.P1_TIGER)) {
            return;
        }
        if(!isPlayer1 && !(piece == Pieces.P2_VILLAGER || piece == Pieces.P2_TOTORO || piece == Pieces.P2_TIGER)) {
            return;
        }

        // J = jungle, R = rocky, L = lake, G = grasslands, V = volcano
        char terrainTypeList[] = {'J', 'R', 'L', 'G'};

        //try to expand settlement for each terrain type
        for (int i = 0; i < terrainTypeList.length; i++) {
            //check if location and terrain type have already been added to map
            if (currentMap.get(terrainTypeList[i]).contains(hexLocation)) {
                continue;
            }
            try {
                ArrayList<Point> theExpandMap = gamerules.tryToExpand(player, terrainTypeList[i], hexLocation);
                if(theExpandMap.size() > 0) {
                    currentMap.get(terrainTypeList[i]).add(hexLocation);
                }
            } catch(GameRulesException e) {}
        }
    }

    private void tryToAddTiger(boolean isPlayer1, Player player, Point hexLocation, ArrayList<Point> currentList) {
        int coordX = (int)hexLocation.getX();
        int coordY = (int)hexLocation.getY();
        Point[] hexNeighborLocations = {new Point(coordX, coordY - 1),
                                        new Point(coordX - 1, coordY),
                                        new Point(coordX - 1, coordY + 1),
                                        new Point(coordX, coordY + 1),
                                        new Point(coordX + 1, coordY),
                                        new Point(coordX + 1, coordY - 1)};

        //check if it has already been added to list or if intended hex is null
        Hex intendedHex = gameboard.getHexAtPointP(hexLocation);
        if (currentList.contains(hexLocation) || intendedHex == null) {
            return;
        }
        for (int i = 0; i < hexNeighborLocations.length; i++) {
            Hex Neighborhex = gameboard.getHexAtPointP(hexNeighborLocations[i]);
            //check if hex is null
            if (Neighborhex == null) {
                continue;
            }

            //check if NeighborHex contains player's settlement
            Pieces piece = Neighborhex.getPiece();
            if (isPlayer1 && (Pieces.P1_VILLAGER == piece)) {
                try {
                    gamerules.tryToAddTiger(player, hexLocation, hexNeighborLocations[i]);
                    currentList.add(hexLocation);
                } catch(GameRulesException e) {}
            }
            else if (!isPlayer1 && (Pieces.P2_VILLAGER == piece)) {
                try {
                    gamerules.tryToAddTiger(player, hexLocation, hexNeighborLocations[i]);
                    currentList.add(hexLocation);
                } catch(GameRulesException e) {}
            }
        }
    }

    private void tryToAddTotoro(boolean isPlayer1, Player player, Point hexLocation, ArrayList<Point> currentList) {
        int coordX = (int)hexLocation.getX();
        int coordY = (int)hexLocation.getY();
        Point[] hexNeighborLocations = {new Point(coordX, coordY - 1),
                new Point(coordX - 1, coordY),
                new Point(coordX - 1, coordY + 1),
                new Point(coordX, coordY + 1),
                new Point(coordX + 1, coordY),
                new Point(coordX + 1, coordY - 1)};

        //check if it has already been added to list or if intended hex is null
        Hex intendedHex = gameboard.getHexAtPointP(hexLocation);
        if (currentList.contains(hexLocation) || intendedHex == null) {
            return;
        }

        for (int i = 0; i < hexNeighborLocations.length; i++) {
            Hex Neighborhex = gameboard.getHexAtPointP(hexNeighborLocations[i]);
            //check if NeighborHex is null
            if (Neighborhex == null) {
                continue;
            }

            //check if NeighborHex contains player's settlement
            Pieces piece = Neighborhex.getPiece();

            if (isPlayer1 && (Pieces.P1_VILLAGER == piece)) {
                try {
                    gamerules.tryToAddTotoro(player, hexLocation, hexNeighborLocations[i]);
                    currentList.add(hexLocation);
                } catch(GameRulesException e) {}
            }
            else if (!isPlayer1 && (Pieces.P2_VILLAGER == piece)) {
                try {
                    gamerules.tryToAddTotoro(player, hexLocation, hexNeighborLocations[i]);
                    currentList.add(hexLocation);
                } catch(GameRulesException e) {}
            }
        }
    }

}
