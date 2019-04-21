package vip.mystery0.coolqtgbot

import com.sobte.cqp.jcq.entity.*
import com.sobte.cqp.jcq.entity.IRequest.*
import com.sobte.cqp.jcq.event.JcqApp
import com.sobte.cqp.jcq.event.JcqAppAbstract

import javax.swing.*

/**
 * 本文件是JCQ插件的主类<br></br>
 * <br></br>
 *
 *
 * 注意修改json中的class来加载主类，如不设置则利用appid加载，最后一个单词自动大写查找<br></br>
 * 例：appid(com.example.demo) 则加载类 com.example.Demo<br></br>
 * 文档地址： https://gitee.com/Sobte/JCQ-CoolQ <br></br>
 * 帖子：https://cqp.cc/t/37318 <br></br>
 * 辅助开发变量: [CQ][JcqAppAbstract.CQ]([酷Q核心操作类][com.sobte.cqp.jcq.entity.CoolQ]),
 * [CC][JcqAppAbstract.CC]([酷Q码操作类][com.sobte.cqp.jcq.message.CQCode]),
 * 具体功能可以查看文档
 */
class JCQApp : JcqAppAbstract(), ICQVer, IMsg, IRequest {
	private val config by lazy { ConfigFactory.get(JcqApp.CQ.appDirectory) }
	private val dir by lazy { JcqApp.CQ.appDirectory }

	/**
	 * 打包后将不会调用 请不要在此事件中写其他代码
	 *
	 * @return 返回应用的ApiVer、Appid
	 */
	override fun appInfo(): String {
		/**
		 * 本函数【禁止】处理其他任何代码，以免发生异常情况。
		 * 如需执行初始化代码请在 startup 事件中执行（Type=1001）。
		 */
		return ICQVer.CQAPIVER.toString() + ",vip.mystery0.coolq-tgbot"
	}

	/**
	 * 酷Q启动 (Type=1001)<br></br>
	 * 本方法会在酷Q【主线程】中被调用。<br></br>
	 * 请在这里执行插件初始化代码。<br></br>
	 * 请务必尽快返回本子程序，否则会卡住其他插件以及主程序的加载。
	 *
	 * @return 请固定返回0
	 */
	override fun startup(): Int {
		ConfigFactory.save(JcqApp.CQ.appDirectory)
		return 0
	}

	/**
	 * 酷Q退出 (Type=1002)<br></br>
	 * 本方法会在酷Q【主线程】中被调用。<br></br>
	 * 无论本应用是否被启用，本函数都会在酷Q退出前执行一次，请在这里执行插件关闭代码。
	 *
	 * @return 请固定返回0，返回后酷Q将很快关闭，请不要再通过线程等方式执行其他代码。
	 */
	override fun exit(): Int = 0

	/**
	 * 应用已被启用 (Type=1003)<br></br>
	 * 当应用被启用后，将收到此事件。<br></br>
	 * 如果酷Q载入时应用已被启用，则在 [startup][.startup](Type=1001,酷Q启动) 被调用后，本函数也将被调用一次。<br></br>
	 * 如非必要，不建议在这里加载窗口。
	 *
	 * @return 请固定返回0。
	 */
	override fun enable(): Int {
		JcqAppAbstract.enable = true
		return 0
	}

	/**
	 * 应用将被停用 (Type=1004)<br></br>
	 * 当应用被停用前，将收到此事件。<br></br>
	 * 如果酷Q载入时应用已被停用，则本函数【不会】被调用。<br></br>
	 * 无论本应用是否被启用，酷Q关闭前本函数都【不会】被调用。
	 *
	 * @return 请固定返回0。
	 */
	override fun disable(): Int {
		JcqAppAbstract.enable = false
		return 0
	}

	/**
	 * 私聊消息 (Type=21)<br></br>
	 * 本方法会在酷Q【线程】中被调用。<br></br>
	 *
	 * @param subType 子类型，11/来自好友 1/来自在线状态 2/来自群 3/来自讨论组
	 * @param msgId   消息ID
	 * @param fromQQ  来源QQ
	 * @param msg     消息内容
	 * @param font    字体
	 * @return 返回值*不能*直接返回文本 如果要回复消息，请调用api发送<br></br>
	 * 这里 返回  [MSG_INTERCEPT][IMsg.MSG_INTERCEPT] - 截断本条消息，不再继续处理<br></br>
	 * 注意：应用优先级设置为"最高"(10000)时，不得使用本返回值<br></br>
	 * 如果不回复消息，交由之后的应用/过滤器处理，这里 返回  [MSG_IGNORE][IMsg.MSG_IGNORE] - 忽略本条消息
	 */
	override fun privateMsg(subType: Int, msgId: Int, fromQQ: Long, msg: String, font: Int): Int {
		JcqApp.CQ.sendPrivateMsg(fromQQ, "我是一个机器人哦~")
		if (fromQQ != config.manager && config.manager != 0L) {
			val type = when (subType) {
				11 -> "来自好友"
				1 -> "来自在线状态"
				2 -> "来自群"
				3 -> "来自讨论组"
				else -> "来自未知的黑洞"
			}
			JcqApp.CQ.sendPrivateMsg(config.manager, "接收到来自【$fromQQ】的消息（$type），消息内容是【$msg】")
		}
		return IMsg.MSG_IGNORE
	}

