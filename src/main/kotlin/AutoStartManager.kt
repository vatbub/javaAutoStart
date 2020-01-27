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

import com.sun.jna.platform.win32.Advapi32Util
import com.sun.jna.platform.win32.WinReg
import java.io.File

/**
 * Manages the auto start settings of an app.
 * IMPORTANT: The behavior of this class is undefined if not on Windows!
 * @param appName The name of the app. Must be unique. If not unique, the settings of the conflicting program will be overwritten.
 * @param jarLocation The location of the jar to add to the auto start. If not specified, the class will try to find the location automatically.
 */
class AutoStartManager @JvmOverloads constructor(private val appName: String, jarLocation: File? = null) {
    private val javawExecutable = File(System.getProperty("java.home"), "bin").toPath().resolve("javaw.exe").toFile()
    private val jarLocation = jarLocation ?: File(javaClass.protectionDomain.codeSource.location.toURI())
    private val keyParentPath = "Software\\Microsoft\\Windows\\CurrentVersion\\Run"

    /**
     * Checks whether the app managed by this instance is currently in the auto start of windows.
     */
    val isInAutoStart: Boolean
        get() = Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, keyParentPath, appName)


    /**
     * Adds the app managed by this instance to the auto start of windows.
     * If an app with the same name already exists, its entry will be overwritten.
     * Consequently, this means that the additionalArgs can be updated simply by calling this method again.
     *
     * When starting automatically, the app will be started using javaw, consequently having no standard in- and output.
     *
     * @param additionalArgs Additional args to be supplied to the app on auto start.
     */
    @JvmOverloads
    fun addToAutoStart(additionalArgs: String? = null) {
        var value = "\"$javawExecutable\" -jar \"$jarLocation\""
        if (additionalArgs != null)
            value = "$value $additionalArgs"

        if (!Advapi32Util.registryKeyExists(WinReg.HKEY_CURRENT_USER, keyParentPath)) {
            val createKeyResult = Advapi32Util.registryCreateKey(WinReg.HKEY_CURRENT_USER, keyParentPath)
            if (!createKeyResult)
                throw IllegalStateException("Unable to create the registry key for an unknown reason")
        }
        Advapi32Util.registrySetStringValue(WinReg.HKEY_CURRENT_USER, keyParentPath, appName, value)
    }

    /**
     * Removes the app managed by this instance from the auto start. No-op if [isInAutoStart] is `false`.
     */
    fun removeFromAutoStart() {
        if (isInAutoStart)
            Advapi32Util.registryDeleteValue(WinReg.HKEY_CURRENT_USER, keyParentPath, appName)
    }
}
