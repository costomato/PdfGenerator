package com.sample.pdfgeneratorandroid

import android.content.Context
import android.print.PrintAttributes
import android.print.PrintJob
import android.print.PrintManager
import android.webkit.WebView

class PdfGenerator(
    private val context: Context,
    private val tableHeads: ArrayList<String>,
    private val tableContent: ArrayList<ArrayList<String>>
) {
    companion object {
        const val MIME_TYPE = "text/html; charset=utf-8"
        const val ENCODING = "UTF-8"
    }

    private lateinit var pdfGenerateListener: PdfGenerateListener

    fun setOnPdfGenerateListener(pdfGenerateListener: PdfGenerateListener) {
        this.pdfGenerateListener = pdfGenerateListener
        if (this::printJob.isInitialized) {
            when {
                printJob.isFailed -> pdfGenerateListener.onTaskFinished("Failure")
                printJob.isCompleted -> pdfGenerateListener.onTaskFinished("Completed")
                printJob.isBlocked -> pdfGenerateListener.onTaskFinished("Blocked")
                printJob.isCancelled -> pdfGenerateListener.onTaskFinished("Cancelled")
                printJob.isQueued -> pdfGenerateListener.onTaskFinished("Queued")
                printJob.isStarted -> pdfGenerateListener.onTaskFinished("Started")
                else -> pdfGenerateListener.onTaskFinished("Error")
            }
        }
    }

    fun loadPdfInWebView(webView: WebView) {
        val htmlData = StringBuilder()
        htmlData.append(
            "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                    "    <style>\n" +
                    "       th {" +
                    "           background: rgba(93, 240, 255, 256);" +
                    "       }\n" +
                    "       td, th, table {" +
                    "           border: 1px solid black;\n" +
                    "           border-collapse: collapse;\n" +
                    "           margin: auto;" +
                    "           font-size: 22px;" +
                    "       }" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "<table>\n" +
                    "    <tr>"
        )


        tableHeads.forEach {
            htmlData.append("<th>$it</th>")
        }

        htmlData.append("</tr>\n")

        tableContent.forEach { content ->
            htmlData.append("<tr>")
            content.forEach {
                htmlData.append("<td>$it</td>")
            }
            htmlData.append("</tr>")
        }

        htmlData.append("</table></body></html>")

        webView.loadData(htmlData.toString(), MIME_TYPE, ENCODING)
    }

    private lateinit var printJob: PrintJob
    fun createPdf(fileName: String) {
        val webView = WebView(context)
        loadPdfInWebView(webView)
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = webView.createPrintDocumentAdapter(fileName)
        printJob = printManager.print(fileName, printAdapter, PrintAttributes.Builder().build())
    }

    interface PdfGenerateListener {
        fun onTaskFinished(responseCode: String)
    }
}