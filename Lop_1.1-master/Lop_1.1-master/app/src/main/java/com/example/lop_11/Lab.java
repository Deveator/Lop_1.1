package com.example.lop_11;

import org.opencv.core.Mat;

import java.util.ArrayList;

import static com.example.lop_11.CustomView.DrawRect.xOrg;
import static com.example.lop_11.CustomView.DrawRect.xRed;
import static com.example.lop_11.CustomView.DrawRect.yGreen;
import static com.example.lop_11.CustomView.DrawRect.yRed;
import static com.example.lop_11.MainActivity.allColors_AL;
import static com.example.lop_11.MainActivity.borderPoints_AL;
import static com.example.lop_11.MainActivity.colorDefiningAl;
import static com.example.lop_11.MainActivity.oImage;

public class Lab {

    public static ArrayList<Integer> x_Cor = new ArrayList<Integer>();
    public static ArrayList<Integer> y_Cor = new ArrayList<Integer>();

    public static ArrayList<Integer> x_Cor_ROI = new ArrayList<Integer>();
    public static ArrayList<Integer> y_Cor_ROI = new ArrayList<Integer>();

    public static ArrayList<String> xy_Cor_string = new ArrayList<String>();
    public static ArrayList<Integer> all_A_Values = new ArrayList<Integer>();
    public static ArrayList<Integer> all_B_Values = new ArrayList<Integer>();
    public static ArrayList<Integer> all_C_Values = new ArrayList<Integer>();
    public static ArrayList<String> clearROI_yx = new ArrayList<String>();
    public static int mDone = 0;

    public static int maxA, minA, rMin, rMax, minIntense, maxIntense;

    public static int count = 0;
    public static int upDownBorderPoints = 0;
    public static int downUpBorderPoints = 0;
    public static int leftRightBorderPoints = 0;
    public static int rightLeftBorderPoints = 0;
    public static int downLeftUpRightBorderPoints = 0;
    public static int downRightUpLeftBorderPoints = 0;
    public static int upLeftDownRightBorderPoints = 0;
    public static int upRightDownLeftBorderPoints = 0;
    public static int Nocount = 0;
    public static int countAll = 0;

    public static int starter_x;
    public static int starter_y;

    public static boolean rightMet = false;
    public static boolean leftMet = false;
    public static boolean upMet = false;
    public static boolean downMet = false;

    public static int rightUp_x;
    public static int rightUp_y;
    public static int leftUp_x;
    public static int leftUp_y;
    public static int rightDown_x;
    public static int rightDown_y;
    public static int leftDown_x;
    public static int leftDown_y;

    public static int next_x;
    public static int next_y;

    public static int squareNum = 1;
    public static int isFirst = 0;

    public static int squareNumHrzntl = 0;
    public static int squareNumVrtcl = 0;

    public static final String VERT_RLTN = "vertical relation";
    public static final String NO_VERT_RLTN = "no vertical relation";//just for developing
    public static final String HOR_RLTN = "horizontal relation";
    public static final String NO_HOR_RLTN = "no horizontal relation";//just for developing
    public static final String VERT_HOR_RLTN = "vertical horizontal relation";
    public static final String NO_RLTN = "no relation";

    public static final String START_POINT = "start point";
    public static final String SEMI_USUAL_POINT_X_0 = "semi usual point x=0";
    public static final String SEMI_USUAL_POINT_Y_0 = "semi usual point y=0";
    public static final String USUAL_POINT = "usual point";

    public static boolean isWhite(int y, int x) {
        boolean r2 = false;
        double[] draftValue = oImage.get(y, x);

        int r = (int) draftValue[0];
        int g = (int) draftValue[1];
        int b = (int) draftValue[2];

        if (r == 255 && g == 255 && b == 255) r2 = true;
        return r2;
    }


    public static void findChangeDirectionPoints() {

        outer:
        for (int i = yRed; i < yGreen; i++) {
            for (int j = xRed; j < xOrg; j++) {
                if (isWhite(i, j)) {
                    leftUp_x = j;
                    leftUp_y = i;
                    System.out.println("leftUp " + i + " - " + j);
                    break outer;
                }
            }
        }

        outer2:
        for (int i = yGreen; i > yRed; i--) {
            for (int j = xRed; j < xOrg; j++) {
                if (isWhite(i, j)) {
                    leftDown_x = j;
                    leftDown_y = i;
                    System.out.println("leftDown " + i + " - " + j);
                    break outer2;
                }
            }
        }

        outer3:
        for (int i = yRed; i < yGreen; i++) {
            for (int j = xOrg; j > xRed; j--) {
                if (isWhite(i, j)) {
                    rightUp_x = j;
                    rightUp_y = i;
                    System.out.println("rightUp " + i + " - " + j);
                    break outer3;
                }
            }
        }

        outer4:
        for (int i = yGreen; i > yRed; i--) {
            for (int j = xOrg; j > xRed; j--) {
                if (isWhite(i, j)) {
                    rightDown_x = j;
                    rightDown_y = i;
                    System.out.println("rightDown " + i + " - " + j);
                    break outer4;
                }
            }
        }
    }

    public static void findStarter() {
        int starter = 0;

        for (int i = xRed; i < xOrg; i++) {
            if (isWhite(yRed, i)) {
                starter = 1;
                starter_y = yRed;
                starter_x = i;
                System.out.println(yRed + " + " + i);
                break;
            }
        }
        if (starter == 0) {
            for (int i = yRed; i < yGreen; i++) {
                if (isWhite(i, xOrg)) {
                    starter = 1;
                    starter_y = i;
                    starter_x = xOrg;
                    System.out.println(i + " + " + xOrg);
                    break;
                }
            }
        }
        if (starter == 0) {
            for (int i = xOrg; i < xRed; i--) {
                if (isWhite(yGreen, i)) {
                    starter = 1;
                    starter_y = yGreen;
                    starter_x = i;
                    System.out.println(yGreen + " + " + i);
                    break;
                }
            }
        }
        if (starter == 0) {
            for (int i = yGreen; i < yRed; i--) {
                if (isWhite(i, xRed)) {
                    starter = 1;
                    starter_y = i;
                    starter_x = xRed;
                    System.out.println(i + " + " + xRed);
                    break;
                }
            }
        }
        if (starter == 0) {
            System.out.println("Try another place for ROI");
        } else {
            findWhiteNeighbour();
        }


    }

