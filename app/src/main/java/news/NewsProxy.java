package news;

import java.util.* ;

/**
 * Created by kzf on 2017/9/5.
 */

public class NewsProxy {

    private NewsFilter newsfilter ;
    private int patchSize ;

    void setFilter(NewsFilter filter) {newsfilter = filter ;}
    /* List<News> getNewsList() ; // 参数自己定，内部使用即可
    News readNews(News) ;
    News shareNews(News) ;
    List<News> updateNews() ;
    List<News> moreNews() ;*/
}
