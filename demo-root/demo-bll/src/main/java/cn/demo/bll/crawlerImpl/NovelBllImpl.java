package cn.demo.bll.crawlerImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.demo.bean.NovelPageInfo;
import cn.demo.bll.crawler.NovelBll;
import cn.demo.dao.crawler.NovelPageInfoMapper;
@Component
public class NovelBllImpl implements NovelBll{

	@Autowired
	private NovelPageInfoMapper novelPageInfoMapper;
	@Override
	public List<NovelPageInfo> getNovelPageListByUrl(String url) {
		
		return novelPageInfoMapper.getNovelPageListByUrl(url);
	}

}
