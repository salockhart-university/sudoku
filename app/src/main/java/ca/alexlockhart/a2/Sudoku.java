package ca.alexlockhart.a2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lockhart on 2017-02-15.
 */

public class Sudoku {

    public List<Integer> initPositions;
    private String[] items;

    public Sudoku(String[] items) {
        this.items = items;
        initPositions = new ArrayList<>();
        for (int i = 0; i < items.length; i++) {
            if (!items[i].equals("")) {
                initPositions.add(i);
            }
        }
    }

    public int length() {
        return items.length;
    }

    public List<Integer> getInitPositions() {
        return initPositions;
    }

    boolean gameIsComplete() {
        return !Arrays.asList(items).contains("");
    }

    public int getPosition(int column, int row) {
        return row * 9 + column;
    }

    public void setPosition(int position, String value) {
        items[position] = value;
    }

    public String getAtPosition(int position) {
        return items[position];
    }

    public static int getRow(int position) {
        return (position - (position % 9)) / 9;
    }

    public static int getColumn(int position) {
        return position % 9;
    }

    public List<String> getRowContaining(int position) {
        int rowPosition = getRow(position) * 9;
        return Arrays.asList(items).subList(rowPosition, rowPosition + 9);
    }

    public List<String> getColumnContaining(int position) {
        int column = getColumn(position);
        List<String> list = new ArrayList<>();
        for (int i = column; i < items.length; i += 9) {
            list.add(items[i]);
        }
        return list;
    }

    public List<String> getSquareContaining(int position) {
        // Column and row of the requested field
        int column = getColumn(position);
        int row = getRow(position);

        // Column and row of the top left square of the requested square
        int squareLeft = column - column % 3;
        int squareTop = row - row % 3;

        int start = getPosition(squareLeft, squareTop);

        List<String> list = new ArrayList<>();

        for (int i = 0; i < 19; i += 9) {
            for (int j = 0; j < 3; j++) {
                list.add(items[start + i + j]);
            }
        }

        return list;
    }
}
