package Model;
import java.util.ArrayList;
import java.util.List;

public class Board {
    public int size;
    public PlayingPiece[][]board;

    public Board(int size){
        this.size = size;
        board = new PlayingPiece[size][size];
    }

    public boolean addPiece(int row, int column, PlayingPiece playingPiece){
        if(row>=size || column >= size || board[row][column] != null){
            return false;
        }
        board[row][column] = playingPiece;
        return true;
    }

    public List<int[]> getFreeCells(){
        List<int[]> freeCells = new ArrayList<>();

        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                if(board[i][j] == null){
                    int[] rowColumn = new int[]{i,j};
                    freeCells.add(rowColumn);

                }
            }
        }
        return freeCells;
    }
    public void printBoard(){
        for (int i = 0; i < size; i++) {
            System.out.print("| ");
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] == null?" ":board[i][j].pieceType);
                if (j < size - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println(" |");
        }
    }
}
