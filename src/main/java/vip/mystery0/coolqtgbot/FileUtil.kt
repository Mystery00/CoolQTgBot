package vip.mystery0.coolqtgbot

import java.io.*
import vip.mystery0.coolqtgbot.GsonFactory.toT

object FileUtil {
	fun write(filePath: String, data: Config) {
		val file = File(filePath)
		if (file.exists()) file.delete()
		if (!file.parentFile.exists()) file.parentFile.mkdirs()
		val writer = FileWriter(file)
		writer.write(GsonFactory.toJson(data))
		writer.close()
	}

	fun read(filePath: String): Config? {
		val file = File(filePath)
		if (!file.exists()) return null
		return try {
			val stringBuilder = StringBuilder()
			val bufferedReader = BufferedReader(FileReader(file))
			var line: String? = bufferedReader.readLine()
			while (line != null) {
				stringBuilder.appendln(line)
				line = bufferedReader.readLine()
			}
			bufferedReader.close()
			stringBuilder.toString().toT(Config::class.java)
		} catch (e: Exception) {
			e.printStackTrace()
			null
		}
	}
}