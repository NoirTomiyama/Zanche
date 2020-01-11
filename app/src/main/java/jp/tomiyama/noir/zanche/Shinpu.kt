package jp.tomiyama.noir.zanche

import java.io.Serializable

data class Shinpu(var index: Int, var name: String, var number: Int = 0) : Serializable