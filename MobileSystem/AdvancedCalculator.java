package MobileSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Stack;

public class AdvancedCalculator {
    private JFrame frame;
    private JTextField display;
    private ArrayList<String> history;

    public AdvancedCalculator() {
        frame = new JFrame("Advanced Calculator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 600);

        display = new JTextField();
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Arial", Font.PLAIN, 24));
        display.setEditable(false);
        display.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        display.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (Character.isDigit(c) || "+-*/^().".indexOf(c) >= 0) {
                    display.setText(display.getText() + c);
                } else if (c == '\n') {
                    calculate();
                }
            }
        });

        history = new ArrayList<>();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 4, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] buttons = {
                "C", "Back", "History", "/",
                "7", "8", "9", "*",
                "4", "5", "6", "-",
                "1", "2", "3", "+",
                "0", ".", "^", "=",
                "(", ")", "Bin", "Hex"
        };

        for (String text : buttons) {
            JButton button = new JButton(text);
            button.setFont(new Font("Arial", Font.PLAIN, 20));
            button.addActionListener(new ButtonClickListener());
            panel.add(button);
        }

        frame.setLayout(new BorderLayout());
        frame.add(display, BorderLayout.NORTH);
        frame.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = ((JButton) e.getSource()).getText();

            switch (command) {
                case "C":
                    display.setText("");
                    break;
                case "Back":
                    String current = display.getText();
                    if (!current.isEmpty()) {
                        display.setText(current.substring(0, current.length() - 1));
                    }
                    break;
                case "History":
                    showHistory();
                    break;
                case "Bin":
                    showBinary();
                    break;
                case "Hex":
                    showHexadecimal();
                    break;
                case "=":
                    calculate();
                    break;
                default:
                    if (isValidInput(command)) {
                        display.setText(display.getText() + command);
                    }
                    break;
            }
        }
    }

    private boolean isValidInput(String input) {
        if (input.matches("[0-9+\\-*/^().]")) return true;
        return false;
    }

    private void calculate() {
        try {
            String expression = display.getText();
            if (expression.isEmpty()) return;
            double result = evaluate(expression);
            String formattedResult = String.format("%.4f", result).replaceAll("\\.?0+$", "");
            history.add(expression + " = " + formattedResult);
            display.setText(formattedResult);
        } catch (Exception e) {
            display.setText("Error");
        }
    }

    private double evaluate(String expression) {
        expression = expression.replace(" ", "");
        return evaluateExpression(expression);
    }

    private double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int i = 0;

        while (i < expression.length()) {
            char ch = expression.charAt(i);

            if (ch == '-' && (i == 0 || expression.charAt(i - 1) == '(')) {
                StringBuilder sb = new StringBuilder("-");
                i++;
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                numbers.push(Double.parseDouble(sb.toString()));
                continue;
            }

            if (Character.isDigit(ch) || ch == '.') {
                StringBuilder sb = new StringBuilder();
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    sb.append(expression.charAt(i++));
                }
                numbers.push(Double.parseDouble(sb.toString()));
                continue;
            }

            if (ch == '(') {
                operators.push(ch);
            } else if (ch == ')') {
                while (!operators.isEmpty() && operators.peek() != '(') {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                if (!operators.isEmpty()) operators.pop();
            } else if (isOperator(ch)) {
                while (!operators.isEmpty() && precedence(ch) <= precedence(operators.peek())) {
                    numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                }
                operators.push(ch);
            }
            i++;
        }

        while (!operators.isEmpty()) {
            numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
        }

        return numbers.pop();
    }

    private boolean isOperator(char ch) {
        return "+-*/^".indexOf(ch) != -1;
    }

    private int precedence(char op) {
        switch (op) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
        }
        return -1;
    }

    private double applyOperation(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return b != 0 ? a / b : 0;
            case '^': return Math.pow(a, b);
        }
        return 0;
    }

    private void showBinary() {
        try {
            int value = (int) Double.parseDouble(display.getText());
            String binary = Integer.toBinaryString(value);
            display.setText(binary);
        } catch (Exception e) {
            display.setText("Error");
        }
    }

    private void showHexadecimal() {
        try {
            int value = (int) Double.parseDouble(display.getText());
            String hex = Integer.toHexString(value).toUpperCase();
            display.setText(hex);
        } catch (Exception e) {
            display.setText("Error");
        }
    }

    private void showHistory() {
        JFrame historyFrame = new JFrame("Calculation History");
        historyFrame.setSize(300, 400);

        JTextArea historyArea = new JTextArea();
        historyArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        historyArea.setEditable(false);
        history.forEach(h -> historyArea.append(h + "\n"));

        historyFrame.add(new JScrollPane(historyArea));
        historyFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AdvancedCalculator::new);
    }
}

