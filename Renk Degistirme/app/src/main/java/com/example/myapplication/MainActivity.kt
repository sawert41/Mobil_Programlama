package com.example.myapplication

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var timer: CountDownTimer? = null
    private var currentColor = Color.parseColor("#020617")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val seekMin = findViewById<SeekBar>(R.id.seekMin)
        val seekMax = findViewById<SeekBar>(R.id.seekMax)
        val textMin = findViewById<TextView>(R.id.textMin)
        val textMax = findViewById<TextView>(R.id.textMax)
        val buttonStart = findViewById<Button>(R.id.buttonStart)

        val layoutSelect = findViewById<LinearLayout>(R.id.layoutSelect)
        val layoutTimer = findViewById<FrameLayout>(R.id.layoutTimer)
        val textTime = findViewById<TextView>(R.id.textTime)
        val buttonReset = findViewById<Button>(R.id.buttonReset)
        val colorOverlay = findViewById<View>(R.id.colorOverlay)

        seekMin.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
                textMin.text = "Min: $value"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        seekMax.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, value: Int, fromUser: Boolean) {
                textMax.text = "Max: $value"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        buttonStart.setOnClickListener {
            val min = seekMin.progress
            val max = seekMax.progress

            if (min >= max) {
                Toast.makeText(this, "Min < Max olmalı", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val randomValue = Random.nextInt(min, max + 1)

            layoutSelect.visibility = View.GONE
            layoutTimer.visibility = View.VISIBLE

            startTimer(randomValue, textTime, colorOverlay)
        }

        buttonReset.setOnClickListener {
            timer?.cancel()
            layoutTimer.visibility = View.GONE
            layoutSelect.visibility = View.VISIBLE
            colorOverlay.setBackgroundColor(Color.parseColor("#020617"))
            currentColor = Color.parseColor("#020617")
        }
    }

    private fun startTimer(time: Int, text: TextView, view: View) {

        timer = object : CountDownTimer((time * 1000).toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                text.text = seconds.toString()

                val newColor = Color.rgb(
                    Random.nextInt(180),
                    Random.nextInt(180),
                    Random.nextInt(180)
                )

                animateColor(view, currentColor, newColor)
                currentColor = newColor
            }

            override fun onFinish() {
                text.text = "Uygulama bitmiştir"
            }

        }.start()
    }

    private fun animateColor(view: View, from: Int, to: Int) {
        val animator = ValueAnimator.ofObject(ArgbEvaluator(), from, to)
        animator.duration = 600
        animator.addUpdateListener {
            view.setBackgroundColor(it.animatedValue as Int)
        }
        animator.start()
    }
}