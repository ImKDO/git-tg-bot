package boysband.updateprocerssor.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class LastValueCache {
    private val logger = LoggerFactory.getLogger(LastValueCache::class.java)
    private val cache = ConcurrentHashMap<Long, String>()
    
    fun put(chatId: Long, lastValue: String?) {
        if (lastValue != null) {
            cache[chatId] = lastValue
            logger.debug("Cached lastValue for chatId=$chatId: $lastValue")
        }
    }
    
    fun get(chatId: Long): String? {
        return cache[chatId]
    }
    
    fun remove(chatId: Long) {
        cache.remove(chatId)
    }
    
    fun size(): Int = cache.size
}
