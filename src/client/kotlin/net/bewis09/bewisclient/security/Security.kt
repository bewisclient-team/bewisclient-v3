package net.bewis09.bewisclient.security

import com.google.gson.Gson
import com.google.gson.JsonObject
import net.bewis09.bewisclient.generated.BuildInfo
import net.bewis09.bewisclient.util.EventEntrypoint
import net.bewis09.bewisclient.util.catch
import net.bewis09.bewisclient.util.logic.BewisclientInterface
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.Util
import java.io.File
import java.io.FileNotFoundException
import java.net.URI
import java.security.MessageDigest

@Suppress("ClassName")
object Security : BewisclientInterface, EventEntrypoint {
    var verificationState: VerificationState = UNVERIFIED

    open class VerificationState(val allowed: Boolean)

    object UNVERIFIED : VerificationState(true)
    object VERIFIED : VerificationState(true)
    class VERIFICATION_FAILED(val reason: String) : VerificationState(true)
    class ILLEGAL(val reason: String) : VerificationState(false)

    override fun onInitializeClient() {
        Util.getIoWorkerExecutor().execute {
            verify()
        }
    }

    fun verify() {
        try {
            verifyStep("Bewisclient file was changed to pretend to be a different mod.") {
                FabricLoader.getInstance().allMods.any { it.metadata.id == "bewisclient" }
            }

            verifyStep("Bewisclient file was changed to an unrecognized version.") {
                val version = FabricLoader.getInstance().allMods.first { it.metadata.id == "bewisclient" }.metadata.version.friendlyString
                version == BuildInfo.VERSION
            }

            try {
                val mod = FabricLoader.getInstance().allMods.first { it.metadata.id == "bewisclient" }
                val file: File = mod.origin.paths.firstOrNull()?.toFile() ?: run {
                    verificationState = VERIFIED
                    return
                }

                if (file.isDirectory) return

                val connection = URI("https://api.modrinth.com/v2/version_file/" + sha1(file.readBytes())).toURL().openConnection()
                connection.getInputStream().use { input ->
                    val bytes = input.readBytes()

                    val json = Gson().fromJson(String(bytes), JsonObject::class.java)
                    val versionNumber = json.get("version_number")

                    if (!(versionNumber == null || versionNumber.asString != BuildInfo.VERSION)) {
                        verificationState = VERIFIED
                        return
                    } else {
                        throw SecurityException("Security verification failed: Bewisclient version is not recognized as a valid Modrinth release.")
                    }
                }
            } catch (e: Exception) {
                if (e is FileNotFoundException) {
                    throw SecurityException("Security verification failed: Bewisclient file is not recognized as a valid Modrinth release.")
                }

                verificationState = VERIFICATION_FAILED("Could not verify Bewisclient on Modrinth: ${e.message ?: "Unknown reason"}")
                return
            }
        } catch (e: Exception) {
            verificationState = ILLEGAL(e.message ?: "Unknown reason")
        }
    }

    fun verifyStep(message: String, block: () -> Boolean) {
        if (catch { !block() } ?: throw SecurityException("Security verification failed: $message")) {
            throw SecurityException("Security verification failed: $message")
        }
    }

    fun sha1(input: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-1")
        digest.update(input)
        return digest.digest().joinToString("") { "%02x".format(it) }
    }
}