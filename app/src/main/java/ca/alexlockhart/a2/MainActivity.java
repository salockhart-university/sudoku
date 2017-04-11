package ca.alexlockhart.a2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    final int HIGHLIGHT_TRANSITION_LENGTH = 500;

    public SudokuGenerator sudokuGenerator;
    public GridView gridView;
    public Sudoku sudoku;
    public List<Integer> initPositions;
    public HashMap<Integer, Handler> currentHighlights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sudokuGenerator = new SudokuGenerator();

        gridView = (GridView) this.findViewById(R.id.sudoku);

        initPositions = new ArrayList<>();

        currentHighlights = new HashMap<>();

        newGame();
    }

    public void set(View view) {
        final String label = ((Button) view).getText().toString();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (checkForErrors(position, label)) {
                    return;
                }

                sudoku.setPosition(position, label.toLowerCase().equals("clear") ? "" : label);
                updateGrid();

                if (sudoku.gameIsComplete()) {
                    displayDialog();
                }
            }
        });
    }

    public void newGame() {
        sudoku = new Sudoku(sudokuGenerator.getRandomGame());

        initPositions = sudoku.getInitPositions();

        updateGrid();
    }

    public void newGame(View view) {
        newGame();
    }

    public void resetGame() {
        sudoku = new Sudoku(sudokuGenerator.getLastGame());
        updateGrid();
    }

    public void resetGame(View view) {
        resetGame();
    }

    void updateGrid() {
        SudokuGridAdaptor gridAdaptor = new SudokuGridAdaptor(MainActivity.this, sudoku);
        gridView.setAdapter(gridAdaptor);
    }

    public void highlightElement(final int position) {
        LinearLayout container = (LinearLayout) gridView.getChildAt(position);
        TextView element = (TextView) container.findViewById(R.id.grid_item);
        final TransitionDrawable transition = (TransitionDrawable) element.getBackground();
        if (currentHighlights.containsKey(position)) {
            currentHighlights.get(position).removeCallbacksAndMessages(null);
            transition.resetTransition();
            currentHighlights.remove(position);
        }
        Handler handler = new Handler();
        currentHighlights.put(position, handler);
        transition.startTransition(HIGHLIGHT_TRANSITION_LENGTH);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                transition.reverseTransition(HIGHLIGHT_TRANSITION_LENGTH);
                currentHighlights.remove(position);
            }
        }, HIGHLIGHT_TRANSITION_LENGTH);
    }

    boolean checkForErrors(int position, String label) {
        boolean isError = false;

        if (initPositions.contains(position)) {
            Log.d("Placement", "Can't, initial value");
            highlightElement(position);
            isError = true;
        }

        List<String> rowContaining = sudoku.getRowContaining(position);
        if (rowContaining.contains(label)) {
            Log.d("Placement", "Can't, row contains value already");
            int dupIndex = rowContaining.indexOf(label);
            highlightElement(sudoku.getPosition(dupIndex, sudoku.getRow(position)));
            isError = true;
        }

        List<String> columnContaining = sudoku.getColumnContaining(position);
        if (columnContaining.contains(label)) {
            Log.d("Placement", "Can't, column contains value already");
            int dupIndex = columnContaining.indexOf(label);
            highlightElement(sudoku.getPosition(sudoku.getColumn(position), dupIndex));
            isError = true;
        }

        List<String> squareContaining = sudoku.getSquareContaining(position);
        if (squareContaining.contains(label)) {
            Log.d("Placement", "Can't, square contains value already");
            int dupIndex = squareContaining.indexOf(label);
            int column = sudoku.getColumn(position) - (sudoku.getColumn(position) % 3) + (dupIndex % 3);
            int row = sudoku.getRow(position) - (sudoku.getRow(position) % 3) + (dupIndex / 3);
            highlightElement(sudoku.getPosition(column, row));
            isError = true;
        }

        return isError;
    }

    void displayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Game Complete!")
                .setPositiveButton("Start Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        newGame();
                        dialog.dismiss();
                    }
                });
        final AlertDialog dialog = builder.create();
        dialog.show();
    }
}
