package com.cryptomine.mobile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView

class MainActivity : Activity() {
    private lateinit var status: TextView
    private lateinit var pool: EditText
    private lateinit var wallet: EditText
    private lateinit var threads: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pad = (18 * resources.displayMetrics.density).toInt()
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
            setBackgroundColor(0xFF0B1220.toInt())
        }
        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, 0, 0, pad)
        }
        fun label(text: String, size: Float = 15f) = TextView(this).apply {
            this.text = text
            textSize = size
            setTextColor(0xFFF3F4F6.toInt())
            setPadding(0, pad / 2, 0, pad / 2)
        }
        fun input(hintText: String, value: String = "") = EditText(this).apply {
            hint = hintText
            setText(value)
            setSingleLine(true)
            setTextColor(0xFFFFFFFF.toInt())
            setHintTextColor(0xFF9CA3AF.toInt())
            setPadding(pad / 2, pad / 2, pad / 2, pad / 2)
        }

        content.addView(label("CRYPTOMINE MOBILE", 25f))
        content.addView(label("Android mining control • Experimental build"))
        content.addView(label("Engine status: NOT INSTALLED"))
        content.addView(label("This starter does not report fake hashrate or earnings. A native ARM64 mining engine must be built and audited before mining can run."))
        content.addView(label("Mining pool host:port"))
        pool = input("e.g. pool.example:port")
        content.addView(pool)
        content.addView(label("Public payout address"))
        wallet = input("Paste public address only")
        content.addView(wallet)
        content.addView(label("CPU threads (balanced: 2)", 15f))
        threads = input("2", "2")
        content.addView(threads)
        status = label("Status: Idle. No mining is running.")
        content.addView(status)

        val start = Button(this).apply {
            text = "Start mining"
            setOnClickListener {
                if (pool.text.isNullOrBlank() || wallet.text.isNullOrBlank()) {
                    status.text = "Status: Enter a pool and public payout address first."
                } else {
                    val intent = Intent(this@MainActivity, MiningService::class.java).apply {
                        action = MiningService.ACTION_START
                        putExtra(MiningService.EXTRA_POOL, pool.text.toString().trim())
                        putExtra(MiningService.EXTRA_WALLET, wallet.text.toString().trim())
                        putExtra(MiningService.EXTRA_THREADS, threads.text.toString().toIntOrNull()?.coerceIn(1, 3) ?: 2)
                    }
                    startService(intent)
                    status.text = "Status: Checking for a compatible native engine…"
                }
            }
        }
        content.addView(start, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val stop = Button(this).apply {
            text = "Stop mining"
            setOnClickListener {
                startService(Intent(this@MainActivity, MiningService::class.java).setAction(MiningService.ACTION_STOP))
                status.text = "Status: Stop requested."
            }
        }
        content.addView(stop, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        content.addView(label("Safety: phone mining can generate substantial heat and wear the battery. Start only while you can monitor the device. Stop if it becomes hot."))
        scroll.addView(content)
        root.addView(scroll)
        setContentView(root)
    }
}
