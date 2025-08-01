package ru.netology.nmedia.ui

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.animation.BounceInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val root = binding.container.apply {
            layoutTransition = LayoutTransition().apply {
                setInterpolator(LayoutTransition.CHANGE_APPEARING, BounceInterpolator())
                setDuration(LayoutTransition.CHANGE_APPEARING, 500)
            }
        }
        binding.start.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.view, root, false)
            root.addView(view, 0)
        }
    }
}