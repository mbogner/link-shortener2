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

import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class UrlRepository(
    private val redisOps: ReactiveRedisOperations<String, UrlVO>,

    @Value("\${app.redis.url.key.prefix}")
    private val keyPrefix: String,
) {

    fun save(
        key: String,
        value: UrlVO
    ): Mono<Boolean> {
        return redisOps.opsForValue().set(
            "${keyPrefix}${key}",
            value
        )
    }

    fun get(
        shortened: String
    ): Mono<UrlVO> {
        return redisOps.opsForValue().get("${keyPrefix}${shortened}")
    }

}