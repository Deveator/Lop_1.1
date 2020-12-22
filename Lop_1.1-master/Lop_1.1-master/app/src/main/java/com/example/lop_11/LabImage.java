package com.example.lop_11;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.lop_11.CustomView.DrawRect.xRed;
import static com.example.lop_11.CustomView.DrawRect.yRed;
import static com.example.lop_11.MainActivity.clusterStep;

public class LabImage {

    public static ArrayList<String> Lab_Values = new ArrayList<String>();
    public static ArrayList<String> yx_Values = new ArrayList<String>();
    public static ArrayList<String> diffrent_Lab_values = new ArrayList<String>();
    public static ArrayList<Integer> diffrent_Lab_indexes = new ArrayList<Integer>();
    public static ArrayList<Integer> diffA_Values = new ArrayList<Integer>();
    public static ArrayList<Integer> diffB_Values = new ArrayList<Integer>();
    public static ArrayList<Integer> diffL_Values = new ArrayList<Integer>();
    public static ArrayList<ArrayList<Integer>> clustersA_ = new ArrayList<ArrayList<Integer>>();
    public static ArrayList<ArrayList<Integer>> clustersA_Index = new ArrayList<ArrayList<Integer>>();
    public static ArrayList<ArrayList<Integer>> clustersB_ = new ArrayList<ArrayList<Integer>>();
    public static ArrayList<ArrayList<Integer>> clustersB_Index = new ArrayList<ArrayList<Integer>>();
    public static ArrayList<ArrayList> clustersB_IndexCopy = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList<Integer>> clustersL_ = new ArrayList<ArrayList<Integer>>();
    public static ArrayList<ArrayList<Integer>> clustersL_Index = new ArrayList<ArrayList<Integer>>();
    public static ArrayList<ArrayList> clustersL_IndexCopy = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList> clusterS_yx_Values = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList> finalSortedCluster = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList> all_diffX_Values = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList> all_diffY_Values = new ArrayList<ArrayList>();

    public static void getClustersFromLabROIImg(Mat mat) {

        Mat labMat = new Mat();

        Imgproc.cvtColor(mat, labMat, Imgproc.COLOR_BGR2Lab);

        for (int x = 0; x < labMat.cols(); x++) {
            for (int y = 0; y < labMat.rows(); y++) {
                double[] fullLabValue = labMat.get(y, x);

                int L = (int) fullLabValue[0];
                int a = (int) fullLabValue[1];
                int b = (int) fullLabValue[2];

                String s = a + "." + b + "." + L;
                Lab_Values.add(s);

                String s1 = (y + yRed) + "." + (x + xRed);
                yx_Values.add(s1);
            }
        }
        getDifferentLabValues();
        getAandBarrays();
        System.out.println("True YX coordinates" + yx_Values.get(0));


        clusterizeA(clusterStep);
        clusterizeB(clusterStep);
        clusterizeL(clusterStep);
        sortArray();
        // Imgproc.cvtColor(mat, labMat, Imgproc.COLOR_Lab2BGR);
        System.out.println("COMPLETED");
    }

    public static void getDifferentLabValues() {
        // find different values
        ///  TreeSet<String> tSetNumbers = new TreeSet(Lab_Values);
        ///  diffrent_Lab_values.addAll(tSetNumbers);
        ///  System.out.println("diffrent_Lab_values - " + diffrent_Lab_values.size());
        int num = 0;
        for (int i = 0; i < Lab_Values.size(); i++) {
            String s = Lab_Values.get(i);
            if (!diffrent_Lab_values.contains(s)) {
                diffrent_Lab_values.add(s);
                diffrent_Lab_indexes.add(num);
                num++;
            }
        }
        System.out.println("different_Lab_values - " + diffrent_Lab_values.size());
    }

    public static void getAandBarrays() {
        for (int i = 0; i < diffrent_Lab_values.size(); i++) {
            String r = String.valueOf(diffrent_Lab_values.get(i));
            String[] arrOfStr = r.split("\\.");
            diffA_Values.add(Integer.valueOf(arrOfStr[0]));
            diffB_Values.add(Integer.valueOf(arrOfStr[1]));
            diffL_Values.add(Integer.valueOf(arrOfStr[2]));
        }
    }

    public static void clusterizeA(int clusterStep) {

        for (int i = 0; i < diffA_Values.size(); i++) {
            int aVal = diffA_Values.get(i);
            ArrayList<Integer> aL1 = new ArrayList<>();
            ArrayList<Integer> indexFromAStep = new ArrayList<>();
            aL1.add(aVal);
            indexFromAStep.add(i);

            for (int i2 = 0; i2 < diffA_Values.size(); i2++) {
                if (i2 != i) {
                    int aVal2 = diffA_Values.get(i2);
                    int res = aVal2 - aVal;
                    if ((res >= 0) && (res < clusterStep)) {
                        aL1.add(aVal2);
                        indexFromAStep.add(i2);
                    }
                }
            }
            clustersA_.add(aL1);
            clustersA_Index.add(indexFromAStep);
        }
        System.out.println("clustersA_ - " + clustersA_.size());
        ///   for (int i = 0; i < 10; i++) {
        ///       System.out.println(i + " - " + clustersA_.get(i).size());
        ///   }
    }

