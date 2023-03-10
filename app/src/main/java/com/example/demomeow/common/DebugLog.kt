package com.example.demomeow.common

import android.util.Log
import com.example.demomeow.di.App
import org.koin.android.BuildConfig
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.DateFormat
import java.util.*

class DebugLog {

    private fun isDebuggable(): Boolean {
        return BuildConfig.DEBUG
    }

    private val logFolder by lazy {
        FileUtils.folder(App.context.filesDir, LOG_FOLDER)
    }

    /**
     * Get call class name / Method name
     * @return
     */

    private fun getMethodNameString(): String {
        //get information about caller [4]
        val element = Thread.currentThread().stackTrace[POSITION_STACK_TRACE]

        //get class name
        val fullClassName = element.className
        val simpleClassName = fullClassName.substring(fullClassName.lastIndexOf(".") + 1)
        //get method name
        val methodName = element.methodName
        //get the number of rows
        val lineNumber = element.lineNumber

        //Method name string generation
        return "$simpleClassName#$methodName:$lineNumber"
    }

    /**
     * Add message text to new line in the log file
     *
     * @param text
     */
    private fun appendLogFile(text: Any) {
        val asthmaLog1 = File(App.context.filesDir, LOG_FILE_NAME + "_1.log")

        // if asthmaLog1 is existed
        if (asthmaLog1.exists()) {
            // if asthmaLog1 reached 1MB
            if (asthmaLog1.length() >= MAXIMUM_LOG_FILE_SIZE) {
                // prepare log files
                prepareLogFiles()
            }
        } else {
            try {
                asthmaLog1.createNewFile()
            } catch (e: IOException) {
                Log.d(TAG, e.message ?: "")
            }
        }
        try {
            BufferedWriter(FileWriter(asthmaLog1, true)).run {
                append(DateFormat.getDateInstance().format(Date()))
                append(": ")
                append(text.toString())
                newLine()
                close()
            }

        } catch (e: IOException) {
            Log.d(TAG, e.message ?: "")
        }
    }

    /**
     * Logcat output and file output of specified LEVEL or higher
     * @param logType = LogLevel
     * @param fullLog = log Message
     */

    private fun logOut(logType: Int, fullLog: String) {
        //True if LOGGER_ENTRY_MAX_LEN is exceeded (= output remaining log)
        var retryFlag = false
        //Output Log less than LOGGER_ENTRY_MAX_LEN
        var log = fullLog
        val length = fullLog.length
        if (length >= LOGGER_ENTRY_MAX_LEN) {
            retryFlag = true
            log = fullLog.substring(0, LOGGER_ENTRY_MAX_LEN)
        }

        //Logcat output
        if (isDebuggable()) {
            when (logType) {
                Log.ASSERT -> Log.wtf(TAG, log)
                Log.ERROR -> Log.wtf(TAG, log)
                Log.WARN -> Log.wtf(TAG, log)
                Log.INFO -> Log.wtf(TAG, log)
                Log.DEBUG -> Log.wtf(TAG, log)
                Log.VERBOSE -> Log.wtf(TAG, log)
                else -> {
                    Log.d(TAG, log)
                }
            }
        }

        //LogFile output
        if (logType >= LOG_OUT_LEVEL) appendLogFile(log)

        // If LOGGER_ENTRY_MAX_LEN is exceeded, the rest will be output again.
        if (retryFlag) logOut(logType, fullLog.substring(LOGGER_ENTRY_MAX_LEN))
    }

    /**
     * Show error level message
     *
     * @param message
     */
    fun e(message: String) {
        logOut(Log.ERROR, "[ERROR] " + getMethodNameString() + " " + message)
    }

    /**
     * Show info level message
     *
     * @param message
     */
    fun i(message: String) {
        logOut(Log.INFO, "[INFO] " + getMethodNameString() + " " + message)
    }

    /**
     * Show debug level message in Logcat
     *
     * @param message
     */
    fun d(message: String) {
        logOut(Log.DEBUG, "[DEBUG] " + getMethodNameString() + " " + message)
    }

    /**
     * Show verbose level message
     *
     * @param message
     */
    fun v(message: String) {
        logOut(Log.VERBOSE, "[VERBOSE] " + getMethodNameString() + " " + message)
    }

    /**
     * Show warning level message log in Logcat
     *
     * @param message
     */
    fun w(message: String) {
        logOut(Log.WARN, "[WARN] " + getMethodNameString() + " " + message)
    }

    /**
     * Create new file
     */
    private fun prepareLogFiles() {
        val logfiles = arrayOfNulls<File>(LOG_FILE_MAX_COUNT)
        //create name file
        for (fIndex in 0 until LOG_FILE_MAX_COUNT) {
            logfiles[fIndex] =
                File(logFolder, LOG_FILE_NAME + "_" + (fIndex + 1).toString() + ".log")
        }
        // Delete the oldest file if the maximum number of files has been reached
        if (logfiles[LOG_FILE_MAX_COUNT - 1] != null && logfiles[LOG_FILE_MAX_COUNT - 1]!!.exists()) {
            logfiles[LOG_FILE_MAX_COUNT - 1]!!.delete()
        }
        // Increment file name
        for (fIndex in LOG_FILE_MAX_COUNT - 1 downTo 1) {
            if (logfiles[fIndex - 1] != null && logfiles[fIndex - 1]!!.exists()) {
                logfiles[fIndex - 1]?.renameTo(File(logfiles[fIndex]?.absolutePath ?: ""))
            }
        }
        // create new asthmaLog1
        try {
            logfiles[0]?.createNewFile()
        } catch (e: IOException) {
            Log.d(TAG, e.message ?: "")
        }
    }

    companion object {
        //Log tag
        private const val TAG = "DEBUG"

        //Log level. Default is INFO
        private const val LOG_OUT_LEVEL: Int = Log.INFO

        //Maximum number of output of Log file
        private const val LOG_FILE_MAX_COUNT = 3

        //File name log
        private const val LOG_FILE_NAME = "log"
        private const val LOG_FOLDER = "logs"

        /**
         * Length for split output
         * (4 * 1024)
         * */
        private const val LOGGER_ENTRY_MAX_LEN = 3000

        //Maximum size of 1 file
        private const val MAXIMUM_LOG_FILE_SIZE = 1048576
        private const val POSITION_STACK_TRACE = 4
    }

}
