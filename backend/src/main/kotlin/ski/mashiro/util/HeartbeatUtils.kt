package ski.mashiro.util

import okio.ByteString
import org.apache.commons.lang3.StringUtils
import ski.mashiro.common.GlobalBean.JSON_MAPPER
import ski.mashiro.common.GlobalBean.roomConfig
import ski.mashiro.const.DataHeaderConsts
import ski.mashiro.entity.bilibili.AuthorizeBody
import ski.mashiro.entity.bilibili.DataHeader

/**
 * @author mashirot
 */
object HeartbeatUtils {

    fun generateAuthorizeBag(): ByteString {
        val authorizeBody =
            if (StringUtils.isNotBlank(roomConfig.cookie)) {
                AuthorizeBody(
                    roomConfig.uid,
                    roomConfig.roomId,
                    roomConfig.buvId,
                    roomConfig.key
                )
            } else {
                AuthorizeBody(roomConfig.roomId)
            }
        val body = JSON_MAPPER.writeValueAsBytes(authorizeBody)
        val totalLength = 16 + body.size
        val header = DataHeader(totalLength, DataHeaderConsts.HEARTBEAT_PROTOCOL, DataHeaderConsts.CLIENT_AUTHORIZE)
        return ReqUtils.getByteString(header, body)
    }

    fun generateHeartbeatBag(): ByteString {
        val header = DataHeader(
            DataHeaderConsts.HEADER_LENGTH_INT,
            DataHeaderConsts.HEARTBEAT_PROTOCOL,
            DataHeaderConsts.CLIENT_HEARTBEAT
        )
        return ReqUtils.getByteString(header, byteArrayOf())
    }

}