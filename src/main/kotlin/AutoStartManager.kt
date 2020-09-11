/*-
 * #%L
 * Java Auto Start
 * %%
 * Copyright (C) 2016 - 2020 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.github.vatbub.javaautostart

import com.github.vatbub.javaautostart.Interpreter.java
import com.github.vatbub.javaautostart.Interpreter.javaw
import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinReg
import org.apache.commons.lang3.SystemUtils
import java.io.File

enum class Interpreter {
    java, javaw
}

data class AutoStartLaunchConfig @JvmOverloads constructor(val jarFileLocation: File = File(AutoStartManager::class.java.protectionDomain.codeSource.location.toURI()),
                                                           val interpreter: Interpreter = javaw,
                                                           val jvmOptions: String? = null,
                                                           val additionalArgs: String? = null) {

    val asCommand: String by lazy {
        val jreBin = File(System.getProperty("java.home"), "bin")
        val interpreterLocation = when (interpreter) {
            java -> jreBin.toPath().resolve("java.exe").toFile()
            javaw -> jreBin.toPath().resolve("javaw.exe").toFile()
        }
        val commandBuilder = StringBuilder("\"$interpreterLocation\"")

        if (jvmOptions != null)
            commandBuilder.append(" $jvmOptions")

        commandBuilder.append(" -jar ")
                .append("\"$jarFileLocation\"")

        if (additionalArgs != null)
            commandBuilder.append(" $additionalArgs")

        commandBuilder.toString()
    }
}

/**
 * Manages the auto start settings of an app.
 *
 * @param appName The name of the app. Must be unique. If not unique, the settings of the conflicting program will be overwritten.
 * @throws IllegalStateException If the os is not windows
 */
class AutoStartManager(private val appName: String) {
    private val keyParentPath = "Software\\Microsoft\\Windows\\CurrentVersion\\Run"

    /**
     * Checks whether the app managed by this instance is currently in the auto start of windows.
     */
    val isInAutoStart: Boolean
        get() {
            verifyOs()
            return Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, keyParentPath, appName)
        }


    /**
     * Adds the app managed by this instance to the auto start of windows.
     * If an app with the same name already exists, its entry will be overwritten.
     * Consequently, this means that the [autoStartLaunchConfig] can be updated simply by calling this method again.
     *
     * @param autoStartLaunchConfig The configuration to be used to launch the app
     */
    @JvmOverloads
    fun addToAutoStart(autoStartLaunchConfig: AutoStartLaunchConfig = AutoStartLaunchConfig()) {
        verifyOs()

        if (!Advapi32Util.registryKeyExists(WinReg.HKEY_CURRENT_USER, keyParentPath)) {
            val createKeyResult = Advapi32Util.registryCreateKey(WinReg.HKEY_CURRENT_USER, keyParentPath)
            if (!createKeyResult)
                throw IllegalStateException("Unable to create the registry key for an unknown reason")
        }
        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, keyParentPath, appName, autoStartLaunchConfig.asCommand)
    }

    /**
     * Removes the app managed by this instance from the auto start. No-op if [isInAutoStart] is `false`.
     */
    fun removeFromAutoStart() {
        verifyOs()
        if (isInAutoStart)
            Advapi32Util.registryDeleteValue(WinReg.HKEY_CURRENT_USER, keyParentPath, appName)
    }

    private fun verifyOs() {
        if (!SystemUtils.IS_OS_WINDOWS)
            throw IllegalStateException("Only Windows is supported")
    }
}
