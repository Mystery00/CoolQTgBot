package vip.mystery0.coolqtgbot

import java.io.File

object ConfigFactory {
	fun save(parentPath: String, config: Config = Config(), forceReplace: Boolean = false) {
		val file = File(parentPath, getConfigName())
		if (!file.parentFile.isDirectory)
			file.parentFile.delete()
		if (!file.parentFile.exists())
			file.parentFile.mkdirs()
		if (forceReplace || !file.exists())
			FileUtil.write(file.absolutePath, config)
	}

	fun get(parentPath: String): Config {
		val file = File(parentPath, getConfigName())
		var config = FileUtil.read(file.absolutePath)
		if (config == null) {
			config = Config()
			save(parentPath, config, forceReplace = true)
		}
		return config
	}

	private fun getConfigName(): String = "config.json"
}