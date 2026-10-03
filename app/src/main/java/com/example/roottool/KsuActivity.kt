package com.example.roottool

import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class KsuActivity : AppCompatActivity() {

    private lateinit var content: LinearLayout
    private lateinit var navHome: TextView
    private lateinit var navSu: TextView
    private lateinit var navMod: TextView
    private lateinit var navSet: TextView
    private val navs = mutableListOf<TextView>()

    private val modules = mutableListOf(
        Module("Zygisk - LSPosed", "在 Zygisk 环境下运行 LSPosed 框架", "LSPosed Developers", false),
        Module("系统主机广告拦截", "通过 hosts 文件拦截常见广告域名", "KernelSU 社区", false),
        Module("BusyBox for Android NDK", "为 Android 提供完整的 BusyBox 工具集", "osm0sis", false),
        Module("Shamiko", "隐藏 Root 痕迹，绕过检测", "LSPosed", false),
        Module("MagiskHide Props Config", "修改系统属性以通过完整性检测", "Didgeridoohan", false)
    )

    private val features = listOf(
        "隐藏 Root 状态检测", "随机包名生成", "内核级 Zygisk 注入",
        "Systemless Hosts 挂载", "OverlayFS 系统分区重挂载", "SELinux 策略临时修改",
        "自动清理 Dalvik 缓存", "电池优化白名单", "多用户提权隔离",
        "提权日志一键导出", "系统应用冻结", "核心破解",
        "内存读写注入", "隐藏应用列表", "启动服务劫持",
        "网络请求抓包", "权限动态申请", "安全模式回退",
        "提权时长自定义", "开发者调试选项"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ksu)

        content = findViewById(R.id.ksuContent)
        navHome = findViewById(R.id.navHome)
        navSu = findViewById(R.id.navSu)
        navMod = findViewById(R.id.navMod)
        navSet = findViewById(R.id.navSet)
        navs.addAll(listOf(navHome, navSu, navMod, navSet))

        navHome.setOnClickListener { select(0) }
        navSu.setOnClickListener {
            select(1)
            Toast.makeText(this, "无法连接到超级用户服务", Toast.LENGTH_SHORT).show()
        }
        navMod.setOnClickListener { select(2) }
        navSet.setOnClickListener { select(3) }

        select(0)
    }

    private fun select(idx: Int) {
        navs.forEachIndexed { i, v ->
            v.setTextColor(if (i == idx) 0xFF00FFC8.toInt() else 0xFF5A6A78.toInt())
        }
        when (idx) {
            0 -> showHome()
            1 -> showSuperUser()
            2 -> showModules()
            3 -> showSettings()
        }
    }

    private fun clear() { content.removeAllViews() }

    private fun tv(txt: String, size: Float, color: Int = 0xFFE8F0F5.toInt()): TextView =
        TextView(this).apply {
            text = txt
            textSize = size
            setTextColor(color)
            setPadding(0, 14, 0, 14)
            includeFontPadding = false
        }

    private fun createCard(glow: Boolean = false): LinearLayout {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 40, 40, 40)
            setBackgroundResource(if (glow) R.drawable.card_bg_glow else R.drawable.card_bg)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            lp.setMargins(0, 8, 0, 8)
            layoutParams = lp
        }
        return card
    }

    private fun showHome() {
        clear()
        val card = createCard(glow = true)
        val ring = TextView(this).apply {
            text = "✓"
            textSize = 36f
            setTextColor(0xFF00FFC8.toInt())
            gravity = Gravity.CENTER
            setBackgroundResource(R.drawable.ring_ok)
            val lp = LinearLayout.LayoutParams(100, 100).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            layoutParams = lp
        }
        card.addView(ring)
        card.addView(tv("KernelSU 正在运行", 19f).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            typeface = Typeface.DEFAULT_BOLD
            setPadding(0, 22, 0, 6)
        })
        card.addView(tv("已获得临时 Root 权限", 13f, 0xFF00FFC8.toInt()).apply {
            gravity = Gravity.CENTER_HORIZONTAL
        })
        content.addView(card)

        content.addView(tv("设备信息", 11f, 0xFF5A6A78.toInt()).apply { setPadding(0, 20, 0, 4) })
        val infoCard = createCard()
        addRow(infoCard, "内核版本", "4.14.190-临时")
        addRow(infoCard, "管理器版本", "v0.9.5 (11023)")
        addRow(infoCard, "工作模式", "KernelSU", 0xFF00FFC8.toInt())
        addRow(infoCard, "SELinux", "Permissive")
        addRow(infoCard, "已授权应用", "0")
        content.addView(infoCard)

        content.addView(tv("运行状态", 11f, 0xFF5A6A78.toInt()).apply { setPadding(0, 20, 0, 4) })
        val stCard = createCard()
        addRow(stCard, "su 二进制", "已注入", 0xFF00FFC8.toInt())
        addRow(stCard, "/data/adb/ksu", "已挂载", 0xFF00FFC8.toInt())
        addRow(stCard, "Zygote", "已重启", 0xFF00FFC8.toInt())
        content.addView(stCard)
    }

    private fun addRow(parent: LinearLayout, k: String, v: String, vColor: Int = 0xFFE8F0F5.toInt()) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 18, 0, 18)
        }
        val kTv = tv(k, 13.5f, 0xFF7A8A99.toInt())
        kTv.layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        row.addView(kTv)
        row.addView(tv(v, 13.5f, vColor))
        parent.addView(row)
    }

    private fun showSuperUser() {
        clear()
        val card = createCard()
        val ring = TextView(this).apply {
            text = "!"
            textSize = 36f
            setTextColor(0xFFFF5A5A.toInt())
            gravity = Gravity.CENTER
            setBackgroundResource(R.drawable.ring_bad)
            val lp = LinearLayout.LayoutParams(100, 100).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            layoutParams = lp
        }
        card.addView(ring)
        card.addView(tv("超级用户服务未响应", 17f).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            typeface = Typeface.DEFAULT_BOLD
            setPadding(0, 22, 0, 6)
        })
        card.addView(tv("无法连接到 KernelSU 守护进程\n请稍后重试或重启设备", 13f, 0xFF7A8A99.toInt()).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            setLineSpacing(6f, 1f)
        })
        val btn = tv("重 试", 13f, 0xFF00FFC8.toInt()).apply {
            gravity = Gravity.CENTER
            setBackgroundResource(R.drawable.chip_bg)
            setPadding(50, 22, 50, 22)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { gravity = Gravity.CENTER_HORIZONTAL; topMargin = 26 }
            layoutParams = lp
            setOnClickListener {
                Toast.makeText(this@KsuActivity, "连接超时，请稍后重试", Toast.LENGTH_SHORT).show()
            }
        }
        card.addView(btn)
        content.addView(card)
    }

    private fun showModules() {
        clear()
        // 修复：先把 count 算出来，避免嵌套 lambda 的 it 解析问题
        val installedCount = modules.count { it.installed }
        val totalCount = modules.size
        content.addView(tv("已安装模块 · $installedCount/$totalCount",
            11f, 0xFF5A6A78.toInt()))

        modules.forEachIndexed { idx, m ->
            val card = createCard()
            card.addView(tv(m.name, 15f).apply { typeface = Typeface.DEFAULT_BOLD })
            card.addView(tv(m.desc, 12.5f, 0xFF7A8A99.toInt()))
            card.addView(tv("作者：${m.author}", 11f, 0xFF4A5A68.toInt()))
            val installBtn = tv(if (m.installed) "已安装" else "安 装",
                if (m.installed) 0xFF5A6A78.toInt() else 0xFF00FFC8.toInt()).apply {
                setBackgroundResource(if (m.installed) R.drawable.chip_bg_dim else R.drawable.chip_bg)
                setPadding(46, 20, 46, 20)
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { topMargin = 20 }
                layoutParams = lp
                if (!m.installed) setOnClickListener { install(idx) }
            }
            card.addView(installBtn)
            content.addView(card)
        }
    }

    private fun install(idx: Int) {
        val m = modules[idx]
        Toast.makeText(this, "正在安装 ${m.name} …", Toast.LENGTH_SHORT).show()
        content.postDelayed({
            modules[idx] = m.copy(installed = true)
            Toast.makeText(this, "${m.name} 安装成功", Toast.LENGTH_SHORT).show()
            showModules()
        }, 1400)
    }

    private fun showSettings() {
        clear()
        content.addView(tv("高级功能 · 20 项", 11f, 0xFF5A6A78.toInt()))
        features.forEach { feature ->
            val card = createCard()
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
            }
            val label = tv(feature, 14f)
            label.layoutParams = LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            )
            label.setPadding(0, 0, 0, 0)
            row.addView(label)

            val sw = Switch(this).apply {
                isChecked = false
                setOnCheckedChangeListener { _, b ->
                    Toast.makeText(this@KsuActivity,
                        "$feature 已${if (b) "开启" else "关闭"}", Toast.LENGTH_SHORT).show()
                }
            }
            row.addView(sw)
            card.addView(row)
            content.addView(card)
        }
    }

    data class Module(
        val name: String,
        val desc: String,
        val author: String,
        val installed: Boolean
    )
}
