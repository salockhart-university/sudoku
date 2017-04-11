package ca.alexlockhart.a2;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by lockhart on 2017-02-01.
 */

public class SudokuGridAdaptor extends BaseAdapter {

    LayoutInflater inflater;
    private Context context;
    private Sudoku items;

    public SudokuGridAdaptor(Context context, Sudoku items) {
        this.context = context;
        this.items = items;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.cell, null);
        }
        TextView t = (TextView) convertView.findViewById(R.id.grid_item);
        t.setText(items.getAtPosition(position));

        int row = Sudoku.getRow(position);
        int column = Sudoku.getColumn(position);

        switch (row % 3) {
            case 0:
                switch (column % 3) {
                    case 0:
                        t.setBackgroundResource(R.drawable.border_topleft);
                        break;
                    case 1:
                        t.setBackgroundResource(R.drawable.border_topmiddle);
                        break;
                    case 2:
                        t.setBackgroundResource(R.drawable.border_topright);
                        break;
                }
                break;
            case 1:
                switch (column % 3) {
                    case 0:
                        t.setBackgroundResource(R.drawable.border_middleleft);
                        break;
                    case 1:
                        t.setBackgroundResource(R.drawable.border_middlemiddle);
                        break;
                    case 2:
                        t.setBackgroundResource(R.drawable.border_middleright);
                        break;
                }
                break;
            case 2:
                switch (column % 3) {
                    case 0:
                        t.setBackgroundResource(R.drawable.border_bottomleft);
                        break;
                    case 1:
                        t.setBackgroundResource(R.drawable.border_bottommiddle);
                        break;
                    case 2:
                        t.setBackgroundResource(R.drawable.border_bottomright);
                        break;
                }
                break;
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return items.length();
    }

    @Override
    public Object getItem(int position) {
        return items.getAtPosition(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public GradientDrawable backgroundWithoutBorder(int radius) {
        GradientDrawable gdDefault = new GradientDrawable();
        gdDefault.setColor(context.getResources().getColor(R.color.puzzle_dark));
        return gdDefault;
    }
}
