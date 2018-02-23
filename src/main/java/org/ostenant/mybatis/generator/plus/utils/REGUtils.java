package org.ostenant.mybatis.generator.plus.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class REGUtils {
    public static final Pattern REG_PATTERN_FOR_PHONE_NUMBER = Pattern.compile("^1([34578])\\d{9}$");
    public static final Pattern REG_PATTERN_FOR_NUMBER = Pattern.compile("^(-?\\d+)(\\.\\d*)?(" +
            "世纪|年|月|周|日|时|点|分|秒|毫秒|微秒|纳秒|" +
            "美元|元|万美元|美元" +
            "十|百|千|万|十万|百万|千万|亿|十亿|百亿|千亿|" +
            "次|位|人|人次|" +
            "串|事|册|丘|乘|下|丈|丝|两|" +
            "举|具|美|包|厘|刀|分|列|则|" +
            "剂|副|些|匝|队|陌|陔|部|出|" +
            "个|介|令|份|伙|件|任|倍|儋|" +
            "卖|亩|记|双|发|叠|节|茎|莛|" +
            "荮|落|蓬|蔸|巡|过|进|通|造|" +
            "遍|道|遭|对|尊|头|套|弓|引|" +
            "张|弯|开|庄|床|座|庹|帖|帧|" +
            "席|常|幅|幢|口|句|号|台|只|" +
            "吊|合|名|吨|和|味|响|骑|门|" +
            "间|阕|宗|客|家|彪|层|尾|届|" +
            "声|扎|打|扣|把|抛|批|抔|抱|" +
            "拨|担|拉|抬|拃|挂|挑|挺|捆|" +
            "掬|排|捧|掐|搭|提|握|摊|摞|" +
            "撇|撮|汪|泓|泡|注|浔|派|湾|" +
            "溜|滩|滴|级|纸|线|组|绞|统|" +
            "绺|综|缕|缗|场|块|坛|垛|堵|" +
            "堆|堂|塔|墩|回|团|围|圈|孔|" +
            "贴|点|煎|熟|车|轮|转|载|辆|" +
            "料|卷|截|户|房|所|扇|炉|炷|" +
            "觉|斤|笔|本|朵|杆|束|条|杯|" +
            "枚|枝|柄|栋|架|根|桄|梃|样|" +
            "株|桩|梭|桶|棵|榀|槽|犋|爿|" +
            "片|版|歇|手|拳|段|沓|班|文|" +
            "曲|替|股|肩|脬|腔|支|步|武|" +
            "瓣|秒|秩|钟|钱|铢|锊|铺|锤|" +
            "锭|锱|章|盆|盏|盘|眉|眼|石|" +
            "码|砣|碗|磴|票|罗|畈|番|窝|" +
            "联|缶|耦|粒|索|累|緉|般|艘|" +
            "竿|筥|筒|筹|管|篇|箱|簇|角|" +
            "重|身|躯|酲|起|趟|面|首|项|" +
            "领|顶|颗|顷|袭|群|袋)?$");
    public static final Pattern REG_PATTERN_FOR_BOOLEAN = Pattern.compile("(true|false)+");

    public static final Pattern REG_PATTERN_FOR_DATE = Pattern.compile(
            "((^((1[8-9]\\d{2})|([2-9]\\d{3}))([-/\\._])(10|12|0?[13578])([-/\\._])(3[01]|[12][0-9]|0?[1-9]).*$)" +
                    "|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-/\\._])(11|0?[469])([-/\\._])(30|[12][0-9]|0?[1-9]).*$)" +
                    "|(^((1[8-9]\\d{2})|([2-9]\\d{3}))([-/\\._])(0?2)([-/\\._])(2[0-8]|1[0-9]|0?[1-9]).*$)" +
                    "|(^([2468][048]00)([-/\\._])(0?2)([-/\\._])(29).*$)" +
                    "|(^([3579][26]00)([-/\\._])(0?2)([-/\\._])(29).*$)" +
                    "|(^([1][89][0][48])([-/\\._])(0?2)([-/\\._])(29).*$)" +
                    "|(^([2-9][0-9][0][48])([-/\\._])(0?2)([-/\\._])(29).*$)" +
                    "|(^([1][89][2468][048])([-/\\._])(0?2)([-/\\._])(29).*$)" +
                    "|(^([2-9][0-9][2468][048])([-/\\._])(0?2)([-/\\._])(29).*$)" +
                    "|(^([1][89][13579][26])([-/\\._])(0?2)([-/\\._])(29).*$)" +
                    "|(^([2-9][0-9][13579][26])([-/\\._])(0?2)([-/\\._])(29).*$))");

    public static final String INVALID_FLOAT_CHAR_REG = "[^(1|2|3|4|5|6|7|8|9|0|\\.|\\-|\\+)]*";
    public static final String INVALID_INTEGER_CHAR_REG = "[^(1|2|3|4|5|6|7|8|9|0|\\-|\\+)]*";

    public static final Pattern TIME_STR_PATTERN = Pattern.compile("((\\d{1,2})(-|时|小时|:))?((\\d{1,2})(-|分|分钟|:))?((\\d{1,2})秒?)");

    /**
     * 计算指定模式的字符串在指定字符串中出现的次数
     *
     * @param checkStr 指定待检验字符串
     * @param pattern  指定字符串模式
     * @return 指定模式的字符串在指定字符串中出现的次数
     */
    public static int getMatchedCount(String checkStr, String pattern) {
        if (StringUtils.isEmpty(checkStr) || StringUtils.isEmpty(pattern)) {
            return 0;
        }

        Matcher matcher = Pattern.compile(pattern).matcher(checkStr);

        int matchedGroupCount = 0;
        while (matcher.find()) {
            matchedGroupCount++;
        }

        return matchedGroupCount;
    }

    /**
     * 计算指定模式字符串在指定字符串中存在匹配的最长子字符串的长度
     *
     * @param checkStr 指定待检验字符串
     * @param pattern  指定字符串模式
     * @return 指定模式字符串在指定字符串中存在匹配的最长子字符串的长度
     */
    public static int getMaxMatchedLength(String checkStr, String pattern) {
        if (StringUtils.isEmpty(checkStr) || StringUtils.isEmpty(pattern)) {
            return 0;
        }

        Matcher matcher = Pattern.compile(pattern).matcher(checkStr);

        int maxMatchedLength = 0;
        String matchedStr;
        while (matcher.find()) {
            matchedStr = matcher.group(1);

            if (maxMatchedLength < matchedStr.length()) {
                maxMatchedLength = matchedStr.length();
            }
        }

        return maxMatchedLength;
    }

    /**
     * 获取字符串中出现的最大数值
     *
     * @param checkStr 待检验的字符串
     * @param pattern  用于过滤单个数字字符的正则表达式
     * @return 字符串中出现的最大数值
     */
    public static int getMaxMatchedValue(String checkStr, String pattern) {
        if (StringUtils.isEmpty(checkStr) || StringUtils.isEmpty(pattern)) {
            return 0;
        }

        Matcher matcher = Pattern.compile(pattern).matcher(checkStr);

        int maxMatchedValue = 0;
        String matchedStr;
        while (matcher.find()) {
            matchedStr = matcher.group(1);

            if (maxMatchedValue < Integer.valueOf(matchedStr)) {
                maxMatchedValue = Integer.valueOf(matchedStr);
            }
        }

        return maxMatchedValue;
    }
}
