
import java.util.ArrayList;
import java.lang.Thread;

class GameController extends Thread {
    private Grid grid;
    private static volatile GameController instance;

    public static GameController getInstance() {
        GameController localInstance  = instance;
        if(localInstance == null) {
            synchronized (GameController.class) {
                localInstance = instance;
                if(localInstance == null) {
                    instance = localInstance = new GameController();
                }
            }
        }
        return localInstance;
    }
    
    void initializeGrid(int size) {
        if(size < 10) {
            System.out.println("Illegal size. Minimum size is 10");
            return;
        }
        setGrid(new Grid(size, this));
    }

    @Override
    public void start() {
        while(true) {
            try {
                getGrid().printGrid();
                Thread.sleep(1000);    
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void revive(int y, int x) {
        getGrid().revive(x, y);
    }

    private void printGrid() {
        getGrid().printGrid();
    }

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid newGrid) {
        this.grid = newGrid;
    }

    @Override
    public String toString() {
        return "GameController { " + grid.toString() + " }";
    }
}