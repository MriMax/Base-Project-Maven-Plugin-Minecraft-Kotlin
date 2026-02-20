package ru.mrimax

import org.bukkit.plugin.java.JavaPlugin

class Test: JavaPlugin() {

    // Перемеенные для чтения
    val version = this.description.version //версия плагина

    override fun onEnable() {
        //Инициализация глдавного класса
        instance = this;
        // Ваш код
        logger.apply {
            info("Плагин включён!")
            info("Версия плагина: $version")
        }
    }

    override fun onDisable() {
        // Ваш код
        logger.info("Плагин выключился!")
    }

    companion object { lateinit var instance: Test } //Переодрисачция в класс JavaPlugin
}




