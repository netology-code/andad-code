package ru.netology.nmedia.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.AutoTransition
import androidx.transition.ChangeBounds
import androidx.transition.Scene
import androidx.transition.TransitionManager
import ru.netology.nmedia.R

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<Button>(R.id.start).setOnClickListener{
            val root = findViewById<ViewGroup>(R.id.root)
            val endScene = Scene.getSceneForLayout(root, R.layout.end_scene, this)

            TransitionManager.go(endScene, ChangeBounds().apply {
                duration = 10000
                // TODO: apply any property setup
            })
        }
    }
}