	/**
	 * 群消息 (Type=2)<br></br>
	 * 本方法会在酷Q【线程】中被调用。<br></br>
	 *
	 * @param subType       子类型，目前固定为1
	 * @param msgId         消息ID
	 * @param fromGroup     来源群号
	 * @param fromQQ        来源QQ号
	 * @param fromAnonymous 来源匿名者
	 * @param msg           消息内容
	 * @param font          字体
	 * @return 关于返回值说明, 见 [私聊消息][.privateMsg] 的方法
	 */
	override fun groupMsg(subType: Int, msgId: Int, fromGroup: Long, fromQQ: Long, fromAnonymous: String, msg: String,
						  font: Int): Int {
		if (fromQQ != config.manager)//不是管理员的消息
			return IMsg.MSG_IGNORE
		if (!config.enableGroup.contains(fromGroup))//不是启用群的消息
			return IMsg.MSG_IGNORE
		if (!msg.startsWith(config.command))//不是命令
			return IMsg.MSG_IGNORE
		val cmd = msg.substring(1)
		when {
			cmd.startsWith("warn") -> {
				val atList = msg.parseAT()
				if (atList.isEmpty()) {
					val message = "命令格式错误，请输入： {${config.command}warn [@somebody]}"
					JcqApp.CQ.sendGroupMsg(fromGroup, "${JcqApp.CC.at(fromQQ)}\n$message")
					return IMsg.MSG_IGNORE
				}
				val atQQ = CC.getAt(atList[0])
				if (atQQ == -1000L) {//解析出错
					val message = "命令格式错误，请输入： {${config.command}warn [@somebody]}"
					JcqApp.CQ.sendGroupMsg(fromGroup, "${JcqApp.CC.at(fromQQ)}\n$message")
					return IMsg.MSG_IGNORE
				}
				val warn = config.warnList[atQQ]
				if (warn != null) {//此前有过警告记录
					val banTime = (warn * 2 + 1) * 60
					if (warn >= config.maxWarn) {//超过阈值，移除出群
						JcqApp.CQ.setGroupKick(fromGroup, atQQ, false)
						JcqApp.CQ.sendGroupMsg(fromGroup, "屡教不改，已移除出群！")
					} else {
						config.warnList[atQQ] = warn + 1
						JcqApp.CQ.setGroupBan(fromGroup, atQQ, banTime.toLong())
						JcqApp.CQ.sendGroupMsg(fromGroup, "${JcqApp.CC.at(atQQ)} 警告 ${warn + 1} 次！超过阈值（当前最多警告次数为：${config.maxWarn} 次）将会自动移除出群！")
					}
				} else {//第一次警告
					config.warnList[atQQ] = 1
					JcqApp.CQ.setGroupBan(fromGroup, atQQ, 60L)
					JcqApp.CQ.sendGroupMsg(fromGroup, "${JcqApp.CC.at(atQQ)} 警告 1 次！超过阈值（当前最多警告次数为：${config.maxWarn}次）将会自动移除出群！")
				}
			}
			cmd.startsWith("unwarn") -> {
				val atList = msg.parseAT()
				if (atList.isEmpty()) {
					val message = "命令格式错误，请输入： {${config.command}unwarn [@somebody]}"
					JcqApp.CQ.sendGroupMsg(fromGroup, "${JcqApp.CC.at(fromQQ)}\n$message")
					return IMsg.MSG_IGNORE
				}
				val atQQ = CC.getAt(atList[0])
				if (atQQ == -1000L) {//解析出错
					val message = "命令格式错误，请输入： {${config.command}unwarn [@somebody]}"
					JcqApp.CQ.sendGroupMsg(fromGroup, "${JcqApp.CC.at(fromQQ)}\n$message")
					return IMsg.MSG_IGNORE
				}
				val warn = config.warnList[atQQ]
				if (warn == null || warn == 0) {
					JcqApp.CQ.sendGroupMsg(fromGroup, "${JcqApp.CC.at(atQQ)} 该成员无警告信息。")
				} else {
					config.warnList[atQQ] = warn - 1
					JcqApp.CQ.setGroupBan(fromGroup, atQQ, 0L)
					JcqApp.CQ.sendGroupMsg(fromGroup, "操作完成！")
				}
			}
			cmd.startsWith("kick") -> {
				val atList = msg.parseAT()
				if (atList.isEmpty()) {
					val message = "命令格式错误，请输入： {${config.command}kick [@somebody]}"
					JcqApp.CQ.sendGroupMsg(fromGroup, "${JcqApp.CC.at(fromQQ)}\n$message")
					return IMsg.MSG_IGNORE
				}
				val atQQ = CC.getAt(atList[0])
				if (atQQ == -1000L) {//解析出错
					val message = "命令格式错误，请输入： {${config.command}kick [@somebody]}"
					JcqApp.CQ.sendGroupMsg(fromGroup, "${JcqApp.CC.at(fromQQ)}\n$message")
				}
				JcqApp.CQ.setGroupKick(fromGroup, atQQ, false)
				JcqApp.CQ.sendGroupMsg(fromGroup, "${JcqApp.CC.at(atQQ)} 已移除出群！")
			}
			else -> {
				JcqApp.CQ.sendGroupMsg(fromGroup, "${JcqApp.CC.at(fromQQ)} 未知指令")
			}
		}
		config.update(dir)
		return IMsg.MSG_IGNORE
	}