    public static void clusterizeB(int clusterStep) {

        for (int i = 0; i < clustersA_Index.size(); i++) {

            ArrayList<Integer> helpForB = clustersA_Index.get(i);// arrayList with indexex
            ArrayList<Integer> bL1 = new ArrayList<>();
            ArrayList<Integer> indexFromBStep = new ArrayList<>();
            ArrayList<Integer> indexFromBStepCopy = new ArrayList<>();

            int bVal = diffB_Values.get(helpForB.get(0));

            bL1.add(bVal);// start value
            indexFromBStep.add(helpForB.get(0));// start index
            indexFromBStepCopy.add(helpForB.get(0));

            for (int y = 1; y < helpForB.size(); y++) {
                int bVal1 = diffB_Values.get(helpForB.get(y));
                int res = bVal1 - bVal;
                if ((res >= 0) && (res < clusterStep)) {
                    bL1.add(bVal1);
                    indexFromBStep.add(helpForB.get(y));
                    indexFromBStepCopy.add(helpForB.get(y));
                }
            }
            clustersB_.add(bL1);
            clustersB_Index.add(indexFromBStep);
            clustersB_IndexCopy.add(indexFromBStepCopy);
        }
        System.out.println("clustersB_ - " + clustersB_.size());
        ///   for (int i = 0; i < 10; i++) {
        ///       System.out.println(i + " - " + clustersB_.get(i).size());
        ///   }
    }

    public static void clusterizeL(int clusterStep) {

        for (int i = 0; i < clustersB_Index.size(); i++) {

            ArrayList<Integer> helpForL = clustersB_Index.get(i);// arrayList with indexex
            ArrayList<Integer> lL1 = new ArrayList<>();
            ArrayList<Integer> indexFromLStep = new ArrayList<>();
            ArrayList<Integer> indexFromLStepCopy = new ArrayList<>();

            int lVal = diffL_Values.get(helpForL.get(0));

            lL1.add(lVal);// start value
            indexFromLStep.add(helpForL.get(0));// start index
            indexFromLStepCopy.add(helpForL.get(0));

            for (int y = 1; y < helpForL.size(); y++) {
                int lVal1 = diffL_Values.get(helpForL.get(y));
                int res = lVal1 - lVal;
                if ((res >= 0) && (res < clusterStep)) {
                    lL1.add(lVal1);
                    indexFromLStep.add(helpForL.get(y));
                    indexFromLStepCopy.add(helpForL.get(y));
                }
            }
            clustersL_.add(lL1);
            clustersL_Index.add(indexFromLStep);
            clustersL_IndexCopy.add(indexFromLStepCopy);
        }
        System.out.println("clustersL_ - " + clustersL_.size());
        /// for (int i = 0; i < 10; i++) {
        ///     System.out.println(i + " - " + clustersL_.get(i).size());
        ///  }

    }

    public static void sortArray() {

        ArrayList<Integer> aList = new ArrayList<Integer>();
        // array help to find required array
        ArrayList<Integer> aList2 = new ArrayList<Integer>();

        for (int i = 0; i < clustersL_Index.size(); i++) {
            aList.add(clustersL_Index.get(i).size());
        }
        Collections.sort(aList);
        Collections.reverse(aList);
        System.out.println("aList size - " + aList.size());
        System.out.println("aList first - " + aList.get(0));

        for (int y = 0; y < clustersL_IndexCopy.size(); y++) {
            for (int j = 0; j < clustersL_IndexCopy.size(); j++) {
                if ((aList.get(y) != 0) && (aList.get(y) == clustersL_IndexCopy.get(j).size())) {
                    aList2.add(j);
                    clustersL_IndexCopy.get(j).clear();
                    break;
                }
            }
        }
        System.out.println("aList2 size - " + aList2.size());
        System.out.println("aList2 first - " + aList2.get(0));

        // sorted array with different indexes
        ArrayList<ArrayList> aL_2 = new ArrayList<ArrayList>();

        for (int i = 0; i < aList2.size(); i++) {
            aL_2.add(clustersL_Index.get(aList2.get(i)));
        }
//**********************************************************
        for (int i = 0; i < aL_2.size() - 1; i++) {

            for (int y = 0; y < aL_2.get(i).size(); y++) {

                ArrayList<Integer> helpAL = aL_2.get(i);

                int val = helpAL.get(y);

                for (int u = (i + 1); u < aL_2.size(); u++) {

                    for (int p = 0; p < aL_2.get(u).size(); p++) {

                        ArrayList<Integer> helpAL2 = aL_2.get(u);

                        int val2 = helpAL2.get(p);

                        if (val == val2) {

                            helpAL2.remove(p);
                            p--;
                        }
                    }
                }
            }
            removeZero(aL_2);
            ArrayList<ArrayList> nAl = sortDesc(aL_2);
            aL_2.clear();
            for (int i2 = 0; i2 < nAl.size(); i2++) {
                aL_2.add((ArrayList) nAl.get(i2).clone());
            }
            nAl.clear();
            removeZero(aL_2);
        }

        for (int i = 0; i < aL_2.size(); i++) {
            finalSortedCluster.add(aL_2.get(i));
        }
        System.out.println("finalSortedCluster - " + finalSortedCluster.size());
        System.out.println(finalSortedCluster.get(0).size());
        System.out.println(finalSortedCluster.get(0).get(0));
        // System.out.println(diffrent_a_b_values.get(122));

        reColor();


    }

