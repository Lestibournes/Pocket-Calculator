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

import org.junit.Test;

import java.text.ParseException;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BasicCalculatorUnitTest {
    /**
     * Helper function for testing if Calculator.calculate throws an exception for the given input.
     * @param input a string representing a desired calculation
     * @return true if ParseException thrown. False if no exception is thrown.
     */
    private boolean throwsParseException(String input) {
        BasicCalculator calculator = new BasicCalculator();
        
        try {
            calculator.calculate(input);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    /**
     * Testing that basic operations work.
     * @throws ParseException
     */
    @Test
    public void simple_operations() throws ParseException {
        BasicCalculator calculator = new BasicCalculator();

        assert calculator.calculate("5") == 5;
        assert calculator.calculate("-5") == -5;
        assert calculator.calculate("5+5") == 10;
        assert calculator.calculate("5-5") == 0;
        assert calculator.calculate("5*5") == 25;
        assert calculator.calculate("5/5") == 1;
        assert calculator.calculate("5^3") == 125;
    }

    /**
     * Testing order of operations.
     * @throws ParseException
     */
    @Test
    public void moderate_operations() throws ParseException {
        BasicCalculator calculator = new BasicCalculator();

        assert calculator.calculate("54-6/3*2+9") == 59;
        assert calculator.calculate("12/2/2") == 3;
        assert calculator.calculate("100/2^3*2") == 25;
    }

    /**
     * Testing calculations involving negative numbers.
     * @throws ParseException
     */
    @Test
    public void operations_with_negative() throws ParseException {
        BasicCalculator calculator = new BasicCalculator();

        //Minus only in the first number:
        assert calculator.calculate("-5+5") == 0;
        assert calculator.calculate("-5*5") == -25;
        assert calculator.calculate("-5/5") == -1;
        assert calculator.calculate("-5^3") == -125;

        //Minus only in the second number:
        assert calculator.calculate("5+-5") == 0;
        assert calculator.calculate("5--5") == 10;
        assert calculator.calculate("5*-5") == -25;
        assert calculator.calculate("5/-5") == -1;
        assert calculator.calculate("5^-3") == 1.0/125.0;

        //Minus in both numbers:
        assert calculator.calculate("-5+-5") == -10;
        assert calculator.calculate("-5-5") == -10;
        assert calculator.calculate("-5--5") == 0;
        assert calculator.calculate("-5*-5") == 25;
        assert calculator.calculate("-5/-5") == 1;
        assert calculator.calculate("-5^-3") == 1.0/(-125.0);

//        This test returns false. It appears to be a problem with Math.pow(),
//        since I do pass -12.5 and 1.5 as parameters, so my code parsed it and passed it along correctly.
//        From the documentation of Math.pow():
//        If the first argument is finite and less than zero if the second argument is finite and not an integer, then the result is NaN.
//        assert new BigDecimal(calculator.calculate("-12.5^1.5")) == new BigDecimal(-44.1941738241592202750527726315530649553);
    }

    /**
     * Testing divide by zero.
     * @throws ParseException
     * @throws ArithmeticException
     */
    @Test(expected=ArithmeticException.class)
    public void divide_by_zero() throws ParseException, ArithmeticException {
        BasicCalculator calculator = new BasicCalculator();

        calculator.calculate("5/0");
    }

    /**
     * Testing invalid inputs, including invalid characters and invalid combinations of valid characters.
     */
    @Test
    public void invalid_inputs() {
        BasicCalculator calculator = new BasicCalculator();

        assert throwsParseException("");

        assert throwsParseException("+");
        assert throwsParseException("-");
        assert throwsParseException("*");
        assert throwsParseException("/");
        assert throwsParseException("^");
        assert throwsParseException(".");

        assert throwsParseException("+5");
        assert throwsParseException("*5");
        assert throwsParseException("/5");
        assert throwsParseException("^3");

        assert throwsParseException("5+");
        assert throwsParseException("5-");
        assert throwsParseException("5*");
        assert throwsParseException("5/");
        assert throwsParseException("5^");

        assert throwsParseException("5++5");
        assert throwsParseException("5+*5");
        assert throwsParseException("5+*5");
        assert throwsParseException("5+^3");

        assert throwsParseException("5-+5");
        assert throwsParseException("5-*5");
        assert throwsParseException("5-/5");
        assert throwsParseException("5-^3");

        assert throwsParseException("5*+5");
        assert throwsParseException("5**5");
        assert throwsParseException("5*/5");
        assert throwsParseException("5*^3");

        assert throwsParseException("5/+5");
        assert throwsParseException("5/*5");
        assert throwsParseException("5//5");
        assert throwsParseException("5/^3");

        assert throwsParseException("a");

        assert throwsParseException("a+");
        assert throwsParseException("a-");
        assert throwsParseException("a*");
        assert throwsParseException("a/");
        assert throwsParseException("a^");

        assert throwsParseException("+a");
        assert throwsParseException("-a");
        assert throwsParseException("*a");
        assert throwsParseException("/a");
        assert throwsParseException("^a");

        assert throwsParseException("a+b");
        assert throwsParseException("a-b");
        assert throwsParseException("a*b");
        assert throwsParseException("a/b");
        assert throwsParseException("a^b");

        assert throwsParseException("a+5");
        assert throwsParseException("a-5");
        assert throwsParseException("a*5");
        assert throwsParseException("a/5");
        assert throwsParseException("a^5");

        assert throwsParseException("#+5");
        assert throwsParseException("%-5");
        assert throwsParseException("&*5");
        assert throwsParseException("!/5");
        assert throwsParseException("~^5");
    }
}