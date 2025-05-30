package com.ezee.store.config;

import java.time.Duration;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class CacheConfig {
	@Bean
	public RedisCacheConfiguration redisCacheConfiguration() {
		return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(2))
				.serializeKeysWith(
						RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new GenericJackson2JsonRedisSerializer()))
				.disableCachingNullValues();
	}

	@Bean
	public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
		return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration()).build();
	}

	@Bean
	public CacheManager ehCacheManager() {
		CacheConfiguration<Object, Object> cacheConfig = CacheConfigurationBuilder
				.newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(100))
				.withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofMinutes(2))).build();

		CachingProvider cachingProvider = Caching.getCachingProvider();
		javax.cache.CacheManager cacheManager = cachingProvider.getCacheManager();

		cacheManager.createCache("taxEhCache", Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfig));
		cacheManager.createCache("discountEhCache", Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfig));
		cacheManager.createCache("localCustomers", Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfig));
		cacheManager.createCache("localCategory", Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfig));
		cacheManager.createCache("localWeight", Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfig));
		cacheManager.createCache("localProduct", Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfig));
		cacheManager.createCache("localVendor", Eh107Configuration.fromEhcacheCacheConfiguration(cacheConfig));

		return new JCacheCacheManager(cacheManager);
	}

	@Primary
	@Bean
	public CacheManager compositeCacheManager(CacheManager ehCacheManager, CacheManager redisCacheManager) {
		CompositeCacheManager cacheManager = new CompositeCacheManager(ehCacheManager, redisCacheManager);
		cacheManager.setFallbackToNoOpCache(false);
		return cacheManager;
	}
}

//.withExpiry(ExpiryPolicyBuilder.timeToIdleExpiration(Duration.ofMinutes(1)))
// ResourcePoolsBuilder.newResourcePoolsBuilder().heap(5000,
// EntryUnit.ENTRIES).offheap(50, MemoryUnit.MB).disk(200, MemoryUnit.MB, true)
// .withExpiry(ExpiryPolicyBuilder.expiry().access(Duration.ofSeconds(45)).create(Duration.ofSeconds(45)).build())