    public static void findWhiteNeighbour() {

        if (starter_x == xRed) System.out.println("Move Up");
        if (starter_x == xOrg) System.out.println("Move Down");
        if (starter_y == yRed) {
            System.out.println("Move right");
            rightMove();
            System.out.println("SHOWTIME");
            System.out.println(clearROI_yx.size());
            for (int i = 0; i < clearROI_yx.size(); i++) {
                System.out.println(i + " - " + clearROI_yx.get(i));
            }
        }
        if (starter_y == yGreen) System.out.println("Move left");

    }

    public static String getStringCoord_yx(int y1, int x1) {
        return y1 + "." + x1;
    }

    public static void findPoligon() {
        fromLeftUpToRightUp();
        fromRightUpToRightDown();
    }

    private static void fromLeftUpToRightUp() {

        next_x = leftUp_x;
        next_y = leftUp_y;
        clearROI_yx.add(getStringCoord_yx(next_y, next_x));// create array with coordinates with white value
        System.out.println(next_y + " - - " + next_x);
        do {
            if (next_y == yRed) {

                rightMove_case_2();

            } else if (next_y > yRed) {

                rightMove_case_1();

            }
           /*
            if (next_y > yRed) {

                rightMove_case_1();

            } else {
                rightMove_case_2();

            }
            */
        } while (!getStringCoord_yx(next_x, next_y).equals(getStringCoord_yx(rightUp_x, rightUp_y)));
    }

    public static void fromRightUpToRightDown() {

        do {
            if (next_x == xOrg) {

                downMove_case_2();

            } else if (next_x < xOrg) {
                downMove_case_1();

            }
            /*
            if (next_x < xOrg) {

                downMove_case_1();

            } else {
                downMove_case_2();

            }*/
        } while (!getStringCoord_yx(next_x, next_y).equals(getStringCoord_yx(rightDown_x, rightDown_y)));
    }

    public static void fromRightDownToLeftDown() {

        do {
            if (next_y < yGreen) {

                //   downMove_case_1();

            } else {
                leftMove_case_2();

            }
        } while (!getStringCoord_yx(next_x, next_y).equals(getStringCoord_yx(leftDown_x, leftDown_y)));
    }

    private static void leftMove_case_2() {

    }