    public static void removeZero(ArrayList<ArrayList> aL) {
        for (int i = 0; i < aL.size(); i++) {
            if (aL.get(i).size() == 0) {
                aL.remove(i);
                i--;
            }
        }
    }

    public static ArrayList<ArrayList> sortDesc(ArrayList<ArrayList> _aL) {

        //copy to help
        ArrayList<ArrayList> allArraysCopy = new ArrayList<ArrayList>();
        for (int i = 0; i < _aL.size(); i++) {
            allArraysCopy.add((ArrayList) _aL.get(i).clone());
        }

        ArrayList<Integer> arrayListSize = new ArrayList<>();
        for (int i = 0; i < _aL.size(); i++) {
            arrayListSize.add(_aL.get(i).size());
        }
        Collections.sort(arrayListSize);
        Collections.reverse(arrayListSize);

        ArrayList<Integer> aList2 = new ArrayList<Integer>();

        for (int y = 0; y < allArraysCopy.size(); y++) {
            for (int j = 0; j < allArraysCopy.size(); j++) {
                if ((arrayListSize.get(y) != 0) && (arrayListSize.get(y) == allArraysCopy.get(j).size())) {
                    aList2.add(j);
                    allArraysCopy.get(j).clear();
                    break;
                }
            }
        }
        // sorted array with different indexes
        ArrayList<ArrayList> aL_2 = new ArrayList<ArrayList>();

        for (int i = 0; i < aList2.size(); i++) {
            aL_2.add(_aL.get(aList2.get(i)));
        }
        return aL_2;
    }

    public static void reColor() {

        ArrayList<String> aL = new ArrayList<>();
        ArrayList<ArrayList> aL_All = new ArrayList<>();
        // changes
        for (int c = 0; c < finalSortedCluster.size(); c++) {
            ArrayList<Integer> help_aL = finalSortedCluster.get(c);
            for (int i = 0; i < help_aL.size(); i++) {
                int y = help_aL.get(i);
                aL.add(diffrent_Lab_values.get(y));
            }
            aL_All.add((ArrayList) aL.clone());
            aL.clear();
        }

        ArrayList<String> aL_2 = new ArrayList<>();

        for (int c = 0; c < aL_All.size(); c++) {
            ArrayList<String> help_aL = aL_All.get(c);
            for (int i = 0; i < help_aL.size(); i++) {
                for (int q = 0; q < Lab_Values.size(); q++) {
                    if (help_aL.get(i).equals(Lab_Values.get(q))) {
                        aL_2.add(yx_Values.get(q));
                    }
                }
            }
            clusterS_yx_Values.add((ArrayList) aL_2.clone());
            aL_2.clear();
        }

        int res = 0;
        System.out.println("*********************");
        System.out.println(clusterS_yx_Values.size());
        System.out.println(clusterS_yx_Values.get(0).size());

        for (int i = 0; i < clusterS_yx_Values.size(); i++) {
            res = res + clusterS_yx_Values.get(i).size();
        }
        System.out.println("Count of all arrays " + res);

        getYandXarrays();

    }

    public static void getYandXarrays() {

        ArrayList<Integer> _diffYValues = new ArrayList<>();
        ArrayList<Integer> _diffXValues = new ArrayList<>();

        for (int c = 0; c < clusterS_yx_Values.size(); c++) {

            ArrayList<String> help_aL = clusterS_yx_Values.get(c);

            for (int i = 0; i < help_aL.size(); i++) {

                String r = help_aL.get(i);

                String[] arrOfStr = r.split("\\.");

                _diffYValues.add(Integer.valueOf(arrOfStr[0]));

                _diffXValues.add(Integer.valueOf(arrOfStr[1]));
            }
            all_diffY_Values.add((ArrayList) _diffYValues.clone());
            all_diffX_Values.add((ArrayList) _diffXValues.clone());
            _diffYValues.clear();
            _diffXValues.clear();
        }
    }


}
