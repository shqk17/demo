package cn.demo.ui.crawler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.demo.crawler.PostgresCrawlerFactory;
import cn.demo.crawler.form.CrawlerConditionForm;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

@Controller
@RequestMapping("/crawler")
public class CrawlerWebController {
	private  static final Logger logger = LoggerFactory.getLogger(CrawlerWebController.class);
	
	@Value("${crawl.storage.folder}")
	private String crawlStorageFolder;

	@Value("${crawl.databases.url}")
	private String DataBasesUrl;
	
	
	@RequestMapping("getHtml")
	@ResponseBody
	public void login(CrawlerConditionForm crawlerConditionForm, HttpServletRequest request,
			HttpServletResponse response) {
		CrawlConfig config = new CrawlConfig();
		// 等待时间 毫秒
		config.setPolitenessDelay(crawlerConditionForm.getWaitTime());
		config.setCrawlStorageFolder(crawlStorageFolder);
		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		try {
			CrawlController	controller = new CrawlController(config, pageFetcher, robotstxtServer);
			/*
			 * For each crawl, you need to add some seed urls. These are the first URLs that
			 * are fetched and then the crawler starts following links which are found in
			 * these pages 种子路径
			 */
			String[] seedGroup = crawlerConditionForm.getSeedUrl().split(";");
			if(seedGroup==null) {
				//todo
			}
			for(String seed : seedGroup) {
				controller.addSeed(seed);
			}
			/*
			 * Start the crawl. This is a blocking operation, meaning that your code will
			 * reach the line after this only when crawling is finished.
			 */
			 long startTime =System.currentTimeMillis();
		        controller.start(new PostgresCrawlerFactory(DataBasesUrl,"root",""), crawlerConditionForm.getCrawlersNumber());
		        controller.isFinished();
		        controller.shutdown();
		        long endTime = System.currentTimeMillis();
		        System.out.println("用时 " + (endTime - startTime) / 1000 + "秒...");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
