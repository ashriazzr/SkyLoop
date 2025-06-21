package com.ticketing.skyloop.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.ticketing.skyloop.Model.Flight;
import com.ticketing.skyloop.databinding.ActivityTicketDetailBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TicketDetailActivity extends BaseActivity {
    private ActivityTicketDetailBinding binding;
    private Flight flight;
    private static final int STORAGE_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }

    private void setVariable() {
        binding.backBtn.setOnClickListener(v -> {
            // Kembali ke halaman utama dan clear semua activity di stack
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
        binding.fromTxt.setText(flight.getFromShort());
        binding.fromSmallTxt.setText(flight.getFrom());
        binding.toTxt.setText(flight.getTo());
        binding.toShortTxt.setText(flight.getToShort());
        binding.toSmallTxt.setText(flight.getTo());
        binding.dateTxt.setText(flight.getDate());
        binding.timeTxt.setText(flight.getTime());
        binding.arrivalTxt.setText(flight.getArriveTime());
        binding.classTxt.setText(flight.getClassSeat());
        binding.priceTxt.setText("$" + flight.getPrice());
        binding.airlines.setText(flight.getAirlineName());
        binding.seatsTxt.setText(flight.getPassenger());

        // Set download button click listener
        binding.downloadTicketBtn.setOnClickListener(v -> {
            if (checkStoragePermission()) {
                generatePDF();
            } else {
                requestStoragePermission();
            }
        });

        Glide.with(TicketDetailActivity.this)
                .load(flight.getAirlineLogo())
                .into(binding.logo);
    }

    private void getIntentExtra() {
        flight = (Flight) getIntent().getSerializableExtra("flight");
    }

    private boolean checkStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ doesn't need WRITE_EXTERNAL_STORAGE for app-specific directories
            return true;
        } else {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                generatePDF();
            } else {
                Toast.makeText(this, "Permission denied. Cannot download ticket.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void generatePDF() {
        try {
            // Create a bitmap from the ticket layout
            View ticketView = binding.getRoot().findViewById(com.ticketing.skyloop.R.id.main)
                    .findViewById(1); // You might need to give an ID to your ticket container

            // Alternative: Create PDF content programmatically
            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create(); // A4 size
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

            Canvas canvas = page.getCanvas();
            Paint paint = new Paint();

            // Draw ticket content
            drawTicketContent(canvas, paint);

            pdfDocument.finishPage(page);

            // Save PDF file
            String fileName = "Ticket_" + flight.getFromShort() + "_to_" + flight.getToShort() + "_" +
                    new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) + ".pdf";

            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);

            FileOutputStream fos = new FileOutputStream(file);
            pdfDocument.writeTo(fos);
            pdfDocument.close();
            fos.close();

            Toast.makeText(this, "Ticket downloaded successfully!\nSaved to: " + file.getAbsolutePath(),
                    Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void drawTicketContent(Canvas canvas, Paint paint) {
        // Set paint properties
        paint.setColor(Color.BLACK);
        paint.setTextSize(16);

        int yPosition = 50;
        int leftMargin = 50;
        int lineHeight = 25;

        // Title
        paint.setTextSize(24);
        paint.setFakeBoldText(true);
        canvas.drawText("FLIGHT TICKET", leftMargin, yPosition, paint);
        yPosition += lineHeight * 2;

        // Reset text size and bold
        paint.setTextSize(16);
        paint.setFakeBoldText(false);

        // Airline information
        canvas.drawText("Airline: " + flight.getAirlineName(), leftMargin, yPosition, paint);
        yPosition += lineHeight;

        // Flight route
        paint.setTextSize(20);
        paint.setFakeBoldText(true);
        canvas.drawText(flight.getFromShort() + " → " + flight.getToShort(), leftMargin, yPosition, paint);
        yPosition += lineHeight;

        paint.setTextSize(14);
        paint.setFakeBoldText(false);
        canvas.drawText(flight.getFrom() + " to " + flight.getTo(), leftMargin, yPosition, paint);
        yPosition += lineHeight * 2;

        // Flight details
        paint.setTextSize(16);
        canvas.drawText("Date: " + flight.getDate(), leftMargin, yPosition, paint);
        yPosition += lineHeight;

        canvas.drawText("Departure Time: " + flight.getTime(), leftMargin, yPosition, paint);
        yPosition += lineHeight;

        canvas.drawText("Arrival Time: " + flight.getArriveTime(), leftMargin, yPosition, paint);
        yPosition += lineHeight;

        canvas.drawText("Class: " + flight.getClassSeat(), leftMargin, yPosition, paint);
        yPosition += lineHeight;

        canvas.drawText("Passengers: " + flight.getPassenger(), leftMargin, yPosition, paint);
        yPosition += lineHeight;

        // Price
        paint.setTextSize(18);
        paint.setFakeBoldText(true);
        canvas.drawText("Price: $" + flight.getPrice(), leftMargin, yPosition, paint);
        yPosition += lineHeight * 2;

        // Draw a simple line separator
        paint.setStrokeWidth(2);
        canvas.drawLine(leftMargin, yPosition, 545, yPosition, paint);
        yPosition += lineHeight;

        // Footer
        paint.setTextSize(12);
        paint.setFakeBoldText(false);
        canvas.drawText("Thank you for choosing our airline!", leftMargin, yPosition, paint);
        yPosition += lineHeight;
        canvas.drawText("Generated on: " + new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date()),
                leftMargin, yPosition, paint);

        // Draw a simple border around the ticket
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawRect(30, 30, 565, yPosition + 30, paint);
    }
}