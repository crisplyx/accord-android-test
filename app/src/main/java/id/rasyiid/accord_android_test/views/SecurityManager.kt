package id.rasyiid.accord_android_test.views

import android.content.Context
import com.auth0.android.jwt.JWT
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import id.rasyiid.accord_android_test.BuildConfig
import id.rasyiid.accord_android_test.domain.auth.dto.SignInResponseDto
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject

@ActivityScoped
class SecurityManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private lateinit var mSecretByte: ByteArray
    private val mFile = File(context.filesDir.absolutePath)
    private var mFiles: Array<File>? = null

    private fun randomId(size: Int): String = List(size) {
        (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
    }.joinToString("")

    fun storeAuthInfo(signInResponseDto: SignInResponseDto) {
        val json = Json.encodeToString(signInResponseDto)
        if(isAuthFileExist()) clear()
        saveAuthFile(fileName = randomId(64), value = json.toByteArray())
    }

    private fun saveAuthFile(fileName: String, value: ByteArray) {
        try {
            val file = File("${context.filesDir.absolutePath}/$fileName${FILE_EXTENSION}")
            val isNewFileCreated: Boolean = file.createNewFile()

            if (isNewFileCreated) {
                file.writeBytes(value)
            }
        } catch (e: Exception) {
            if(BuildConfig.DEBUG) e.printStackTrace()
        }
    }

    private fun isAuthFileExist(): Boolean {
        var result = false
        val files = mFile.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (i in files.indices) {
                if (files[i].name.contains(FILE_EXTENSION)) {
                    result = true
                    break
                }
            }
        }
        return result
    }

    private fun getByteArray(): ByteArray? {
        var result: ByteArray? = null
        mFiles = mFile.listFiles()
        if (mFiles!!.isNotEmpty()) {
            for (i in mFiles!!.indices) {
                if (mFiles!![i].name.contains(FILE_EXTENSION)) {
                    result = mFiles!![i].readBytes()
                    break
                }
            }
        }
        return result
    }

    /**
     * This function will return Token or null.
     * */
    fun getToken(): String? {
        var result: String? = null
        getSignInResponseDto()?.let {
            result = it.token
        }
        return result
    }

    /**
     * This function will return JWT Token user claims.
     * */
    fun getSub(): Int {
        var sub = -1
        getToken()?.let{
            sub = Integer.valueOf(JWT(it).subject)
        }
        return sub
    }

    private fun getSignInResponseDto(): SignInResponseDto? {
        var result: SignInResponseDto? = null
        getByteArray()?.let {
            mSecretByte = it
            result = Json.decodeFromString<SignInResponseDto>(String(mSecretByte))
        }
        return result
    }

    fun clear() {
        val files = mFile.listFiles()
        if (files != null && files.isNotEmpty()) {
            for (i in files.indices) {
                if (files[i].name.contains(FILE_EXTENSION)) files[i].delete()
            }
        }
    }

    companion object {
        private const val FILE_EXTENSION = ".sf"
    }
}