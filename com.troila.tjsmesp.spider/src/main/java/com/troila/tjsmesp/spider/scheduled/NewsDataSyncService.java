package com.troila.tjsmesp.spider.scheduled;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.troila.tjsmesp.spider.config.DataSyncSettings;
import com.troila.tjsmesp.spider.constant.SpiderModuleEnum;
import com.troila.tjsmesp.spider.service.NewsService;

@Service
public class NewsDataSyncService implements Runnable{
	
	private final static Logger logger = LoggerFactory.getLogger(NewsDataSyncService.class);
	
	@Autowired
	private NewsService newsService;	
	@Autowired
	private DataSyncSettings dataSyncSettings;
	
//	private static final String SYNC_NEWS_CRON_STR = "0 0/22 * * * ? ";
	
//	private static final String SYNC_NEWS_CRON_STR = "0 20 0-20/1 ? 1-12 1,2,3,4,5 ";
	
	private static final String SYNC_NEWS_CRON_STR = "0 0 9 ? 1-12 1,2,3,4,5 ";
	
	@Override
	public void run() {
		long start = System.currentTimeMillis();
		logger.info("数据同步任务现在开始，请稍候……");
		syncNewsDataLastNDay();
		logger.info("本次数据同步任务结束,用时{}ms",(System.currentTimeMillis()-start));
	}
	
	@Scheduled(cron = SYNC_NEWS_CRON_STR)
	public void syncNewsDataLastNDay() {
		try {
			if(dataSyncSettings.isNewsIsEnabled()) {
				logger.info("{}开始执行数据同步任务，……",new Date());  //数据查重问题
				// 同步要闻焦点(国家)
				newsService.newsDataSync(SpiderModuleEnum.POLICY_NEWS_FOCUS_GUOJIA, dataSyncSettings.getNewsLastDays());
				// 同步要闻焦点(部委)
				newsService.newsDataSync(SpiderModuleEnum.POLICY_NEWS_FOCUS_BUWEI, dataSyncSettings.getNewsLastDays());
				// 同步要闻焦点（天津）
				newsService.newsDataSync(SpiderModuleEnum.POLICY_NEWS_FOCUS_TIANJIN, dataSyncSettings.getNewsLastDays());			
				// 同步区域资讯
				newsService.newsDataSync(SpiderModuleEnum.POLICY_REGIONAL_DYNAMIC, dataSyncSettings.getNewsLastDays());
				// 同步产业资讯
				newsService.newsDataSync(SpiderModuleEnum.POLICY_INDUSTRY_INFO, dataSyncSettings.getNewsLastDays());
				// 同步静海窗口平台要闻焦点
				newsService.newsDataSync(SpiderModuleEnum.JINGHAI_INDUSTRIAL_CLUSTERS_NEWS, dataSyncSettings.getNewsLastDays());
				// 同步静海窗口平台通知公告
				newsService.newsDataSync(SpiderModuleEnum.JINGHAI_INDUSTRIAL_CLUSTERS_NOTICE, dataSyncSettings.getNewsLastDays());
				logger.info("{}数据同步任务结束，……",new Date());    //数据查重问题			
			}else {
				logger.info("新闻类同步信息已关闭，不进行同步");
			}
		} catch (Exception e) {
			logger.error("数据同步任务出现异常，异常信息如下：{}",e);
		}		
	}
}
