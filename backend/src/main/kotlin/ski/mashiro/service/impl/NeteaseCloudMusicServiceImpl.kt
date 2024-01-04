package ski.mashiro.service.impl

import org.apache.commons.lang3.StringUtils
import ski.mashiro.common.GlobalBean.JSON_MAPPER
import ski.mashiro.common.GlobalBean.neteaseCloudMusicConfig
import ski.mashiro.entity.music.NeteaseCloudMusic
import ski.mashiro.exception.NeteaseCouldMusicException
import ski.mashiro.factory.OkHttpClientFactory
import ski.mashiro.factory.RequestBuilderFactory
import ski.mashiro.service.NeteaseCloudMusicService
import java.util.*

/**
 * @author mashirot
 */
object NeteaseCloudMusicServiceImpl : NeteaseCloudMusicService {

    override suspend fun login() {
        if (StringUtils.isNotBlank(neteaseCloudMusicConfig.cookie) && getLoginStatus()) {
            return
        }
        if (Objects.isNull(neteaseCloudMusicConfig.phoneNumber) ||
            (Objects.isNull(neteaseCloudMusicConfig.password) && Objects.isNull(neteaseCloudMusicConfig.passwordMd5))
        ) {
            return
        }
        val okHttpClient = OkHttpClientFactory.getOkHttpClient()
        val loginReq = RequestBuilderFactory.getReqBuilderWithUA()
            .url("${neteaseCloudMusicConfig.cloudMusicApiUrl}/login/cellphone?" +
                    "phone=${neteaseCloudMusicConfig.phoneNumber}&".also {
                        if (Objects.nonNull(neteaseCloudMusicConfig.passwordMd5)) {
                            it + "md5_password=${neteaseCloudMusicConfig.passwordMd5}"
                            return@also
                        }
                        it + "password=${neteaseCloudMusicConfig.password}"
                    }
            )
            .build()
        val json = okHttpClient.newCall(loginReq).execute().run {
            body!!.string()
        }
        val respResult = JSON_MAPPER.readValue(json, HashMap::class.java)
        if ((respResult["code"] as Int) != 200) {
            // TODO 提示登陆失败
            val msg = respResult["msg"]
            return
        }
        val cookie = "MUSIC_U=${respResult["token"]}"
        neteaseCloudMusicConfig.cookie = cookie
    }

    override suspend fun getMusicByKeyword(keyword: String): NeteaseCloudMusic {
        val okHttpClient = OkHttpClientFactory.getOkHttpClient()
        val searchReq = RequestBuilderFactory.getReqBuilderWithUA()
            .url("${neteaseCloudMusicConfig.cloudMusicApiUrl}/search?keywords=$keyword&limit=2")
            .build()
        val json = okHttpClient.newCall(searchReq).execute().run {
            body!!.string()
        }
        val respResult = JSON_MAPPER.readValue(json, HashMap::class.java)
        val result = respResult["result"] as HashMap<*, *>
        val musics = result["songs"] as List<*>
        if (musics.isEmpty()) {
            throw NeteaseCouldMusicException("未找到歌曲")
        }
        val music = musics[0] as HashMap<*, *>
        val artists = music["artists"] as List<*>
        var singer = ""
        val iter = artists.iterator()
        while (iter.hasNext()) {
            val artist = iter.next() as HashMap<*, *>
            singer += artist["name"].toString()
            if (iter.hasNext()) {
                singer += ", "
            }
        }
        return NeteaseCloudMusic(
            music["id"].toString().toLong(),
            music["name"] as String,
            singer,
            music["duration"].toString().toLong(),
            null
        )
    }

    override suspend fun getMusicById(music: NeteaseCloudMusic): NeteaseCloudMusic {
        if (!getSongStatusById(music.id)) {
            throw NeteaseCouldMusicException("歌曲无法播放")
        }
        val okHttpClient = OkHttpClientFactory.getOkHttpClient()
        val urlReq = RequestBuilderFactory.getReqBuilderWithNeteaseCloudMusicCookieAndUA()
            .url("${neteaseCloudMusicConfig.cloudMusicApiUrl}/song/url?id=${music.id}")
            .build()
        val json = okHttpClient.newCall(urlReq).execute().run {
            body!!.string()
        }
        val respResult = JSON_MAPPER.readValue(json, HashMap::class.java)
        val data = (respResult["data"] as List<*>)[0] as HashMap<*, *>
        music.url = data["url"] as String
        return music
    }

    override suspend fun getSongStatusById(songId: Long): Boolean {
        val okHttpClient = OkHttpClientFactory.getOkHttpClient()
        val checkReq = RequestBuilderFactory.getReqBuilderWithNeteaseCloudMusicCookieAndUA()
            .url("${neteaseCloudMusicConfig.cloudMusicApiUrl}/check/music?id=$songId")
            .build()
        val json = okHttpClient.newCall(checkReq).execute().run {
            body!!.string()
        }
        val respResult = JSON_MAPPER.readValue(json, HashMap::class.java)
        return respResult["success"] as Boolean
    }

    override suspend fun getLoginStatus(): Boolean {
        if (StringUtils.isBlank(neteaseCloudMusicConfig.cookie)) {
            return false
        }
        val okHttpClient = OkHttpClientFactory.getOkHttpClient()
        val urlReq = RequestBuilderFactory.getReqBuilderWithNeteaseCloudMusicCookieAndUA()
            .url("${neteaseCloudMusicConfig.cloudMusicApiUrl}/login/status")
            .build()
        val json = okHttpClient.newCall(urlReq).execute().run {
            body!!.string()
        }
        val respResult = JSON_MAPPER.readValue(json, HashMap::class.java)
        return respResult["account"] != null
    }

}