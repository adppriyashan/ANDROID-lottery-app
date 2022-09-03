package com.codetek.lottaryapp.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Size;
import android.widget.Toast;

import com.codetek.lottaryapp.Interfaces.QRCodeFoundListener;
import com.codetek.lottaryapp.Models.QRCodeImageAnalyzer;
import com.codetek.lottaryapp.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Scan extends AppCompatActivity {
    private PreviewView dashboard_qr_preview;
    IntentIntegrator intentIntegrator;
    private static final int PERMISSION_REQUEST_CAMERA = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.codetek.lottaryapp.R.layout.activity_scan);

        dashboard_qr_preview=findViewById(R.id.dashboard_qr_preview);

        requestCamera();
    }

    private void startCamera() {
        intentIntegrator = new IntentIntegrator(Scan.this);
        intentIntegrator.setPrompt("Scan Lottery QR");
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.initiateScan();
        intentIntegrator.setBeepEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Pairing Terminated", Toast.LENGTH_SHORT).show();
            } else {
                String _qrCode=intentResult.getContents();
                if(_qrCode.length()>4){

                }else{
                    Toast.makeText(this, "Invalid QR Code", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void requestCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(Scan.this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
            }
        }
    }

    private void bindCameraPreview(@NonNull ProcessCameraProvider cameraProvider) {
        dashboard_qr_preview.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);

        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(dashboard_qr_preview.createSurfaceProvider());

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setTargetResolution(new Size(1280, 720))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), new QRCodeImageAnalyzer(new QRCodeFoundListener() {
            @Override
            public void onQRCodeFound(String _qrCode) {


            }

            @Override
            public void qrCodeNotFound() {
            }
        }));

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) Scan.this, cameraSelector, preview);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera();
            } else {
                Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}