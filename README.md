# GuGu - Audio on demand

## 简介

一款Bilibili弹幕点歌机，灵感来源于Bilibili直播间[850221](https://live.bilibili.com/850221)，是一个能带给人<del>永生</del>快乐的直播间

是一个compose for desktop练手作品，并非安卓开发，代码实现可能丑陋，请见谅，欢迎issue, pr

网易云部分依赖项目：[NeteaseCloudMusicApi](https://github.com/Binaryify/NeteaseCloudMusicApi)

音频播放部分依赖项目：[vlcj](https://github.com/caprica/vlcj)

项目采用GPLv3协议开源，文字部分采用CC BY-SA 4.0协议许可使用，icon版权归属：[露蒂丝](https://space.bilibili.com/52522)

## UI

![Home](readmeImg/Home.png)
![ManualReq](readmeImg/ManualSongReq.png)
![FuncSetting](readmeImg/FuncSetting.png)
![Setting](readmeImg/Setting.png)

## 使用

1. 本程序依赖于VLC播放音频，VLC 是一款免费的开源跨平台多媒体播放器，请到[官网](https://www.videolan.org/vlc/)下载安装
2. 从release下载.zip或是.exe版本，.zip为免安装版本，推荐此版本，如需在Mac, Linux上运行，请自行编译
   <br/>
   请确保程序运行目录有写入权限
3. 运行 `gugu-aod.exe`，进入左侧设置界面，填写相关信息，请注意保存
4. 点击左侧菜单栏，连接，如状态变为已连接，则开始监听直播间弹幕

## 配置说明

> - ☆ 必填
> - ▲ 重启生效
> - ^ 不稳定
>
> 更改后务必保存

### 功能设置

> 赛季模式暂时无法使用

- 是否启用赛季模式^: 赛季模式指待播放歌曲>=单赛季歌曲数量后暂时禁止点歌，直到待播放歌曲为空
- 赛季重置是否重置歌曲冷却: -
- 单赛季歌曲数量: -
- 日志等级▲: 默认info，有问题可改成debug，查看日志并issues反馈
- 黑名单: 歌名关键词黑名单，匹配则不进入待播放列表

### 房间设置

- 房间号☆：直播间房间号，可以填写短号
- UID：小号(建议)的uid
- Cookie：登录状态的cookie

> 说明：
> - UID和Cookie用于解决B站23/11更新的风控导致弹幕用户名不可见问题
> - 获取Cookie的操作如下：
>   1. 登录B站网页版
>   2. 随便点进一个直播间
>   3. 点开网络的Filter，选择`Fetch/XHR`
>   4. 刷新页面
>   5. 随便点一个请求，找到请求头里的`Cookie`一栏
>   6. 全部复制到Cookie中
>   7. 保存

### 点歌设置

- 弹幕前缀☆：弹幕: `点歌 给自己的歌`，请填写 `点歌`
- 歌单大小☆：主页歌单的最大长度，>=此值时不再添加新歌
- 勋章名称：粉丝牌名称，为空不限制
- 勋章等级：如填写了粉丝勋章，则此项必填，否则无效
- 用户冷却☆ ▲：用户点歌冷却，填写规则见下
- 歌曲冷却☆ ▲：歌曲播放冷却，填写规则见下

> 冷却时间接受如下两种格式：
> 1. ISO-8601 如: P1DT2H3M4.058S
> 2. 字符串 如: 10s, 15m, 1h, 1h30m

### 网易云设置

- 手机号：网易云登陆用
- 密码/密码MD5：网易云登陆用，密码MD5优先级>密码优先级，会优先使用MD5，只需提供一项即可
- Cookie：网易云Cookie，用于播放会员歌曲
- ApiUrl☆：网易云ApiUrl，请自行搭建，见此项目：[NeteaseCloudMusicApi](https://github.com/Binaryify/NeteaseCloudMusicApi)

> 下面操作的前提是配置ApiUrl<br/>
> 说明:
> - 手机号和密码并非必须项，仅用于获取Cookie，在登陆成功后可以删除，或手动获取Cookie
> - 手动获取Cookie步骤：
>   1. 登录[网易云音乐](https://music.163.com/)
>   2. 进入你的个人主页(都行)，打开开发者控制台(F12)，切换到网络(Network)
>   3. 点开网络的Filter，选择`Fetch/XHR`
>   4. 刷新页面
>   5. 随便点一个请求，找到请求头里的`Cookie`一栏
>   6. 复制开头为`MUSIC_U`的部分，实例：`MUSIC_U=123abc....`
>   7. 粘贴到Cookie中，点击保存
>   8. 等待`登陆状态`变为`已登录`，如未变，则操作有误/Cookie失效

## 已知问题

- <del>无法播放直链后缀为.flac的音频，猜测是三方库问题</del> 已解决
- 由于[NeteaseCloudMusicApi](https://github.com/Binaryify/NeteaseCloudMusicApi)于24/1/22删库，请寻找三方fork仓库进行搭建