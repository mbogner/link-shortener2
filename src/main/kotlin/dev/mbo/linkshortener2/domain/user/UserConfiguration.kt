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

package dev.mbo.linkshortener2.domain.user

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

@Configuration
internal class UserConfiguration(
    private val objectMapper: ObjectMapper
) {

    @Bean
    fun redisUserOperations(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, User> {
        val serializer = Jackson2JsonRedisSerializer(
            objectMapper,
            User::class.java
        )
        val builder = RedisSerializationContext.newSerializationContext<String, User>(StringRedisSerializer())
        val context = builder.value(serializer).build()
        return ReactiveRedisTemplate(
            factory,
            context
        )
    }

}