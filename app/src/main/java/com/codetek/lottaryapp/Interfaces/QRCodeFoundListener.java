package com.codetek.lottaryapp.Interfaces;

public interface QRCodeFoundListener {
    void onQRCodeFound(String qrCode);
    void qrCodeNotFound();
}
