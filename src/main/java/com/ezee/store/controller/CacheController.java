package com.ezee.store.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.jcache.JCacheCache;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ezee.store.exception.ResponseException;

@Controller
@RequestMapping("/cache")
public class CacheController {

	@Autowired
	private CacheManager cacheManager;

	@GetMapping("/redis")
	public ResponseEntity<?> cache(@RequestParam String cacheName, @RequestParam String id) {
		ResponseEntity<?> responseEntity = null;
		Cache cache = cacheManager.getCache(cacheName);
		if (cache != null) {
			ValueWrapper valueWrapper = cache.get(id);

			if (valueWrapper != null) {
				responseEntity = ResponseException.success(valueWrapper.get());
			} else {
				responseEntity = ResponseException.failure("Cache miss in this Id: " + id);
			}
		} else {
			responseEntity = ResponseException.failure("Cache not found.");
		}

//		System.out.println(Objects.requireNonNull(cache.getNativeCache()).toString());
		return responseEntity;
	}

	@GetMapping("/eh")
	public ResponseEntity<?> ehCache(@RequestParam String cacheName, @RequestParam(required = false) String id) {
		ResponseEntity<?> responseEntity = ResponseException.failure("Cache entry not found for id: " + id);

		Cache springCache = cacheManager.getCache(cacheName);
		if (springCache == null) {
			responseEntity = ResponseException.failure("Cache not found.");
		}

		javax.cache.Cache<Object, Object> jcache = (javax.cache.Cache<Object, Object>) ((JCacheCache) springCache)
				.getNativeCache();

		if (id != null && !id.isEmpty()) {
			for (javax.cache.Cache.Entry<Object, Object> entry : jcache) {
				if (entry.getKey().toString().equals(id)) {
					responseEntity = ResponseException
							.success(Collections.singletonMap(entry.getKey(), entry.getValue()));
				}
			}
		} else {
			Map<Object, Object> entries = new HashMap<>();
			for (javax.cache.Cache.Entry<Object, Object> entry : jcache) {
				entries.put(entry.getKey(), entry.getValue());
			}
			if (!entries.isEmpty()) {
				responseEntity = ResponseException.success(entries);
			} else {
				responseEntity = ResponseException.failure("Cache is empty.");
			}
		}

		return responseEntity;
	}
}
