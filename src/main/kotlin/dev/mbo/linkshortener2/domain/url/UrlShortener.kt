/*
 * Copyright (c) 2023.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.mbo.linkshortener2.domain.url

import java.net.URL

internal object UrlShortener {

    private const val RADIX = 36

    fun shorten(
        url: URL,
        time: Long = System.nanoTime()
    ): String {
        val checksum = url.toString().sumOf { it.code }.toLong() + time
        return checksum.toString().reversed().toLong().toString(RADIX)
    }

}