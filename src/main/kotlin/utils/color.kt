package ru.mrimax.utils

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.regex.Pattern

object color {
    //Форматирование цвета!
    private val HEX_PATTERN = Pattern.compile("(&?#)([A-Fa-f0-9]{6})")
    private val GRADIENT_PATTERN = Pattern.compile("<gradient:([A-Fa-f0-9]{6}),([A-Fa-f0-9]{6})>(.*?)</gradient>")

    fun format(text: String, vararg args: Pair<String, String>): String {
        var result = applyArgs(text, *args)
        result = applyGradients(result)
        result = convertHexColors(result)
        return ChatColor.translateAlternateColorCodes('&', result)
    }

    private fun convertHexColors(text: String): String {
        val matcher = HEX_PATTERN.matcher(text)
        val buffer = StringBuffer()

        while (matcher.find()) {
            val hex = matcher.group(2)
            val minecraftHex = hex.toCharArray()
                .joinToString("") { "§$it" }
            matcher.appendReplacement(buffer, "§x$minecraftHex")
        }
        matcher.appendTail(buffer)

        return buffer.toString()
    }

    private fun applyGradients(text: String): String {
        val matcher = GRADIENT_PATTERN.matcher(text)
        val buffer = StringBuffer()

        while (matcher.find()) {
            val startHex = matcher.group(1)
            val endHex = matcher.group(2)
            val content = matcher.group(3)

            val gradient = generateGradient(content, startHex, endHex)
            matcher.appendReplacement(buffer, gradient)
        }
        matcher.appendTail(buffer)

        return buffer.toString()
    }

    private fun generateGradient(text: String, startHex: String, endHex: String): String {
        val start = Color.fromRGB(
            Integer.parseInt(startHex.substring(0, 2), 16),
            Integer.parseInt(startHex.substring(2, 4), 16),
            Integer.parseInt(startHex.substring(4, 6), 16)
        )

        val end = Color.fromRGB(
            Integer.parseInt(endHex.substring(0, 2), 16),
            Integer.parseInt(endHex.substring(2, 4), 16),
            Integer.parseInt(endHex.substring(4, 6), 16)
        )

        val length = text.length
        return text.mapIndexed { index, char ->
            val ratio = index.toDouble() / (length - 1)
            val color = interpolateColor(start, end, ratio)
            "§x${colorToHex(color)}${char}"
        }.joinToString("")
    }

    private fun interpolateColor(start: Color, end: Color, ratio: Double): Color {
        val red = (start.red + (end.red - start.red) * ratio).toInt()
        val green = (start.green + (end.green - start.green) * ratio).toInt()
        val blue = (start.blue + (end.blue - start.blue) * ratio).toInt()
        return Color.fromRGB(red, green, blue)
    }

    private fun colorToHex(color: Color): String {
        return String.format("%02x%02x%02x", color.red, color.green, color.blue)
            .toCharArray().joinToString("") { "§$it" }
    }

    private fun applyArgs(text: String, vararg args: Pair<String, String>): String {
        var result = text
        args.forEach { (key, value) ->
            result = result.replace("{$key}", value)
        }
        return result
    }
    //Метод отправки сообщения
    fun CommandSender.message(msg: String, vararg args: Pair<String, String>) {
        sendMessage (format(msg, *args))
    }
    //Метод отправки много сообщений
    fun CommandSender.message(msg: List<String>, vararg args: Pair<String, String>) {
        msg.forEach { msg ->
            sendMessage(format(msg, *args))
        }
    }
    //Метод возвращения сообщения в метод (bukkit методах и т.д)
    fun hex(msg: String, vararg args: Pair<String, String>): String {
        return format(msg, *args)
    }
    //Метод отправки игроку Тайтла на экран
    fun Player.title (msg: String, subMsg: String, fadeIn: Int = 10, stay: Int = 20, fadeOut: Int = 10, vararg args: Pair<String, String>) {
        sendTitle(format(msg, *args), format(subMsg, *args), fadeIn, stay, fadeOut)
    }
    //Метод для отправки Обьявления
    fun broadcast(msg: String, vararg args: Pair<String, String>) {
        Bukkit.getOnlinePlayers().forEach { it.message(msg, *args) }
    }
    //Метод для отправки сообщения над хотбаром
    fun Player.actionbar(msg: String, vararg args: Pair<String, String>) {
        sendActionBar(format(msg, *args))
    }
    // Создайте extension функцию
    fun String.toBooleanCommand(): Boolean? {
        return when (this.lowercase()) {
            "true" -> true
            "false" -> false
            else -> null
        }
    }
}