	/**
	 * 讨论组消息 (Type=4)<br></br>
	 * 本方法会在酷Q【线程】中被调用。<br></br>
	 *
	 * @param subtype     子类型，目前固定为1
	 * @param msgId       消息ID
	 * @param fromDiscuss 来源讨论组
	 * @param fromQQ      来源QQ号
	 * @param msg         消息内容
	 * @param font        字体
	 * @return 关于返回值说明, 见 [私聊消息][.privateMsg] 的方法
	 */
	override fun discussMsg(subtype: Int, msgId: Int, fromDiscuss: Long, fromQQ: Long, msg: String, font: Int): Int = IMsg.MSG_IGNORE

	/**
	 * 群文件上传事件 (Type=11)<br></br>
	 * 本方法会在酷Q【线程】中被调用。<br></br>
	 *
	 * @param subType   子类型，目前固定为1
	 * @param sendTime  发送时间(时间戳)// 10位时间戳
	 * @param fromGroup 来源群号
	 * @param fromQQ    来源QQ号
	 * @param file      上传文件信息
	 * @return 关于返回值说明, 见 [私聊消息][.privateMsg] 的方法
	 */
	override fun groupUpload(subType: Int, sendTime: Int, fromGroup: Long, fromQQ: Long, file: String): Int = IMsg.MSG_IGNORE

	/**
	 * 群事件-管理员变动 (Type=101)<br></br>
	 * 本方法会在酷Q【线程】中被调用。<br></br>
	 *
	 * @param subtype        子类型，1/被取消管理员 2/被设置管理员
	 * @param sendTime       发送时间(时间戳)
	 * @param fromGroup      来源群号
	 * @param beingOperateQQ 被操作QQ
	 * @return 关于返回值说明, 见 [私聊消息][.privateMsg] 的方法
	 */
	override fun groupAdmin(subtype: Int, sendTime: Int, fromGroup: Long, beingOperateQQ: Long): Int = IMsg.MSG_IGNORE

	/**
	 * 群事件-群成员减少 (Type=102)<br></br>
	 * 本方法会在酷Q【线程】中被调用。<br></br>
	 *
	 * @param subtype        子类型，1/群员离开 2/群员被踢
	 * @param sendTime       发送时间(时间戳)
	 * @param fromGroup      来源群号
	 * @param fromQQ         操作者QQ(仅子类型为2时存在)
	 * @param beingOperateQQ 被操作QQ
	 * @return 关于返回值说明, 见 [私聊消息][.privateMsg] 的方法
	 */
	override fun groupMemberDecrease(subtype: Int, sendTime: Int, fromGroup: Long, fromQQ: Long, beingOperateQQ: Long): Int = IMsg.MSG_IGNORE

	/**
	 * 群事件-群成员增加 (Type=103)<br></br>
	 * 本方法会在酷Q【线程】中被调用。<br></br>
	 *
	 * @param subtype        子类型，1/管理员已同意 2/管理员邀请
	 * @param sendTime       发送时间(时间戳)
	 * @param fromGroup      来源群号
	 * @param fromQQ         操作者QQ(即管理员QQ)
	 * @param beingOperateQQ 被操作QQ(即加群的QQ)
	 * @return 关于返回值说明, 见 [私聊消息][.privateMsg] 的方法
	 */
	override fun groupMemberIncrease(subtype: Int, sendTime: Int, fromGroup: Long, fromQQ: Long, beingOperateQQ: Long): Int = IMsg.MSG_IGNORE

