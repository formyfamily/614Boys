package controller;

import news.* ;
/**
 * Created by kzf on 2017/9/7.
 */

public class NewsReader {
    private NewsReader() {} ;
    private static NewsReader newsReader = new NewsReader() ;

    NewsReader getInstance() {return newsReader ;}
    void readNews(News news)
    {

    }

}
