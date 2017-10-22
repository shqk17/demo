package cn.demo.bll.crawler;

import java.util.List;

import cn.demo.bean.NovelPageInfo;

public interface NovelBll {

	List<NovelPageInfo> getNovelPageListByUrl(String url);

}
