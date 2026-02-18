package ru.mrimax

import org.bukkit.plugin.java.JavaPlugin

class Test: JavaPlugin() {

    // Перемеенные для чтения
    val version = this.description.version //версия плагина

    override fun onEnable() {
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


}



