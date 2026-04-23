package hu.nye.models;

import java.util.Arrays;

public class GameBoard {
    private final char [][] board;
    private final int rows;
    private final int columns;

    public GameBoard(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.board = new char[this.rows][this.columns];
        for(char[] row: this.board){
            Arrays.fill(row,'-');
        }
    }

    /**
     *
     * @param column    Az adott oszlop sorszáma
     * @param disc      A korong jelzése
     * @return          Elhelyezi az adott korongot validálás után, validásál eredményétől függően visszatér true/false-al
     */
    public boolean placeDisc(int column, char disc){
        //validálás
        if(column-1<0 || column-1 >= this.columns){
            return false;
        }

        for(int row = this.rows-1; row >= 0; row--){
            if(this.board[row][column-1] == '-'){
                this.board[row][column-1] = disc;
                return true;
            }
        }

        return false;
    }

    /**
     *
     * @param disc      Aktuálisan lerakott korong
     * @return          Leellenőrzi, hogy az adott lépés után  nyert-e a játékos és ennek megfelelően true/false-al tér vissza
     */
    public boolean checkWin(char disc){

        for(int row = 0; row < this.rows; row++){
            for(int col = 0; col < this.columns; col++){
                if (checkDirection(row, col, 1, 0, disc) || // horizontális -
                        checkDirection(row, col, 0, 1, disc) || // vertilális |
                        checkDirection(row, col, 1, 1, disc) || // átlós \
                        checkDirection(row, col, 1, -1, disc)) { // átlós /
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Segítő metódus, ellenőrzi, hogy egy adott irányban négy egymást követő korong megegyezik-e a megadott típussal.
     *
     * @param row   A kiinduló pozíció sora.
     * @param col   A kiinduló pozíció oszlopa.
     * @param dRow  Az irány sora mentén való elmozdulásának értéke.
     * @param dCol  Az irány oszlopa mentén való elmozdulásának értéke.
     * @param disc  Az ellenőrizendő korong típusa.
     * @return      true, ha az adott irányban négy egymást követő korong megegyezik;
     *              különben false.
     */
    private boolean checkDirection(int row, int col, int dRow, int dCol, char disc) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            int r = row + i * dRow;
            int c = col + i * dCol;
            if (r >= 0 && r < this.rows && c >= 0 && c < this.columns && this.board[r][c] == disc) {
                count++;
            } else {
                break;
            }
        }
        return count == 4;
    }

    /**
     * Kiírja az aktuális játéktáblát
     */
    public void printBoard() {
        for (char[] row : this.board) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     *  Betölt egy pályát egy karakter mátrixból
     * @param matrix Egy adott pálya
     */
    public void loadGameboard(char[][] matrix){
        for(int i = 0;i<matrix.length; i++){
            for(int j = 0; j< matrix[i].length; j++){
                this.board[i][j] = matrix[i][j];
            }
        }
    }

    public char[][] getBoard() {
        return board;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
