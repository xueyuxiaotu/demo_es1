package com.troila.tjsmesp.spider.crawler.site;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.troila.tjsmesp.spider.constant.CrawlConst;
import com.troila.tjsmesp.spider.constant.FromSiteEnum;
import com.troila.tjsmesp.spider.crawler.processor.base.PageSettings;
import com.troila.tjsmesp.spider.model.primary.NewsSpider;
import com.troila.tjsmesp.spider.util.MD5Util;
import com.troila.tjsmesp.spider.util.TimeUtils;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.selector.Selectors;
/**
 * 
 * @ClassName:  TjGovCn   
 * @Description:天津政务网的爬取时的页面逻辑处理
 * @author: xgj
 * @date:   2019年6月19日 下午3:26:06   
 *
 */
public class TjGovCnProcessor implements SpiderProcess{
	private static final Logger logger = LoggerFactory.getLogger(TjGovCnProcessor.class);
	
	private List<String> pastCrawledUrls;		
	/**
	 * 
	 * @Description 天津政务网-》处理列表页信息
	 * @param page
	 * @param spiderModuleEnum
	 */
	@Override
	public void listProcess(Page page, PageSettings pageSettings) {
		List<String> list =  page.getHtml().xpath("//div[@class='left leftlist']").links().all();		
		//	将当前列表页所有的最新政策文章详情页加入到后续的url地址，有待继续爬取
		List<String> articleList = list.stream().filter(p->p.matches(pageSettings.getArticleUrlRegex())).collect(Collectors.toList());
		if(null != articleList && articleList.size() > 0) {
			// 过滤掉以前已经爬取过的记录，不再重复爬取
			pastCrawledUrls = pageSettings.getProcessorService().getCrawledUrls(pageSettings.getModule().getIndex());	
			if(pastCrawledUrls != null  && pastCrawledUrls.size()>0) {
				articleList = articleList.stream().filter(p->!pastCrawledUrls.contains(p)).collect(Collectors.toList());
			}		
			page.addTargetRequests(articleList);			
		}
		//将下一页的列表页链接加入到后续的url地址，有待继续爬取
		List<String> urlList = list.stream().filter(p->p.matches(pageSettings.getListUrlRegex())).collect(Collectors.toList());
		if(null != urlList && urlList.size() > 0){
			page.addTargetRequests(urlList);  			
		}
		
		// 将不用的集合置空，避免产生大量的内存垃圾
		list = null;
		articleList = null;
		pastCrawledUrls = null;		
		urlList = null;
		System.gc();
	}
	
	/**
	 * 
	 * @Description 天津政务网-》处理详细页信息  
	 * @param page
	 * @param spiderModuleEnum
	 */
	@Override
	public void detailProcess(Page page, PageSettings pageSettings) {
		NewsSpider spider = new NewsSpider();
		String selectedDiv = page.getHtml().xpath("//div[@class='left leftlist']").toString();		
		if(StringUtils.isEmpty(selectedDiv)) {
			logger.info("文章链接页{}，未找到指定的页面内容，跳过该页面",page.getUrl());
			page.setSkip(true);
			return;
		}
		spider.setTitle(Selectors.xpath("//div[@class='title']/text()").select(selectedDiv));
		List<Element> list1 = Selectors.xpath("//div[@class='time xwlc pd']/span").selectElements(selectedDiv);
		String[] sourceStrArray =  Selectors.xpath("//span/text()").select(list1.get(0).toString()).split("来源：");
		// 注意判断一下，避免有的记录没有指定该字段，那么数据项为空，出现异常
		if(sourceStrArray.length > 1) {
			spider.setSource(sourceStrArray[1]);				
		}
		String[] dateStrArray = Selectors.xpath("//span/text()").select(list1.get(1).toString()).split("发布时间：");
		if(dateStrArray.length > 1) {				
			spider.setPublishDate(TimeUtils.getFormatDate(new SimpleDateFormat("yyyy-MM-dd HH:mm"), dateStrArray[1]));
		}
		String content = Selectors.xpath("//div[@class='concon']").select(selectedDiv);
		spider.setContent(content);
		spider.setPublishUrl(page.getUrl().toString());
		spider.setFromSite(FromSiteEnum.TIANJINZHENGWUWANG.getName());
		spider.setFromLink(FromSiteEnum.TIANJINZHENGWUWANG.getLink());
		spider.setSpiderCode(MD5Util.getMD5(spider.getPublishUrl()));   //根据特定的内容生成MD5，作为该条记录的id
		spider.setSpiderModule(pageSettings.getModule().getIndex());
		page.putField(CrawlConst.CRAWL_ITEM_KEY, spider);	
	}
}
