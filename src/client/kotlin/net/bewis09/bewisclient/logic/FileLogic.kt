package net.bewis09.bewisclient.logic

import net.fabricmc.loader.api.FabricLoader
import java.io.File

interface FileLogic {
    /**
     * Saves the given content to a file at the specified relative path.
     * The path is relative to the game directory.
     *
     * @param content The content to save in the file.
     * @param path The relative path where the file should be saved.
     */
    fun saveRelativeFile(content: String, vararg path: String) {
        val file = File(FabricLoader.getInstance().gameDir.toString() + "/${path.joinToString("/")}")

        file.parentFile.mkdirs()
        file.writeText(content)
    }

    /**
     * Reads the content of a file at the specified relative path.
     * The path is relative to the game directory.
     *
     * @param path The relative path of the file to read.
     * @return The content of the file as a String, or null if the file does not exist.
     */
    fun readRelativeFile(vararg path: String): String? {
        val file = File(FabricLoader.getInstance().gameDir.toString() + "/${path.joinToString("/")}")

        if (!file.exists()) return null

        return file.readText()
    }
}
