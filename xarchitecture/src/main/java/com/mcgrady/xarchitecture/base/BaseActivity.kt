package com.mcgrady.xarchitecture.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zackratos.ultimatebarx.ultimatebarx.navigationBar
import com.zackratos.ultimatebarx.ultimatebarx.statusBar

/**
 * Created by mcgrady on 5/5/21.
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        statusBar {
            transparent()
        }
        navigationBar {
            transparent()
        }
    }
}