package com.sample.pdfgenerator

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import com.sample.pdfgeneratorandroid.PdfGenerator

class MainActivity : AppCompatActivity() {
    private lateinit var pdfGenerator: PdfGenerator

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView = findViewById<WebView>(R.id.web)
        webView.settings.javaScriptEnabled = true
        webView.settings.builtInZoomControls = true
        webView.settings.setSupportZoom(true)
        webView.settings.displayZoomControls = true

        val tableHeads = arrayListOf("Col A")
        tableHeads.add("Col B")
        tableHeads.add("Col C")
        val tableContent = arrayListOf(arrayListOf("Data 1", "Data 2", "Data 3"))
        tableContent.add(arrayListOf("Data 4", "Data 5", "Data 6"))
        pdfGenerator = PdfGenerator(this, tableHeads, tableContent)

        pdfGenerator.loadPdfInWebView(webView)

        findViewById<Button>(R.id.printBtn).setOnClickListener {
            pdfGenerator.createPdf("PdfGenerator")
        }
    }

    override fun onResume() {
        super.onResume()

        pdfGenerator.setOnPdfGenerateListener(object : PdfGenerator.PdfGenerateListener {
            override fun onTaskFinished(responseCode: String) {
                Toast.makeText(applicationContext, responseCode, Toast.LENGTH_SHORT).show()
            }
        })
    }
}