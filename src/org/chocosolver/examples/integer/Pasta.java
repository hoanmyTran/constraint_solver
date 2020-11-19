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
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
    IntVar[][] attrBis;
    Model modelBis ;
    private IntVar zebra;

    @Override
    public void buildModel() {

        model = new Model();
        modelBis = new Model();

        attr = model.intVarMatrix("attr", SIZE, SIZE, 1, SIZE);
        attrBis=modelBis.intVarMatrix("attr", SIZE  , SIZE, 1, SIZE);;

        IntVar Angie   = attr[NOM][0];
        IntVar Damon = attr[NOM][1];
        IntVar Claudia   = attr[NOM][2];
        IntVar Elisa = attr[NOM][3];

        IntVar AngieBis   = attrBis[NOM][0];
        IntVar DamonBis = attrBis[NOM][1];
        IntVar ClaudiaBis   = attrBis[NOM][2];
        IntVar ElisaBis = attrBis[NOM][3];

        IntVar the_other_type1    = attr[SAUCE][0];
        IntVar arrabiata_sauce   = attr[SAUCE][1];
        IntVar marinara_sauce = attr[SAUCE][2];
        IntVar puttanesca_sauce  = attr[SAUCE][3];

        IntVar the_other_type1Bis    = attrBis[SAUCE][0];
        IntVar arrabiata_sauceBis   = attrBis[SAUCE][1];
        IntVar marinara_sauceBis = attrBis[SAUCE][2];
        IntVar puttanesca_sauceBis  = attrBis[SAUCE][3];

        IntVar capellini = attr[PASTA][0];
        IntVar farfaelle   = attr[PASTA][1];
        IntVar tagliolini   = attr[PASTA][2];
        IntVar rotini   = attr[PASTA][3];

        IntVar capelliniBis = attrBis[PASTA][0];
        IntVar farfaelleBis   = attrBis[PASTA][1];
        IntVar taglioliniBis   = attrBis[PASTA][2];
        IntVar rotiniBis   = attrBis[PASTA][3];



        model.allDifferent(attr[SAUCE]).post();
        model.allDifferent(attr[NOM]).post();
        model.allDifferent(attr[PASTA]).post();

        modelBis.allDifferent(attrBis[SAUCE]).post();
        modelBis.allDifferent(attrBis[NOM]).post();
        modelBis.allDifferent(attrBis[PASTA]).post();

        capellini.lt(arrabiata_sauce).post();//1
        tagliolini.gt(Angie).post();//2
        tagliolini.lt(marinara_sauce).post();//3
        Claudia.ne(puttanesca_sauce).post();//4
        rotini.dist(Damon).eq(2).post(); // new 5
        capellini.eq(Damon).or(capellini.eq(Claudia)).post();//6
        arrabiata_sauce.eq(Angie).or(arrabiata_sauce.eq(Elisa)).post();//7
        arrabiata_sauce.eq(farfaelle).post();//8

        capelliniBis.lt(arrabiata_sauceBis).post();//1
        taglioliniBis.gt(AngieBis).post();//2
        taglioliniBis.lt(marinara_sauceBis).post();//3
        ClaudiaBis.ne(puttanesca_sauceBis).post();//4
        rotiniBis.dist(DamonBis).eq(2).post(); // new 5
        capelliniBis.eq(DamonBis).or(capelliniBis.eq(ClaudiaBis)).post();//6
        arrabiata_sauceBis.eq(AngieBis).or(arrabiata_sauceBis.eq(ElisaBis)).post();//7
        arrabiata_sauceBis.eq(farfaelleBis).post();//8

    }

    @Override
    public void configureSearch() {
    }

    public void oldSolve() {
        try {
            model.getSolver().propagate();
        } catch (ContradictionException e) {
            e.printStackTrace();
        }

        for(int i=0; i < SIZE; i++) {
            for(int j=0; j < SIZE; j++) {
                System.out.println("-----------Index's values : [i,j]=["+i+","+j+"]--------------");
                Iterator iterator = this.attr[i][j].iterator();

                // for each possible value of attr
                while(iterator.hasNext()) {
                    Integer priceIndex = (Integer) iterator.next();
                    // creation of constraint
                    Constraint constraint = this.attr[i][j].eq(priceIndex).decompose();
                    model.post(constraint);

                    try {
                        // save attr's state
                        model.getSolver().getEnvironment().worldPush();
                        model.getSolver().propagate();
                        System.out.println(model);
                    } catch (ContradictionException e) {
                        System.out.println("-----------OUPS-----------");
                        e.printStackTrace();
                        model.getSolver().getEnvironment().worldPop();
                        model.unpost(constraint);
                    }
                }
            }
        }

      //  print(attr);
//        try {
//            model.getSolver().propagate();//.getEnvironement.worldpush , world pup
//        } catch (ContradictionException e) {
//            e.printStackTrace();
//        }

//        while (model.getSolver().solve()) {
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
//            print(attr);
//        }
    }

    @Override
    public void solve() {
        candidateExplanation();
    }

    private void print(IntVar[][] pos) {
        System.out.printf("%-20s%-20s%-20s%-20s%-20s%n", "",
                sCost[0], sCost[1], sCost[2], sCost[3]);

        for (int i = 0; i < SIZE-1; i++) {
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
    }

    public static void main(String[] args) {
        new Pasta().execute(args);
    }

    public void candidateExplanation(){
//        List <IntVar>candidates= new ArrayList<IntVar>();
        List <IntVar>IdifJ= new ArrayList<IntVar>();
        try {
            model.getSolver().propagate();
        } catch (ContradictionException e) {
            e.printStackTrace();
        }
        IdifJ = getDifferences(attr,attrBis);
        System.out.println(IdifJ);
        ArrayList <Constraint> constraints=new ArrayList<Constraint>() ;
        ArrayList <Constraint> unpostedConstraint=new ArrayList<Constraint>() ;

        Constraint constraintsBis [] =  modelBis.getCstrs();

        for(int i=0; i < SIZE; i++) {
            for(int j=0; j < SIZE; j++) {
                //System.out.println("-----------Index's values : [i,j]=["+i+","+j+"]--------------");
                Iterator iterator = this.attr[i][j].iterator();

                // for each possible value of attr
                while(iterator.hasNext()) {
                    Integer priceIndex = (Integer) iterator.next();
                    // creation of constraint
                    Constraint constraint = this.attr[i][j].eq(priceIndex).decompose();
                    model.post(constraint);

                    try {
                        // save attr's state
                        model.getSolver().getEnvironment().worldPush();
                        model.getSolver().propagate();
                        IdifJ = getDifferences(attr,attrBis);
                        System.out.println(IdifJ);
                        //constraints[constraints.length-1]
                        constraints.add(constraint);
                    } catch (ContradictionException e) {
                        System.out.println("-----------OUPS-----------");
                        e.printStackTrace();
                        model.getSolver().getEnvironment().worldPop();
                        unpostedConstraint.add(constraint);
                        model.unpost(constraint);
                    }
                }
                print(attrBis);
                print(attr);

            }
        }
//        Iterator iterator = constraints.iterator();
//        while(iterator.hasNext()){
//            System.out.println(iterator.next());
//        }
//        System.out.println(":::::::::::::::");
//        Iterator iteratorUnpost = unpostedConstraint.iterator();
//        while(iteratorUnpost.hasNext()){
//            System.out.println(iteratorUnpost.next());
//        }

    }

    private List<IntVar> getDifferences(IntVar[][] previousAttr,IntVar[][] actualAttr){
        List <IntVar> differences = new ArrayList<IntVar>();
        for(int i = 0 ; i<SIZE -1;i++){
            for (int j=0;j<SIZE;j++){
                //casting both matrix to array list in order to compare
                Iterator<Integer> previousAttrIt = previousAttr[i][j].iterator();
                ArrayList<Integer>previousValuesList = new ArrayList<Integer>();

                while(previousAttrIt.hasNext()){

                   previousValuesList.add(previousAttrIt.next());
                }
                Iterator<Integer> acualAttrIt = actualAttr[i][j].iterator();
                ArrayList<Integer>actualValuesList = new ArrayList<Integer>();
                while(acualAttrIt.hasNext()){
                    actualValuesList.add(acualAttrIt.next());
                }

                //comparaison
                if(!previousValuesList.equals(actualValuesList)){
                    differences.add(actualAttr[i][j]);
                }
            }
        }
        return differences;
    }
}