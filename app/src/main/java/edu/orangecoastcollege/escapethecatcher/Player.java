package edu.orangecoastcollege.escapethecatcher;

public class Player {
    private int mRow;
    private int mCol;

    public Player(int row, int column) {
        mRow = row;
        mCol = column;
    }

    public void move(int[][] gameBoard, String button) {

        if (button.equals("RIGHT")) {
            if (gameBoard[mRow][mCol + 1] != BoardCodes.OBSTACLE) {
                mCol++;
            }
        } else if (button.equals("LEFT")) {
            if (gameBoard[mRow][mCol - 1] != BoardCodes.OBSTACLE)
                mCol--;
        } else if (button.equals("UP")) {
            if (gameBoard[mRow - 1][mCol] != BoardCodes.OBSTACLE) {
                mRow--;
            }
        } else if (button.equals("DOWN")) {
            if (gameBoard[mRow + 1][mCol] != BoardCodes.OBSTACLE) {
                mRow++;
            }
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
