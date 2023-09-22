import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import Model.Board;
import Model.PieceType;
import Model.Player;
import Model.PlayingPieceO;
import Model.PlayingPieceX;

public class TicTacToe {
    Deque<Player> players;
    Board gameBoard;
    Scanner inputScanner = new Scanner(System.in);
    TicTacToe(){
        initializeGame();
    }

    public void initializeGame(){
        //creating 2 Players
        players = new LinkedList<>();
        System.out.print("Enter Player 1 Name: ");
        String name1 = inputScanner.nextLine();
        //We can take input from user to set the choise piece
        PlayingPieceX crossPiece = new PlayingPieceX();
        Player player1 = new Player(name1, crossPiece);
        System.out.print("Enter Player 2 Name: ");
        String name2 = inputScanner.nextLine();
        PlayingPieceO nougthPiece = new PlayingPieceO();
        Player player2 = new Player(name2, nougthPiece);
        //inputScanner.close();
        players.add(player1);
        players.add(player2);
        //We can take input from user to set the board size.
        //initializing board
        gameBoard = new Board(3);

    }

    public String startGame(){
        boolean noWinner = true;
        while(noWinner){
            Player playerTurn = players.removeFirst();
            gameBoard.printBoard();

            List<int[]> freeSpaces = gameBoard.getFreeCells();
            if(freeSpaces.isEmpty()){
                noWinner = false;
                continue;
            }
            //Read the user input
            System.out.print("Player: " + playerTurn.getName() + " Enter row,column: ");
            String s = inputScanner.nextLine();
            //inputScanner.close();
            String[] values = s.split(",");
            if(!checkInput(values)){
                System.out.println("Incorrect input");
                players.addFirst(playerTurn);
                continue;
            }
            int inputRow = Integer.valueOf(values[0]);
            int inputColumn = Integer.valueOf(values[1]);
            //Place the piece
            boolean pieceAddedSuccessfully = gameBoard.addPiece(inputRow, inputColumn, playerTurn.getPlayingPiece());
            if(!pieceAddedSuccessfully){
                System.out.println("Incorrect position chosen, try again");
                players.addFirst(playerTurn);
                continue;
            }
            players.addLast(playerTurn);
            boolean winner = isThereWinner(inputRow,inputColumn,playerTurn.getPlayingPiece().pieceType);
            if(winner){
                return playerTurn.getName();
            }
        }
        return "tie";
    }

    public boolean isThereWinner(int row, int column, PieceType pieceType){
        boolean rowMatch = true;
        boolean columnMatch = true;
        boolean diagonalMatch = true;
        boolean antiDiagonalMatch = true;

        for(int i =0; i < gameBoard.size; i++){
            if(gameBoard.board[row][i] == null || gameBoard.board[row][i].pieceType != pieceType){
                rowMatch = false;
                break;
            }
        }
        for(int i =0; i < gameBoard.size; i++){
            if(gameBoard.board[i][column] == null || gameBoard.board[i][column].pieceType != pieceType){
                columnMatch = false;
            }
        }
        for(int i =0,j=0; i < gameBoard.size; i++,j++){
            if(gameBoard.board[i][j] == null || gameBoard.board[i][j].pieceType != pieceType){
                diagonalMatch = false;
            }
        }
        for(int i =0,j=gameBoard.size-1; i < gameBoard.size; i++,j--){
            if(gameBoard.board[i][j] == null || gameBoard.board[i][j].pieceType != pieceType){
                antiDiagonalMatch = false;
            }
        }
        
        return rowMatch || columnMatch || diagonalMatch || antiDiagonalMatch;
    }
    boolean checkInput(String[] values){
        if (values.length!=2){
            return false;
        }
        try {
            Integer.valueOf(values[0]);
            Integer.valueOf(values[1]);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
