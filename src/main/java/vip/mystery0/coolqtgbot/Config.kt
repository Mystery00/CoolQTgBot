package vip.mystery0.coolqtgbot

class Config {
	var command: String = "?"
	var enableGroup = ArrayList<Long>()
	var maxWarn = 3
	var warnList = HashMap<Long, Int>()

	fun update(filePath: String) {
		ConfigFactory.save(filePath, this, true)
	}
}