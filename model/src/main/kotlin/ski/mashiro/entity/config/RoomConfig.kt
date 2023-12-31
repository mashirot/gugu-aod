package ski.mashiro.entity.config

import com.fasterxml.jackson.annotation.JsonIgnore

/**
 * @author mashirot
 */
data class RoomConfig(
    var roomId: Long,
    @JsonIgnore
    var anchormanUID: Long?,
    var uid: Long,
    val buvId: String = "850221AA-6657-6657-6657-91043281097552522infoc",
    @JsonIgnore
    var key: String?,
    var cookie: String,
    val ua: String = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
) {
    constructor(
        roomId: Long,
        uid: Long,
        cookie: String
    ) : this(
        roomId,
        null,
        uid,
        "850221AA-6657-6657-6657-91043281097552522infoc",
        "",
        cookie,
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
    )
}