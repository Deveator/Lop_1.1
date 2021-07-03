package com.example.lop_11;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lop_11.CustomView.DrawRect;
import com.example.lop_11.CustomView.MyImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.DMatch;
import org.opencv.core.KeyPoint;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.KAZE;
import org.opencv.imgproc.Imgproc;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.lop_11.ContourImage.getColorLines;
import static com.example.lop_11.ContourImage.rgb_string_array;
import static com.example.lop_11.CustomView.DrawRect.xOrg;
import static com.example.lop_11.CustomView.DrawRect.xRed;
import static com.example.lop_11.CustomView.DrawRect.yGreen;
import static com.example.lop_11.CustomView.DrawRect.yRed;
import static com.example.lop_11.ImageResize.newW;
import static com.example.lop_11.KmeansStuff.colorInWhite;
import static com.example.lop_11.KmeansStuff.createColorArrays;
import static com.example.lop_11.KmeansStuff.returnToOriginal;
import static com.example.lop_11.KmeansStuff.showClusters;
import static com.example.lop_11.KmeansStuff.viceVersaColorInWhite;
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
import static com.example.lop_11.LabImage.hasWhiteSpace;
import static com.example.lop_11.LabImage.yx_Values;
import static com.example.lop_11.ZoomStuff.insertZoomedInOrg;
import static com.example.lop_11.ZoomStuff.returnFromX4EveryMatPoint;
import static com.example.lop_11.ZoomStuff.x4EveryMatPoint;
import static com.example.lop_11.ZoomStuff.x_start;
import static com.example.lop_11.ZoomStuff.x_stop;
import static com.example.lop_11.ZoomStuff.y_start;
import static com.example.lop_11.ZoomStuff.y_stop;
import static org.opencv.core.Core.kmeans;
import static org.opencv.imgproc.Imgproc.THRESH_TOZERO;
import static org.opencv.imgproc.Imgproc.cvtColor;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static boolean isChanged = false;
    private GestureDetector mDetector;
    TextView alphaTv, betaTv;
    Button bttn7, bttn10, bttn11, bttn12, bttn13, bttn14, bttn24, bttn31;
    public static String path;
    public static int screenWidth, idealWidth, idealHeight, originalHeight, originalWidth;
    public static Mat oImageClusterColored, imgROIfromClustered, oImage, notChangedOImage, add_oImage, kMeansRoi, imgROIfromClustered_dbl, imgROIfromClustered_3, imgROIfromClustered_notChanged;
    Bitmap bitmapS;
    EditText eT, eT1;
    public static int doubleTapCount = 3;// using in MyImageView class
    public static int clusterStep = 10;
    public static double alpha = 1.0;
    public static double beta = 0.0;
    View view3;
    Spinner spinner;
    MyImageView iV, iVadd;
    MyImageView submatL, submatR;
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
    public static boolean isZoomed = false;
    public static int count2 = 3;


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
        mDetector = new GestureDetector(this, new MyGestureListener());
        // Add a touch listener to the view
        // The touch listener passes all its events on to the gesture detector
        iV.setOnTouchListener(touchListener);
        iVadd = findViewById(R.id.imageVadd);
        submatL = findViewById(R.id.submatL);
        submatR = findViewById(R.id.submatR);
        iV2 = findViewById(R.id.imageV2);
        iVadd2 = findViewById(R.id.imageVadd2);
        view3 = findViewById(R.id.imageV5);
        eT1 = findViewById(R.id.eT1);
        bttn31 = findViewById(R.id.button31);
        alphaTv = findViewById(R.id.alphaTv);
        alphaTv.setText(String.valueOf(alpha));
        betaTv = findViewById(R.id.betaTv);
        betaTv.setText(String.valueOf(beta));

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);

   /*     spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                String ss = spinner.getSelectedItem().toString();
                Toast.makeText(getBaseContext(), ss, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
*/

    }

    // This touch listener passes everything on to the gesture detector.
    // That saves us the trouble of interpreting the raw touch events
    // ourselves.
    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // pass the events to the gesture detector
            // a return value of true means the detector is handling it
            // a return value of false means the detector didn't
            // recognize the event
            return mDetector.onTouchEvent(event);

        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Mat left = new Mat();
        Mat right= new Mat();
        String ss = spinner.getSelectedItem().toString();
       // displayImage(oImage, iV);
        switch(ss){
            case("Rotate") :
                StitchAction.rotate(oImage, 90);
                displayImage(oImage, iV);
                System.out.println(oImage.cols());
                System.out.println(oImage.rows());
                break;

            case("Submat"):
                left = oImage.submat(0,oImage.rows() - 1, 0, 500);
                displayImage(left, submatL);

                right = oImage.submat(0,oImage.rows() - 1, 250, oImage.cols() - 1);
                displayImage(right, submatR);

                break;

            case("Stitch"):

                long startTime = System.nanoTime();

                Imgproc.cvtColor(left, left, Imgproc.COLOR_RGB2GRAY);
                Imgproc.cvtColor(right, right, Imgproc.COLOR_RGB2GRAY);

                // At this point search for keypoints in both images and compute the matches
                MatOfKeyPoint keyPoints1 = new MatOfKeyPoint();
                MatOfKeyPoint keyPoints2 = new MatOfKeyPoint();

                Mat descriptors1 = new Mat();
                Mat descriptors2 = new Mat();
                // Since FeatureDetector and Descriptor extractor are marked deprecated and
                // crash whatever value they get, use this construct for detecting and computing...
                // Source: https://stackoverflow.com/questions/36691050/opencv-3-list-of-available-featuredetectorcreate-and-descriptorextractorc
                KAZE kaze = KAZE.create();
                kaze.detect(left, keyPoints1);
                kaze.detect(right, keyPoints2);
                kaze.compute(left, keyPoints1, descriptors1);
                kaze.compute(right, keyPoints2, descriptors2);

                MatOfDMatch matches = new MatOfDMatch();

                DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
                matcher.match(descriptors1, descriptors2, matches);

                // Calculate min and max distance between the keypoints in the two images.
                double max_dist = 0; double min_dist = 100;
                List<DMatch> listMatches = matches.toList();

                for( int i = 0; i < listMatches.size(); i++ ) {
                    double dist = listMatches.get(i).distance;
                    if( dist < min_dist ) min_dist = dist;
                    if( dist > max_dist ) max_dist = dist;
                }
                Log.i(this.getClass().getSimpleName(), "Min: " + min_dist);
                Log.i(this.getClass().getSimpleName(), "Max: " + max_dist);

                // Reduce the list of matching keypoints to a list of good matches...
                LinkedList<DMatch> good_matches = new LinkedList<DMatch>();
                MatOfDMatch goodMatches = new MatOfDMatch();
                for(int i = 0; i < listMatches.size(); i++) {
                    if(listMatches.get(i).distance < 2*min_dist) {
                        good_matches.addLast(listMatches.get(i));
                    }
                }

                goodMatches.fromList(good_matches);
                Log.i(this.getClass().getSimpleName(), "Number of matches: " + listMatches.size());
                Log.i(this.getClass().getSimpleName(), "Number of good matches: " + good_matches.size());

                // Calculate the homograohy between the two images...
                LinkedList<Point> imgPoints1List = new LinkedList<Point>();
                LinkedList<Point> imgPoints2List = new LinkedList<Point>();
                List<KeyPoint> keypoints1List = keyPoints1.toList();
                List<KeyPoint> keypoints2List = keyPoints2.toList();

                for(int i = 0; i<good_matches.size(); i++) {
                    imgPoints1List.addLast(keypoints1List.get(good_matches.get(i).queryIdx).pt);
                    imgPoints2List.addLast(keypoints2List.get(good_matches.get(i).trainIdx).pt);
                }

                MatOfPoint2f obj = new MatOfPoint2f();
                obj.fromList(imgPoints1List);
                MatOfPoint2f scene = new MatOfPoint2f();
                scene.fromList(imgPoints2List);

                Mat H = Calib3d.findHomography(obj, scene, Calib3d.RANSAC,3);

                int imageWidth = right.cols();
                int imageHeight = right.rows();

                // To avoid missing some of the possible stitching scenarios, we offset the homography to the middle of a mat which has three time the size of one of the pictures.
                // Extracted from this: https://stackoverflow.com/questions/21618044/stitching-2-images-opencv
                Mat Offset = new Mat(3, 3, H.type());
                Offset.put(0,0, new double[]{1});
                Offset.put(0,1, new double[]{0});
                Offset.put(0,2, new double[]{imageWidth});
                Offset.put(1,0, new double[]{0});
                Offset.put(1,1, new double[]{1});
                Offset.put(1,2, new double[]{imageHeight});
                Offset.put(2,0, new double[]{0});
                Offset.put(2,1, new double[]{0});
                Offset.put(2,2, new double[]{1});

                // Multiply the homography mat with the offset.
                Core.gemm(Offset, H, 1, new Mat(), 0, H);

                Mat obj_corners = new Mat(4,1,CvType.CV_32FC2);
                Mat scene_corners = new Mat(4,1,CvType.CV_32FC2);

                obj_corners.put(0,0, new double[]{0,0});
                obj_corners.put(0,0, new double[]{imageWidth,0});
                obj_corners.put(0,0,new double[]{imageWidth,imageHeight});
                obj_corners.put(0,0,new double[]{0,imageHeight});

                Core.perspectiveTransform(obj_corners, scene_corners, H);

                // The resulting mat will be three times the size (width and height) of one of the source images. (We assume, that both images have the same size.
                Size s = new Size(imageWidth *3,imageHeight*3);
                Mat img_matches = new Mat(new Size(left.cols()+right.cols(),left.rows()), CvType.CV_32FC2);

                // Perform the perspective warp of img1 with the given homography and place it on the large result mat.
                Imgproc.warpPerspective(left, img_matches, H, s);

                // Create another mat which is used to hold the second image and place it in the middle of the large sized result mat.
                int m_xPos = (int)(img_matches.size().width/2 - right.size().width/2);
                int m_yPos = (int)(img_matches.size().height/2 - right.size().height/2);
                Mat m = new Mat(img_matches,new Rect(m_xPos, m_yPos, right.cols(), right.rows()));

                // Copy img2 to the mat in the middle of the large result mat
                right.copyTo(m);

                // Some debug logging... and some duration logging following...
                Log.i(this.getClass().getSimpleName(), "Size of img2: width=" + right.size().width + "height=" + right.size().height);
                Log.i(this.getClass().getSimpleName(), "Size of m: width=" + m.size().width + "height=" + m.size().height);
                Log.i(this.getClass().getSimpleName(), "Size of img_matches: width=" + img_matches.size().width + "height=" + img_matches.size().height);

                long elapsedTime = System.nanoTime() - startTime;
                elapsedTime = elapsedTime / 1000000; // Milliseconds (1:1000000)
                Log.i(this.getClass().getSimpleName(), "Stitching 2 images took " + elapsedTime + "ms");
                //loadedImagesText.append("Stitching 2 images took " + elapsedTime + "ms\n");

                // The resulting mat is way to big. It holds a lot of empty "transparent" space.
                // We will not crop the image, so that only the "region of interest" remains.
                startTime = System.nanoTime();
                int stepping = 6;

                //Rect imageBoundingBox3 = findImageBoundingBox2(img_matches, stepping, true);

                elapsedTime = System.nanoTime() - startTime;
                elapsedTime = elapsedTime / 1000000; // Milliseconds (1:1000000)
                //Log.i(this.getClass().getSimpleName(), "Resulting rect has tl(x=" + imageBoundingBox3.tl().x + ", y=" + imageBoundingBox3.tl().y +") and br(x=" + imageBoundingBox3.br().x + ", y=" + imageBoundingBox3.br().y +") with stepping="+stepping+" and auto-correct=true\n");
                Log.i(this.getClass().getSimpleName(), "Cropping stitched image (v2.1) took " + elapsedTime + "ms");

                //loadedImagesText.append("Resulting rect has tl(x=" + imageBoundingBox3.tl().x + ", y=" + imageBoundingBox3.tl().y +") and br(x=" + imageBoundingBox3.br().x + ", y=" + imageBoundingBox3.br().y +") with stepping="+stepping+" and auto-correct=true\n");
                //loadedImagesText.append("Cropping stitched image (v2.1) took " + elapsedTime + "ms\n");

                // Extract the calculated region of interest from the result mat.
               // Mat regionOfInterest = img_matches.submat(imageBoundingBox3);





        }


        //String ss = spinner.getSelectedItem().toString();
        //int po = spinner.getSelectedItemPosition();
        //Toast.makeText(getBaseContext(), ss + " - " + po, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // In the SimpleOnGestureListener subclass you should override
    // onDown and any other gesture that you want to detect.
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("TAG", "onDown: ");

            // don't return false here or else none of the other
            // gestures will work
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("TAG", "onSingleTapConfirmed: ");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("TAG", "onLongPress: ");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            int matWidth = newW;
            int screenWidth = 1080;
            Log.i("newW", String.valueOf(newW));
            Log.i("TAG", "onDoubleTap: ");
            int x = (int) e.getX();
            x = x - (1080 - newW) / 2;
            Log.i("X", String.valueOf(x));
            int y = (int) e.getY();
            Log.i("Y", String.valueOf(y));
            imgROIfromClustered = ZoomStuff.zoomExample(x, y, oImage);
            imgROIfromClustered_notChanged = ZoomStuff.zoomExample(x, y, notChangedOImage);
            imgROIfromClustered_dbl = imgROIfromClustered.clone();
            imgROIfromClustered_3 = imgROIfromClustered.clone();
            displayImage(imgROIfromClustered, iV);
            Log.i("Size", String.valueOf(imgROIfromClustered.size()));
            isZoomed = true;


            return true;
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.i("TAG", "onScroll: ");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d("TAG", "onFling: ");
            return true;
        }

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
            add_oImage = oImage.clone();
            notChangedOImage = oImage.clone();
            oImageClusterColored = oImage.clone();
            idealWidth = oImage.cols();
            idealHeight = oImage.rows();
            view3.setVisibility(View.INVISIBLE);

            if (g == 0) {
                displayImage(oImage, iV);
                System.out.println("----------");
                System.out.println(getScreenWidth());
                System.out.println(getScreenHeight());
                g++;
            } else {
                displayImage(oImage, iVadd2);
            }
        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private byte saturate(double val) {
        int iVal = (int) Math.round(val);
        iVal = iVal > 255 ? 255 : (iVal < 0 ? 0 : iVal);
        return (byte) iVal;
    }

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
        if (isZoomed) {

            DrawRect.getCoord();
            double[] whiteClr = {255.0, 255.0, 255.0};
            for (int i = xRed; i < xOrg; i++) {
                for (int j = yRed; j < yGreen; j++) {
                    imgROIfromClustered.put(j, i, whiteClr);
                }
            }
            displayImage(imgROIfromClustered, iV);


        } else {
            DrawRect.getCoord();
            double[] whiteClr = {255.0, 255.0, 255.0};
            for (int i = xRed; i < xOrg; i++) {
                for (int j = yRed; j < yGreen; j++) {
                    oImage.put(j, i, whiteClr);
                }
            }
            displayImage(oImage, iV);
        }


    }

    public double getValFromTv(TextView tv) {
        return Double.parseDouble((String) tv.getText());
    }


    public void alphaPlus(View view) {
        insertZoomedInOrg(imgROIfromClustered, oImage, y_start, y_stop, x_start, x_stop);
        displayImage(oImage, iV);
        isZoomed = false;
        /*
        double d = getValFromTv(alphaTv);
        d++;
        alphaTv.setText(String.valueOf(d));*/
    }

    public void alphaMinus(View view) {
        Mat grMat = new Mat();
        Mat m1 = KmeansStuff.getMatFromROI_km(oImage);
        Imgproc.cvtColor(m1, grMat, Imgproc.COLOR_RGB2GRAY);
        Imgproc.threshold(grMat, grMat, 254, 254, THRESH_TOZERO);
        //  displayImage(grMat, iVadd);

        Imgproc.Canny(grMat, grMat, 50, 200, 3, false);
        // Standard Hough Line Transform
        Mat lines = new Mat(); // will hold the results of the detection
        Imgproc.HoughLines(grMat, lines, 1, Math.PI / 180, 150); // runs the actual detection
        // Draw the lines
        for (int x = 0; x < lines.rows(); x++) {
            double rho = lines.get(x, 0)[0],
                    theta = lines.get(x, 0)[1];
            double a = Math.cos(theta), b = Math.sin(theta);
            double x0 = a * rho, y0 = b * rho;
            Point pt1 = new Point(Math.round(x0 + 1000 * (-b)), Math.round(y0 + 1000 * (a)));
            Point pt2 = new Point(Math.round(x0 - 1000 * (-b)), Math.round(y0 - 1000 * (a)));
            Imgproc.line(grMat, pt1, pt2, new Scalar(0, 0, 255), 3, Imgproc.LINE_AA, 0);
        }


        displayImage(grMat, iVadd);



      /*
        double d = getValFromTv(alphaTv);
        d--;
        alphaTv.setText(String.valueOf(d));
        */
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
        if (isZoomed) {
            // get mat from ROI and get Kmeans matImage
            Mat m1 = KmeansStuff.getMatFromROI_km(imgROIfromClustered);
            kMeansRoi = KmeansStuff.getKMeanImage(m1);
            // put clustered color of ROI in image for work
            KmeansStuff.changeRoiInKmeans(kMeansRoi, imgROIfromClustered_dbl);
            createColorArrays(kMeansRoi, numClust);
        } else {
            // get mat from ROI and get Kmeans matImage
            Mat m1 = KmeansStuff.getMatFromROI_km(notChangedOImage);
            kMeansRoi = KmeansStuff.getKMeanImage(m1);
            // put clustered color of ROI in image for work
            KmeansStuff.changeRoiInKmeans(kMeansRoi, add_oImage);
            createColorArrays(kMeansRoi, numClust);
        }
    }

    // put clustered color of ROI in image for work
    public void _2_stage(View view) {
        if (isZoomed) {
            showClusters(imgROIfromClustered);
            displayImage(imgROIfromClustered, iV);
        } else {
            showClusters(oImage);
            displayImage(oImage, iV);
        }
    }


    public void _3_stage(View view) {
        imgROIfromClustered = returnFromX4EveryMatPoint(imgROIfromClustered);
        displayImage(imgROIfromClustered, iV);
        Log.i("Size fromX4", String.valueOf(imgROIfromClustered.size()));
        System.out.println("3 STAGE COMPLETED");
    }

    public void saveImage(View view) {

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

    // return ROI to original color
    public void _5_stage(View view) {

        if (isZoomed) {
            returnToOriginal(imgROIfromClustered_3, imgROIfromClustered);
            displayImage(imgROIfromClustered, iV);

        } else {
            returnToOriginal(oImageClusterColored, oImage);
            displayImage(oImage, iV);
        }

        System.out.println("5 STAGE COMPLETED");
    }

    // recolor in white
    public void saveShow(View view) {
        if (isZoomed) {
            colorInWhite(imgROIfromClustered);
            displayImage(imgROIfromClustered, iV);
        } else {
            colorInWhite(oImage);
            displayImage(oImage, iV);
        }
    }

    public void viseVersa(View view) {
        if (isZoomed) {
            viceVersaColorInWhite(imgROIfromClustered);
            displayImage(imgROIfromClustered, iV);
            returnToOriginal(imgROIfromClustered_3, imgROIfromClustered);
            displayImage(imgROIfromClustered, iV);
        } else {
            viceVersaColorInWhite(oImage);
            displayImage(oImage, iV);
            returnToOriginal(oImageClusterColored, oImage);
            displayImage(oImage, iV);

        }

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
        displayImage(oImage, iV);
        isZoomed = false;
    }

    public void betaMinus(View view) {

        System.out.println(oImage.size());
        Mat m = oImage.submat(0, oImage.rows(), 0, 250);
        System.out.println(m.size());


    }

}
