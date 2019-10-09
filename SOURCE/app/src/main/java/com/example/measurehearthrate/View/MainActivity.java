package com.example.measurehearthrate.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.measurehearthrate.ArrayBeat;
import com.example.measurehearthrate.Filter;
import com.example.measurehearthrate.Database.DatabaseHelper;
import com.example.measurehearthrate.Processing.ImageProcessing;
import com.example.measurehearthrate.Model.ItemHistory;
import com.example.measurehearthrate.Adapter.ListAge;
import com.example.measurehearthrate.Adapter.ListHistories;
import com.example.measurehearthrate.MyToast;
import com.example.measurehearthrate.MyXAxisValueFormatter;
import com.example.measurehearthrate.R;
import com.example.measurehearthrate.StatCamera;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.navigation.NavigationView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context,MainActivity.class));
    }


    @Override
    protected void onStart() {
        super.onStart();
        // load camera

    }

    // Filter
    private Spinner filterState;
    private ArrayList<String> listStateLabel;
    private Filter gfilter = new Filter("", "", 1);
    // config time
    private int TIME_CONFIG = 17;//s
    private int take_image = 0;
    // Age Feature
    private RecyclerView list_age = null;
    // Database SQlite
    DatabaseHelper mData;
    int isRefreshHistory = -1;
    // History
    private ListView view_history = null;
    private ListHistories listHistories = null;
    // Menu
    private View content_main = null;
    private View content_history = null;
    private View content_chart = null;
    private View content_age = null;
    public int state = -1;
    public int choose_age = -1;
    // Chart
    private LineGraphSeries<DataPoint> graphView;
    private GraphView graphChart = null;
    private TextView result_avg = null;
    private TextView result_max = null;
    private TextView result_min = null;
    private TextView time_max = null;
    private TextView time_min = null;
    private BarChart graphBarChart = null;
    // Line Chart
    private LineChart lineChart = null;
    // ********************************************************************************
    // processing
    private static final AtomicBoolean processing = new AtomicBoolean(false);
    public Boolean doing = false;
    public Boolean waitingRed = false;

    private SurfaceView preview = null;
    private SurfaceHolder previewHolder = null;
    private Camera camera = null;
    private TextView hearthbeat = null;
    private final Toast toast = null;
    private ProgressBar progressBar = null;
    private int percent = 0;
    // for view camera
    private TextureView textureView;
    private CameraManager mCameraManager;
    private String cameraId;
    private Size imageDimension;
    protected CameraDevice cameraDevice;
    protected CaptureRequest.Builder captureRequestBuilder;
    private ImageReader mImageReader;
    protected CameraCaptureSession cameraCaptureSessions;
    private ImageReader imageReader;
    private int max_width = 0;
    private int max_height = 0;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private int maxPercent = 1000;
    private int minPercent = 0;
    // permission
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    //handler
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    // Processing Image
    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];


    private static int beatsIndex = 0;
    private static final int  beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long  startTime = 0;
    public static enum TYPE {
        GREEN, RED
    }
    // check
    private TextView tcheck = null;

    // type
    private static MainActivity.TYPE currentType = MainActivity.TYPE.GREEN;
    public static MainActivity.TYPE getCurrent() {
        return currentType;
    }

    // image
    private ImageView heartframe = null;
    public ImageView firer = null;

    // thread progressBar
    public Thread ThreadStart = null;
    public Thread ThreadStop = null;

    // modal - result
    public ConstraintLayout modal_save = null;
    public Button close_modal_save = null;
    public Button close_modal_without_save = null;
    public TextView content_modal = null;
    public Boolean modal = false;

    // Khoi tao cac view
    private void InitView() {
            //Init texture
            textureView = (TextureView) findViewById(R.id.camera_view);

            assert textureView != null;

            textureView.setSurfaceTextureListener(textureListener);
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    // OPEN CAMERA
    private void openCamera() {
        //to get manage of all cameras
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        mCameraManager = manager;
        try {
            //Lay 1 camera
            cameraId = manager.getCameraIdList()[0];
            // to get features of the selected camera
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            // to get stream configuration from features
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//            CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM
            assert map != null;
            // to get the size that the camera supports
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            max_width = 176;
            max_height = 144;
            /* D/w+h: 4128 3096 === 176x144
            Log.d("w+h", String.valueOf(imageDimension.getWidth()) + " " + String.valueOf(imageDimension.getHeight()));
            */
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_CAMERA_PERMISSION);
                    return;
                }
                return;
            }
            //OPEN CAMERA VA CALLBACK
            manager.openCamera(cameraId, stateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    // CALL BACK CUA CAMERA
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            //Khi Camera Open thi thuc thi Ham Show Preview
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };


    // Ham hien thi CAMERA PREVIEW
    private void createCameraPreview() {
        try {
            //setUpCameraOutputs(1,1);
            //Get SurfaceTexture từ stream images
            SurfaceTexture texture = textureView.getSurfaceTexture();

            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            // to set the format of captured images and the maximum number of images that can be accessed in mImageReader
            //  mImageReader = ImageReader.newInstance(imageDimension.getWidth(),imageDimension.getHeight(), ImageFormat.YUV_420_888,2);
            mImageReader = ImageReader.newInstance(max_width, max_height, ImageFormat.YUV_420_888,2);
            mImageReader.setOnImageAvailableListener(mOnImageAvaiableListener, mBackgroundHandler);

            // Our new output surface for preview frame data
            Surface mImageSurface = mImageReader.getSurface();

            // the first added target surface is for camera PREVIEW display
            captureRequestBuilder.addTarget(surface);
            // the second added target mImageReader.getSurface() is for ImageReader Callback where we can access EACH frame
            captureRequestBuilder.addTarget(mImageSurface);

            //output Surface
            List<Surface> outputSurfaces = new ArrayList<>();
            outputSurfaces.add(surface);
            outputSurfaces.add(mImageSurface);

            // Here, we create a CameraCaptureSession for camera preview.
            cameraDevice.createCaptureSession(outputSurfaces, new CameraCaptureSession.StateCallback(){
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    //The camera is already closed
                    if (null == cameraDevice) {
                        return;
                    }
                    // When the session is ready, we start displaying the preview.
                    cameraCaptureSessions = cameraCaptureSession;
                    updatePreview(false);
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(MainActivity.this, "Configuration change", Toast.LENGTH_SHORT).show();
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    private StatCamera statCamera = new StatCamera(1, 1);
    public Boolean isOnFlash = false;

    private void updatePreview(Boolean check) {
        isOnFlash = check;
        if(check == true)
        //  captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
        else
            captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (Throwable e){}
//                CameraAccessException e) {
//            e.printStackTrace();
//        }
    }
    public int kk = -1;
    public long _time = 0, __time;
    public int fps_device;
    private ImageReader.OnImageAvailableListener mOnImageAvaiableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            kk += 1;

            if(_time == 0) _time = System.currentTimeMillis();
            else if(_time > 0){
                    __time = (System.currentTimeMillis() - _time) / 1000l;
                    if(__time > 0){
                    fps_device = (int) (kk/__time);
//                    Log.d("FPS: ", String.valueOf(fps_device) + " ---- ");
                }
            }
            final int final_fps = fps_device;
            Image image = null;
            image = reader.acquireNextImage();
            final Image.Plane[] planes = image.getPlanes();
            final int total = planes[0].getRowStride() * 144;
            int[] mRgbBuffer = new int[total];
            ImageProcessing imageProcessing = new ImageProcessing();
//            Log.d("kakaka: ", String.valueOf(!(state == R.id.nav_measure || (state == R.id.nav_measure_by_age && choose_age != -1))));
            if(modal == true || !(state == R.id.nav_measure || (state == R.id.nav_measure_by_age && choose_age != -1))) {
                if (image != null) {
                    image.close();
                }
                return;
            }

            byte[] mh = imageProcessing.YUV_420_888toNV21(image);

            ArrayList<Double> _mh = imageProcessing.decodeYUV420SPtoRedAvg(mh, image.getWidth(), image.getHeight());

            if (image != null) {
                image.close();
            }
            Thread newThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        double percentr = _mh.get(0)*100;
                        double avgr = _mh.get(1);
                        double avgg = _mh.get(2);
                        double avgb = _mh.get(3);

//            Double result1 = imageProcessing.getRGBIntFromPlanes(image);

                        final int imgAvg = (int) Math.floor(avgr);

                        int state = statCamera.state(imgAvg, percentr, avgg, avgb);
                        if(state == 0) {
                            if(isOnFlash == true && arrayBeat.getSeconds() > 1) {
                                updatePreview(false);
                                statCamera.reset();
                                arrayBeat.reset();
                            }
                        } else if(state == 2){
                            updatePreview(true);
                        }
                        final int beatsAvg;

                        if(statCamera.getCondition() == true) {
                            arrayBeat.add(imgAvg);
                            // start beating
                            if (percent == minPercent || percent == maxPercent) {
                                if(percent == minPercent) {
                                    doing = false;
                                    if(ThreadStop != null){
                                        ThreadStop = null;
                                    }
                                }
                                if (waitingRed == true && modal == false) {
                                    waitingRed = false;
                                    myHandler.sendEmptyMessage(minPercent);
                                    percent = minPercent;
                                    startTime = 0;
                                    beats = 0;
                                    if(ThreadStop != null){
                                        ThreadStop = null;
                                    }
                                }

                                if (doing == false) {
                                    doing = true;
                                    startTime = System.currentTimeMillis();
                                    ThreadStart = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int i = 0; i <= maxPercent+1; i++) {
                                                if (doing == true) {
                                                    try {
                                                        percent = i;
                                                        if (percent >= maxPercent) percent = maxPercent;
                                                        myHandler.sendEmptyMessage(percent);
                                                        Thread.sleep(TIME_CONFIG);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }
                                                } else break;
                                            }
                                        }
                                    });
                                    ThreadStart.start();
                                }
                            }
                            if(doing == true){
                            /*if(statCamera.getMode() == 2) {
                                Thread draw = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        drawLineChart.sendEmptyMessage(imgAvg);
                                    }
                                });
                                draw.start();
                            }*/
                                beatsAvg = (int) arrayBeat.exec(final_fps, (int) __time);
//                                Log.d("no thread:", String.valueOf(final_fps));
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(beatsAvg != 0) {
                                            hearthbeat.setText(String.valueOf(beatsAvg));
                                            renderFire(beatsAvg);
                                        } else {
                                            hearthbeat.setText("#");
                                            renderFire(0);
                                        }
                                    };
                                });
