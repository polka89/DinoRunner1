package com.example.dinorunner.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.dinorunner.model.Dino

class GameView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : SurfaceView(context, attrs), SurfaceHolder.Callback, Runnable {

    private var thread: Thread? = null
    @Volatile private var running = false

    private var score = 0
    var isGameOver = false
    var onGameOverListener: ((Int) -> Unit)? = null

    private val dino = Dino(resources.displayMetrics.heightPixels)
    private var obstacles = mutableListOf<RectF>()
    private val paintObstacle = Paint().apply { color = Color.RED }
    private val paintText = Paint().apply { color = Color.BLACK; textSize = 60f }

    private var lastTime = 0L
    private var spawnTimer = 0f
    private var speed = 600f

    init {
        holder.addCallback(this)
        isFocusable = true
    }

    override fun surfaceCreated(h: SurfaceHolder) {
        running = true
        thread = Thread(this).also { it.start() }
    }

    override fun surfaceDestroyed(h: SurfaceHolder) {
        running = false
        thread?.join()
    }

    override fun surfaceChanged(h: SurfaceHolder, f: Int, w: Int, hh: Int) {}

    fun dinoJump() { if (!isGameOver) dino.jump() }
    fun dinoDuck(on: Boolean) { if (!isGameOver) dino.duck(on) }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun run() {
        lastTime = System.nanoTime()
        while (running) {
            val now = System.nanoTime()
            val dt = (now - lastTime) / 1_000_000_000f
            lastTime = now

            if (!isGameOver) {
                update(dt.coerceAtMost(0.05f))
                drawFrame()
                sleepFrame()
            } else {
                sleepFrame()
            }
        }
    }

    private fun update(dt: Float) {
        dino.update(dt)
        score += (dt * 10).toInt()

        spawnTimer += dt
        if (spawnTimer > 1.5f) {
            spawnTimer = 0f
            val obsY = resources.displayMetrics.heightPixels - 200f
            obstacles.add(RectF(resources.displayMetrics.widthPixels.toFloat(), obsY, resources.displayMetrics.widthPixels + 100f, obsY + 100f))
        }

        val iterator = obstacles.iterator()
        while (iterator.hasNext()) {
            val obs = iterator.next()
            obs.offset(-speed * dt, 0f)

            if (RectF.intersects(dino.rect, obs)) {
                isGameOver = true
                onGameOverListener?.invoke(score)
            }

            if (obs.right < 0) iterator.remove()
        }
    }

    private fun drawFrame() {
        val canvas = holder.lockCanvas() ?: return
        canvas.drawColor(Color.parseColor("#F7F7F7"))
        canvas.drawRect(0f, resources.displayMetrics.heightPixels - 100f, resources.displayMetrics.widthPixels.toFloat(), resources.displayMetrics.heightPixels.toFloat(), Paint().apply { color = Color.GRAY })
        dino.draw(canvas)
        for (obs in obstacles) {
            canvas.drawRect(obs, paintObstacle)
        }
        canvas.drawText("Score: $score", 50f, 100f, paintText)
        holder.unlockCanvasAndPost(canvas)
    }

    private fun sleepFrame() {
        val frameMs = 1000L / 60
        val elapsed = (System.nanoTime() - lastTime) / 1_000_000
        val wait = frameMs - elapsed
        if (wait > 0) Thread.sleep(wait)
    }

    fun restart() {
        score = 0
        isGameOver = false
        obstacles.clear()
        spawnTimer = 0f
        dino.y = dino.startY
        dino.state = Dino.State.RUNNING
        lastTime = System.nanoTime()
    }
}