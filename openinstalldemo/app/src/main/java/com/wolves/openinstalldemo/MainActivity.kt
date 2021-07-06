package com.wolves.openinstalldemo

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fm.openinstall.OpenInstall
import com.fm.openinstall.listener.AppWakeUpAdapter
import com.fm.openinstall.model.AppData


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 初使化 openinstall sdk
        OpenInstall.init(this)
        OpenInstall.getWakeUp(getIntent(), wakeUpAdapter);
    }

    fun onInstall(appData: AppData) {
        // 打印数据便于调试
        println ("getInstall : installData = $appData")
        // 获取渠道数据
        val channelCode = appData.getChannel()
        // 获取自定义数据
        val bindData = appData.getData()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        // 此处要调用，否则App在后台运行时，会无法获取
        OpenInstall.getWakeUp(intent, wakeUpAdapter)
    }

    var wakeUpAdapter: AppWakeUpAdapter? = object : AppWakeUpAdapter() {
        override fun onWakeUp(appData: AppData) {
            // 打印数据便于调试
            println ("getWakeUp : wakeupData = $appData")
            // 获取渠道数据
            val channelCode = appData.getChannel()
            // 获取绑定数据
            val bindData = appData.getData()
            val text: TextView = findViewById(R.id.info) as TextView
            text.setText(bindData.toString())

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        wakeUpAdapter = null
    }

}