//                    Log.d("Di", /*String.valueOf(beatsAvg) +*/ " " + String.valueOf(arrayBeat.getBrightness().size()) + " " + String.valueOf(arrayBeat.getSeconds()) + " " + String.valueOf(arrayBeat.getFps()));
                                if (percent >= maxPercent) {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
//                                ArrayList<Double> ar = arrayBeat.getBrightness();
//                                String s = new String();
//                                s += "[";
//                                for(int i=0; i < ar.size();i++)
//                                    s += String.valueOf(ar.get(i)) + ", ";
//                                int fps = arrayBeat.getFps();
//                                long times = arrayBeat.getSeconds();
//                                s += "]";
                                            hearthbeat.setText(String.valueOf(beatsAvg));
                                            renderFire(beatsAvg);
                                            content_modal.setText(String.valueOf(beatsAvg));
                                            modal_save.setVisibility(modal_save.VISIBLE);
                                            modal = true;
                                            statCamera.reset();
                                            arrayBeat.reset();
                                        }
                                    });
                                }
                            }
                        } else {
                            // stop anyway
                            if(ThreadStart != null)
                                ThreadStart.interrupt();
                            ThreadStart = null;

                            if(percent > minPercent && percent < maxPercent){
                                doing = false;
                                if(ThreadStop == null) {
                                    ThreadStop = new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            for(int i = percent; i >= 0; i--){
                                                try {
                                                    myHandler.sendEmptyMessage(i);
                                                    ThreadStop.sleep(2);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            percent = 0;
                                            startTime = 0;
                                            beats = 0;
                                        }

                                    });
                                    ThreadStop.start();
                                }
                            } else if(percent >= maxPercent) {
                                percent = maxPercent;
                                waitingRed = true;
                            }
                        }
                    }
            });
            newThread.start();

        }
    };
    public int getValueDrawble(int value){
        if(value == 0){
            return R.drawable.gray;
        } else if(value < 40){
            return R.drawable.light;
        } else if(value < 90){
            return R.drawable.greenlight;
        } else {
            return R.drawable.red;
        }
    }
    public int getValueDrawble(String value){
        int _value;
        try {
            _value = Integer.parseInt(value);
        }
        catch (NumberFormatException e)
        {
            _value = 0;
        }
        return getValueDrawble(_value);
    }
    private void renderFire(int beatsAvg) {
        int drawbleValue = getValueDrawble(beatsAvg);
        firer.setImageDrawable(this.getResources().getDrawable(drawbleValue, null));
    }

    // subroutine to run camera
    protected void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    protected void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //CLOSE CAMERA
    private void closeCamera() {
        try {
            captureRequestBuilder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF);
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(),null,null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        if (null != cameraDevice) {
            cameraDevice.close();
            cameraDevice = null;
        }
        if (null != imageReader) {
            imageReader.close();
            imageReader = null;
        }
    }

    public void addAndRenderHistories(String result){
        DateFormat df = new SimpleDateFormat("HH:mm, dd-MM-yyyy");
        String date = df.format(Calendar.getInstance().getTime());
        EditText _state = (EditText) findViewById(R.id.state);
        String state = _state.getText().toString();
        ItemHistory item = new ItemHistory(getValueDrawble(result), date, result, state);
        listHistories.addItemHistory(item);
        mData.addData(item);
        listHistories.notifyDataSetChanged();
        view_history.setAdapter(listHistories);
    }

    public void refreshHistory(int position){
        listHistories.remove(position);
        listHistories.notifyDataSetChanged();
        view_history.setAdapter(listHistories);
    }

    private ArrayBeat arrayBeat = null;

    @SuppressLint("WrongViewCast")
    @Override
    //*1
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Hearth Rate Calc
        ArrayList<Double> temp = new ArrayList<Double>();
        arrayBeat = new ArrayBeat(temp);
        // Menu
        content_main = (View) findViewById(R.id.content_main);
        content_history = (View) findViewById(R.id.content_history);
        content_chart = (View) findViewById(R.id.content_chart);
        content_age = (View) findViewById(R.id.content_age);
        state = R.id.nav_measure;
        // List Age
        list_age = (RecyclerView) findViewById(R.id.list_age);
        ArrayList<String> ages = new ArrayList<>();
        ages.add("0-6 years old");
        ages.add("7-12 years old");
        ages.add("13-20 years old");
        ages.add("21-25 years old");
        ages.add("25-30 years old");
        ages.add("31-40 years old");
        ages.add("41-50 years old");
        ages.add("50+ years old");
        ListAge ageView = new ListAge(this, ages);
        list_age.setAdapter(ageView);
        list_age.setLayoutManager(new LinearLayoutManager(this));

        // SQLite
        mData = new DatabaseHelper(this);
        // History
        List<ItemHistory> histories = new ArrayList<>();
        histories = mData.getHistorybyFilter(gfilter);
        view_history = (ListView) findViewById(R.id._histories);
        listHistories = new ListHistories(this, R.layout.item_history, histories, mData, new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                isRefreshHistory = msg.what;
            }
        });
        view_history.setAdapter(listHistories);
        new Thread(){
            public void run() {
                ScheduledExecutorService executor =
                        Executors.newSingleThreadScheduledExecutor();

                Runnable periodicTask = new Runnable() {
                    public void run() {
                        // Invoke method(s) to do the work
                        runOnUiThread(new Runnable() {
                            public void run() {
                                if (isRefreshHistory != -1) {
                                    refreshHistory(isRefreshHistory);
                                    isRefreshHistory = -1;
                                }
                            }
                        });
                    }
                };
                executor.scheduleAtFixedRate(periodicTask, 0, 1, TimeUnit.SECONDS);
            }
        }.start();
        // Filter
        filterState = (Spinner) findViewById(R.id.filterState);
        listStateLabel = listHistories.getStateLabel();
        ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listStateLabel);
        filterState.setAdapter(listAdapter);
        filterState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {
                    gfilter = new Filter("", "", 1);
//                    updateChartStat();
                } else {
                    Filter filter = new Filter(listStateLabel.get(position), "", 2);
//                    updateChartStat();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Chart
        graphView = new LineGraphSeries<>();
        graphChart = (GraphView) findViewById(R.id.graph_chart);
        graphBarChart = (BarChart) findViewById(R.id.graph_bar_chart);

        result_min = (TextView) findViewById(R.id.result_min);
        result_max = (TextView) findViewById(R.id.result_max);
        time_min = (TextView) findViewById(R.id.time_min);
        time_max = (TextView) findViewById(R.id.time_max);
        result_avg = (TextView) findViewById(R.id.result_avg);

        updateChartStat();
        // LineChart
        lineChart = (LineChart) findViewById(R.id.line_chart);
        updateLineChart();
        // *******************************************************************************

        modal_save = (ConstraintLayout) findViewById(R.id.modal_save);
        close_modal_save = (Button) findViewById(R.id.close_modal_save);
        close_modal_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modal = false;
                modal_save.setVisibility(modal_save.GONE);
                addAndRenderHistories((String) hearthbeat.getText());
                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                MyToast.makeText(MainActivity.this, "Saved result", MyToast.LONG, MyToast.SUCCESS, false).show();
            }
        });
        close_modal_without_save = (Button) findViewById(R.id.close_modal_without_save);
        close_modal_without_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modal = false;
                modal_save.setVisibility(modal_save.GONE);
            }
        });
        content_modal = (TextView) findViewById(R.id.content_modal);

        // firer
        firer = (ImageView) findViewById(R.id.firer);
        //
        Button b1 = (Button) findViewById(R.id.start1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(percent == minPercent || percent == maxPercent){
                    doing = true;
                    ThreadStart = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i = 0; i <= 101;i++){
                                if(doing == true){
                                    try {
                                        percent = i;
                                        if(percent >= maxPercent) percent = maxPercent;
                                        myHandler.sendEmptyMessage(percent);
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else break;
                            }
                        }
                    });
                    ThreadStart.start();
                }
            }
        });
        Button b2 = (Button) findViewById(R.id.stop1);

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doing = false;
                if(ThreadStart != null)
                    ThreadStart.interrupt();
                ThreadStart = null;
                if(percent > minPercent && percent < maxPercent){
                    ThreadStop = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for(int i = percent; i >= 0; i--){
                                try {
                                    myHandler.sendEmptyMessage(i);
                                    ThreadStop.sleep(30);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            percent = 0;
                        }
                    });
                    ThreadStop.start();
                } else if(percent >= maxPercent) {
                    myHandler.sendEmptyMessage(minPercent);
                    percent = 0;
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressBar = (ProgressBar) findViewById(R.id.per_processing);
        hearthbeat = (TextView) findViewById(R.id.hearthbeat);
        tcheck = (TextView) findViewById(R.id.check);

        // load camera
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
//        }
        camera = (Camera) Camera.open();
        InitView();
        startBackgroundThread();

    }
    Handler drawLineChart = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setData(msg.what);
        }
    };
    ArrayList<Entry> values_line = new ArrayList<>();
    int index = 99, maxValues = 100;
    private void setData(int _default) {
        if(index < 0){
            for(int i = 0;i < maxValues-1;i++){
                values_line.add(i, values_line.get(i+1));
            }
            values_line.add(maxValues-1, new Entry(maxValues-1, _default));
        } else {
            values_line.add(new Entry(index, _default));
            index = index - 1;
        }
        LineDataSet set1;
        if (lineChart.getData() != null && lineChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set1.setValues(values_line);
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            set1 = new LineDataSet(values_line, "Measure Data");
            set1.setDrawCircleHole(true);
            set1.setDrawFilled(false);
            set1.setDrawCircles(false);
            set1.setDrawValues(false);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            lineChart.setData(data);
        }
        lineChart.invalidate();
    }

    private void updateLineChart() {
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getAxisRight().setDrawGridLines(false);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setEnabled(false);
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setEnabled(false);
        lineChart.setDrawBorders(false);
        lineChart.setDrawGridBackground(false);

        // hide legend
        lineChart.getLegend().setEnabled(false);
        // no description text
        lineChart.getDescription().setEnabled(false);

        // enable touch gestures
        lineChart.setTouchEnabled(true);

        // enable scaling and dragging
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);
        lineChart.setAutoScaleMinMaxEnabled(true);
        // ************************************************************
        lineChart.setDrawBorders(false);
        lineChart.invalidate();
        setData(1);
    }

    private void updateChartStat() {
        ArrayList<BarEntry> statData = new ArrayList<BarEntry>();
        List<ItemHistory> histories = listHistories.getListbyFilter(gfilter);
        for(int i = 0;i <= 6; i++) {
            statData.add(new BarEntry(i, listHistories.getAvgHeartRate(i)));
        }
        BarDataSet dataSet = new BarDataSet(statData, "dataSet1");
//        dataSet.setColor(R.color.colorCrimson);

        BarData barData = new BarData();
        barData.addDataSet(dataSet);
        /* thickness */
        barData.setBarWidth(0.1f);
        barData.setDrawValues(false);
        graphBarChart.setDrawValueAboveBar(false);
        graphBarChart.setData(barData);
        /* hide background grid */
        graphBarChart.getXAxis().setDrawGridLines(false);
        graphBarChart.getAxisLeft().setDrawGridLines(true);
        graphBarChart.getAxisRight().setDrawGridLines(false);

        XAxis xAxis = graphBarChart.getXAxis();
        xAxis.setEnabled(true);
        /* setup into bottom */
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setTextSize(8);
        ArrayList<String> lbs = new ArrayList<String>();
        String[] tmp = new String[]{"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
        for(String i : tmp) lbs.add(i);
        xAxis.setValueFormatter(new MyXAxisValueFormatter(lbs));
        /*********************************/

        YAxis yAxis = graphBarChart.getAxisLeft();
        yAxis.setEnabled(true);
        yAxis.setAxisLineWidth(0.1f);
        yAxis.enableGridDashedLine(10f, 10f, 0f);

        ArrayList<String> lbsy = new ArrayList<String>();
        for(int i = 40;i <= 120;i+=10){
//            Log.d("lolo", String.valueOf(i));
            lbsy.add(String.valueOf(i));
        }
        yAxis.setValueFormatter(new MyXAxisValueFormatter(lbsy));
        yAxis.setSpaceTop(100f);
        yAxis.setSpaceBottom(40f);
        yAxis.setAxisMinimum(0f);

        YAxis yAxis2 = graphBarChart.getAxisRight();
        yAxis2.setEnabled(false);

        graphBarChart.setDrawBorders(false);
        graphBarChart.setDrawGridBackground(false);

        // hide legend
        graphBarChart.getLegend().setEnabled(false);
        // no description text
        graphBarChart.getDescription().setEnabled(false);

        // enable touch gestures
        graphBarChart.setTouchEnabled(true);

        // enable scaling and dragging
        graphBarChart.setDragEnabled(false);
        graphBarChart.setScaleEnabled(false);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        graphBarChart.setPinchZoom(false);
        graphBarChart.setAutoScaleMinMaxEnabled(true);
        // ************************************************************
        graphBarChart.setDrawBorders(false);
        graphBarChart.invalidate();

        if(listHistories.isEmpty()){
            result_min.setText("##");
            time_min.setText("--:--,--------");
            result_max.setText("##");
            time_max.setText("--:--,--------");
            result_avg.setText("##");
        } else {
            ItemHistory minimum = listHistories.getMinimumHeartRate();
            result_min.setText(minimum.getResult());
            time_min.setText(minimum.getDate());
            ItemHistory maximum = listHistories.getMaximumHeartRate();
            result_max.setText(maximum.getResult());
            time_max.setText(maximum.getDate());
            result_avg.setText(String.valueOf(listHistories.getAvgHeartRate()));
            listStateLabel = listHistories.getStateLabel();
            ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listStateLabel);
            filterState.setAdapter(listAdapter);
        }

    }

    Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what >= minPercent && msg.what <= maxPercent){
                progressBar.setProgress(msg.what);
            }
        }
    };

    private void increasePercent() {
        percent += 1;
        if(percent >= maxPercent) percent = maxPercent;
        myHandler.sendEmptyMessage(percent);
    }

    private void decreasePercent(){
        percent -= 1;
        if(percent <= minPercent) percent = minPercent;
        myHandler.sendEmptyMessage(percent);
    }
    private void drawerTest(final int speed){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int value = 0;
                int dir = 1;
                while(true){
                    if(percent >= maxPercent) dir = 1;
                    if(percent <= minPercent) dir = 0;
                    if(dir == minPercent){
                        increasePercent();
                    } else {
                        decreasePercent();
                    }
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (textureView.isAvailable()) {
            openCamera();
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
//        wakeLock.acquire();
//        camera = android.hardware.Camera.open();
//        startTime = System.currentTimeMillis();
    }

    @Override

    protected void onPause() {
        closeCamera();
        stopBackgroundThread();
        super.onPause();
//        wakeLock.release();

    }

    // SurfaceHolder
    private SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try{
                camera.setPreviewDisplay(previewHolder);
                camera.setPreviewCallback(new Camera.PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, android.hardware.Camera camera) {
                        // process with image
                        if(data == null) return;
                        if(camera == null) return;
                    }
                });
            } catch (Throwable error){
                Log.e("preview", "Error", error);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // setting and start camera
            Camera.Parameters params = camera.getParameters();
            Camera.Size size = getSmallestPreviewSize(width, height, params);
            if (size != null) {
                params.setPreviewSize(size.width, size.height);
            }
            camera.setParameters(params);
//            camera.startPreview();
        }

        private Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters params) {
            Camera.Size result = null;

            for (Camera.Size size : params.getSupportedPreviewSizes()) {
                if (size.width <= width && size.height <= height) {
                    if (result == null) {
                        result = size;
                    } else {
                        int resultArea = result.width * result.height;
                        int newArea = size.width * size.height;

                        if (newArea < resultArea) result = size;
                    }
                }
            }
            return result;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    public long timeFirstBackPressed = 0;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(timeFirstBackPressed == 0){
                timeFirstBackPressed = System.currentTimeMillis();
                MyToast.makeText(MainActivity.this, "Push back again to exit", MyToast.SHORT, MyToast.WARNING, false).show();
            } else if(timeFirstBackPressed > 0){
                if((System.currentTimeMillis() - timeFirstBackPressed) <= MyToast.SHORT){
                    super.onBackPressed();
                } else {
                    timeFirstBackPressed = System.currentTimeMillis();
                    MyToast.makeText(MainActivity.this, "Push back again to exit", MyToast.SHORT, MyToast.WARNING, false).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        state = id;
        if (id == R.id.nav_measure) {
            content_main.setVisibility(content_main.VISIBLE);
            content_history.setVisibility(content_main.INVISIBLE);
            content_chart.setVisibility(content_main.INVISIBLE);
            content_age.setVisibility(content_main.INVISIBLE);
        } else if (id == R.id.nav_chart) {
            updateChartStat();
            content_main.setVisibility(content_main.INVISIBLE);
            content_history.setVisibility(content_main.INVISIBLE);
            content_chart.setVisibility(content_main.VISIBLE);
            content_age.setVisibility(content_main.INVISIBLE);
        } else if (id == R.id.nav_history) {
            content_main.setVisibility(content_main.INVISIBLE);
            content_history.setVisibility(content_main.VISIBLE);
            content_chart.setVisibility(content_main.INVISIBLE);
            content_age.setVisibility(content_main.INVISIBLE);
        } else if (id == R.id.nav_measure_by_age) {
            content_main.setVisibility(content_main.INVISIBLE);
            content_history.setVisibility(content_main.INVISIBLE);
            content_chart.setVisibility(content_main.INVISIBLE);
            content_age.setVisibility(content_main.VISIBLE);
        } else if (id == R.id.nav_share) {
        } else if (id == R.id.nav_quit) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Wanna shut down app?")
                    .setTitle("Quit")
                    .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finishAffinity();
                    System.exit(0);
                }
            });
            builder.setNegativeButton("Cancel", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
