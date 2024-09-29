import java.util.Random; //import random for generateFood();
import java.util.Scanner; //import scanner for user input

// Purpose: Implement a simple snake game in Java.
public class SnakeGame {

    // 2D array to represent game grid
    public static char[][] grid ;//2D array

    //initialize snake array and starting snake length
    public static int[][] snake;
    public static int snakeLength;

    //initialize food coordinates x,y
    public static int foodX,foodY;

    // The game is over flag
    public static boolean isGameOver = false;

    // Implement the following constants
    public static final int GRID_SIZE_X = 20;// the number of columns
    public static final int GRID_SIZE_Y = 10;// the number of rows
    public static final char SNAKE_CHAR = 's';
    public static final char FOOD_CHAR = '@';
    public static final char EMPTY_CHAR = ' ';
    public static final char WALL_CHAR = '#';
    public static final int MAX_SNAKE_LENGTH = 100;


    //This method initializes the game grid, walls, snake and food
    public static void initializeGame() {
        grid = new char[GRID_SIZE_Y][GRID_SIZE_X]; //creates game grid

        //fills the grid with walls and empty spaces where needed
        for (int y = 0; y < GRID_SIZE_Y; y++){
            for (int x = 0; x < GRID_SIZE_X;x++){
                if (y == 0 || y == GRID_SIZE_Y-1||x == 0||x == GRID_SIZE_X - 1) {
                    grid[y][x] = WALL_CHAR; //places walls at borders of grid
                } else {
                    grid[y][x] = EMPTY_CHAR; //places empty spaces in middle of grid
                }
            }
        }
        generateSnake(); //method to generate initial snake
        generateFood(); //method to generate food

    }
    //method below generates the snakes starting position
    public static void generateSnake(){
        snake = new int[MAX_SNAKE_LENGTH][2];//2D snake array with max length 100
        snakeLength =3; // starting snake length

        //starting position of snake, within grid bounds
        snake[0][0] = 5; //initial X position of snakes head
        snake[0][1] = 5; //initial Y position of snakes head

        //update the game grid with snakes head initial position
        for (int i = 0; i < snakeLength; i++){
            grid[snake[i][1]][snake[i][0]] = SNAKE_CHAR;
        }
    }

    // Update snakes position based on the move
    public static void updateSnakePosition(int newX, int newY) {
        // move all body segments of snake to the position of previous segment
        for (int i = snakeLength - 1; i>0;i--) {
            snake[i][0] = snake[i-1][0]; //Move X coordinate
            snake[i][1] = snake[i-1][1]; //Move Y coordinate
        }
        //update the head position to the new coordinates
        snake[0][0]=newX;
        snake[0][1]=newY;

        //clear the grid (leaving the walls) then update grid with new snake position
        for (int y = 1; y < GRID_SIZE_Y-1; y++){
            for (int x = 1; x < GRID_SIZE_X-1; x++){
                    grid[y][x] = EMPTY_CHAR;
                }
            }
        //places updated snake back on the grid
        for (int i = 0; i < snakeLength; i++){
            grid[snake[i][1]][snake[i][0]] = SNAKE_CHAR;
        }

        //adds the food back to the grid since it was removed
        grid[foodY][foodX] = FOOD_CHAR;
    }



    //check if the move is valid or not, makes sure the move is within bounds
    public static boolean isValidMove(int x, int y) {
        //check if the new coordinates are within grid bounds
        if (x<0 || x>= GRID_SIZE_X|| y < 0 || y >=GRID_SIZE_Y){
            return false; //returns false if out of bounds
        }
        //check to see if the snake collides with itself
        for (int i = 0; i<snakeLength; i++){
            if (snake[i][0] == x && snake[i][1] == y){
                return false; //returns false if there is a collision with itself
            }
        }
        //check if snake collides with wall
        if (grid[y][x] == WALL_CHAR){
            return false;
        }
        return true; //if all above checks are met, no collision so move valid, returns true
    }
    //displays the current game state to the console
    public static void displayGrid() {
        for (int y = 0; y < GRID_SIZE_Y; y++){
            for (int x = 0; x < GRID_SIZE_X; x++){
                System.out.print(grid[y][x]);
            }
            System.out.println();
        }
    }
    //Moves the snake based on user input "move"
    public static void moveSnake(char move) {
        //the current head position of snake
        int headX = snake[0][0]; // x coordinate
        int headY = snake[0][1]; // y coordinate

        int newX = headX;
        int newY = headY;

        switch (move) {
            case 'w': newY--; break; //move up
            case 'a': newX--;break; //move left
            case 's': newY++;break; //move down
            case 'd': newX++;break; // move right
            case 'q': isGameOver = true;
                return; //quits game
            default:
                System.out.println("Invalid input");
                return; //ignores invalid input
        }

        // Checks if the new position is within grid bounds
        if (newX < 0 || newX >= GRID_SIZE_X || newY < 0 || newY >= GRID_SIZE_Y) {
            isGameOver = true;
            return;
        }

        //Checks if the move is valid using the isValidMove method
        if (!isValidMove(newX, newY)) {
            isGameOver = true;
            return;
        }

        //checks if the snake eats food, if so increases snake length and generates new food
        if (newX == foodX && newY == foodY) {
            snakeLength++;
            generateFood();
        }
        //updates the snakes position using the method
        updateSnakePosition(newX, newY);
    }

    //Generates food at a random empty position on the game grid
    public static void generateFood(){
        Random random = new Random();
        do{
            foodX = random.nextInt(GRID_SIZE_X-2 )+1; //food must be inside walls
            foodY = random.nextInt(GRID_SIZE_Y-2)+1; //same but Y coordinate for walls
        } while (grid[foodY][foodX] != EMPTY_CHAR); // ensures food is placed on an empty spot

        grid[foodY][foodX]=FOOD_CHAR; //places food on grid
    }
        //main method to play the game
        public static void main (String[]args){
            //initialize game
            Scanner scanner = new Scanner(System.in); //for player input
            initializeGame();
            displayGrid();


            //main game loop
            while (!isGameOver) {
                System.out.print("Enter: w/a/s/d/q");
                char move = scanner.next().charAt(0); //player input
                moveSnake(move); // updates snakes position based on input
                displayGrid(); // displays updated game grid
            }

            //game over message
            System.out.println("Game Over!");

            scanner.close(); //closes scanner

        }}
