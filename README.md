# 网易云播放器

### 项目介绍

本项目基于 [网易云音乐开源 Api]( https://github.com/Binaryify/NeteaseCloudMusicApi )  进行开发，实现了登录后获取个人歌单列表展示、歌单详情展示，获取当前音乐播放列表，播放MP3等格式的音乐文件，并能够控制播放，暂停，歌曲切换，指定播放某一片段等功能。

### 项目结构

- common： 基础依赖库，包括网络请求，缓存等。

- music：音乐播放
  - auth：登录
  - music_player：音乐播放相关功能
  - personal_list：个人歌单
  - playlist：歌单详情

### 实现效果

* 登录 / 个人歌单列表

![NetEase0](D:\learning_materials\2s\MusicPlayer\NetEase0.png)

* 歌单歌曲列表/播放列表

  ![NetEase2](D:\learning_materials\2s\MusicPlayer\NetEase2.png)

* 音乐播放

  ![NetEase1](D:\learning_materials\2s\MusicPlayer\NetEase1.png)