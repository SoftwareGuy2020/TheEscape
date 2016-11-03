package edu.orangecoastcollege.escapethecatcher;

public class Zombie {

    private int mRow;
    private int mCol;

    public Zombie(int row, int column) {
        mRow = row;
        mCol = column;
    }

    public void move(int[][] gameBoard, int playerCol, int playerRow) {
        if (mCol < playerCol && gameBoard[mRow][mCol + 1] == BoardCodes.EMPTY) {
            mCol++;
        } else if (mCol > playerCol && gameBoard[mRow][mCol - 1] == BoardCodes.EMPTY) {
            mCol--;
        } else if (mRow < playerRow && gameBoard[mRow + 1][mCol] == BoardCodes.EMPTY) {
            mRow++;
        } else if (mRow > playerRow && gameBoard[mRow - 1][mCol] == BoardCodes.EMPTY) {
            mRow--;
        }
    }

    public void setRow(int row) {
        mRow = row;
    }

    public int getRow() {
        return mRow;
    }

    public void setCol(int col) {
        mCol = col;
    }

    public int getCol() {
        return mCol;
    }

}
