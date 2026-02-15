package net.bewis09.bewisclient.process

import net.minecraft.util.Util
import java.io.File

object ProcessCreator {
    fun create(clazz: Class<*>, vararg args: String, onFinish: ((exitCode: Int) -> Unit)? = null) {
        Util.getMainWorkerExecutor().execute {
            val javaHome = System.getProperty("java.home")
            val javaExe = javaHome + File.separator + "bin" + File.separator + "java"
            val processBuilder = ProcessBuilder(javaExe, "-cp", clazz.protectionDomain.codeSource.location.toURI().path, clazz.getName(), *args)
            val process = processBuilder.start()
            val output = process.waitFor()
            onFinish?.invoke(output)
        }
    }
}