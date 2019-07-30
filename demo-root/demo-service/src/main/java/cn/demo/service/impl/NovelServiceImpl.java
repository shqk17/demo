package cn.demo.service.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.demo.bean.NovelPageInfo;
import cn.demo.bll.crawler.NovelBll;
import cn.demo.service.NovelService;
import yhjp.bll.common.ChineseToNumber;

@Component
public class NovelServiceImpl implements NovelService {
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(NovelServiceImpl.class);
	private static Pattern titleNum = Pattern.compile("第(.+?)章");
	private static Pattern contentPa = Pattern.compile("下一章((.)+?)上一章");

	private static Pattern novel = Pattern.compile(">(.+?)<br/");
	@Autowired
	private NovelBll novelBll;

	@Override
	public InputStream productNovel(String url, HttpServletResponse response) throws IOException {
		final File htmlFile = File.createTempFile("temp", ".txt");// 创建临时文件
		logger.info("临时文件所在的本地路径：" + htmlFile.getCanonicalPath());
		htmlFile.deleteOnExit();
		FileWriter fileWriter = new FileWriter(htmlFile);
		// 流的方式
		BufferedWriter bufferWriter = new BufferedWriter(fileWriter);

		List<NovelPageInfo> novelList = novelBll.getNovelPageListByUrl(url);
		HashMap<Integer, NovelPageInfo> novelOrderMap = new HashMap<>();
		HashMap<Integer,String> title = new HashMap<>();
		for (NovelPageInfo page : novelList) {
			Matcher titlePatM = titleNum.matcher(page.getTitle());
			if (titlePatM.find()) {
				Integer intTitle =ChineseToNumber.ChineseToNum(titlePatM.group(1));
				novelOrderMap.put(intTitle, page);
				title.put(intTitle,page.getTitle());
			}
		}
		StringBuilder novrlString = new StringBuilder();
		for (int i = 0; i < novelOrderMap.size() - 1; i++) {
			if (novelOrderMap.get(i + 1) == null) {
				continue;
			}
			System.out.println("------现在是第----" + i);
			Document doc = Jsoup.parse(novelOrderMap.get(i + 1).getHtml());
			Element content = doc.getElementById("content");
			if(content == null) {
				content = doc.body();
			}
			//继续解析格式；
			bufferWriter.write(title.get(i + 1) + "\r\n");
			novrlString.append(content.text().split("下一章")[1].split("上一章")[0]);

			novrlString.append(novelOrderMap.get(i + 1).getTitle()).append("\r\n");
			
			bufferWriter.write(novrlString + "\r\n");
			if (novrlString != null) {
				novrlString.delete(0, novrlString.length());
			}
		}
		bufferWriter.close();
		response.addHeader("Content-Disposition", "attachment;filename=" + url + ".txt");
		return new FileInputStream(htmlFile);
	}

}
