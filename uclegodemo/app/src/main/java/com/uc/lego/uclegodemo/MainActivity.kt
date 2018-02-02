package com.uc.lego.uclegodemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.uc.lego.DesignLibrary.DSL

class MainActivity : AppCompatActivity() {

    val x = DSL()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generated_layout_file_dsl)
    }
}
