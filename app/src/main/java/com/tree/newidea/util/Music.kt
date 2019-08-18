package com.tree.newidea.util

import android.media.MediaPlayer
import com.tree.newidea.R
import com.tree.newidea.api.recommendedSongUri
import java.io.IOException
import java.net.URI

/**
 * Created by Tree on 2019/8/15 20:51
 */
object Music {
    private val mediaPlayer = MediaPlayer()
    var isPlaying = false

    fun paly(songMid: String,function:(Int)->Unit={}) {
        if (isPlaying) {
            mediaPlayer.stop()
            function(R.drawable.ic_play)
            isPlaying =false
        } else {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(recommendedSongUri(songMid))
            mediaPlayer.prepareAsync()
            // 异步准备Prepared完成监听
            mediaPlayer.setOnPreparedListener { mediaPlayer ->
                // 开始播放
                mediaPlayer.start()
                function(R.drawable.ic_playing)
                isPlaying = true
            }
        }
    }





}