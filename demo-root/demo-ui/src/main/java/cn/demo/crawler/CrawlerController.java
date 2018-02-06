package cn.demo.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class CrawlerController {

    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "D:/crawlDate";
        int numberOfCrawlers = 10;

        CrawlConfig config = new CrawlConfig();
        //等待时间 毫秒
        config.setPolitenessDelay(100);

        config.setCrawlStorageFolder(crawlStorageFolder);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages  种子路径
         */
        controller.addSeed("http://www.biquge.com.tw/19_19315/");
        /*controller.addSeed("http://www.23us.com/html/43/43197");
        controller.addSeed("https://pt.wikipedia.org/wiki/JDBC");
        controller.addSeed("https://pt.wikipedia.org/wiki/Protocolo");
        controller.addSeed("https://de.wikipedia.org/wiki/Datenbank");*/

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */

        long startTime =System.currentTimeMillis();
        controller.start(new PostgresCrawlerFactory("jdbc:mysql://127.0.0.1:3306/novel","root",""), numberOfCrawlers);
        long endTime = System.currentTimeMillis();
        System.out.println("用时 " + (endTime - startTime) / 1000 + "秒...");
    }

}
