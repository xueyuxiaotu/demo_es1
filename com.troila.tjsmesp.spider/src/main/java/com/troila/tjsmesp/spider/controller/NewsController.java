package com.troila.tjsmesp.spider.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.troila.tjsmesp.spider.constant.SpiderModuleEnum;
import com.troila.tjsmesp.spider.model.primary.NewsSpider;
import com.troila.tjsmesp.spider.model.secondary.BmsPlatformPublishInfo;
import com.troila.tjsmesp.spider.repository.mysql.NewsSpiderRepositoryMysql;
import com.troila.tjsmesp.spider.service.NewsService;
import com.troila.tjsmesp.spider.util.TimeUtils;

@RestController
public class NewsController {
	
	@Autowired
	private NewsService newsService; 
	@Autowired
	private NewsSpiderRepositoryMysql newsSpiderRepositoryMysql;
			
	@GetMapping("/news/getLastNDay")
	public Date  getLastNDay(@RequestParam int lastNDays) {
		Date lastDay = TimeUtils.getLastNDay(lastNDays);
		System.out.println(lastDay);
		return lastDay;
	}
	
	@GetMapping("/news/dataSync")
	public List<BmsPlatformPublishInfo> newsDataSync(@RequestParam int spiderModule, int lastNDays){
		return newsService.newsDataSync(SpiderModuleEnum.getSpiderModuleEnum(spiderModule), lastNDays);
	}
	
	@GetMapping("/news/dataUpdate")
	public int newsDataUpdate(@RequestParam int spiderModule) {
		List<NewsSpider> list = newsService.dataUpdate(SpiderModuleEnum.getSpiderModuleEnum(spiderModule));
		return list.size();
	}
	
	@GetMapping("/news/crawledUrls")
	public int CrawledUrls(@RequestParam int spiderModule){
		List<String> list = newsSpiderRepositoryMysql.findCrawledUrlsBySpiderModule(spiderModule);	
		System.out.println(list.size());
		return list.size();
	}
	
	@GetMapping("/news/assign")
	public List<NewsSpider> getAssignDay(@RequestParam int spiderModule, int lastNDays,int number) {
		Date lastDay = TimeUtils.getLastNDay(lastNDays);
		List<NewsSpider> list = newsSpiderRepositoryMysql.findByPublishDateGreaterThanEqualAndSpiderModuleOrderByPublishDateDesc(lastDay, spiderModule, number);
		return list;
	}

}
