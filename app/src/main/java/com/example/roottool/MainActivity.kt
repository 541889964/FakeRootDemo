package com.example.roottool

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Random

class MainActivity : AppCompatActivity() {

    private val rnd = Random()
    private lateinit var splashIcon: TextView
    private lateinit var splashTitle: TextView
    private lateinit var splashSub: TextView
    private lateinit var splashOverlay: LinearLayout
    private lateinit var pulse1: View
    private lateinit var pulse2: View
    private lateinit var pulse3: View
    private lateinit var drop1: View
    private lateinit var drop2: View
    private lateinit var drop3: View
    private lateinit var drop4: View
    private lateinit var coreBall: TextView
    private lateinit var statusText: TextView
    private lateinit var percentText: TextView
    private lateinit var statusDot: View
    private lateinit var barFill: ProgressBar
    private lateinit var logBox: TextView
    private lateinit var logBoxScroll: ScrollView
    private lateinit var startBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        splashIcon = findViewById(R.id.splashIcon)
        splashTitle = findViewById(R.id.splashTitle)
        splashSub = findViewById(R.id.splashSub)
        splashOverlay = findViewById(R.id.splashOverlay)
        pulse1 = findViewById(R.id.pulse1)
        pulse2 = findViewById(R.id.pulse2)
        pulse3 = findViewById(R.id.pulse3)
        drop1 = findViewById(R.id.drop1)
        drop2 = findViewById(R.id.drop2)
        drop3 = findViewById(R.id.drop3)
        drop4 = findViewById(R.id.drop4)
        coreBall = findViewById(R.id.coreBall)
        statusText = findViewById(R.id.statusText)
        percentText = findViewById(R.id.percentText)
        statusDot = findViewById(R.id.statusDot)
        barFill = findViewById(R.id.barFill)
        logBox = findViewById(R.id.logBox)
        logBoxScroll = findViewById(R.id.logBoxScroll)
        startBtn = findViewById(R.id.startBtn)

        if (Build.VERSION.SDK_INT >= 31) {
            listOf(drop1, drop2, drop3, drop4).forEach {
                it.setRenderEffect(
                    RenderEffect.createBlurEffect(70f, 70f, Shader.TileMode.CLAMP)
                )
            }
        }

        startFloatingAnimation()
        startSplashAnimation()
        startCoreBreath()
        startPulseRings()
        startDotBlink()

