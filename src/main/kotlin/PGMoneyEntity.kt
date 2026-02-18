package ru.mrimax

import org.bukkit.plugin.java.JavaPlugin

class PGMoneyEntity: JavaPlugin() {

    // Перемеенные для чтения
    val version = this.description.version //версия плагина

    override fun onEnable() {
        logger.apply {
            info("Плагин включён!")
            info("Версия плагина: $version")
        }
    }

    override fun onDisable() {
        logger.info("Плагин выключился!")
    }


}

