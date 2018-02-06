package cn.demo.crawler.form;

public class CrawlerConditionForm {
	
	private Integer crawlersNumber;
	
	private String seedUrl ;

	private Integer waitTime;

	public Integer getCrawlersNumber() {
		return crawlersNumber;
	}

	public void setCrawlersNumber(Integer crawlersNumber) {
		this.crawlersNumber = crawlersNumber;
	}

	public String getSeedUrl() {
		return seedUrl;
	}

	public void setSeedUrl(String seedUrl) {
		this.seedUrl = seedUrl;
	}

	public Integer getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(Integer waitTime) {
		this.waitTime = waitTime;
	}
	
}
