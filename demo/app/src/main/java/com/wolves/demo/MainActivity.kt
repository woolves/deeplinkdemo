package com.wolves.demo

import android.R.attr.data
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val uri: Uri? = intent.data
        val scheme: String? = uri?.getScheme()
        val host: String? = uri?.getHost()
        val path: String? = uri?.getPath()
        val query: String? = uri?.getQuery()
        val type: String? = uri?.getQueryParameter("type")
        val msg : String = "scheme = $scheme, host = $host, path = $path, query = $query, type = $type"
        val text: TextView = findViewById(R.id.info_text) as TextView
        text.setText(msg)
    }
}