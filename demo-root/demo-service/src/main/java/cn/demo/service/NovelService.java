package cn.demo.service;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

public interface NovelService {

	InputStream productNovel(String novelUrl, HttpServletResponse response) throws IOException;

}
