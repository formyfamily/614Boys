# 614Boys
Android App Development


## 1.架构
  该系统采用三级架构来实现前后端的数据处理。
* 第一层结构为news，该部分负责从api获取新闻，对新闻进行处理，以及和文件系统的交互。（包括存储等）。这一部分的文件保存在java/news中。
* 第二层结构为activity，顾名思义就是用于处理页面中的操作（包括onCreate以及各种event）的核心模块。这一部分的文件保存在java/activity中。
* 第三层结构即为前端代码。这一部分的文件保存在res中。

## 2.news的接口说明
  为了方便前后端的交流，这里先规定三种接口和类：
* News类，用来保存一条新闻。
* Newsproxy类，用来处理所有已经获得了的新闻，相当于一个文件系统。
* Newsfilter接口，用来对新闻进行筛选。
  
  News类的接口如下：
  
  | 成员名称 | 类型 | 用途 |
  | -------- | -------- | -------- |
  | Id| String | 该条新闻的Id |
  | title | String | 该条新闻的标题 |
  | content | String | 该条新闻的文字内容 |
  | postImage | 待定 | 封面图片 |
  | ........ | ........ | ........ |
  
  | 方法名称 | 用途 |
  | -------- | -------- |
  | void updateFullContent() | 获取该条新闻的所有信息 |
  | String getTitle() | 返回该新闻的标题 |
  | ........ | ........ |
  
  注意News的资料不一定是时刻完整的，因此在调用上述的过程中记得判断。
  
  NewsProxy的接口如下：
  
  | 成员名称 | 类型 | 用途 |
  | -------- | -------- | -------- |
  | patchSize | int | 每次应当获得的新闻数量 |
  
  | 方法名称 | 用途 |
  | -------- | -------- |
  | void setFilter(newsFilter filter) | 为该接口制定一个筛选方式 |
  | News readNews(String newsId) | 前端点开了一条新闻，此时调用这个函数。后端应当将这条新闻的所有内容读取进来，保存在本地储存内，然后返回该条新闻。|
  | News shareNews(String newsId) | 前端对一条新闻进行了分享，此时调用这个函数。该函数效果和readNews类似，但是具体之后可能会要完成一些更多的工作。|
  | List<News> updateNews() | 前端上拉获取了最新的新闻，此时调用这个函数。该函数应当将已有的list和刚刚获取的新闻进行连接然后返回。|
  | List<News> moreNews() | 前端通过下拉要求获得更多的新闻，此时调用这个函数。效果和updateNews类似。注意这个函数一次获取的新闻量为patchSize。 |
  
  NewsFilter的接口如下：
  
  | 方法名称 | 用途 |
  | -------- | -------- |
  | boolean filter(News news) | 判断一条新闻是否满足该Filter中的条件。 |
  
