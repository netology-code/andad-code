package ru.netology.nmedia.ui

import android.content.res.AssetFileDescriptor
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R

class AppActivity : AppCompatActivity(R.layout.activity_app) {
    private val mediaObserver = MediaLifecycleObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

/*
        findViewById<Button>(R.id.play).setOnClickListener {
            MediaPlayer.create(this, R.raw.ring).apply {
                setOnCompletionListener {
                    it.release()
                }
            }.start()
        }
*/

        lifecycle.addObserver(mediaObserver)

/*
        findViewById<Button>(R.id.play).setOnClickListener {
            mediaObserver.apply {
                resources.openRawResourceFd(R.raw.ring).use { afd ->
                    player?.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
                }
            }.play()
        }
*/
/*
        findViewById<Button>(R.id.play).setOnClickListener {
            mediaObserver.apply {
                player?.setDataSource(
                    "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
                )
            }.play()
        }
*/

        findViewById<VideoView>(R.id.video).apply {
            setMediaController(MediaController(this@AppActivity))
            setVideoURI(
                Uri.parse("https://archive.org/download/BigBuckBunny1280x720Stereo/big_buck_bunny_720_stereo.mp4")
            )
            setOnPreparedListener {
                start()
            }
            setOnCompletionListener {
                stopPlayback()
            }
        }
    }
}



