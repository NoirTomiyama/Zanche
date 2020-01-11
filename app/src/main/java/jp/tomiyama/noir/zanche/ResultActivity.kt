package jp.tomiyama.noir.zanche

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_result.*


class ResultActivity : AppCompatActivity() {

    private lateinit var shinpus: List<Shinpu>
    lateinit var adapter: ShinpuAdapter

    // TODO バックした際に、EditTextを消す

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        shinpus = intent.getSerializableExtra("shinpus") as ArrayList<Shinpu>
        adapter = ShinpuAdapter(shinpus as ArrayList<Shinpu>)

        val manager = LinearLayoutManager(applicationContext)

        recyclerView.layoutManager = manager
        recyclerView.adapter = adapter

        // 区切り専用のオブジェクトを生成
        val decorator = DividerItemDecoration(applicationContext, manager.orientation)
        // RecyclerViewに区切り線オブジェクトを設定
        recyclerView.addItemDecoration(decorator)

        val stringBuilder = StringBuilder()
        for (s in shinpus) {
            if (s.index == 0) continue
            stringBuilder.append("${s.index} : ${s.name}\t${s.number}\n")
        }

        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        copy_button.setOnClickListener {
            clipboardManager.setPrimaryClip(ClipData.newPlainText("", stringBuilder.toString()))
            Toast.makeText(this, "クリップボードにコピーしました", Toast.LENGTH_SHORT).show()
        }
    }
}