package com.example.advancedcalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private TextView display;
    private double firstNumber = 0;
    private String operator = "";
    private boolean isNewNumber = true;
    private double memory = 0;
    private DecimalFormat df = new DecimalFormat("#.##########");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
        setupNumberButtons();
        setupOperatorButtons();
        setupMemoryButtons();
        setupScientificButtons();
    }

    private void setupNumberButtons() {
        int[] numberIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                          R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        
        for (int id : numberIds) {
            Button button = findViewById(id);
            button.setOnClickListener(v -> {
                if (isNewNumber) {
                    display.setText(((Button) v).getText());
                    isNewNumber = false;
                } else {
                    display.append(((Button) v).getText());
                }
            });
        }
    }

    private void setupOperatorButtons() {
        findViewById(R.id.btnPlus).setOnClickListener(v -> performOperation("+"));
        findViewById(R.id.btnMinus).setOnClickListener(v -> performOperation("-"));
        findViewById(R.id.btnMultiply).setOnClickListener(v -> performOperation("×"));
        findViewById(R.id.btnDivide).setOnClickListener(v -> performOperation("÷"));
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculateResult());
        findViewById(R.id.btnClear).setOnClickListener(v -> clearCalculator());
        findViewById(R.id.btnDecimal).setOnClickListener(v -> addDecimal());
        findViewById(R.id.btnPercent).setOnClickListener(v -> calculatePercent());
        findViewById(R.id.btnBackspace).setOnClickListener(v -> backspace());
    }

    private void setupMemoryButtons() {
        findViewById(R.id.btnMC).setOnClickListener(v -> memory = 0);
        findViewById(R.id.btnMR).setOnClickListener(v -> {
            display.setText(df.format(memory));
            isNewNumber = true;
        });
        findViewById(R.id.btnMPlus).setOnClickListener(v -> {
            memory += Double.parseDouble(display.getText().toString());
        });
        findViewById(R.id.btnMMinus).setOnClickListener(v -> {
            memory -= Double.parseDouble(display.getText().toString());
        });
    }

    private void setupScientificButtons() {
        findViewById(R.id.btnSin).setOnClickListener(v -> {
            double value = Math.sin(Math.toRadians(Double.parseDouble(display.getText().toString())));
            display.setText(df.format(value));
            isNewNumber = true;
        });

        findViewById(R.id.btnCos).setOnClickListener(v -> {
            double value = Math.cos(Math.toRadians(Double.parseDouble(display.getText().toString())));
            display.setText(df.format(value));
            isNewNumber = true;
        });

        findViewById(R.id.btnTan).setOnClickListener(v -> {
            double value = Math.tan(Math.toRadians(Double.parseDouble(display.getText().toString())));
            display.setText(df.format(value));
            isNewNumber = true;
        });

        findViewById(R.id.btnSqrt).setOnClickListener(v -> {
            double value = Math.sqrt(Double.parseDouble(display.getText().toString()));
            display.setText(df.format(value));
            isNewNumber = true;
        });
    }

    private void performOperation(String op) {
        firstNumber = Double.parseDouble(display.getText().toString());
        operator = op;
        isNewNumber = true;
    }

    private void calculateResult() {
        if (operator.isEmpty()) return;
        
        double secondNumber = Double.parseDouble(display.getText().toString());
        double result = 0;

        switch (operator) {
            case "+": result = firstNumber + secondNumber; break;
            case "-": result = firstNumber - secondNumber; break;
            case "×": result = firstNumber * secondNumber; break;
            case "÷": result = firstNumber / secondNumber; break;
        }

        display.setText(df.format(result));
        operator = "";
        isNewNumber = true;
    }

    private void clearCalculator() {
        display.setText("0");
        firstNumber = 0;
        operator = "";
        isNewNumber = true;
    }

    private void addDecimal() {
        if (!display.getText().toString().contains(".")) {
            display.append(".");
        }
    }

    private void calculatePercent() {
        double value = Double.parseDouble(display.getText().toString());
        display.setText(df.format(value / 100));
        isNewNumber = true;
    }

    private void backspace() {
        String currentText = display.getText().toString();
        if (currentText.length() > 1) {
            display.setText(currentText.substring(0, currentText.length() - 1));
        } else {
            display.setText("0");
            isNewNumber = true;
        }
    }
} 