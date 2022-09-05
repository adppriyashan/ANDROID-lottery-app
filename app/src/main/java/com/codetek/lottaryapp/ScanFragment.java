package com.codetek.lottaryapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.codetek.lottaryapp.Models.LotteryIdentification;
import com.codetek.lottaryapp.Models.Utils;
import com.codetek.lottaryapp.Views.Results;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanFragment extends Fragment {

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button scanNow;
    String intentData = "";
    boolean isEmail = false;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_scanner, container, false);
        txtBarcodeValue=view.findViewById(R.id.txtBarcodeValue);
        initViews();
        return view;
    }

    private void initViews() {
        surfaceView = view.findViewById(R.id.surfaceView);
        scanNow =  view.findViewById(R.id.scan_now_button_new);
//        scanNow.setEnabled(false);
        scanNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Result");

                String content=intentData.replaceAll("[\\t\\n\\r]+"," ");
                content="KOTIPATHI KAPRUKA 751 2021/08/31 0751086602786509 18 26 56 72 Q  www.dlb.lk";
                Utils.scanned= new LotteryIdentification().checkLottery(view.getContext(),content);
                startActivity(new Intent(view.getContext(), Results.class));

                if (intentData.length() > 0) {
//                    String content=intentData.replaceAll("[\\t\\n\\r]+"," ");
//                    content="KOTIPATHI KAPRUKA 751 2021/08/31 0751086602786509 18 26 56 72 Q  www.dlb.lk";
//                    Utils.scanned= new LotteryIdentification().checkLottery(view.getContext(),content);
//                    startActivity(new Intent(view.getContext(), Results.class));
                }
            }
        });
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(view.getContext().getApplicationContext() , "Barcode scanner started" , Toast.LENGTH_SHORT).show();
        barcodeDetector = new BarcodeDetector.Builder(view.getContext().getApplicationContext())
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        cameraSource = new CameraSource.Builder(view.getContext().getApplicationContext() , barcodeDetector)
                .setRequestedPreviewSize(500 , 500)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(view.getContext().getApplicationContext() , Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());

                        startScan();
                    } else {
                        ActivityCompat.requestPermissions(getActivity() , new
                                String[]{Manifest.permission.CAMERA} , REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder , int format , int width , int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });



    }

    private void startScan(){
        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    txtBarcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                            scanNow.setEnabled(true);
                            intentData = barcodes.valueAt(0).displayValue;
                            txtBarcodeValue.setText(intentData);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        cameraSource.release();
        super.onDestroy();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        initialiseDetectorsAndSources();
        super.onViewStateRestored(savedInstanceState);
    }
}