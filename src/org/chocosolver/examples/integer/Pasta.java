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
    private final int NOM = 0, SAUCE = 1, PRIX = 2, PASTA = 3;
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


        Angie.eq(capellini).post();
        capellini.lt(arrabiata_sauce).post();//1
        tagliolini.gt(Angie).post();//2
        tagliolini.lt(marinara_sauce).post();//3
        rotini.gt(Damon).or(rotini.gt(Damon)).post();//4

        tagliolini.ne(marinara_sauce).post();//3
        //Angie.ne(tagliolini).post();//2
        Claudia.ne(puttanesca_sauce).post();//indice 4
        Damon.eq(capellini).or(Claudia.eq(capellini)).post();//indice6
        Angie.eq(arrabiata_sauce).or(Elisa.eq(arrabiata_sauce)).post();//indice7
        farfaelle.eq(arrabiata_sauce).post();//indice8


        //Elisa.eq(capellini).or(Claudia.eq(capellini)).post();//exemple or


//        eng.eq(red).post(); // 2. the Englishman lives in the red house
//        spain.eq(dog).post(); // 3. the Spaniard owns a dog
//        coffee.eq(green).post(); // 4. coffee is drunk in the green house
//        ukr.eq(tea).post(); // 5. the Ukr drinks tea
//        ivory.add(1).eq(green).post(); // 6. green house is to right of ivory house
//        oldGold.eq(snails).post(); // 7. oldGold smoker owns snails
//        kools.eq(yellow).post(); // 8. kools are smoked in the yellow house
//        milk.eq(3).post(); // 9. milk is drunk in the middle house
//        norge.eq(1).post(); // 10. Norwegian lives in first house on the left
//        chest.dist(fox).eq(1).post(); // 11. chesterfield smoker lives next door to the fox owner
//        kools.dist(horse).eq(1).post(); // 12. kools smoker lives next door to the horse owner
//        lucky.eq(oj).post(); // 13. lucky smoker drinks orange juice
//        jap.eq(parly).post(); // 14. Japanese smokes parliament
//        norge.dist(blue).eq(1).post(); // 15. Norwegian lives next to the blue house
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