        splashOverlay.postDelayed({ showIntroDialog() }, 2000)
        startBtn.setOnClickListener { startAscend() }
    }

    private fun startFloatingAnimation() {
        listOf(drop1, drop2, drop3, drop4).forEachIndexed { i, drop ->
            val dx = 70f + rnd.nextInt(130)
            val dy = 70f + rnd.nextInt(130)
            ObjectAnimator.ofFloat(drop, "translationX", 0f, dx, 0f, -dx, 0f).apply {
                duration = 14000L + i * 1800L
                repeatCount = ValueAnimator.INFINITE
                interpolator = AccelerateDecelerateInterpolator()
            }.start()
            ObjectAnimator.ofFloat(drop, "translationY", 0f, -dy, 0f, dy, 0f).apply {
                duration = 16000L + i * 1500L
                repeatCount = ValueAnimator.INFINITE
                interpolator = AccelerateDecelerateInterpolator()
            }.start()
        }
    }

    private fun startSplashAnimation() {
        splashIcon.scaleX = 0.3f; splashIcon.scaleY = 0.3f; splashIcon.alpha = 0f
        splashIcon.animate().scaleX(1f).scaleY(1f).alpha(1f)
            .setDuration(1200).setInterpolator(DecelerateInterpolator()).start()
        splashTitle.alpha = 0f; splashTitle.translationY = 40f
        splashTitle.animate().alpha(1f).translationY(0f)
            .setDuration(900).setStartDelay(400).start()
        splashSub.alpha = 0f
        splashSub.animate().alpha(1f).setDuration(700).setStartDelay(1000).start()
    }

    private fun startCoreBreath() {
        ObjectAnimator.ofFloat(coreBall, "scaleX", 1f, 1.08f, 1f).apply {
            duration = 3200; repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }.start()
        ObjectAnimator.ofFloat(coreBall, "scaleY", 1f, 1.08f, 1f).apply {
            duration = 3200; repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }.start()
    }

    private fun startPulseRings() {
        listOf(pulse1, pulse2, pulse3).forEachIndexed { i, p ->
            ObjectAnimator.ofFloat(p, "scaleX", 0.8f, 2.6f).apply {
                duration = 3000; repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                startDelay = (i * 1000).toLong()
            }.start()
            ObjectAnimator.ofFloat(p, "scaleY", 0.8f, 2.6f).apply {
                duration = 3000; repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                startDelay = (i * 1000).toLong()
            }.start()
            ObjectAnimator.ofFloat(p, "alpha", 1f, 0f).apply {
                duration = 3000; repeatCount = ValueAnimator.INFINITE
                interpolator = LinearInterpolator()
                startDelay = (i * 1000).toLong()
            }.start()
        }
    }

    private fun startDotBlink() {
        ObjectAnimator.ofFloat(statusDot, "alpha", 1f, 0.3f, 1f).apply {
            duration = 1600; repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }.start()
    }

    private fun showIntroDialog() {
        splashOverlay.visibility = View.GONE
        val msg = "欢迎使用临时提权工具。本工具采用内核级漏洞利用与内存注入技术，" +
                "为您的设备提供临时的超级用户权限，用于运行需要 Root 权限的应用程序与系统模块。" +
                "提权过程中请保持屏幕常亮，不要关闭应用或切换到后台。整个过程大约需要数十秒，" +
                "完成后会自动进入 KernelSU 管理界面。请在合法合规的前提下使用本工具，" +
                "避免修改系统关键文件，以免造成设备无法启动。祝你使用愉快。"
        AlertDialog.Builder(this)
            .setTitle("使用须知")
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton("开始使用") { _, _ -> }
            .setNegativeButton("退出") { _, _ -> finish() }
            .show()
    }

    private fun startAscend() {
        startBtn.isEnabled = false
        startBtn.text = "提 权 中 …"
        statusText.text = "正在准备…"

        val fullLog = buildFakeLog()
        logBox.text = ""

        val chunk = 260
        var pos = 0
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                if (pos < fullLog.length) {
                    val end = minOf(pos + chunk, fullLog.length)
                    logBox.append(fullLog.substring(pos, end))
                    logBoxScroll.post { logBoxScroll.fullScroll(View.FOCUS_DOWN) }
                    pos = end
                    handler.postDelayed(this, 22)
                }
            }
        }
        handler.post(runnable)

        ValueAnimator.ofInt(0, 100).apply {
            duration = 4200
            interpolator = LinearInterpolator()
            addUpdateListener {
                val v = it.animatedValue as Int
                barFill.progress = v
                percentText.text = "$v%"
            }
            start()
        }

        val phases = listOf(
            "初始化提权环境" to 0L,
            "检测内核版本 → Linux 4.14.190" to 500L,
            "申请临时 root 权限" to 1100L,
            "重新挂载 /system 为可读写" to 1600L,
            "写入 su 二进制 → /system/xbin/su" to 2100L,
            "修补 boot 镜像" to 2600L,
            "注入 KernelSU 驱动模块" to 3100L,
            "创建 /data/adb/ksu 工作目录" to 3500L,
            "重启 zygote 进程" to 3900L,
            "提权完成" to 4400L
        )
        phases.forEach { (txt, delay) -> handler.postDelayed({ statusText.text = txt }, delay) }

        handler.postDelayed({
            Toast.makeText(this, "提权成功", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, KsuActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 4600)
    }

    private fun buildFakeLog(): String {
        val pool = listOf(
            "ksu: driver v0.9.5 loaded, protocol ver 4",
            "selinux: avc: granted { setcontext } for scontext=u:r:su:s0",
            "kernel: ksu_manager_hook: init hook ret=0",
            "overlayfs: mount -t overlay lowerdir=/system upperdir=/data/adb/ksu/overlay",
            "nsenter: enter pid namespace ret=0",
            "magisk: magiskhide daemon started",
            "ksu: su binary injected at /system/xbin/su",
            "avc: denied { write } for scontext=u:r:init:s0 tclass=file",
            "zygote: preload class com.example.roottool.Hook",
            "kernel: audit: type=1400 audit(0.0:42): avc: granted",
            "ksud: ksu_install_module name=zygisk-lsposed",
            "susfs: hide path /data/adb/ksu",
            "ksu: uid=0 euid=0 gid=0 egid=0 cap=0x3fffffffff",
            "init: starting service 'ksud' pid=1421",
            "binder: 1421:1421 transaction failed 29189/-22",
            "ksu: manager uid=10142 verified",
            "selinux: policy loaded from /sys/fs/selinux/policy",
            "kernel: ksu: setuid_hook applied for pid=8821",
            "tracing: ksu_handle_execveat hooking",
            "ksu: overlay mount at /data/adb/modules",
            "ksud: module list scan count=5",
            "shamiko: whitelist mode enabled",
            "ksu: randomize package name suffix=_a3f9",
            "kernel: cgroup: ksu_isolate pid=7701",
            "avc: granted { read } for scontext=u:r:su:s0 tclass=proc",
            "ksu: mount namespace cloned for zygote",
            "ksu: hide susfs path /proc/self/mounts",
            "sukisu: resetprop sys.oem_unlock_allowed=1",
            "kernel: ksu: kprobe registered on do_execveat_common",
            "ksu: module overlayfs activated",
            "zygisk: injected into com.android.systemui",
            "ksu: safemode flag=false"
        )
        val sb = StringBuilder(12000)
        var i = 0
        while (sb.length < 11500) {
            sb.append("[%06d] ".format(i++))
            sb.append("%08d ".format(rnd.nextInt(99999999)))
            sb.append("%04d ".format(rnd.nextInt(9999)))
            sb.append(pool[rnd.nextInt(pool.size)])
            sb.append(" pid=").append(rnd.nextInt(20000))
            sb.append(" uid=").append(rnd.nextInt(11000))
            sb.append(" ret=").append(rnd.nextInt(3))
            sb.append('\n')
        }
        return sb.toString()
    }
}
