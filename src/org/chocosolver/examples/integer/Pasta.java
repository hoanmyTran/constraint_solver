/*
 * This file is part of examples, http://choco-solver.org/
 *
 * Copyright (c) 2020, IMT Atlantique. All rights reserved.
 *
 * Licensed under the BSD 4-clause license.
 *
 * See LICENSE file in the project root for full license information.
 */
package org.chocosolver.examples.integer;

import org.chocosolver.examples.AbstractProblem;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;

/**
 * Simple example which solve Zebra puzzle
 * <br/>
 *
 * @author Gael benaissa , My le bogosss
 * @since 29/01/19
 */
public class Pasta extends AbstractProblem {

    //private final String[] sPasta = {"capellini", "farfaelle", "tagliolini", "rotini"}; // type of pasta
    private final String[] sCost = {"4 ", "8", "12", "16"}; // type of pasta
    private final int SIZE = sCost.length;
    private final int NOM = 0, SAUCE = 1, PASTA = 2;
    private final String [] sAttrTitle = {"Nom", "Sauce","Pasta"};
    private final String [][] sAttr = {
            {"Angie", "Damon", "Claudia", "Elisa"},
            {"the_other_type1", "arrabiata_sauce", "marinara_sauce", "puttanesca_sauce"},
            {"capellini", "farfaelle", "tagliolini", "rotini"}
    };
    private IntVar[][] attr;
    private IntVar zebra;

    @Override
    public void buildModel() {


        model = new Model();

        attr = model.intVarMatrix("attr", SIZE, SIZE, 1, SIZE);

        IntVar Angie   = attr[NOM][0];
        IntVar Damon = attr[NOM][1];
        IntVar Claudia   = attr[NOM][2];
        IntVar Elisa = attr[NOM][3];

        IntVar the_other_type1    = attr[SAUCE][0];
        IntVar arrabiata_sauce   = attr[SAUCE][1];
        IntVar marinara_sauce = attr[SAUCE][2];
        IntVar puttanesca_sauce  = attr[SAUCE][3];

//        IntVar four = attr[PRIX][0];
//        IntVar eight   = attr[PRIX][1];
//        IntVar twelve   = attr[PRIX][2];
//        IntVar sixteen   = attr[PRIX][3];

        IntVar capellini = attr[PASTA][0];
        IntVar farfaelle   = attr[PASTA][1];
        IntVar tagliolini   = attr[PASTA][2];
        IntVar rotini   = attr[PASTA][3];

       zebra  = attr[NOM][0];

        model.allDifferent(attr[SAUCE]).post();
       // model.allDifferent(attr[PRIX]).post();
        model.allDifferent(attr[NOM]).post();
        model.allDifferent(attr[PASTA]).post();





        capellini.lt(arrabiata_sauce).post();//1
        tagliolini.gt(Angie).post();//2
        tagliolini.lt(marinara_sauce).post();//3
        Claudia.ne(puttanesca_sauce).post();//4
        rotini.gt(Damon).or(rotini.lt(Damon)).post();//5
        capellini.eq(Damon).or(capellini.eq(Claudia)).post();//6
        arrabiata_sauce.eq(Angie).or(arrabiata_sauce.eq(Elisa)).post();//7
        arrabiata_sauce.eq(farfaelle).post();//8
    }

    @Override
    public void configureSearch() {
    }

    @Override
    public void solve() {
//        try {
//            model.getSolver().propagate();//.getEnvironement.worldpush , world pup
//        } catch (ContradictionException e) {
//            e.printStackTrace();
//        }

        while (model.getSolver().solve()) {
//            int z = zebra.getValue();
//            int n = -1;
//            for (int i = 0; i < SIZE; i++) {
//                if (z == attr[NOM][i].getValue()) {
//                    n = i;
//                }
//            }
//            if (n >= 0) {
//                System.out.printf("%n%-20s%s%s%s%n", "",
//                        "============> The pasta is owned by the ", sAttr[NOM][n], " <============");
//            }
            System.out.println("1");
            print(attr);
        }
        System.out.println("1");

    }
    private void print(IntVar[][] pos) {
        System.out.println("1");

        System.out.printf("%-20s%-20s%-20s%-20s%-20s%n", "",
                sCost[0], sCost[1], sCost[2], sCost[3]);

        for (int i = 0; i < SIZE; i++) {
            String[] sortedLine = new String[SIZE];
            for (int j = 0; j < SIZE; j++) {

                sortedLine[pos[i][j].getValue() - 1] = sAttr[i][j];
            }
            System.out.printf("%-20s", sAttrTitle[i]);
            for (int j = 0; j < SIZE; j++) {


                System.out.printf("%-20s", sortedLine[j]);
            }
            System.out.println();
        }
        System.out.println("1");
    }

    public static void main(String[] args) {
        System.out.println("aaaaaa");
        new Pasta().execute(args);
    }
}