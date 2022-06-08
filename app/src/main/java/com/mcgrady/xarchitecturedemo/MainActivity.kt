package com.mcgrady.xarchitecturedemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mcgrady.xarch.ext.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toast("dasdf")
    }
}