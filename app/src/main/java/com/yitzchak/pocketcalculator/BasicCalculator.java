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

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class BasicCalculator implements CalculatorInterface {
    public static final DecimalFormat regular = new DecimalFormat("#.########"), scientific = new DecimalFormat("#.########E0");

    /**
     * Parse the given input string into an array of strings, each a number or the sign of the operation to be performed on the 2 adjacent numbers.
     * @param input A string representing a desired mathematical calculation.
     * @return An array of strings representing each number or sign, in the same order as they appear in the input string.
     * @throws ParseException
     */
    private ArrayList<String> parse(String input) throws ParseException {
        if (input.length() <= 0) {
            throw new ParseException("Empty Input", 0);
        }

        String temp = "";
        ArrayList<String> strings = new ArrayList<String>();
        ArrayList<String> signs = new ArrayList<String>();
        signs.add("+");
        signs.add("-");
        signs.add("*");
        signs.add("/");
        signs.add("^");

        for (int i = input.length() - 1; i >= 0; i--) {
            if (input.charAt(i) == '+' || input.charAt(i) == '*' || input.charAt(i) == '/' || input.charAt(i) == '^') {
                strings.add(0, temp);
                strings.add(0, String.valueOf(input.charAt(i)));
                temp = "";
            }
            else if (input.charAt(i) == '-') {
                if (i == 0 || signs.contains("" + input.charAt(i - 1))) {
                    temp = input.charAt(i) + temp;
                }
                else {
                    strings.add(0, temp);
                    strings.add(0, String.valueOf(input.charAt(i)));
                    temp = "";
                }
            }
            else {
                temp = input.charAt(i) + temp;
            }

        }

        strings.add(0, temp);

        return strings;
    }

    /**
     * Takes an array of strings representing a desired mathematical calculation and arranges them into a tree structure according to the order of operations.
     * @param strings An array of strings representing the desired mathematical calculation.
     * @return The root of a binary tree representing the desired mathematical calculation by order of operation.
     * @throws ParseException
     */
    private Node buildTree(List<String> strings) throws ParseException {
        if (strings.size() <= 0) {
            throw new ParseException("Invalid Input", 0);
        }
        for (int i = strings.size() - 1; i >= 0 ; i--) {
            if (strings.get(i).equalsIgnoreCase("+") || strings.get(i).equalsIgnoreCase("-")) {
                Node center = new Node(strings.get(i));
                center.left = buildTree(strings.subList(0, i));
                center.right = buildTree(strings.subList(i + 1, strings.size()));
                return center;
            }
        }

        for (int i = strings.size() - 1; i >= 0 ; i--) {
            if (strings.get(i).equalsIgnoreCase("*")) {
                Node center = new Node(strings.get(i));
                center.left = buildTree(strings.subList(0, i));
                center.right = buildTree(strings.subList(i + 1, strings.size()));
                return center;
            }
        }

        for (int i = strings.size() - 1; i >= 0 ; i--) {
            if (strings.get(i).equalsIgnoreCase("/")) {
                Node center = new Node(strings.get(i));
                center.left = buildTree(strings.subList(0, i));
                center.right = buildTree(strings.subList(i + 1, strings.size()));
                return center;
            }
        }

        for (int i = strings.size() - 1; i >= 0 ; i--) {
            if (strings.get(i).equalsIgnoreCase("^")) {
                Node center = new Node(strings.get(i));
                center.left = buildTree(strings.subList(0, i));
                center.right = buildTree(strings.subList(i + 1, strings.size()));
                return center;
            }
        }

        return new Node(strings.get(0));
    }

    @Override
    public double calculate(String input) throws ParseException {
        return calculate(buildTree(parse(input)));
    }

    @Override
    public DecimalFormat getDecimalFormat(double number) {
        if (number > 99999999) {
            return BasicCalculator.scientific;
        }
        else {
            return BasicCalculator.regular;
        }
    }

    /**
     * Takes a root node of a tree of nodes and recursively calculates the value of each node, starting from the leaves and working back up to the root.
     * @param node The root node of a tree of nodes representing a desired mathematical calculation in order of operation.
     * @return The result of the calculation.
     * @throws ParseException
     */
    private double calculate(Node node) throws ParseException {
        if (node.value.equalsIgnoreCase("+")) {
            return calculate(node.left) + calculate(node.right);
        }
        else if (node.value.equalsIgnoreCase("-")) {
            return calculate(node.left) - calculate(node.right);
        }
        else if (node.value.equalsIgnoreCase("*")) {
            return calculate(node.left) * calculate(node.right);
        }
        else if (node.value.equalsIgnoreCase("/")) {
            if (calculate(node.right) == 0) {
                throw new ArithmeticException("Can't divide by zero");
            }

            return calculate(node.left) / calculate(node.right);
        }
        else if (node.value.equalsIgnoreCase("^")) {
            return Math.pow(calculate(node.left), calculate(node.right));
        }
        else {
            return BasicCalculator.scientific.parse(node.value).doubleValue(); //Warning: Method invocation 'doubleValue' may produce 'NullPointerException'
        }
    }
}

/**
 * Represents a node in a binary tree of String values.
 */
class Node {
    Node left, right;
    String value;

    Node(String value) {
        this.value = value;
    }

    @Override @NonNull
    public String toString() {
        return (this.left == null ? "" : this.left.toString()) + this.value + (this.right == null ? "" : this.right.toString());
    }
}

interface CalculatorInterface {
    /**
     * Calculates the result of the desired mathematical calculation represented by the input string.
     * @param input The desired mathematical calculation.
     * @return The result.
     * @throws ParseException
     */
    double calculate(String input) throws ParseException;

    /**
     * Get the decimal format used by this calculator to parse strings and format values.
     * The Decimal format will be chosen based on the value of the number provided, which is presumably the number that needs formatting.
     * @param number the number to be formatted. A different decimal format me be provided based on the value of number.
     * @return a DecimalFormat that this calculator deems suitable for formatting values of the value number.
     */
    DecimalFormat getDecimalFormat(double number);
}