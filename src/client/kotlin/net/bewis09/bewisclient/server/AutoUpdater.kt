package net.bewis09.bewisclient.server

import com.google.gson.Gson
import net.bewis09.bewisclient.common.Util
import Updater
import net.bewis09.bewisclient.common.catch
import net.bewis09.bewisclient.common.getMinecraftVersion
import net.bewis09.bewisclient.generated.BuildInfo
import net.bewis09.bewisclient.features.sidebar.General
import net.bewis09.bewisclient.util.EventEntrypoint
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.util.jar.JarFile
import kotlin.io.path.pathString

object AutoUpdater : EventEntrypoint {
    var downloadedFile: File? = null

    override fun onInitializeClient() {
        Util.nonCriticalIoPool().execute {
            catch {
                if (General.autoUpdate.get()) {
                    checkForUpdates()
                }
            }
        }
    }

    fun checkForUpdates() {
        val versions = downloadSync("https://api.modrinth.com/v2/project/bewisclient/version").decodeToString()
        val bcVersion = BuildInfo.VERSION
        val mrVersion = getMinecraftVersion()
        var found = false
        var version: Modrinth.Version? = null
        for (it in Gson().fromJson(versions, Array<Modrinth.Version>::class.java).sortedByDescending(Modrinth.Version::date_published)) {
            if (it.version_number == bcVersion) {
                found = true
                break
            }

            if (version == null && it.version_type == "release" && it.game_versions.contains(mrVersion)) {
                version = it
            }
        }

        if (!found || version == null) return

        val file = version.files.firstOrNull { it.primary } ?: return
        val bytes = downloadSync(file.url)
        val sha1 = Security.sha1(bytes)

        if (sha1 != file.hashes.sha1) return

        downloadedFile = FabricLoader.getInstance().gameDir.resolve("bewisclient/server/updates/files/${file.filename}").toFile()
        saveRelativeFile(bytes, "bewisclient", "server", "updates", "files", file.filename)

        loadClassFile()
    }

    fun loadClassFile() {
        val bytes = getClassBytecode(Updater::class.java.protectionDomain.codeSource.location.path, Updater::class.java.name.replace('.', '/') + ".class") ?: return
        saveRelativeFile(bytes, "bewisclient", "server", "updates", "java", "Updater.class")
    }

    fun getClassBytecode(jarPath: String, classPath: String): ByteArray? {
        return try {
            JarFile(jarPath).use { jarFile ->
                jarFile.getJarEntry(classPath)?.let { entry ->
                    jarFile.getInputStream(entry).use { inputStream ->
                        inputStream.readBytes()
                    }
                }
            }
        } catch (_: Exception) {
            null
        }
    }

    override fun onDestroy() {
        val file = downloadedFile ?: return
        if (!System.getProperty("os.name").lowercase().contains("win")) return

        if (General.autoUpdate.get()) {
            val javaHome = System.getProperty("java.home")
            val f = File("$javaHome\\bin\\javaw.exe")
            val cmd = "cd " + FabricLoader.getInstance().gameDir.pathString + "\\bewisclient\\server\\updates\\java && " +
                    f.path + " Updater " + file.path + " " + Updater::class.java.protectionDomain.codeSource.location.path

            info(cmd)
            val builder = ProcessBuilder(
                "cmd.exe", "/c",
                cmd
            )
            builder.redirectErrorStream(true)
            builder.start()
        }
    }
}