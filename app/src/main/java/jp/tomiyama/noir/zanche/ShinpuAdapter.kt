package jp.tomiyama.noir.zanche

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_shinpu.view.*

class ShinpuAdapter(private var mShinpus: ArrayList<Shinpu>) :
    RecyclerView.Adapter<ShinpuAdapter.RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // レイアウトインフレータを取得
        val layoutInflater = LayoutInflater.from(parent.context)
        // layout_shinpu.xmlをインフレートし，1行分の画面部品とする
        val view = layoutInflater.inflate(R.layout.layout_shinpu, parent, false)
        // 生成したビューホルダーをリターンx
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        // リストデータから該当1行分のデータを取得
        val shinpu = mShinpus[position]
        holder.shinpuIndex.text = (shinpu.index + 1).toString()
        holder.shinpuName.text = shinpu.name
        if (position == 0) {
            holder.shinpuNumber.text = "残数"
        } else {
            holder.shinpuNumber.text = shinpu.number.toString()
        }

        if (shinpu.number > 0) {
            holder.shinpuIndex.setTextColor(Color.parseColor("#f44336"))
            holder.shinpuName.setTextColor(Color.parseColor("#f44336"))
            holder.shinpuNumber.setTextColor(Color.parseColor("#f44336"))
        } else if (shinpu.number < 0) {
            holder.shinpuIndex.setTextColor(Color.parseColor("#3f51b5"))
            holder.shinpuName.setTextColor(Color.parseColor("#3f51b5"))
            holder.shinpuNumber.setTextColor(Color.parseColor("#3f51b5"))
        } else {
            holder.shinpuIndex.setTextColor(Color.parseColor("#808080"))
            holder.shinpuName.setTextColor(Color.parseColor("#808080"))
            holder.shinpuNumber.setTextColor(Color.parseColor("#808080"))
        }
    }

    override fun getItemCount(): Int {
        return mShinpus.size
    }

    inner class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var shinpuIndex: TextView = view.shinpu_index
        val shinpuName: TextView = view.shinpu_name
        var shinpuNumber: TextView = view.shinpu_number
    }
}