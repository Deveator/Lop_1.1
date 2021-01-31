package com.example.lop_11;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lop_11.CustomView.DrawRect;
import com.example.lop_11.CustomView.MyImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.TermCriteria;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.lop_11.ContourImage.getColorLines;
import static com.example.lop_11.ContourImage.rgb_string_array;
import static com.example.lop_11.CustomView.DrawRect.xOrg;
import static com.example.lop_11.CustomView.DrawRect.xRed;
import static com.example.lop_11.CustomView.DrawRect.yGreen;
import static com.example.lop_11.CustomView.DrawRect.yRed;
import static com.example.lop_11.KmeansStuff.colorInWhite;
import static com.example.lop_11.KmeansStuff.createColorArrays;
import static com.example.lop_11.KmeansStuff.returnToOriginal;
import static com.example.lop_11.KmeansStuff.showClusters;
import static com.example.lop_11.Lab.diagonal_1;
import static com.example.lop_11.Lab.diagonal_2;
import static com.example.lop_11.Lab.diagonal_3;
import static com.example.lop_11.Lab.diagonal_4;
import static com.example.lop_11.Lab.downUp;
import static com.example.lop_11.Lab.findChangeDirectionPoints;
import static com.example.lop_11.Lab.findPoligon;
import static com.example.lop_11.Lab.leftRight;
import static com.example.lop_11.Lab.rightLeft;
import static com.example.lop_11.Lab.showBorderPointsArrays;
import static com.example.lop_11.Lab.upDown;
import static com.example.lop_11.LabImage.Lab_Values;
import static com.example.lop_11.LabImage.all_diffX_Values;
import static com.example.lop_11.LabImage.all_diffY_Values;
import static com.example.lop_11.LabImage.clusterS_yx_Values;
import static com.example.lop_11.LabImage.clustersA_;
import static com.example.lop_11.LabImage.clustersA_Index;
import static com.example.lop_11.LabImage.clustersB_;
import static com.example.lop_11.LabImage.clustersB_Index;
import static com.example.lop_11.LabImage.clustersB_IndexCopy;
import static com.example.lop_11.LabImage.clustersL_;
import static com.example.lop_11.LabImage.clustersL_Index;
import static com.example.lop_11.LabImage.clustersL_IndexCopy;
import static com.example.lop_11.LabImage.diffA_Values;
import static com.example.lop_11.LabImage.diffB_Values;
import static com.example.lop_11.LabImage.diffL_Values;
import static com.example.lop_11.LabImage.diffrent_Lab_indexes;
import static com.example.lop_11.LabImage.diffrent_Lab_values;
import static com.example.lop_11.LabImage.finalSortedCluster;
import static com.example.lop_11.LabImage.yx_Values;
import static org.opencv.core.Core.kmeans;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class MainActivity extends AppCompatActivity {


    TextView alphaTv, betaTv;
    Button bttn7, bttn10, bttn11, bttn12, bttn13, bttn14, bttn24, bttn31;
    public static String path;
    public static int screenWidth, idealWidth, idealHeight, originalHeight, originalWidth;
    public static Mat oImageClusterColored, imgROIfromClustered, oImage, add_oImage, kMeansRoi;
    Bitmap bitmapS;
    EditText eT, eT1;
    public static int doubleTapCount = 3;// using in MyImageView class
    public static int clusterStep = 10;
    public static double alpha = 1.0;
    public static double beta = 0.0;
    View view3;
    MyImageView iV, iVadd;
    MyImageView iV2, iVadd2;
    static int m = 3;
    static int m2 = 3;
    int clickCount = 0;
    static int numColorSections, numClust = 2;
    public static int _xFromROI;
    public static int _yFromROI;
    public static ArrayList<Double> _1_clr = new ArrayList<Double>();
    public static ArrayList<Double> _2_clr = new ArrayList<Double>();
    public static ArrayList<Double> _3_clr = new ArrayList<Double>();
    public static ArrayList<ArrayList> _1_clr_all = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList> _2_clr_all = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList> _3_clr_all = new ArrayList<ArrayList>();
    public static ArrayList<Integer> y1 = new ArrayList<Integer>();
    public static ArrayList<Integer> x1 = new ArrayList<Integer>();

    //public static ArrayList<String> clearROI_yx = new ArrayList<String>();
    //public static ArrayList<Integer> clearROI_y = new ArrayList<Integer>();
    //public static ArrayList<Integer> clearROI_x = new ArrayList<Integer>();

    public static ArrayList<ArrayList> y1_all = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList> x1_all = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList<ArrayList>> allColors_AL = new ArrayList<ArrayList<ArrayList>>();
    public static ArrayList<ArrayList<ArrayList>> allColors_AL_sorted = new ArrayList<ArrayList<ArrayList>>();
    public static ArrayList<ArrayList> borderPoints_AL = new ArrayList<ArrayList>();
    public static ArrayList<String> linesDscrptn = new ArrayList<String>();
    public static ArrayList<ArrayList> all_colorSquare = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList> colorDefiningAl = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList> yColorCoor = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList> yColorCoor_sorted = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList> xColorCoor = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList> xColorCoor_sorted = new ArrayList<ArrayList>();
    public static ArrayList<ArrayList> help123 = new ArrayList<ArrayList>();
    public static boolean start = true;
    public static int g = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.loadLibrary("opencv_java3");
        OpenCVLoader.initDebug();
        bttn7 = findViewById(R.id.button7);
        bttn10 = findViewById(R.id.button10);
        eT = findViewById(R.id.editText);
        eT.setText(String.valueOf(numClust));
        bttn12 = findViewById(R.id.button12);
        // bttn13 = findViewById(R.id.button13);
        bttn14 = findViewById(R.id.button14);
        bttn24 = findViewById(R.id.button24);
        iV = findViewById(R.id.imageV);
        iVadd = findViewById(R.id.imageVadd);
        iV2 = findViewById(R.id.imageV2);
        iVadd2 = findViewById(R.id.imageVadd2);
        view3 = findViewById(R.id.imageV5);
        eT1 = findViewById(R.id.eT1);
        bttn31 = findViewById(R.id.button31);
        alphaTv = findViewById(R.id.alphaTv);
        alphaTv.setText(String.valueOf(alpha));
        betaTv = findViewById(R.id.betaTv);
        betaTv.setText(String.valueOf(beta));

    }

    public void openGallery(View v) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            path = getPath(imageUri);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            screenWidth = displayMetrics.widthPixels;
            // oImage is main image
            oImage = ImageResize.GetResizedImage(path);
            add_oImage = ImageResize.GetResizedImage(path);
            oImageClusterColored = ImageResize.GetResizedImage(path);///
            idealWidth = oImage.cols();
            idealHeight = oImage.rows();
            view3.setVisibility(View.INVISIBLE);

