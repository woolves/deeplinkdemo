package com.wolves.apkcomment

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        println ("~~~~~~~~~~")
        println (this.packageCodePath)
        var comment : String? = this.readAPK (this.packageCodePath)
        println (comment)
        val text: TextView = findViewById(R.id.info) as TextView
        if (comment != null && comment != "") {
            text.setText(comment.toString())
        }else{
            text.setText("no comment!!")
        }
        println ("~~~~~~~~~~")
    }

    // 做讀取的動作
    fun readAPK (filename:String) : String? {
        //获取缓存的 APK 文件
        val file = File(this.getPackageCodePath())
        var bytes: ByteArray
        var accessFile: RandomAccessFile? = null
        // 从指定的位置找到 WriteAPK.java 写入的信息
        try {
            accessFile = RandomAccessFile(file, "r")
            var index = accessFile!!.length()
            bytes = ByteArray(2)
            index = index - bytes.size
            accessFile.seek(index)
            accessFile.readFully(bytes)
            val contentLength: Short = this.stream2Short(bytes, 0)
            bytes = ByteArray(contentLength.toInt())
            index = index - bytes.size
            accessFile.seek(index)
            accessFile.readFully(bytes)
            return String(bytes, charset("utf-8"))
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (accessFile != null) {
                try {
                    accessFile.close()
                } catch (ignored: IOException) {
                    ignored.printStackTrace()
                }
            }
        }
        return null
    }

    fun stream2Short(stream: ByteArray, offset: Int): Short {
        val buffer: ByteBuffer = ByteBuffer.allocate(2)
        buffer.order(ByteOrder.LITTLE_ENDIAN)
        buffer.put(stream[offset])
        buffer.put(stream[offset + 1])
        return buffer.getShort(0)
    }


}