	/**
	 * 好友事件-好友已添加 (Type=201)<br></br>
	 * 本方法会在酷Q【线程】中被调用。<br></br>
	 *
	 * @param subtype  子类型，目前固定为1
	 * @param sendTime 发送时间(时间戳)
	 * @param fromQQ   来源QQ
	 * @return 关于返回值说明, 见 [私聊消息][.privateMsg] 的方法
	 */
	override fun friendAdd(subtype: Int, sendTime: Int, fromQQ: Long): Int = IMsg.MSG_IGNORE

	/**
	 * 请求-好友添加 (Type=301)<br></br>
	 * 本方法会在酷Q【线程】中被调用。<br></br>
	 *
	 * @param subtype      子类型，目前固定为1
	 * @param sendTime     发送时间(时间戳)
	 * @param fromQQ       来源QQ
	 * @param msg          附言
	 * @param responseFlag 反馈标识(处理请求用)
	 * @return 关于返回值说明, 见 [私聊消息][.privateMsg] 的方法
	 */
	override fun requestAddFriend(subtype: Int, sendTime: Int, fromQQ: Long, msg: String, responseFlag: String): Int {
		/**
		 * REQUEST_ADOPT 通过
		 * REQUEST_REFUSE 拒绝
		 */
		CQ.setFriendAddRequest(responseFlag, REQUEST_REFUSE, "我是一个机器人，嗨嗨嗨~") // 同意好友添加请求
		return IMsg.MSG_IGNORE
	}

	/**
	 * 请求-群添加 (Type=302)<br></br>
	 * 本方法会在酷Q【线程】中被调用。<br></br>
	 *
	 * @param subtype      子类型，1/他人申请入群 2/自己(即登录号)受邀入群
	 * @param sendTime     发送时间(时间戳)
	 * @param fromGroup    来源群号
	 * @param fromQQ       来源QQ
	 * @param msg          附言
	 * @param responseFlag 反馈标识(处理请求用)
	 * @return 关于返回值说明, 见 [私聊消息][.privateMsg] 的方法
	 */
	override fun requestAddGroup(subtype: Int, sendTime: Int, fromGroup: Long, fromQQ: Long, msg: String,
								 responseFlag: String): Int {
		/**
		 * REQUEST_ADOPT 通过
		 * REQUEST_REFUSE 拒绝
		 * REQUEST_GROUP_ADD 群添加
		 * REQUEST_GROUP_INVITE 群邀请
		 */
		if (subtype == 2) {
			CQ.setGroupAddRequest(responseFlag, REQUEST_GROUP_INVITE, REQUEST_ADOPT, null)// 同意进受邀群
		}
		return IMsg.MSG_IGNORE
	}

	/**
	 * 本函数会在JCQ【线程】中被调用。
	 *
	 * @return 固定返回0
	 */
	fun menuSetCommand(): Int {
		val command = JOptionPane.showInputDialog(null, "请输入匹配的命令", "设置", JOptionPane.INFORMATION_MESSAGE)
		if (command != "") {
			config.command = command
			config.update(dir)
		}
		return 0
	}

	/**
	 * 本函数会在酷Q【线程】中被调用。
	 *
	 * @return 固定返回0
	 */
	fun menuSetGroupEnable(): Int {
		val list = JcqApp.CQ.groupList
		val valueList = list.map { "${it.name}(${it.id})" }
		val enable = config.enableGroup
		val jList = JList(valueList.toTypedArray())
		jList.selectedIndices = enable.map { list.indexOf(list.find { group -> it == group.id }) }.toIntArray()
		val jOptionPane = JOptionPane("请选择要启用插件的群")
		jOptionPane.add(jList, 1)
		val dialog = jOptionPane.createDialog("设置")
		dialog.isVisible = true
		dialog.dispose()
		val result = jList.selectedIndices
		config.enableGroup.clear()
		config.enableGroup.addAll(result.map { list[it].id })
		config.update(dir)
		return 0
	}

	/**
	 * 本函数会在JCQ【线程】中被调用。
	 *
	 * @return 固定返回0
	 */
	fun menuSetManager(): Int {
		val command = JOptionPane.showInputDialog(null, "请输入管理员的QQ账号", "设置", JOptionPane.INFORMATION_MESSAGE)
		if (command != "") {
			config.manager = command.toLong()
			config.update(dir)
		}
		return 0
	}

	private fun String.parseAT(): List<String> {
		val list = ArrayList<String>()
		var indexStart = -1
		this.forEachIndexed { index, c ->
			when (c) {
				'[' -> {
					indexStart = index
				}
				']' -> {
					if (indexStart != -1) {//此时有可用的开始符号，则添加进列表
						list.add(this.substring(indexStart, index + 1))
						indexStart = -1
					}
				}
			}
		}
		return list
	}
}
