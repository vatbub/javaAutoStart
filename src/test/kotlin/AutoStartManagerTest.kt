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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.File

class AutoStartManagerTest {
    private val appName = "autoStartManagerTestApp"
    private val manager = AutoStartManager(appName)
    private val currentJarLocation = File(javaClass.protectionDomain.codeSource.location.toURI()).parentFile.toPath().resolve("classes").toFile()
    private val javawExecutable = File(System.getProperty("java.home"), "bin").toPath().resolve("javaw.exe").toFile()
    private val keyParentPath = "Software\\Microsoft\\Windows\\CurrentVersion\\Run"

    private val currentRegistryValue: String
        get() = Advapi32Util.registryGetStringValue(
                WinReg.HKEY_CURRENT_USER, keyParentPath, appName)

    @AfterEach
    fun cleanUp() {
        manager.removeFromAutoStart()
    }

    @Test
    fun addAndRemoveTest() {
        Assertions.assertFalse(manager.isInAutoStart)
        manager.addToAutoStart()
        Assertions.assertTrue(manager.isInAutoStart)
        manager.removeFromAutoStart()
        Assertions.assertFalse(manager.isInAutoStart)
    }

    @Test
    fun testRegistryValue() {
        Assertions.assertFalse(manager.isInAutoStart)
        manager.addToAutoStart()
        Assertions.assertEquals("\"$javawExecutable\" -jar \"$currentJarLocation\"", currentRegistryValue)
    }

    @Test
    fun testRegistryValueWithAdditionalArgs() {
        val additionalArgs = "--noGui"
        Assertions.assertFalse(manager.isInAutoStart)
        manager.addToAutoStart(additionalArgs)
        Assertions.assertEquals("\"$javawExecutable\" -jar \"$currentJarLocation\" $additionalArgs", currentRegistryValue)
    }

    @Test
    fun removeWithoutAdding() {
        Assertions.assertFalse(manager.isInAutoStart)
        manager.removeFromAutoStart()
    }
}
