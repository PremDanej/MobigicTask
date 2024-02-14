package com.merp.mobigic.task.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.merp.mobigic.task.R;

public class WordSearchActivity extends AppCompatActivity {
    private int rows, cols;
    private EditText[][] editTextGrid;
    private GridLayout gridLayout;
    private EditText searchEditText ,rowsInput, colsInput;
    private Button searchButton, resetButton, createGridBtn;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_search);

        rowsInput =         findViewById(R.id.rowsInput);
        colsInput =         findViewById(R.id.colsInput);
        createGridBtn =     findViewById(R.id.createGridBtn);
        gridLayout =        findViewById(R.id.gridLayout);
        searchEditText =    findViewById(R.id.searchEditText);
        searchButton =      findViewById(R.id.searchButton);
        resetButton =       findViewById(R.id.resetButton);
        resultTextView =    findViewById(R.id.resultTextView);

        searchButton.setOnClickListener(v -> searchWord());

        createGridBtn.setOnClickListener(v -> {
            String rowsStr = rowsInput.getText().toString();
            String colsStr = colsInput.getText().toString();

            if (TextUtils.isEmpty(rowsStr) || TextUtils.isEmpty(colsStr)) {
                Toast.makeText(this, "Please enter valid rows and columns", Toast.LENGTH_SHORT).show();
                return;
            }
            rows = Integer.parseInt(rowsStr);
            cols = Integer.parseInt(colsStr);
            createEditTextGrid(rows, cols);
            rowsInput.clearFocus();
            colsInput.clearFocus();
        });

        resetButton.setOnClickListener(view -> {
            rowsInput.setText("");
            colsInput.setText("");
            rowsInput.clearFocus();
            colsInput.clearFocus();
            createEditTextGrid(0,0);
        });
    }

    private void createEditTextGrid(int rows, int cols) {
        editTextGrid = new EditText[rows][cols];
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(cols);
        gridLayout.setRowCount(rows);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                EditText editText = new EditText(this);
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
                editText.setSingleLine();
                editText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                editText.setHint("value");
                gridLayout.addView(editText);
                editTextGrid[i][j] = editText;
            }
        }
    }

    private void searchWord() {
        String searchText = searchEditText.getText().toString().trim().toLowerCase();
        if (TextUtils.isEmpty(searchText)) {
            Toast.makeText(this, "Please enter a word to search", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean wordFound = false;
        StringBuilder result = new StringBuilder();

        // Check each direction for the word
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (checkWord(i, j, searchText)) {
                    wordFound = true;
                    result.append("Word found at position: (").append(i).append(", ").append(j).append(")\n");
                }
            }
        }

        if (!wordFound) {
            Toast.makeText(this, "Word not found", Toast.LENGTH_SHORT).show();
        }

        resultTextView.setText(result.toString());
    }

    private boolean checkWord(int startRow, int startCol, String word) {
        boolean wordFound = false;

        // Check east direction
        if (!wordFound && startCol + word.length() <= cols) {
            for (int i = 0; i < word.length(); i++) {
                String cellText = editTextGrid[startRow][startCol + i].getText().toString().trim().toLowerCase();
                if (cellText.equals(String.valueOf(word.charAt(i)))) {
                    editTextGrid[startRow][startCol + i].setBackgroundResource(R.drawable.highlight_background);
                    wordFound = true;
                } else {
                    // Reset highlighting if any character does not match
                    for (int j = 0; j < i; j++) {
                        editTextGrid[startRow][startCol + j].setBackgroundResource(0);
                    }
                    wordFound = false;
                    break;
                }
            }
        }

        // Check south direction
        if (!wordFound && startRow + word.length() <= rows) {
            for (int i = 0; i < word.length(); i++) {
                String cellText = editTextGrid[startRow + i][startCol].getText().toString().trim().toLowerCase();
                if (cellText.equals(String.valueOf(word.charAt(i)))) {
                    editTextGrid[startRow + i][startCol].setBackgroundResource(R.drawable.highlight_background);
                    wordFound = true;
                } else {
                    // Reset highlighting if any character does not match
                    for (int j = 0; j < i; j++) {
                        editTextGrid[startRow + j][startCol].setBackgroundResource(0);
                    }
                    wordFound = false;
                    break;
                }
            }
        }

        // Check south-east direction
        if (!wordFound && startRow + word.length() <= rows && startCol + word.length() <= cols) {
            for (int i = 0; i < word.length(); i++) {
                String cellText = editTextGrid[startRow + i][startCol + i].getText().toString().trim().toLowerCase();
                if (cellText.equals(String.valueOf(word.charAt(i)))) {
                    editTextGrid[startRow + i][startCol + i].setBackgroundResource(R.drawable.highlight_background);
                    wordFound = true;
                } else {
                    // Reset highlighting if any character does not match
                    for (int j = 0; j < i; j++) {
                        editTextGrid[startRow + j][startCol + j].setBackgroundResource(0);
                    }
                    wordFound = false;
                    break;
                }
            }
        }

        // Check south-west direction
        if (!wordFound && startRow + word.length() <= rows && startCol - word.length() >= -1) {
            for (int i = 0; i < word.length(); i++) {
                String cellText = editTextGrid[startRow + i][startCol - i].getText().toString().trim().toLowerCase();
                if (cellText.equals(String.valueOf(word.charAt(i)))) {
                    editTextGrid[startRow + i][startCol - i].setBackgroundResource(R.drawable.highlight_background);
                    wordFound = true;
                } else {
                    // Reset highlighting if any character does not match
                    for (int j = 0; j < i; j++) {
                        editTextGrid[startRow + j][startCol - j].setBackgroundResource(0);
                    }
                    wordFound = false;
                    break;
                }
            }
        }

        return wordFound;
    }

}