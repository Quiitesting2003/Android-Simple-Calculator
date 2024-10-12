package com.example.calculatorapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText textInput;
    private String input = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textInput = findViewById(R.id.et_result);
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        int[] buttonIds = {

                R.id.buttonZero, R.id.buttonOne, R.id.buttonTwo, R.id.buttonThree,
                R.id.buttonFour, R.id.buttonFive, R.id.buttonSix, R.id.buttonSeven,
                R.id.buttonEight, R.id.buttonNine, R.id.buttonDecimalPoint,
                R.id.buttonAddition, R.id.buttonSubtraction,
                R.id.buttonMultiplication, R.id.buttonDivision,
                R.id.buttonAllClear, R.id.buttonEquals
        };

        for (int id : buttonIds) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        String buttonText = button.getText().toString();

        if (v.getId() == R.id.buttonEquals) {
            calculateResult();
        } else if (v.getId() == R.id.buttonAllClear) {
            clearAll();
        } else {
            input += buttonText;
            updateUI();

        }
    }

    private void calculateResult() {

        try {
            if (input.startsWith("-")) {
                input = input.substring(1);
                double result = -evaluate(input);
                input = formatResult(result);
            } else {
                double result = evaluate(input);
                input = formatResult(result);
            }
            updateUI();
        } catch (Exception e) {
            input = "Error";
            updateUI();
        }
    }

    private String formatResult(double result) {
        if (result == (int) result) {
            return String.valueOf((int) result);
        } else {
            return String.valueOf(result);
        }
    }


    private void clearAll() {
        input = "";
        updateUI();
    }

    private void updateUI() {
        textInput.setText(input);
    }

    private double evaluate(final String str) {
        String[] parts = str.split("(?<=[*/+-])|(?=[*/+-])");

        double result = 0;
        String operator = "+";
        double negativeValue = 0;
        boolean negativeCalculated = false;

        for (String part : parts) {
            if (part.equals("+") || part.equals("-") || part.equals("*") || part.equals("/")) {
                operator = part;
                if (negativeCalculated) {
                    result += negativeValue;
                    negativeCalculated = false;
                }

            } else {
                double operand = Double.parseDouble(part);
                switch (operator) {
                    case "+":
                        result += operand;
                        break;
                    case "-":
                        if (!negativeCalculated) {
                            negativeValue = operand;
                            negativeCalculated = true;
                            continue;
                        } else {
                            result -= operand;
                        }
                        break;
                    case "*":
                        result *= operand;
                        break;
                    case "/":
                        if (operand == 0) throw new ArithmeticException("Divide by zero!");
                        result /= operand;
                        break;
                }
            }
        }

        if (negativeCalculated) {
            result += negativeValue;
        }
        return result;
    }
}