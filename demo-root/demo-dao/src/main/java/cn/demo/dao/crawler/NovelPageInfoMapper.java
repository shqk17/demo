package cn.demo.dao.crawler;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.demo.bean.NovelPageInfo;

public interface NovelPageInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NovelPageInfo record);

    int insertSelective(NovelPageInfo record);

    NovelPageInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NovelPageInfo record);

    int updateByPrimaryKeyWithBLOBs(NovelPageInfo record);

    int updateByPrimaryKey(NovelPageInfo record);

	List<NovelPageInfo> getNovelPageListByUrl(@Param("url")String url);
}