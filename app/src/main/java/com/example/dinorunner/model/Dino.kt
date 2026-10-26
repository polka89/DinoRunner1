package com.example.dinorunner.model

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF

class Dino(screenHeight: Int) {
    var x = 100f
    var y = screenHeight - 300f
    var vy = 0f

    val startY = y
    var state = State.RUNNING
    private val gravity = 2000f
    private val jumpV = -900f
    val w = 100f
    val h = 100f

    private val paint = Paint().apply { color = Color.GREEN }

    enum class State { RUNNING, JUMPING, DUCKING }

    fun jump() {
        if (state != State.JUMPING) {
            vy = jumpV
            state = State.JUMPING
        }
    }

    fun duck(on: Boolean) {
        state = if (on) State.DUCKING else if (y < startY) State.JUMPING else State.RUNNING
    }

    fun update(dt: Float) {
        if (state == State.JUMPING) {
            vy += gravity * dt
            y += vy * dt
            if (y >= startY) {
                y = startY
                vy = 0f
                state = State.RUNNING
            }
        }
    }

    val rect: RectF get() = RectF(x, y, x + w, y + h)

    fun draw(canvas: Canvas) {
        val currentH = if (state == State.DUCKING) h / 2 else h
        canvas.drawRect(x, y + (h - currentH), x + w, y + h, paint)
    }
}