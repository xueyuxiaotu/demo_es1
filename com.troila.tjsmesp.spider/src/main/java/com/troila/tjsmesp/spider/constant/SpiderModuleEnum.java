package com.troila.tjsmesp.spider.constant;

/**
 * 爬取信息对应模块枚举类
 * @author xuanguojing
 *
 */
public enum SpiderModuleEnum {
	
	/**
	 * 对应政策一点通的最新政策
	 */
	POLICY_NEWEST("最新政策", 0 ,"spiderNewest"), 
	/**
	 * 对应政策一点通的政策解读
	 */
	POLICY_READING("政策解读",2,"spiderReading"),	
	/**
	 * 对应产业资讯 中小企业信息网-》产业频道-》行业热点
	 */
	POLICY_INDUSTRY_INFO("产业资讯",3,"spiderIndustryInfo"),	
	/**
	 * 区域动态	中小企业信息网-》政务频道-》地方政府
	 */ 
	POLICY_REGIONAL_DYNAMIC("区域动态",4,"spiderRegionalDynamic"),
	/**
	 * 要闻焦点-》国家  中小企业信息网-》新闻资讯-》焦点新闻
	 */
	POLICY_NEWS_FOCUS_GUOJIA("要闻焦点国家",5,"spiderNewsFocusGuojia"),
	/**
	 * 要问焦点-》部委  中小企业信息网-》政务频道-》部委动态
	 */
	POLICY_NEWS_FOCUS_BUWEI("要闻焦点部委",6,"spiderNewsFocusBuwei"),
	/**
	 * 要闻焦点-》天津，天津政务网-》新闻-》各区动态，
	 */
	POLICY_NEWS_FOCUS_TIANJIN("要闻焦点天津",7,"spiderNewsFocusTianjin"),
	/**
	 * 静海产业集聚的子牙循环经济网园区新闻
	 */
	JINGHAI_INDUSTRIAL_CLUSTERS_NEWS("静海产业集聚园区新闻",8,"spiderJinghaiIndustrialClustersNews"),
	/**
	 * 静海产业集聚的子牙循环经济网园区公告	
	 */
	JINGHAI_INDUSTRIAL_CLUSTERS_NOTICE("静海产业集聚园区公告",9,"spiderJinghaiIndustrialClustersNotice");
	
	
	
	private String name;  
    private int index;  
    private String key;
   
    private SpiderModuleEnum(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }
        
    private SpiderModuleEnum(String name, int index, String key) {
		this(name,index);
		this.key = key;
	}

	public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public int getIndex() {  
        return index;  
    }  
    public void setIndex(int index) {  
        this.index = index;  
    }

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	//通过索引获取key值  
    public static String getKey(int index) {  
        for (SpiderModuleEnum s : SpiderModuleEnum.values()) {  
            if (s.getIndex() == index) {  
                return s.key;  
            }  
        }  
        return null;    //说明该枚举类中不存在
    }  
    
    public static SpiderModuleEnum getSpiderModuleEnum(int index) {  
        for (SpiderModuleEnum s : SpiderModuleEnum.values()) {  
            if (s.getIndex() == index) {  
                return s;  
            }  
        }  
        return null;    //说明该枚举类中不存在
    }  
}
