package com.banking.demo.service;


import com.banking.demo.dto.QrResquest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class QrCodeService {

    public byte[] generateQrCode(QrResquest request, int width, int height) throws WriterException, IOException {
        // Format: "bankapp://transfer?account=1234567890&amount=100.00" or without amount for dynamic
        String qrContent = "bankapp://transfer?account=" + request.getAccountNumber();
        if (request.getFixedAmount() != null && request.getFixedAmount().compareTo(java.math.BigDecimal.ZERO) > 0) {
            qrContent += "&amount=" + request.getFixedAmount().toPlainString();
        }
        // For Bakong-style, you'd use a JSON payload. This is simplified.

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, width, height);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }
}
