package ski.mashiro.common

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.benmanes.caffeine.cache.Cache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.WebSocket
import ski.mashiro.entity.bilibili.Comet
import ski.mashiro.entity.config.NeteaseCloudMusicConfig
import ski.mashiro.entity.config.RoomConfig
import ski.mashiro.entity.config.SongRequestConfig
import ski.mashiro.entity.config.SystemConfig
import ski.mashiro.entity.music.Music
import ski.mashiro.entity.music.NeteaseCloudMusic
import ski.mashiro.util.ObservableAtomicReference
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * @author mashirot
 */
object GlobalBean {
    val JSON_MAPPER = JsonMapper().registerKotlinModule()
    val YAML_MAPPER = YAMLMapper().registerKotlinModule()
    val IO_SCOPE = CoroutineScope(Dispatchers.IO)
    val RESOURCES_FOLDER = File(System.getProperty("compose.application.resources.dir"))
    val CONFIG_FOLDER = File(RESOURCES_FOLDER, "config")
    lateinit var roomConfig: RoomConfig
    lateinit var neteaseCloudMusicConfig: NeteaseCloudMusicConfig
    lateinit var songRequestConfig: SongRequestConfig
    lateinit var systemConfig: SystemConfig
    lateinit var uidCache: Cache<Long, Long>
    lateinit var musicCache: Cache<Long, Music>
    lateinit var keywordBlackList: SnapshotStateList<String>
    val musicWaitingQueue = ConcurrentLinkedQueue<Pair<Music, Triple<String, Long, Boolean>>>()
    val musicList = mutableStateListOf<Pair<String, Music>>()
    var webSocket by mutableStateOf<WebSocket?>(null)
    var neteaseCloudMusicLoginStatus by mutableStateOf(false)
    var seasonMode by mutableStateOf(false)
    val seasonInProgress by lazy {
        ObservableAtomicReference(false) { atomicBoolean, newValue, invokeCallback ->
            val oldValue = atomicBoolean.get()
            if (oldValue == newValue) {
                return@ObservableAtomicReference
            }
            if (atomicBoolean.compareAndSet(oldValue, newValue)) {
                invokeCallback.invoke(newValue)
            }
        }
    }
}