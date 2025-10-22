package com.urlshortener.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * QR Code generation service using ZXing library
 */
@Service
public class QrCodeService {

    private static final Logger log = LoggerFactory.getLogger(QrCodeService.class);

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;

    /**
     * Generate QR code for a given URL
     * 
     * @param url The URL to encode
     * @param width Width of the QR code image
     * @param height Height of the QR code image
     * @return Byte array of the PNG image
     * @throws WriterException If QR code generation fails
     * @throws IOException If image writing fails
     */
    public byte[] generateQrCode(String url, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream.toByteArray();
    }

    /**
     * Generate QR code with default size
     * 
     * @param url The URL to encode
     * @return Byte array of the PNG image
     * @throws WriterException If QR code generation fails
     * @throws IOException If image writing fails
     */
    public byte[] generateQrCode(String url) throws WriterException, IOException {
        return generateQrCode(url, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }
}

