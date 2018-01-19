import java.util.ArrayList;
import java.lang.String;

 class Grid {
    
    private int size;
    private Cell[][] grid;
    private int generation = 0;
    private int multi = 3; 

    private GameController controller;

    Grid (int size, GameController controller) {
        if(size <= 10)
            this.size = 10;        
        this.size = size;
        this.controller = controller;
        initializeGrid(size);
    }

    private void initializeGrid(int size) {
        this.size = size;
        this.grid = new Cell[getRowCount()][getColumnCount()];
        for (int y = 0; y < getRowCount(); y++) {
            for (int x = 0; x < getColumnCount(); x++) {
                this.grid[y][x] = new Cell(y, x, this);
            }
        }
    }

    public void start() {
        for (int y = 0; y < getRowCount(); y++) {
            for (int x = 0; x < getColumnCount(); x++) {
                if(isCellALive(x, y)) {
                    grid[y][x].start();
                }
            }
        }
    }

    void revive(int x, int y) {
        grid[y][x].revive();
    }

    void clearList() {
        grid = new Cell[getRowCount()][getColumnCount()];
    }

    void killCell(int x, int y) {
        if(x < 0 || y < 0 || x >= getColumnCount() || y >= getRowCount())
            return;
        grid[y][x] = null;
    }

    public synchronized WhatToDoEnum obtainWhatToDo(Cell cell) {
        
        WhatToDoEnum todo;
        int x = cell.getX();
        int y = cell.getY();

        boolean isCellALive = isCellALive(x, y);
        int aliveNeighbours = 0;
        
        aliveNeighbours += isCellALive(x+1, y) ? 1 : 0; // right
        aliveNeighbours += isCellALive(x, y+1) ? 1 : 0; // bottom
        aliveNeighbours += isCellALive(x-1, y) ? 1 : 0; // left
        aliveNeighbours += isCellALive(x, y-1) ? 1 : 0; // bottom
        aliveNeighbours += isCellALive(x+1, y+1) ? 1 : 0; // right-bottom
        aliveNeighbours += isCellALive(x-1, y-1) ? 1 : 0; // left-up
        aliveNeighbours += isCellALive(x+1, y-1) ? 1 : 0; // right-up
        aliveNeighbours += isCellALive(x-1, y+1) ? 1 : 0; // right-bottom
        
        if(!isCellALive && aliveNeighbours == 3) {
            todo = WhatToDoEnum.REVIVE;
        } else if(isCellALive && (aliveNeighbours < 2 || aliveNeighbours > 3)) {
            todo = WhatToDoEnum.DIE;
        } else if(isCellALive) {
            todo = WhatToDoEnum.LIVE;
        } else {
            todo = WhatToDoEnum.DIE;
        }
        return todo;
    }

    boolean isCellALive(int x, int y) {
        if(x < 0 || y < 0 || x >= getColumnCount() || y >= getRowCount())
            return false;
        
        Cell checkCell = grid[y][x];
        return checkCell.isAlive();
    }

    void printGrid() {
        String result = "Generation:"+(generation++)+" Grid size:"
            +size+"/"+multi*size+"\n";
        for(int y = 0; y < getRowCount(); y++) {
            for(int x = 0; x < getColumnCount(); x++) {
                if(isCellALive(x, y)) {
                    result += '@';
                } else {
                    result += '-';
                }
            }
            result += '\n';
        }
        System.out.println(result);
    }

    int getRowCount() { return size; }

    int getColumnCount() { return multi*size; }

    @Override
    public String toString() {
        String result = "Grid:\n";
        for (int i = 0; i < getRowCount(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                Cell currentCell = grid[i][j];
                if(currentCell != null) {
                    result += "\n"+currentCell.toString();
                }
            }
        }
        return result;
    }
}