package news;

import java.util.List;

/**
 * Created by kzf on 2017/9/5.
 */

interface NewsFilter {
    boolean filter(News news) ; // return true if the news fits the condition.
}

public class ContentFilter implements NewsFilter {
}
public class TitleFilter implements NewsFilter {

}
public class ComplexFilter implements NewsFilter {
    List<NewsFilter> filters ;
}

