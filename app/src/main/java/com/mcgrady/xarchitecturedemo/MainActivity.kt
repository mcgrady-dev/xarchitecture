package com.mcgrady.xarchitecturedemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.mcgrady.xarch.extension.viewBinding
import com.mcgrady.xarchitecturedemo.databinding.ActivityMainBinding
import com.mcgrady.xarchitecturedemo.ui.main.MainFragment


class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.container.id, MainFragment.newInstance())
                .commitNow()
        }

        with(binding) {
            message.text = "binding activity"
            Log.d("ViewBinding", "binding activity")
        }
    }
}