package ru.netology.nmedia.ui

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.R

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = findViewById<ViewGroup>(R.id.root).apply {
            layoutTransition = LayoutTransition().apply {
                setInterpolator(LayoutTransition.CHANGE_APPEARING, BounceInterpolator())
                setDuration(LayoutTransition.CHANGE_APPEARING, 500)
            }
        }
        findViewById<Button>(R.id.start).setOnClickListener {
            val view = layoutInflater.inflate(R.layout.view, root, false)
            root.addView(view, 0)
        }
    }
}