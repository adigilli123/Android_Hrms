package com.example.CACHE


import android.content.Context
import android.os.Build
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.*
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

class CacheManager(private val context: Context) {

    companion object {
        private const val TAG = "CacheManager"
    }

    fun writeJson(`object`: Any, type: Type, fileName: String) {
        val file = File(context.cacheDir, fileName)
        var outputStream: OutputStream? = null
        val gson = GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create()
        try {
            outputStream = FileOutputStream(file)
            BufferedWriter(OutputStreamWriter(outputStream, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) StandardCharsets.UTF_8 else Charsets.UTF_8)).use { bufferedWriter ->
                gson.toJson(`object`, type, bufferedWriter)
            }
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "writeJson, FileNotFoundException e: $e")
        } catch (e: IOException) {
            Log.e(TAG, "writeJson, IOException e: $e")
        } finally {
            outputStream?.let {
                try {
                    it.flush()
                    it.close()
                } catch (e: IOException) {
                    Log.e(TAG, "writeJson, finally, e: $e")
                }
            }
        }
    }

    fun readJson(type: Type, fileName: String): Any? {
        var jsonData: Any? = null
        val file = File(context.cacheDir, fileName)
        var inputStream: InputStream? = null
        val gson = GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create()
        try {
            inputStream = FileInputStream(file)
            InputStreamReader(inputStream, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) StandardCharsets.UTF_8 else Charsets.UTF_8).use { streamReader ->
                jsonData = gson.fromJson(streamReader, type)
            }
        } catch (e: FileNotFoundException) {
            Log.e(TAG, "readJson, FileNotFoundException e: $e")
        } catch (e: IOException) {
            Log.e(TAG, "readJson, IOException e: $e")
        } finally {
            inputStream?.let {
                try {
                    it.close()
                } catch (e: IOException) {
                    Log.e(TAG, "readJson, finally, e: $e")
                }
            }
        }
        return jsonData
    }
}