/*
            Mat lookUpTable = new Mat(1, 256, CvType.CV_8U);
            byte[] lookUpTableData = new byte[(int) (lookUpTable.total()*lookUpTable.channels())];
            for (int i = 0; i < lookUpTable.cols(); i++) {
                lookUpTableData[i] = saturate(Math.pow(i / 255.0, 1.2) * 255.0);
            }
            lookUpTable.put(0, 0, lookUpTableData);
            Mat img = new Mat();
            Core.LUT(oImage, lookUpTable, img);

*/
           // oImage.convertTo(oImage, -1, 3.0, 100);// make image more contrast
            //cvtColor(oImage, oImage, COLOR_BGR2GRAY);// added for idea
            if (g == 0) {
                displayImage(oImage, iV);
                //displayImage(getKMeanImage(oImage), iV2);
                g++;
            } else {
                displayImage(oImage, iVadd2);
            }
        }
    }

    private byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = iVal > 255 ? 255 : (iVal < 0 ? 0 : iVal);
        return (byte) iVal;
    }

 /*   public Mat getKMeanImage(Mat img) {

        img.convertTo(img, CvType.CV_32F);
        Mat data = img.reshape(1, (int) img.total());
        int K = 4;
        Mat bestLabels = new Mat();
        TermCriteria criteria = new TermCriteria();
        int attempts = 5;
        int flags = Core.KMEANS_PP_CENTERS;
        Mat centers = new Mat();
        double compactness = Core.kmeans(data, K, bestLabels, criteria, attempts, flags, centers);

        Mat draw = new Mat((int) img.total(), 1, CvType.CV_32FC3);
        Mat colors = centers.reshape(3, K);
        for (int i = 0; i < K; i++) {
            Mat mask = new Mat(); // a mask for each cluster label
            Core.compare(bestLabels, new Scalar(i), mask, Core.CMP_EQ);
            Mat col = colors.row(i); // can't use the Mat directly with setTo() (see #19100)
            double d[] = col.get(0, 0); // can't create Scalar directly from get(), 3 vs 4 elements
            draw.setTo(new Scalar(d[0], d[1], d[2]), mask);
        }
        draw = draw.reshape(3, img.rows());
        draw.convertTo(draw, CvType.CV_8U);
        System.out.println(draw.type());
        return draw;
    }
    */

    private String getPath(Uri uri) {
        if (uri == null) {
            return null;
        } else {
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

            if (cursor != null) {
                int col_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(col_index);
            }
        }
        return uri.getPath();
    }

    private void displayImage(Mat mat, MyImageView v) {
        bitmapS = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.RGB_565);
        Utils.matToBitmap(mat, bitmapS);
        v.setMinimumWidth(idealWidth);
        v.setMaxWidth(idealWidth);
        v.setMinimumHeight(idealHeight);
        v.setMaxHeight(idealHeight);
        v.setImageBitmap(bitmapS);
    }

    public static double[] getListColor() {

        double[] list = new double[]{(double) getRandDouble(0, 254), (double) getRandDouble(0, 254), (double) getRandDouble(0, 254)};
        return list;
    }

    public static int getRandDouble(int min, int max) {

        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public void createColorSectionCoordinates() {

        ArrayList<Integer> aL_y = new ArrayList<>();
        ArrayList<Integer> aL_x = new ArrayList<>();

        for (int i = 0; i < all_colorSquare.size(); i++) {

            ArrayList<Integer> aL_1 = all_colorSquare.get(i);

            ArrayList<ArrayList> aL_1_1 = allColors_AL.get(i);

            for (int j = 0; j < aL_1.size(); j++) {

                for (int l = 1; l < aL_1_1.size(); l++) {

                    if (aL_1.get(j).equals(aL_1_1.get(l).get(2))) {

                        aL_y.add((Integer) aL_1_1.get(l).get(0));
                        aL_x.add((Integer) aL_1_1.get(l).get(1));
                    }
                }
                yColorCoor.add((ArrayList) aL_y.clone());
                xColorCoor.add((ArrayList) aL_x.clone());

                aL_y.clear();
                aL_x.clear();
            }
        }

        numColorSections = yColorCoor.size();
        System.out.println("numColorSectionsnum " + numColorSections);
        System.out.println(yColorCoor.size());
        for (int i = 0; i < yColorCoor.size(); i++) {
            System.out.println(yColorCoor.get(i).size());
        }

        sortColorSquare();

        System.out.println(yColorCoor_sorted.size());
        for (int i = 0; i < yColorCoor_sorted.size(); i++) {
            System.out.println(yColorCoor_sorted.get(i).size());
        }


    }

    // display OR hide ROI - start step
    public void displayHideROI(View view) {
        if (m % 2 != 0) {
            view3.setVisibility(View.VISIBLE);
            m++;
        } else {
            view3.setVisibility(View.INVISIBLE);
            m++;
        }
    }

    // show | hide image with clustered ROI
    public void showClusteredROI(View view) {
        if (m2 % 2 != 0) {
            displayImage(oImageClusterColored, iV);
            m2++;
        } else {
            displayImage(oImage, iV);
            m2++;
        }
    }

    // change num of clusters
    public void submitKNum(View view) {
        String s = eT.getText().toString();
        numClust = Integer.parseInt(s);
        eT.setText(String.valueOf(numClust));


    }

    // sort  created color array from big to little
    public void sortColorSquare() {

        ArrayList<Integer> index = new ArrayList<>();
        ArrayList<Integer> size = new ArrayList<>();
        //   ArrayList<Double> size = new ArrayList<>();


        for (int i = 0; i < yColorCoor.size(); i++) {
            // index.add(i);
            size.add(yColorCoor.get(i).size());
        }
        Collections.sort(size);
        Collections.reverse(size);


        for (int i = 0; i < size.size(); i++) {
            int s = size.get(i);

            for (int j = 0; j < yColorCoor.size(); j++) {
                if (yColorCoor.get(j).size() == s) {
                    yColorCoor_sorted.add(yColorCoor.get(j));
                    xColorCoor_sorted.add(xColorCoor.get(j));
                    yColorCoor.remove(j);
                    xColorCoor.remove(j);
                    break;
                }
            }
        }

        // for (int i = 0; i < allColors_AL_sorted.size(); i++) {
        //      System.out.println("allColors_AL_sorted get " + i + " " + allColors_AL_sorted.get(i).size());
        //   }
    }

    // color in white every color section
    public void recolorInWhiteColorSection() {

        if (clickCount <= numColorSections) {

            if (!y1.isEmpty()) {
                y1.clear();
                x1.clear();
                _1_clr.clear();
                _2_clr.clear();
                _3_clr.clear();
            }

            double[] whiteClr = {255.0, 255.0, 255.0};

            ArrayList<Integer> aL_y = yColorCoor_sorted.get(clickCount);
            ArrayList<Integer> aL_x = xColorCoor_sorted.get(clickCount);

            for (int i = 0; i < aL_y.size(); i++) {

                double[] originalColor = oImage.get(aL_y.get(i) + _yFromROI, aL_x.get(i) + _xFromROI);

                // it is for overlaying
                if (originalColor[0] != 255.0 && originalColor[1] != 255.0 && originalColor[2] != 255.0) {

                    y1.add(aL_y.get(i) + _yFromROI);
                    x1.add(aL_x.get(i) + _xFromROI);

                    _1_clr.add(originalColor[0]);
                    _2_clr.add(originalColor[1]);
                    _3_clr.add(originalColor[2]);
                }

                oImage.put(aL_y.get(i) + _yFromROI, aL_x.get(i) + _xFromROI, whiteClr);

            }
            y1_all.add((ArrayList) y1.clone());
            x1_all.add((ArrayList) x1.clone());
            _1_clr_all.add((ArrayList) _1_clr.clone());
            _2_clr_all.add((ArrayList) _2_clr.clone());
            _3_clr_all.add((ArrayList) _3_clr.clone());
            displayImage(oImage, iV);
            System.out.println("clickCount " + clickCount);
            System.out.println("y1_all " + y1_all.size());
            clickCount++;

        } else {
            Toast.makeText(getBaseContext(), "Lines are finished", Toast.LENGTH_LONG).show();
        }
    }

    // return to original color every color section
    public void recolorInOriginalColorSection() {


        if (clickCount < numColorSections) {
            System.out.println(" **--** ");
            /////////////
            System.out.println("y1_all " + y1_all.size());
            ArrayList<Integer> help_y1 = y1_all.get(x1_all.size() - 1);
            ArrayList<Integer> help_x1 = x1_all.get(y1_all.size() - 1);
            ArrayList<Double> help_1_clr = _1_clr_all.get(_1_clr_all.size() - 1);
            ArrayList<Double> help_2_clr = _2_clr_all.get(_2_clr_all.size() - 1);
            ArrayList<Double> help_3_clr = _3_clr_all.get(_3_clr_all.size() - 1);

            for (int i = 0; i < help_y1.size(); i++) {

                double[] color = {help_1_clr.get(i), help_2_clr.get(i), help_3_clr.get(i)};

                oImage.put(help_y1.get(i), help_x1.get(i), color);
                if (i < 3) {
                    System.out.println(" ***** ");
                    System.out.println("y " + help_y1.get(i));
                    System.out.println("x " + help_x1.get(i));
                }

            }
            displayImage(oImage, iV);
            y1_all.remove(y1_all.size() - 1);
            x1_all.remove(x1_all.size() - 1);
            _1_clr_all.remove(_1_clr_all.size() - 1);
            _2_clr_all.remove(_2_clr_all.size() - 1);
            _3_clr_all.remove(_3_clr_all.size() - 1);

        } else {

            Toast.makeText(getBaseContext(), "Lines are finished", Toast.LENGTH_LONG).show();
        }
    }

    public void changeToCanny(View view) {
        System.out.println(help123.size());
        help123.remove(help123.size() - 1);
        System.out.println(help123.size());
        System.out.println(help123.get(help123.size() - 1).get(0));
        System.out.println(help123.get(help123.size() - 1).get(1));
        System.out.println(help123.get(help123.size() - 1).get(2));

    }

    public void saveImg(View view) {
        // START creating directory for images
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/App_images");
        if (!myDir.exists()) {
            myDir.mkdir();
        }
        // STOP creating directory for images
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        String imgPath = file.toString();
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            System.out.println(155);
            // instead of 'zoomedBitmap' should be another
            bitmapS.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(getApplicationContext(), imgPath + " is saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //make white
    public void fullWhite(View view) {
        DrawRect.getCoord();
        double[] whiteClr = {255.0, 255.0, 255.0};
        for (int i = xRed; i < xOrg; i++) {
            for (int j = yRed; j < yGreen; j++) {
                oImage.put(j, i, whiteClr);
            }
        }
        displayImage(oImage, iV);


    }

    public double getValFromTv(TextView tv){
        return Double.parseDouble((String) tv.getText());
    }


    public void alphaPlus(View view) {
        double d = getValFromTv(alphaTv);
        d++;
        alphaTv.setText(String.valueOf(d));
    }
 
    public void alphaMinus(View view) {
        double d = getValFromTv(alphaTv);
        d--;
        alphaTv.setText(String.valueOf(d));
    }



    public void clearROI(View view) {

        if (!start) {
            clearData();
        }
        DrawRect.getCoord();
        findChangeDirectionPoints();
        findPoligon();
        //  findStarter();
    }

    public void minusMaxIntense(View view) {
    }

    public void upDownChange(View view) {
    }

    public void downUpChange(View view) {
        //   getClusterStep();
        //   System.out.println(clusterStep);
    }

    public void setClusterStep() {
        String s = String.valueOf(eT.getText());
        clusterStep = Integer.parseInt(s);
    }

    public void clearData() {
        numColorSections = 0;
        clickCount = 0;
        xColorCoor_sorted.clear();
        yColorCoor_sorted.clear();
        xColorCoor.clear();
        yColorCoor.clear();
        Lab_Values.clear();
        yx_Values.clear();
        diffrent_Lab_values.clear();
        diffrent_Lab_indexes.clear();
        diffA_Values.clear();
        diffB_Values.clear();
        diffL_Values.clear();
        clustersA_.clear();
        clustersA_Index.clear();
        clustersB_.clear();
        clustersB_Index.clear();
        clustersB_IndexCopy.clear();
        clustersL_.clear();
        clustersL_Index.clear();
        clustersL_IndexCopy.clear();
        clusterS_yx_Values.clear();
        finalSortedCluster.clear();
        all_diffX_Values.clear();
        all_diffY_Values.clear();
        rgb_string_array.clear();
        allColors_AL.clear();
        colorDefiningAl.clear();
        borderPoints_AL.clear();
        all_colorSquare.clear();
    }


    // get mat from ROI and get Kmeans matImage
    public void _1_stage(View view) {
        Mat m1 = KmeansStuff.getMatFromROI_km(oImage);
        kMeansRoi = KmeansStuff.getKMeanImage(m1);
    }

    // put clustered color of ROI in image for work
    public void _2_stage(View view) {
        KmeansStuff.changeRoiInKmeans(kMeansRoi, add_oImage);
        //    displayImage(oImage, iV);
        System.out.println("2 STAGE COMPLETED");
    }

    public void _3_stage(View view) {
        createColorArrays(kMeansRoi, numClust);
        System.out.println("3 STAGE COMPLETED");
    }

    // show clusters stepByStep
    public void _4_stage(View view) {
        showClusters(oImage);
        displayImage(oImage, iV);
        System.out.println("4 STAGE COMPLETED");
    }

    // return ROI to original color
    public void _5_stage(View view) {
        returnToOriginal(oImage);
        displayImage(oImage, iV);
        System.out.println("5 STAGE COMPLETED");
    }

    // recolor in white
    public void saveShow(View view) {
        colorInWhite(oImage);
        displayImage(oImage, iV);
    }

    // clear stuff
    public void _7_stage(View view) {
        KmeansStuff.count = 0;
        KmeansStuff._Y.clear();
        KmeansStuff._X.clear();
        KmeansStuff._f.clear();
        System.out.println("7 STAGE COMPLETED");
    }

    // color in white every color section
    public void white(View view) {
        recolorInWhiteColorSection();
    }

    // return to original color every color section
    public void orgnl(View view) {
        recolorInOriginalColorSection();
    }

    public void getZoomedBitmap(View view) {
    }

}
