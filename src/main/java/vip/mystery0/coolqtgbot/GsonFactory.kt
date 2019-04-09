package vip.mystery0.coolqtgbot

import com.google.gson.Gson

object GsonFactory {
	private val gson by lazy { Gson() }

	fun toJson(any: Any): String = gson.toJson(any)

	fun <T> String.toT(clazz: Class<T>): T {
		return gson.fromJson<T>(this, clazz)
	}
}
