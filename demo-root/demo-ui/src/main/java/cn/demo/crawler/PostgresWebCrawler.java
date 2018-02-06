package cn.demo.crawler;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;

import cn.demo.service.PostgresDBService;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import yhjp.bll.common.Tool;

public class PostgresWebCrawler extends WebCrawler {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(PostgresWebCrawler.class);

	private static Pattern FILE_ENDING_EXCLUSION_PATTERN = Pattern.compile(
			".*(\\.(" + "css|js" + "|bmp|gif|jpe?g|JPE?G|png|tiff?|ico|nef|raw" + "|mid|mp2|mp3|mp4|wav|wma|flv|mpe?g"
					+ "|avi|mov|mpeg|ram|m4v|wmv|rm|smil" + "|pdf|doc|docx|pub|xls|xlsx|vsd|ppt|pptx" + "|swf"
					+ "|zip|rar|gz|bz2|7z|bin" + "|xml|txt|java|c|cpp|exe" + "))$");

	private static  String[] Num = {"0","1","2","3","4","5","6","7","8","9"};
	private static  String[] BigNum= {"一","二","三","四","五","六",
			"七","八","九","十","百","千","零"};
	
	private static Pattern URL = Pattern.compile("http://www.biquge.com.tw/19_19315/");
	
	private final PostgresDBService postgresDBService;

	public PostgresWebCrawler(PostgresDBService postgresDBService) {
		this.postgresDBService = postgresDBService;
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		if(!URL.matcher(href).find()) {
        	logger.info("不是我要的链接！！！！！！！！！！！");
        	return false;
        }
		return !FILE_ENDING_EXCLUSION_PATTERN.matcher(href).matches();
	}

	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		logger.info("URL: " + url);
        
		
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			String title = htmlParseData.getTitle();
			/*Pattern titlePat = Pattern.compile("(.+?)章");
			Matcher titlePatM = titlePat.matcher(title);
			if(!titlePatM.find()) {
				logger.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!not a novel page: " + title);
			}
			String titleStr =titlePatM.group();
			StringBuffer titleNum =new StringBuffer();
			for(int i=0;i<titleStr.toCharArray().length;i++) {
				for(String bigNum : BigNum) {
					if(String.valueOf(titleStr.toCharArray()[i]).equals(bigNum)) {
						titleNum.append(bigNum);
					}
				}
				for(String num : Num) {
					if(String.valueOf(titleStr.toCharArray()[i]).equals(num)) {
						titleNum.append(num);
					}
				}
			}*/
			/*int titleNumInteger =ChineseToNumber(titleNum.toString());*/
			
			Set<WebURL> links = htmlParseData.getOutgoingUrls();

			logger.info("Text length: " + text.length());
			logger.info("Html length: " + html.length());
			logger.info("title : " + html);
			logger.info("title length: " + html.length());
			
			logger.info("Number of outgoing links: " + links.size());

			try {
				postgresDBService.store(page,title);
			} catch (RuntimeException e) {
				logger.error("Storing failed", e);
				onBeforeExit();
			}
		}
	}

	public void onBeforeExit() {
		if (postgresDBService != null) {
			postgresDBService.close();
		}
	}
}
