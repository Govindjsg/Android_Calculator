package com.govind05041994.android_calculator;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private TextView screen;
    private String display = "";
    private EditText inputtext;
    private TextView displaytext;
    private String currentOperator = "";
    private String result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton deletevar = findViewById(R.id.butdelet);
        deletevar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletenumber();
            }
        });

        screen = findViewById(R.id.input_box);
        screen.setText(display);
        inputtext = findViewById(R.id.input_box);
        displaytext = findViewById(R.id.result_box);
    }

    private void appendToLast(String str) {
        Editable editable = inputtext.getText();
        int start = Math.max(inputtext.getSelectionStart(), 0);
        int end = Math.max(inputtext.getSelectionEnd(), 0);
        editable.replace(start, end, str);
    }

    public void onClickNumber(View v) {
        Button b = (Button) v;
        display += b.getText();
        appendToLast(display);
        display = "";
    }

    public void onClickOperator(View v) {
        Button b = (Button) v;
        display += b.getText();
        if (endsWithOperator()) {
            replace(display);
            currentOperator = b.getText().toString();
            display = "";
        } else {
            appendToLast(display);
            currentOperator = b.getText().toString();
            display = "";
        }
    }

    public void onClearButton(View v) {
        inputtext.getText().clear();
        displaytext.setText("");
    }

    public void deletenumber() {
        Editable editable = inputtext.getText();
        int start = Math.max(inputtext.getSelectionStart(), 0);
        int end = Math.max(inputtext.getSelectionEnd(), 0);
        if (start > 0 && start <= end && end <= editable.length()) {
            editable.delete(start - 1, end);
        }
    }

    private String getinput() {
        return this.inputtext.getText().toString();
    }

    private boolean endsWithOperator() {
        String input = getinput();
        return input.endsWith("+") || input.endsWith("-") || input.endsWith("/") || input.endsWith("x");
    }

    private void replace(String str) {
        Editable editable = inputtext.getText();
        int start = Math.max(inputtext.getSelectionStart(), 0);
        int end = Math.max(inputtext.getSelectionEnd(), 0);
        if (start > 0 && start <= end && end <= editable.length()) {
            editable.replace(start - 1, end, str);
        }
    }

    private double operate(String a, String b, String cp) {
        switch (cp) {
            case "+":
                return Double.valueOf(a) + Double.valueOf(b);
            case "-":
                return Double.valueOf(a) - Double.valueOf(b);
            case "x":
                return Double.valueOf(a) * Double.valueOf(b);
            case "\u00F7":
                return Double.valueOf(a) / Double.valueOf(b);
            default:
                return -1;
        }
    }

    public void equalresult(View v) {
        String input = getinput();

        if (!endsWithOperator()) {
            if (input.contains("x")) {
                input = input.replaceAll("x", "*");
            }

            if (input.contains("\u00F7")) {
                input = input.replaceAll("\u00F7", "/");
            }

            Expression expression = new ExpressionBuilder(input).build();
            try {
                double result = expression.evaluate();
                displaytext.setText(String.valueOf(result));
            } catch (ArithmeticException ex) {
                // Handle arithmetic exceptions if necessary
                displaytext.setText("Error");
            }
        } else {
            displaytext.setText("");
        }
    }
}
