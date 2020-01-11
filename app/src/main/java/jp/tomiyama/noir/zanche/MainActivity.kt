package jp.tomiyama.noir.zanche

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_main.*

// TODO リセットボタンの作成

class MainActivity : AppCompatActivity() {

    val shinpuList by lazy {
        resources.getStringArray(R.array.shinpuList)
    }

    // 神符数を管理する変数
    var shinpus = arrayListOf<Shinpu>()

    val shinpuNames by lazy {
        listOf<AutoCompleteTextView>(shinpu_name_1, shinpu_name_2, shinpu_name_3, shinpu_name_4, shinpu_name_5, shinpu_name_6, shinpu_name_7, shinpu_name_8, shinpu_name_9, shinpu_name_10)
    }

    val shinpuNumbers by lazy {
        listOf<AutoCompleteTextView>(shinpu_number_1, shinpu_number_2, shinpu_number_3, shinpu_number_4, shinpu_number_5, shinpu_number_6, shinpu_number_7, shinpu_number_8, shinpu_number_9, shinpu_number_10)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 神符一覧表
        shinpu_button.setOnClickListener {
            val dialog = MyDialogFragment(shinpuList)
            dialog.show(supportFragmentManager, "dialog")
        }

        // 出力時に渡す，神符配列の初期化
        for ((index, name) in shinpuList.withIndex()) {
            shinpus.add(Shinpu(index, name))
            Log.d("index, name", "${index}, $name")
        }

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, shinpuList)

        for (i in shinpuNames.indices) {
            shinpuNumbers[i].addTextChangedListener(CustomTextWatcher(shinpuNames[i], shinpuList))
            shinpuNames[i].setAdapter(adapter)
        }

        confirm_button.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("出力確認")
                .setMessage("内容を出力します。よろしいですか。")
                .setPositiveButton("OK") { dialog, which ->
                    // OK button pressed
                    calc()
                    val intent = Intent(applicationContext, ResultActivity::class.java)
                    intent.putExtra("shinpus", shinpus)
                    // 画面遷移
                    startActivity(intent)
                }
                .show()
        }

        var statusNumber = 1

        // 次の10件を取得できるようにする
        next_button.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("title")
                .setMessage("message")
                .setPositiveButton("OK") { _, _ ->
                    // OK button pressed
                    scrollView.fullScroll(ScrollView.FOCUS_UP)
                    statusNumber++
                    status.text = "${statusNumber}枚目"
                    // 値の格納
                    calc()
                    // リセット
                    clearEditText()
                }
                .show()
        }

        toggle.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // The toggle is enabled
                toggle.text = "受け"
            } else {
                // The toggle is disabled
                toggle.text = "譲渡"
            }
        }

//        shinpu_name_1.setOnKeyListener(object : View.OnKeyListener {
//            //コールバックとしてonKey()メソッドを定義
//            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
//                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//
//                //イベントを取得するタイミングには、ボタンが押されてなおかつエンターキーだったときを指定
//                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
//                    //キーボードを閉じる
//                    inputMethodManager.hideSoftInputFromWindow(shinpu_name_1.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN)
//                    return true
//                }
//                return false
//            }
//        })
    }

    private fun calc() {
        for ((index, s_name) in shinpuNames.withIndex()) {
            // 入力されたデータが空でなければ、処理を行う
            if (s_name.text.toString().isNotEmpty()) {
                // 神符名からIndex値を取得
                val targetIndex = shinpuList.indexOf(s_name.text.toString())
                // 入力された数値を神符数に加減する
                var updateNumber = 0
                if (shinpuNumbers[index].text.toString().isNotEmpty()) {
                    updateNumber = shinpus[targetIndex].number + shinpuNumbers[index].text.toString().toInt()
                }
                // 増減した数値をもとに，値の更新
                shinpus[targetIndex] = Shinpu(targetIndex, s_name.text.toString(), updateNumber)
                Log.d("targetIndex", targetIndex.toString())
                Log.d("shinpus[targetIndex]", "${shinpus[targetIndex].index}, ${shinpus[targetIndex].name}, ${shinpus[targetIndex].number}")
            }
        }
    }

    private fun clearEditText() {
        for (sn in shinpuNumbers) {
            sn.editableText.clear()
        }
        for (sn in shinpuNames) {
            sn.editableText.clear()
        }
    }

    // EditTextのバリデーション処理
    class CustomTextWatcher(private val editText: AutoCompleteTextView, private val list: Array<String>) : TextWatcher {

        //操作後のEditTextの状態を取得する
        override fun afterTextChanged(s: Editable?) {
            if (editText.text!!.isNotEmpty() && !list.contains(editText.text.toString())) {
                editText.error = "神符名を確認してください"
            }
        }

        //操作前のEditTextの状態を取得する
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        //操作中のEditTextの状態を取得する
        override fun onTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }
    }

    class MyDialogFragment(private var shinpuList: Array<String>) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val dialog = AlertDialog.Builder(activity)
            dialog.setTitle("神符一覧表")

            val stringBuilder = StringBuilder()
            for (s in shinpuList) {
                if (s == "名称") continue
                stringBuilder.append(s)
                stringBuilder.append("\n")
            }
            dialog.setMessage(stringBuilder.toString())
            dialog.setPositiveButton(
                "OK"
            ) { _, _ ->
                // do nothing.
            }
            return dialog.create()
        }
    }
}
