/*
    Pocket Calculator - An Android calculator app
    Copyright (C) 2020 Gerald Isaac Schwarz

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License version 2 only, as published by
    the Free Software Foundation.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.yitzchak.pocketcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;

public class MainActivity extends AppCompatActivity {
    private TextView display;
    private String answer = ""; //Stores the result of the previous calculation.
    private boolean clear = false; //Whether to clear the display on next input. Set to true after each result is displayed.

    public CalculatorInterface calculator = new BasicCalculator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.display = findViewById(R.id.input);

        if (savedInstanceState != null) {
            display.setText(savedInstanceState.getString("input"));
            answer = savedInstanceState.getString("answer");
            clear = savedInstanceState.getBoolean("clear");
        }

        findViewById(R.id.button_delete).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                display.setText("");
                return true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("input", display.getText().toString());
        bundle.putString("answer", answer);
        bundle.putBoolean("clear", clear);
    }

    /**
     * Adds the input string to the display. If clear == true then it will clear the screen first.
     * @param str The string to be added to the display.
     */
    private void append(String str) {
        if (clear) {
            display.setText(str);
            clear = false;
        }
        else {
            display.append(str);
        }
    }

    public void solve() {
        //Perform the calculation and display the result. If there is a problem, show the user an error message.
        try {
            double result = calculator.calculate(display.getText().toString());
            answer = String.valueOf(result); //Save the result to allow the user to easily paste it into the next calculation.
            clear = true; //When the user starts inputting a new calculation, clear the screen first.

            display.setText(calculator.getDecimalFormat(result).format(result));

        } catch (ParseException e) {
            Toast.makeText(MainActivity.this, "Invalid Expression", Toast.LENGTH_LONG).show();
        } catch (ArithmeticException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void input(String input) {
        String digits = "" + this.getText(R.string.zero) + this.getText(R.string.one) + this.getText(R.string.two) +
                this.getText(R.string.three) + this.getText(R.string.four) + this.getText(R.string.five) +
                this.getText(R.string.six) + this.getText(R.string.seven) + this.getText(R.string.eight) +
                this.getText(R.string.nine);

        String operators1 = "" + this.getText(R.string.plus) + this.getText(R.string.times) +
                this.getText(R.string.divide) + this.getText(R.string.power);

        String operators2 = "" + this.getText(R.string.minus) + this.getText(R.string.dot);

        if (input.length() == 1 && digits.contains(input)) {
            append(input);
        }
        else if (input.length() == 1 && operators1.contains(input)) {
            //If after a calculation the user wants to continue to calculate directly with the answer, allow it:
            if (clear) {
                clear = false;
            }

            //Don't allow a sign of an operation to be the first input. Input must start with a number.
            if (display.getText().length() > 0) {
                append(input);
            }
        }
        else if (input.length() == 1 && operators2.contains(input)) {
            //If after a calculation the user wants to continue to calculate directly with the answer, allow it:
            if (clear) {
                clear = false;
            }

            append(input);
        }
        else if (input.equalsIgnoreCase(this.getString(R.string.clear))) {
            //Clear the input
            display.setText("");
        }
        else if (input.equalsIgnoreCase(this.getString(R.string.delete))) {
            //Backspace
            if (display.getText().length() > 0) {
                display.setText(display.getText().subSequence(0, display.getText().length()-1));
                clear = false;
            }
        }
        else if (input.equalsIgnoreCase(this.getString(R.string.answer))) {
            append(answer);
        }
        else if (input.equalsIgnoreCase(this.getString(R.string.equals))) {
            solve();
        }
    }

    public void clickListener(View v) {
        input(((Button) v).getText().toString());
    }
}