    private static void downMove_case_1() {

        if (isWhite(next_y, next_x + 1)) {
            if (clearROI_yx.contains(getStringCoord_yx(next_y, next_x + 1))) {
                downMove_case_1_1();
            } else {
                next_x = next_x + 1;/////
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }
        } else if (isWhite(next_y + 1, next_x + 1)) {
            next_y = next_y + 1;
            next_x = next_x + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y + 1, next_x)) {
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y + 1, next_x - 1)) {
            next_y = next_y + 1;
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y, next_x - 1)) {
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x - 1)) {
            next_y = next_y - 1;
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x)) {
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x + 1)) {
            next_y = next_y - 1;
            next_x = next_x + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);
        }
    }

    private static void downMove_case_1_1() {

        if (isWhite(next_y + 1, next_x + 1)) {
            if (clearROI_yx.contains(getStringCoord_yx(next_y + 1, next_x + 1))) {
                downMove_case_1_2();
            } else {
                next_y = next_y + 1;
                next_x = next_x + 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }

        } else if (isWhite(next_y + 1, next_x)) {
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y + 1, next_x - 1)) {
            next_y = next_y + 1;
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y, next_x - 1)) {
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x - 1)) {
            next_y = next_y - 1;
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x)) {
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x + 1)) {
            next_y = next_y - 1;
            next_x = next_x + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);
        }


    }

    private static void downMove_case_1_2() {
        if (isWhite(next_y + 1, next_x)) {
            if (clearROI_yx.contains(getStringCoord_yx(next_y + 1, next_x))) {
                downMove_case_1_3();
            } else {
                next_y = next_y + 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }

        } else if (isWhite(next_y + 1, next_x - 1)) {
            next_y = next_y + 1;
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y, next_x - 1)) {
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x - 1)) {
            next_y = next_y - 1;
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x)) {
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x + 1)) {
            next_y = next_y - 1;
            next_x = next_x + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);
        }


    }

    private static void downMove_case_1_3() {

        if (isWhite(next_y + 1, next_x - 1)) {
            if (clearROI_yx.contains(getStringCoord_yx(next_y + 1, next_x - 1))) {
                downMove_case_1_4();
            } else {
                next_y = next_y + 1;
                next_x = next_x - 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }

        } else if (isWhite(next_y, next_x - 1)) {
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x - 1)) {
            next_y = next_y - 1;
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x)) {
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x + 1)) {
            next_y = next_y - 1;
            next_x = next_x + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);
        }
    }

    private static void downMove_case_1_4() {
        if (isWhite(next_y, next_x - 1)) {
            if (clearROI_yx.contains(getStringCoord_yx(next_y, next_x - 1))) {
                downMove_case_1_5();
            } else {
                next_x = next_x - 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }

        } else if (isWhite(next_y - 1, next_x - 1)) {
            next_y = next_y - 1;
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x)) {
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x + 1)) {
            next_y = next_y - 1;
            next_x = next_x + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);
        }
    }

    private static void downMove_case_1_5() {
        if (isWhite(next_y - 1, next_x - 1)) {
            if (clearROI_yx.contains(getStringCoord_yx(next_y - 1, next_x - 1))) {
                downMove_case_1_6();
            } else {
                next_y = next_y - 1;
                next_x = next_x - 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }

        } else if (isWhite(next_y - 1, next_x)) {
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x + 1)) {
            next_y = next_y - 1;
            next_x = next_x + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);
        }
    }

    private static void downMove_case_1_6() {

        if (isWhite(next_y - 1, next_x)) {
            if (clearROI_yx.contains(getStringCoord_yx(next_y - 1, next_x))) {

                next_y = next_y - 1;
                next_x = next_x + 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            } else {
                next_y = next_y - 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }
        } else {
            next_y = next_y - 1;
            next_x = next_x + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);
        }
    }

    private static void downMove_case_2() {

        if (isWhite(next_y + 1, next_x)) {
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y + 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y, next_x - 1)) {
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);
        }

    }

    public static void rightMove() {

        next_x = starter_x;
        next_y = starter_y;
        clearROI_yx.add(getStringCoord_yx(next_y, next_x));// create array with coordinates with white value
        System.out.println(next_y + " - - " + next_x);
        do {
            if (next_y > yRed) {

                rightMove_case_1();

            } else {
                rightMove_case_2();

            }
        } while ((next_x <= (xOrg)) || (next_x != starter_x && next_y != starter_y));

        if (next_x == xOrg) rightMet = true;

    }

    // start from rightMove  if (next_y > yRed)
    public static void rightMove_case_1() {

        if (isWhite(next_y - 1, next_x)) {
            if (clearROI_yx.contains(getStringCoord_yx((next_y - 1), next_x))) {
                rightMove_case_1_1();
            } else {
                next_y = next_y - 1;/////
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }


        } else if (isWhite(next_y - 1, next_x + 1)) {
            next_y = next_y - 1;
            next_x = next_x + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y, next_x + 1)) {
            next_x = next_x + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y + 1, next_x + 1)) {
            next_x = next_x + 1;
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y + 1, next_x)) {
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y + 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y, next_x - 1)) {
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);
        }
    }

    public static void rightMove_case_1_1() {

        if (isWhite(next_y - 1, next_x + 1)) {
            /// next_y = next_y - 1;
            ///next_x = next_x + 1;
            if (clearROI_yx.contains(getStringCoord_yx((next_y - 1), (next_x + 1)))) {
                rightMove_case_1_2();
            } else {
                next_y = next_y - 1;
                next_x = next_x + 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }

        } else if (isWhite(next_y, next_x + 1)) {
            next_x = next_x + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y + 1, next_x + 1)) {
            next_x = next_x + 1;
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y + 1, next_x)) {
            next_y = next_y + 1;
            System.out.println(next_y + " - - " + next_x);
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));//////

        } else if (isWhite(next_y + 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y, next_x - 1)) {
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        }

    }

    public static void rightMove_case_1_2() {
        if (isWhite(next_y, next_x + 1)) {
            // next_x = next_x + 1;

            if (clearROI_yx.contains(getStringCoord_yx(next_y, (next_x + 1)))) {
                rightMove_case_1_3();
            } else {
                next_x = next_x + 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }

        } else if (isWhite(next_y + 1, next_x + 1)) {
            next_x = next_x + 1;
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y + 1, next_x)) {
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y + 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y, next_x - 1)) {
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        }
    }

    public static void rightMove_case_1_3() {
        if (isWhite(next_y + 1, next_x + 1)) {
            /// next_x = next_x + 1;
            /// next_y = next_y + 1;
            if (clearROI_yx.contains(getStringCoord_yx((next_y + 1), (next_x + 1)))) {
                rightMove_case_1_4();
            } else {
                next_x = next_x + 1;
                next_y = next_y + 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }

        } else if (isWhite(next_y + 1, next_x)) {
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y + 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y, next_x - 1)) {
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        }

    }

    public static void rightMove_case_1_4() {

        if (isWhite(next_y + 1, next_x)) {
            //  next_y = next_y + 1;
            if (clearROI_yx.contains(getStringCoord_yx((next_y + 1), next_x))) {
                rightMove_case_1_5();
            } else {
                next_y = next_y + 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }


        } else if (isWhite(next_y + 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y, next_x - 1)) {
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        }
    }

    public static void rightMove_case_1_5() {

        if (isWhite(next_y + 1, next_x - 1)) {
            /// next_x = next_x - 1;
            /// next_y = next_y + 1;
            if (clearROI_yx.contains(getStringCoord_yx((next_y + 1), (next_x - 1)))) {
                rightMove_case_1_6();
            } else {
                next_x = next_x - 1;
                next_y = next_y + 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }


        } else if (isWhite(next_y, next_x - 1)) {
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y - 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        }
    }

    public static void rightMove_case_1_6() {
        if (isWhite(next_y, next_x - 1)) {
            next_x = next_x - 1;
            if (clearROI_yx.contains(getStringCoord_yx(next_y, next_x))) {
                clearROI_yx.add(getStringCoord_yx(next_y - 1, next_x - 1));
                System.out.println((next_y - 1) + " - - " + (next_x - 1));
            } else {
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }


        } else if (isWhite(next_y - 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        }
    }

    public static void rightMove_case_2() {

        if (isWhite(next_y, next_x + 1)) {

            if (clearROI_yx.contains(getStringCoord_yx(next_y, next_x + 1))) {
                rightMove_case_1_3();
            } else {
                next_x = next_x + 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }

        } else if (isWhite(next_y + 1, next_x + 1)) {

            if (clearROI_yx.contains(getStringCoord_yx(next_y + 1, next_x + 1))) {
                rightMove_case_1_4();
            } else {
                next_x = next_x + 1;
                next_y = next_y + 1;
                clearROI_yx.add(getStringCoord_yx(next_y, next_x));
                System.out.println(next_y + " - - " + next_x);
            }

        } else if (isWhite(next_y + 1, next_x)) {
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y + 1, next_x - 1)) {
            next_x = next_x - 1;
            next_y = next_y + 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        } else if (isWhite(next_y, next_x - 1)) {
            next_x = next_x - 1;
            clearROI_yx.add(getStringCoord_yx(next_y, next_x));
            System.out.println(next_y + " - - " + next_x);

        }

    }


    public static void upDown(Mat matRoi) {

        for (int i = 0; i < matRoi.cols(); i++) {

            for (int j = 0; j < matRoi.rows(); j++) {

                double[] colValue = matRoi.get(j, i);
                int r = (int) colValue[0];
                int g = (int) colValue[1];
                int b = (int) colValue[2];
                String rgb_String = r + ";" + g + ";" + b;
                //  System.out.println("---------");
                //  System.out.println(rgb_String);

                int color = getColorAlias(rgb_String);

                if (i == 0 && j == 0) {
                    findRequiredArray(color, START_POINT, j, i);

                } else if (i == 0 && j != 0) {
                    findRequiredArray(color, SEMI_USUAL_POINT_X_0, j, i);

                } else if (i != 0 && j == 0) {

                    findRequiredArray(color, SEMI_USUAL_POINT_Y_0, j, i);

                } else if (i != 0 && j != 0) {
                    findRequiredArray(color, USUAL_POINT, j, i);
                }
            }
        }

        addColorsToBorderPoints(allColors_AL, borderPoints_AL);

        //  showBorderPointsArrays();


        // showArrays();
        // System.out.println(matRoi.cols());
        // System.out.println(matRoi.rows());

    }

    public static void downUp(Mat matRoi) {

        ArrayList<Integer> helpAl_forBorderPoint = new ArrayList<>();

        for (int i = 0; i < matRoi.cols(); i++) {

            for (int c = (matRoi.rows() - 2); c >= 0; c--) {

                double[] colValue = matRoi.get(c, i);
                int r = (int) colValue[0];
                int g = (int) colValue[1];
                int b = (int) colValue[2];
                String rgb_String = r + ";" + g + ";" + b;

                int color1 = getColorAlias(rgb_String);

                for (int j = 0; j < allColors_AL.size(); j++) {

                    if ((int) allColors_AL.get(j).get(0).get(4) == color1) {

                        int _c = c + 1;
                        if (isBorderPointDownUp(allColors_AL.get(j), _c, i)) {
                            helpAl_forBorderPoint.add(c);
                            helpAl_forBorderPoint.add(i);
                            searchAddReqCoordinates(allColors_AL, _c, i, helpAl_forBorderPoint);
                            searchAddReqCoordinates(allColors_AL, c, i, helpAl_forBorderPoint);
                            borderPoints_AL.add((ArrayList) helpAl_forBorderPoint.clone());
                            helpAl_forBorderPoint.clear();
                            downUpBorderPoints++;
                        }
                        break;
                    }
                }
            }
        }
    }

    public static void leftRight(Mat matRoi) {

        ArrayList<Integer> helpAl_forBorderPoint = new ArrayList<>();

        for (int i = 0; i < matRoi.rows(); i++) {

            for (int c = 1; c < matRoi.cols(); c++) {

                double[] colValue = matRoi.get(i, c);
                int r = (int) colValue[0];
                int g = (int) colValue[1];
                int b = (int) colValue[2];
                String rgb_String = r + ";" + g + ";" + b;

                int color1 = getColorAlias(rgb_String);

                for (int j = 0; j < allColors_AL.size(); j++) {

                    if ((int) allColors_AL.get(j).get(0).get(4) == color1) {

                        int _c = c - 1;

                        if (isBorderPointDownUp(allColors_AL.get(j), i, _c)) {
                            helpAl_forBorderPoint.add(i);
                            helpAl_forBorderPoint.add(c);
                            searchAddReqCoordinates(allColors_AL, i, _c, helpAl_forBorderPoint);
                            searchAddReqCoordinates(allColors_AL, i, c, helpAl_forBorderPoint);
                            borderPoints_AL.add((ArrayList) helpAl_forBorderPoint.clone());
                            helpAl_forBorderPoint.clear();
                            leftRightBorderPoints++;
                        }
                        break;
                    }
                }
            }
        }
        //  showBorderPointsArrays();
    }

    public static void rightLeft(Mat matRoi) {

        ArrayList<Integer> helpAl_forBorderPoint = new ArrayList<>();

        for (int i = 0; i < matRoi.rows(); i++) {

            for (int c = (matRoi.cols() - 2); c >= 0; c--) {

                double[] colValue = matRoi.get(i, c);
                int r = (int) colValue[0];
                int g = (int) colValue[1];
                int b = (int) colValue[2];
                String rgb_String = r + ";" + g + ";" + b;

                int color1 = getColorAlias(rgb_String);

                for (int j = 0; j < allColors_AL.size(); j++) {

                    if ((int) allColors_AL.get(j).get(0).get(4) == color1) {

                        int _c = c + 1;

                        if (isBorderPointDownUp(allColors_AL.get(j), i, _c)) {
                            helpAl_forBorderPoint.add(i);
                            helpAl_forBorderPoint.add(c);
                            searchAddReqCoordinates(allColors_AL, i, _c, helpAl_forBorderPoint);
                            searchAddReqCoordinates(allColors_AL, i, c, helpAl_forBorderPoint);
                            borderPoints_AL.add((ArrayList) helpAl_forBorderPoint.clone());
                            helpAl_forBorderPoint.clear();
                            rightLeftBorderPoints++;
                        }
                        break;
                    }
                }
            }
        }
    }

    //downUp leftRight
    public static void diagonal_1(Mat matRoi) {

        int y = matRoi.rows();
        int x = matRoi.cols();

        ArrayList<Integer> helpAl_forBorderPoint = new ArrayList<>();

        for (int d = 0; d <= (y - 2); d++) {

            int x1 = 1;

            for (int y1 = d; y1 >= 0; y1--) {

                double[] colValue = matRoi.get(y1, x1);
                int r = (int) colValue[0];
                int g = (int) colValue[1];
                int b = (int) colValue[2];
                String rgb_String = r + ";" + g + ";" + b;

                int color1 = getColorAlias(rgb_String);

                for (int j = 0; j < allColors_AL.size(); j++) {

                    if ((int) allColors_AL.get(j).get(0).get(4) == color1) {

                        int y2 = y1 + 1;
                        int x2 = x1 - 1;

                        if (isBorderPointDiagonal(allColors_AL.get(j), y2, x2, y1, x1)) {

                            helpAl_forBorderPoint.add(y1);
                            helpAl_forBorderPoint.add(x1);

                            searchAddReqCoordinates(allColors_AL, y2, x2, helpAl_forBorderPoint);
                            searchAddReqCoordinates(allColors_AL, y1, x1, helpAl_forBorderPoint);
                            borderPoints_AL.add((ArrayList) helpAl_forBorderPoint.clone());
                            helpAl_forBorderPoint.clear();
                            downLeftUpRightBorderPoints++;
                        }
                        break;
                    }
                }
                x1++;
                if (x1 >= x) break;
            }
        }

        for (int i = 2; i < x; i++) {

            int y1 = y - 2;

            for (int x1 = i; x1 < x; x1++) {

                double[] colValue = matRoi.get(y1, x1);
                int r = (int) colValue[0];
                int g = (int) colValue[1];
                int b = (int) colValue[2];
                String rgb_String = r + ";" + g + ";" + b;

                int color1 = getColorAlias(rgb_String);

                for (int j = 0; j < allColors_AL.size(); j++) {

                    if ((int) allColors_AL.get(j).get(0).get(4) == color1) {

                        int y2 = y1 + 1;
                        int x2 = x1 - 1;

                        if (isBorderPointDiagonal(allColors_AL.get(j), y2, x2, y1, x1)) {

                            helpAl_forBorderPoint.add(y1);
                            helpAl_forBorderPoint.add(x1);

                            searchAddReqCoordinates(allColors_AL, y2, x2, helpAl_forBorderPoint);
                            searchAddReqCoordinates(allColors_AL, y1, x1, helpAl_forBorderPoint);
                            borderPoints_AL.add((ArrayList) helpAl_forBorderPoint.clone());
                            helpAl_forBorderPoint.clear();
                            downLeftUpRightBorderPoints++;
                        }
                        break;
                    }
                }
                y1--;
            }
        }
    }

    //upDown rightLeft
    public static void diagonal_2(Mat matRoi) {

        int y = matRoi.rows();
        int x = matRoi.cols();

        ArrayList<Integer> helpAl_forBorderPoint = new ArrayList<>();

        for (int d = 2; d <= x; d++) {

            int x1 = (d - 2);

            for (int y1 = 1; y1 < d; y1++) {

                double[] colValue = matRoi.get(y1, x1);
                int r = (int) colValue[0];
                int g = (int) colValue[1];
                int b = (int) colValue[2];
                String rgb_String = r + ";" + g + ";" + b;

                int color1 = getColorAlias(rgb_String);

                for (int j = 0; j < allColors_AL.size(); j++) {

                    if ((int) allColors_AL.get(j).get(0).get(4) == color1) {

                        int y2 = y1 - 1;
                        int x2 = x1 + 1;

                        if (isBorderPointDiagonal(allColors_AL.get(j), y2, x2, y1, x1)) {

                            helpAl_forBorderPoint.add(y1);
                            helpAl_forBorderPoint.add(x1);

                            searchAddReqCoordinates(allColors_AL, y2, x2, helpAl_forBorderPoint);
                            searchAddReqCoordinates(allColors_AL, y1, x1, helpAl_forBorderPoint);
                            borderPoints_AL.add((ArrayList) helpAl_forBorderPoint.clone());
                            helpAl_forBorderPoint.clear();
                            upRightDownLeftBorderPoints++;
                        }
                        break;
                    }
                }
                x1--;
                if (x1 < 0) break;
            }
        }

        for (int d = 2; d <= y; d++) {

            int x1 = (x - 2);

            for (int y1 = d; y1 < y; y1++) {

                double[] colValue = matRoi.get(y1, x1);
                int r = (int) colValue[0];
                int g = (int) colValue[1];
                int b = (int) colValue[2];
                String rgb_String = r + ";" + g + ";" + b;

                int color1 = getColorAlias(rgb_String);
                for (int j = 0; j < allColors_AL.size(); j++) {

                    if ((int) allColors_AL.get(j).get(0).get(4) == color1) {

                        int y2 = y1 - 1;
                        int x2 = x1 + 1;

                        if (isBorderPointDiagonal(allColors_AL.get(j), y2, x2, y1, x1)) {

                            helpAl_forBorderPoint.add(y1);
                            helpAl_forBorderPoint.add(x1);

                            searchAddReqCoordinates(allColors_AL, y2, x2, helpAl_forBorderPoint);
                            searchAddReqCoordinates(allColors_AL, y1, x1, helpAl_forBorderPoint);
                            borderPoints_AL.add((ArrayList) helpAl_forBorderPoint.clone());
                            helpAl_forBorderPoint.clear();
                            upRightDownLeftBorderPoints++;
                        }
                        break;
                    }
                }
                x1--;
            }
        }

    }

    // upDown leftRight
    public static void diagonal_3(Mat matRoi) {

        int y = matRoi.rows();
        int x = matRoi.cols();

        ArrayList<Integer> helpAl_forBorderPoint = new ArrayList<>();

        for (int st = 1; st < x; st++) {

            int x1 = st;

            for (int y1 = 1; y1 < y; y1++) {

                double[] colValue = matRoi.get(y1, x1);
                int r = (int) colValue[0];
                int g = (int) colValue[1];
                int b = (int) colValue[2];
                String rgb_String = r + ";" + g + ";" + b;
                int color1 = getColorAlias(rgb_String);

                for (int j = 0; j < allColors_AL.size(); j++) {

                    if ((int) allColors_AL.get(j).get(0).get(4) == color1) {

                        int y2 = y1 - 1;
                        int x2 = x1 - 1;

                        if (isBorderPointDiagonal(allColors_AL.get(j), y2, x2, y1, x1)) {

                            helpAl_forBorderPoint.add(y1);
                            helpAl_forBorderPoint.add(x1);

                            searchAddReqCoordinates(allColors_AL, y2, x2, helpAl_forBorderPoint);
                            searchAddReqCoordinates(allColors_AL, y1, x1, helpAl_forBorderPoint);
                            borderPoints_AL.add((ArrayList) helpAl_forBorderPoint.clone());
                            helpAl_forBorderPoint.clear();
                            upLeftDownRightBorderPoints++;
                        }
                        break;
                    }
                }
                if (x1 == (x - 1)) break;
                x1++;
            }
        }

        for (int st = 2; st < y; st++) {

            int y1 = st;

            for (int x1 = 1; x1 < x; x1++) {

                if (y1 > (y - 1)) break;

                double[] colValue = matRoi.get(y1, x1);
                int r = (int) colValue[0];
                int g = (int) colValue[1];
                int b = (int) colValue[2];
                String rgb_String = r + ";" + g + ";" + b;
                int color1 = getColorAlias(rgb_String);

                for (int j = 0; j < allColors_AL.size(); j++) {

                    if ((int) allColors_AL.get(j).get(0).get(4) == color1) {

                        int y2 = y1 - 1;
                        int x2 = x1 - 1;

                        if (isBorderPointDiagonal(allColors_AL.get(j), y2, x2, y1, x1)) {

                            helpAl_forBorderPoint.add(y1);
                            helpAl_forBorderPoint.add(x1);

                            searchAddReqCoordinates(allColors_AL, y2, x2, helpAl_forBorderPoint);
                            searchAddReqCoordinates(allColors_AL, y1, x1, helpAl_forBorderPoint);
                            borderPoints_AL.add((ArrayList) helpAl_forBorderPoint.clone());
                            helpAl_forBorderPoint.clear();
                            upLeftDownRightBorderPoints++;
                        }
                        break;
                    }
                }
                y1++;
            }
        }
    }

    //downUp rightLeft
    public static void diagonal_4(Mat matRoi) {

        int y = matRoi.rows();
        int x = matRoi.cols();

        ArrayList<Integer> helpAl_forBorderPoint = new ArrayList<>();

        for (int st = (y - 2); st >= 0; st--) {

            int y1 = st;

            for (int x1 = (x - 2); x1 >= 0; x1--) {

                double[] colValue = matRoi.get(y1, x1);
                int r = (int) colValue[0];
                int g = (int) colValue[1];
                int b = (int) colValue[2];
                String rgb_String = r + ";" + g + ";" + b;
                int color1 = getColorAlias(rgb_String);

                for (int j = 0; j < allColors_AL.size(); j++) {

                    if ((int) allColors_AL.get(j).get(0).get(4) == color1) {

                        int y2 = y1 + 1;
                        int x2 = x1 + 1;

                        if (isBorderPointDiagonal(allColors_AL.get(j), y2, x2, y1, x1)) {

                            helpAl_forBorderPoint.add(y1);
                            helpAl_forBorderPoint.add(x1);

                            searchAddReqCoordinates(allColors_AL, y2, x2, helpAl_forBorderPoint);
                            searchAddReqCoordinates(allColors_AL, y1, x1, helpAl_forBorderPoint);
                            borderPoints_AL.add((ArrayList) helpAl_forBorderPoint.clone());
                            helpAl_forBorderPoint.clear();
                            downRightUpLeftBorderPoints++;
                        }
                        break;
                    }
                }
                y1--;
                if (y1 < 0) break;
            }
        }

        for (int st = (x - 3); st >= 0; st--) {

            int x1 = st;

            for (int y1 = (y - 2); y1 >= 0; y1--) {

                double[] colValue = matRoi.get(y1, x1);
                int r = (int) colValue[0];
                int g = (int) colValue[1];
                int b = (int) colValue[2];
                String rgb_String = r + ";" + g + ";" + b;
                int color1 = getColorAlias(rgb_String);

                for (int j = 0; j < allColors_AL.size(); j++) {

                    if ((int) allColors_AL.get(j).get(0).get(4) == color1) {

                        int y2 = y1 + 1;
                        int x2 = x1 + 1;

                        if (isBorderPointDiagonal(allColors_AL.get(j), y2, x2, y1, x1)) {

                            helpAl_forBorderPoint.add(y1);
                            helpAl_forBorderPoint.add(x1);

                            searchAddReqCoordinates(allColors_AL, y2, x2, helpAl_forBorderPoint);
                            searchAddReqCoordinates(allColors_AL, y1, x1, helpAl_forBorderPoint);
                            borderPoints_AL.add((ArrayList) helpAl_forBorderPoint.clone());
                            helpAl_forBorderPoint.clear();
                            downRightUpLeftBorderPoints++;
                        }
                        break;
                    }
                }
                x1--;
                if (x1 < 0) break;
            }
        }
    }

    public static boolean isBorderPointDiagonal(ArrayList<ArrayList> aL, int yPrev, int xPrev, int yCur, int xCur) {

        boolean isBorder = true;

        for (int i = 1; i < aL.size(); i++) {

            if (((int) aL.get(i).get(0) == yPrev) && ((int) aL.get(i).get(1) == xPrev)) {

                if (getColorSquare(aL, yCur, xCur) == (int) aL.get(i).get(2)) {
                    isBorder = false;

                }
                break;
            }
        }
        return isBorder;
    }

    public static int getColorSquare(ArrayList<ArrayList> aL, int y3, int x3) {

        int colorSquare = 0;

        for (int i = 1; i < aL.size(); i++) {

            if (((int) aL.get(i).get(0) == y3) && ((int) aL.get(i).get(1) == x3)) {

                colorSquare = (int) aL.get(i).get(2);
                break;
            }
        }
        return colorSquare;
    }

    public static boolean isBorderPointDownUp(ArrayList<ArrayList> aL, int y1, int x1) {

        boolean isBorder = true;

        for (int i = 1; i < aL.size(); i++) {

            if (((int) aL.get(i).get(0) == y1) && ((int) aL.get(i).get(1) == x1)) {
                isBorder = false;
                break;
            }
        }
        return isBorder;
    }

    public static void addColorsToBorderPoints(ArrayList<ArrayList<ArrayList>> arrayWithColors, ArrayList<ArrayList> arrayWithBorderPoints) {

        for (int i = 0; i < arrayWithBorderPoints.size(); i++) {

            int yC = (int) arrayWithBorderPoints.get(i).get(0);
            int xC = (int) arrayWithBorderPoints.get(i).get(1);

            searchAddFromCoordinates(arrayWithColors, yC, xC, arrayWithBorderPoints.get(i));
            searchAddReqCoordinates(arrayWithColors, yC, xC, arrayWithBorderPoints.get(i));
            //searchAddFromCoordinates(arrayWithColors, yC, xC, arrayWithBorderPoints.get(i));

        }

    }

    public static void searchAddFromCoordinates(ArrayList<ArrayList<ArrayList>> arrayWithColors, int y, int x, ArrayList<Integer> arrayWithBP) {

        for (int i = 0; i < arrayWithColors.size(); i++) {

            ArrayList<ArrayList> specificColor = arrayWithColors.get(i);

            int clr = (int) specificColor.get(0).get(4);

            for (int j = 1; j < specificColor.size(); j++) {

                if (((int) specificColor.get(j).get(0) == (y - 1)) && ((int) specificColor.get(j).get(1) == x)) {

                    int clrSq = (int) specificColor.get(j).get(2);

                    arrayWithBP.add(clr);
                    arrayWithBP.add(clrSq);

                    break;

                }
            }
        }
    }

    public static void searchAddReqCoordinates(ArrayList<ArrayList<ArrayList>> arrayWithColors, int y, int x, ArrayList<Integer> arrayWithBP) {

        for (int i = 0; i < arrayWithColors.size(); i++) {

            ArrayList<ArrayList> specificColor = arrayWithColors.get(i);

            int clr = (int) specificColor.get(0).get(4);

            for (int j = 1; j < specificColor.size(); j++) {

                if (((int) specificColor.get(j).get(0) == y) && ((int) specificColor.get(j).get(1) == x)) {

                    int clrSq = (int) specificColor.get(j).get(2);

                    arrayWithBP.add(clr);
                    arrayWithBP.add(clrSq);

                    break;

                }
            }
        }
    }

    public static void findRequiredArray(int colorNumber, String pointPosition, int y1, int x1) {

        ArrayList<Integer> helpAl_forBorderPoint = new ArrayList<>();

        for (int i = 0; i < allColors_AL.size(); i++) {
            // found required array and start work with it
            if ((int) allColors_AL.get(i).get(0).get(4) == colorNumber) {

                if (pointPosition.equals(USUAL_POINT)) {

                    if (isVerticalRelation(pointPosition, allColors_AL.get(i), y1, x1)) {


                        if (isHorizontalRelation(pointPosition, allColors_AL.get(i), y1, x1)) {
                            putUsualPointCoordinates(allColors_AL.get(i), USUAL_POINT, y1, x1, VERT_HOR_RLTN);
                        } else {
                            putUsualPointCoordinates(allColors_AL.get(i), USUAL_POINT, y1, x1, VERT_RLTN);
                        }
                    } else {
                        helpAl_forBorderPoint.add(y1);
                        helpAl_forBorderPoint.add(x1);
                        borderPoints_AL.add((ArrayList) helpAl_forBorderPoint.clone());
                        helpAl_forBorderPoint.clear();
                        upDownBorderPoints++;

                        if (isHorizontalRelation(pointPosition, allColors_AL.get(i), y1, x1)) {
                            putUsualPointCoordinates(allColors_AL.get(i), USUAL_POINT, y1, x1, HOR_RLTN);
                        } else {
                            putUsualPointCoordinates(allColors_AL.get(i), USUAL_POINT, y1, x1, NO_RLTN);
                        }
                    }
                }

                if (pointPosition.equals(START_POINT)) {
                    putCoordinates(allColors_AL.get(i), pointPosition, y1, x1);
                }

                if (pointPosition.equals(SEMI_USUAL_POINT_X_0)) {

                    if (isBorderPoint(SEMI_USUAL_POINT_X_0, allColors_AL.get(i), y1, x1)) {

                        putCoordinates(allColors_AL.get(i), SEMI_USUAL_POINT_X_0, y1, x1);

                        helpAl_forBorderPoint.add(y1);
                        helpAl_forBorderPoint.add(x1);
                        borderPoints_AL.add((ArrayList) helpAl_forBorderPoint.clone());
                        helpAl_forBorderPoint.clear();
                        upDownBorderPoints++;

                    } else {
                        putCoordinates(allColors_AL.get(i), SEMI_USUAL_POINT_X_0, y1, x1);
                    }
                }

                if (pointPosition.equals(SEMI_USUAL_POINT_Y_0)) {

                    if (isColorArrayContainPoint(allColors_AL.get(i), y1, x1)) {

                        putCoordinates(allColors_AL.get(i), SEMI_USUAL_POINT_Y_0, y1, x1);

                    } else {
                        putCoordinates(allColors_AL.get(i), SEMI_USUAL_POINT_Y_0, y1, x1);
                    }
                }
                break;
            }
        }
    }

    public static void putUsualPointCoordinates(ArrayList<ArrayList> aL, String pointPosition, int y5, int x5, String relationType) {

        ArrayList<Integer> help_AL = new ArrayList<>();
        int s = aL.size();

        if (s > 1) {

            if (relationType.equals(VERT_RLTN)) {
                help_AL.add(y5);
                help_AL.add(x5);
                help_AL.add(squareNumVrtcl);
                help_AL.add(0);
                aL.add((ArrayList) help_AL.clone());
                help_AL.clear();

            } else if (relationType.equals(HOR_RLTN)) {
                help_AL.add(y5);
                help_AL.add(x5);
                help_AL.add(squareNumHrzntl);
                help_AL.add(isFirst);
                aL.add((ArrayList) help_AL.clone());
                help_AL.clear();

            } else if (relationType.equals(NO_RLTN)) {

                squareNum = getMaxSquareNum(aL) + 1;
                help_AL.add(y5);
                help_AL.add(x5);
                help_AL.add(squareNum);
                help_AL.add(1);
                aL.add((ArrayList) help_AL.clone());
                help_AL.clear();

            } else if (relationType.equals(VERT_HOR_RLTN)) {

                if (squareNumHrzntl > squareNumVrtcl) {

                    squareNum = squareNumVrtcl;

                    changeSquareNum(squareNumHrzntl, squareNum, aL);

                } else if (squareNumHrzntl < squareNumVrtcl) {

                    squareNum = squareNumHrzntl;

                    changeSquareNum(squareNumVrtcl, squareNum, aL);

                } else if (squareNumHrzntl == squareNumVrtcl) {

                    squareNum = squareNumHrzntl;
                }
                help_AL.add(y5);
                help_AL.add(x5);
                help_AL.add(squareNum);
                help_AL.add(0);
                aL.add((ArrayList) help_AL.clone());
                help_AL.clear();
            }

        } else if (s == 1) {
            help_AL.add(y5);// add Y coordinate
            help_AL.add(x5);// add X coordinate
            help_AL.add(1);// startPoint, new color startColorPoint
            help_AL.add(1);// startPoint, startColorPoint
            aL.add((ArrayList) help_AL.clone());
            help_AL.clear();
        }

    }

    public static void changeSquareNum(int exSqNum, int reqSqNum, ArrayList<ArrayList> aL) {

        for (int i = 0; i < aL.size(); i++) {
            if ((int) aL.get(i).get(2) == exSqNum) {
                // int isF = (int) aL.get(i).get(3);
                aL.get(i).remove(3);// remove isFirst index
                aL.get(i).remove(2);// remove squarenNum index
                aL.get(i).add(reqSqNum);
                aL.get(i).add(0);
            }
        }
    }

    public static boolean isVerticalRelation(String pointPosition, ArrayList<ArrayList> aL, int y1, int x1) {

        boolean isRelated = false;
        int s = aL.size();

        if (s > 1) {
            if (pointPosition.equals(USUAL_POINT)) {
                // get color from upper previous point
                if (((int) aL.get(s - 1).get(0) == y1 - 1) && ((int) aL.get(s - 1).get(1) == x1)) {
                    squareNumVrtcl = (int) aL.get(s - 1).get(2);
                    isFirst = 0;
                    isRelated = true;

                } else {
                    squareNumVrtcl = getMaxSquareNum(aL) + 1;
                    isFirst = 1;
                }
            }
        }
        return isRelated;
    }

    public static boolean isHorizontalRelation(String pointPosition, ArrayList<ArrayList> aL, int y5, int x5) {

        boolean isRelated = false;

        int aLsize = aL.size();

        if (aLsize > 1) {

            for (int i = 1; i < aLsize; i++) {

                int _y = (int) aL.get(i).get(0);
                int _x = (int) aL.get(i).get(1);

                if (_y == y5 && _x == (x5 - 1)) {

                    isRelated = true;
                    squareNumHrzntl = (int) aL.get(i).get(2);
                    isFirst = 0;
                    break;

                } else {

                    squareNumHrzntl = getMaxSquareNum(aL) + 1;
                    isFirst = 1;

                }
            }
        }
        return isRelated;
    }

    public static boolean isColorArrayContainPoint(ArrayList<ArrayList> aL, int y4, int x4) {

        boolean contain = false;

        int aLsize = aL.size();

        if (aLsize > 1) {

            for (int i = 1; i < aLsize; i++) {

                int _y = (int) aL.get(i).get(0);
                int _x = (int) aL.get(i).get(1);

                if (_y == y4 && _x == (x4 - 1)) {

                    contain = true;
                    squareNum = (int) aL.get(i).get(2);
                    isFirst = 0;
                    break;

                } else {
                    contain = false;
                    squareNum = getMaxSquareNum(aL) + 1;
                    isFirst = 1;
                }
            }
        } else {
            contain = false;
            isFirst = 1;
            squareNum = 1;
        }
        return contain;
    }

    public static int getMaxSquareNum(ArrayList<ArrayList> aL) {

        int num = 0;

        for (int i = 1; i < aL.size(); i++) {

            if ((int) aL.get(i).get(2) > num) {
                num = (int) aL.get(i).get(2);
            }
        }
        return num;
    }

    public static void putCoordinates(ArrayList<ArrayList> aL, String pointPosition, int y2, int x2) {

        ArrayList<Integer> help_AL = new ArrayList<>();
        int s = aL.size();

        if (pointPosition.equals(START_POINT)) {

            help_AL.add(y2);// add Y coordinate
            help_AL.add(x2);// add X coordinate
            help_AL.add(1);// startPoint, new color startColorPoint
            help_AL.add(1);// startPoint, startColorPoint
            aL.add((ArrayList) help_AL.clone());
            help_AL.clear();
        }

        if (pointPosition.equals(SEMI_USUAL_POINT_X_0)) {

            if (s == 1) {
                help_AL.add(y2);// add Y coordinate
                help_AL.add(x2);// add X coordinate
                help_AL.add(1);// startPoint, new color startColorPoint
                help_AL.add(1);// startPoint, startColorPoint
                aL.add((ArrayList) help_AL.clone());
                help_AL.clear();

            } else {
                help_AL.add(y2);
                help_AL.add(x2);
                help_AL.add(squareNum);
                help_AL.add(isFirst);
                aL.add((ArrayList) help_AL.clone());
                help_AL.clear();
            }
        }

        if (pointPosition.equals(SEMI_USUAL_POINT_Y_0)) {
            if (s == 1) {
                help_AL.add(y2);// add Y coordinate
                help_AL.add(x2);// add X coordinate
                help_AL.add(1);// startPoint, new color startColorPoint
                help_AL.add(1);// startPoint, startColorPoint
                aL.add((ArrayList) help_AL.clone());
                help_AL.clear();

            } else {
                help_AL.add(y2);
                help_AL.add(x2);
                help_AL.add(squareNum);
                help_AL.add(isFirst);
                aL.add((ArrayList) help_AL.clone());
                help_AL.clear();
            }
        }
    }

    public static boolean isBorderPoint(String pointPosition, ArrayList<ArrayList> aL, int y1, int x1) {

        boolean isBorder = false;

        int s = aL.size();

        if (pointPosition.equals(SEMI_USUAL_POINT_X_0)) {
            // get color from upper previous point
            if ((int) aL.get(s - 1).get(0) == y1 - 1) {
                squareNum = (int) aL.get(s - 1).get(2);
                isBorder = false;
                isFirst = 0;
            }
            if ((int) aL.get(s - 1).get(0) != y1 - 1) {
                squareNum = (int) aL.get(s - 1).get(2) + 1;
                isBorder = true;
                isFirst = 1;
            }
        }
        return isBorder;
    }

    public static int getColorAlias(String clrStringValue) {
        int clrAlias = -1;
        for (int i = 0; i < colorDefiningAl.size(); i++) {
            if (colorDefiningAl.get(i).get(1).equals(clrStringValue)) {
                clrAlias = Integer.valueOf((String) colorDefiningAl.get(i).get(0));
                break;
            }
        }
        return clrAlias;
    }

    public static void showBorderPointsArrays() {

        System.out.println("borderPoints_AL " + borderPoints_AL.size());
    }
}
