package org.ostenant.mybatis.generator.plus.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * 用于从json对象中取出指定键对应的属性
 *
 * @author wenchao
 * @version 1.0, 16/10/27
 */
public class JSONUtils {
    /**
     * 判断当前Json对象中指定Key对应的json属性是否存在
     *
     * @param dataJsonObj 当前jsonObject
     * @param key         指定json属性对应的key
     * @return 存在当前key返回true，否则返回false
     */
    public static Boolean isAttributeExists(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return false;
        }

        return dataJsonObj.containsKey(key);
    }

    /**
     * 从当前json对象中取出指定key对应的布尔属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 返回指定key对应的布尔属性，如果指定key不存在或者不是一个有效的bool值，返回null
     */
    public static Boolean getBoolean(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return null;
        }

        String strValue = getString(dataJsonObj, key);
        if (StringUtils.isEmpty(strValue)) {
            return null;
        }

        if (!REGUtils.REG_PATTERN_FOR_BOOLEAN.matcher(strValue).matches()) {
            return null;
        }

        return Boolean.valueOf(strValue);
    }

    /**
     * 从当前json对象中取出指定key对应的布尔属性
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 返回指定key对应的布尔属性，如果指定key不存在或者不是一个有效的bool值，返回defaultValue
     */
    public static Boolean getBoolean(JSONObject dataJsonObj, String key, Boolean defaultValue) {
        Boolean actualValue = getBoolean(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }

    /**
     * 从当前json对象中取出指定键对应的双精度浮点数属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 如果指定key不存在或者不是一个有效的数值属性，返回null
     */
    public static Double getDouble(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return null;
        }

        String strValue = getString(dataJsonObj, key);
        if (StringUtils.isEmpty(strValue)) {
            return null;
        }

        if (!REGUtils.REG_PATTERN_FOR_NUMBER.matcher(strValue).matches()) {
            return null;
        }

        return Double.valueOf(strValue.replaceAll(REGUtils.INVALID_FLOAT_CHAR_REG, ""));
    }

    /**
     * 从指定Json对象中取出指定Key对应的数值属性值
     *
     * @param dataJsonObj  指定Json对象
     * @param key          指定Key
     * @param defaultValue 默认值
     * @return 如果指定key对应的属性为有效的数值属性，那么返回此值，否则返回默认值defaultValue
     */
    public static Double getDouble(JSONObject dataJsonObj, String key, Double defaultValue) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }

    /**
     * 从当前json对象中取出指定键对应的金额属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 如果指定key不存在或者不是一个有效的金额属性，返回null
     */
    public static Double getAmount(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return null;
        }

        String strValue = getString(dataJsonObj, key);
        if (StringUtils.isEmpty(strValue)) {
            return null;
        }

        //替换掉非数值字符
        strValue = strValue.replaceAll(REGUtils.INVALID_FLOAT_CHAR_REG, "");

        if (!REGUtils.REG_PATTERN_FOR_NUMBER.matcher(strValue).matches()) {
            return null;
        }

        return Double.valueOf(strValue);
    }

    /**
     * 从指定Json对象中取出指定Key对应的金额属性值
     *
     * @param dataJsonObj  指定Json对象
     * @param key          指定Key
     * @param defaultValue 默认值
     * @return 如果指定key对应的属性为有效的金额属性，那么返回此值，否则返回默认值defaultValue
     */
    public static Double getAmount(JSONObject dataJsonObj, String key, Double defaultValue) {
        Double actualValue = getAmount(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }

    /**
     * 从当前对象中取出指定键对应的单精度浮点数属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 指定键对应的单精度浮点数属性，如果指定key不存在或者不是一个有效的数值属性，返回null
     */
    public static Float getFloat(JSONObject dataJsonObj, String key) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? null : actualValue.floatValue();
    }

    /**
     * 从当前对象中取出指定键对应的单精度浮点数属性
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定键对应的单精度浮点数属性，如果指定key不存在或者不是一个有效的数值属性，返回默认值
     */
    public static Float getFloat(JSONObject dataJsonObj, String key, Float defaultValue) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue.floatValue();
    }

    /**
     * 从当前对象中取出指定键对应的属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 指定键对应的属性，如果不存在或发生异常返回null
     */
    public static Object get(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty()) {
            return null;
        }

        if (!dataJsonObj.containsKey(key)) {
            return null;
        }

        return dataJsonObj.get(key);
    }

    /**
     * 从当前对象中取出指定键对应的属性
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定键对应的属性，如果不存在返回默认值
     */
    public static Object get(JSONObject dataJsonObj, String key, Object defaultValue) {
        Object actualValue = get(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }

    /**
     * 从当前对象中取出指定键对应的整数属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 指定键对应的整数属性，如果指定key对应的属性不存在或者指定属性不是一个有效的数值属性那么返回null
     */
    public static Integer getInteger(JSONObject dataJsonObj, String key) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? null : actualValue.intValue();
    }

    /**
     * 从当前对象中取出指定键对应的整数属性
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定键对应的整数属性，如果指定key对应的属性不存在或者指定属性不是一个有效的数值属性那么返回默认值
     */
    public static Integer getInteger(JSONObject dataJsonObj, String key, Integer defaultValue) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue.intValue();
    }

    /**
     * 从当前对象中取出指定键对应的长整型整数属性
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 指定键对应的整数属性，如果指定key对应的属性不存在或者不是一个有效的数值属性那么返回null
     */
    public static Long getLong(JSONObject dataJsonObj, String key) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? null : actualValue.longValue();
    }

    /**
     * 从当前对象中取出指定键对应的长整型整数属性
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定键对应的整数属性，如果指定key对应的属性不存在或者不是一个有效的数值属性那么返回默认值
     */
    public static Long getLong(JSONObject dataJsonObj, String key, Long defaultValue) {
        Double actualValue = getDouble(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue.longValue();
    }

    /**
     * 从当前对象中取出指定键对应的json数组
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 指定key对应的json数组，如果指定对应的属性不存在或者不是json数组返回null
     */
    public static JSONArray getJsonArray(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return null;
        }

        Object obj = get(dataJsonObj, key);
        if (obj == null || !(obj instanceof JSONArray)) {
            return null;
        }

        return (JSONArray) obj;
    }

    /**
     * 从当前对象中取出指定键对应的json数组
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定key对应的json数组，如果指定对应的属性不存在或者不是Json数组返回默认值
     */
    public static JSONArray getJsonArray(JSONObject dataJsonObj, String key, JSONArray defaultValue) {
        JSONArray actualValue = getJsonArray(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }

    /**
     * 从当前json对象中取出指定键对应的json对象
     *
     * @param dataJsonObj 当前json对象
     * @param key         指定key
     * @return 指定key对应的json对象属性，如果指定对应的属性不存在或者不是json对象返回null
     */
    public static JSONObject getJsonObject(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return null;
        }

        Object obj = get(dataJsonObj, key);
        if (obj == null || !(obj instanceof JSONObject)) {
            return null;
        }

        return (JSONObject) obj;
    }

    /**
     * 从当前json对象中取出指定键对应的json对象
     *
     * @param dataJsonObj  当前json对象
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定key对应的json对象属性，如果指定对应的属性不存在或者不是json对象返回默认值
     */
    public static JSONObject getJsonObject(JSONObject dataJsonObj, String key, JSONObject defaultValue) {
        JSONObject actualValue = getJsonObject(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }

    /**
     * 从当前json对象中取出指定键对应的字符串
     *
     * @param dataJsonObj 当前jsonObject
     * @param key         指定key
     * @return 指定键对应的字符串，如果不存在返回null
     */
    public static String getString(JSONObject dataJsonObj, String key) {
        if (dataJsonObj == null || dataJsonObj.isEmpty() || StringUtils.isBlank(key)) {
            return null;
        }

        if (!dataJsonObj.containsKey(key)) {
            return null;
        }

        Object obj = get(dataJsonObj, key);

        if (obj == null || obj instanceof JSON) {
            return null;
        }

        return String.valueOf(obj);
    }

    /**
     * 从当前json对象中取出指定键对应的字符串
     *
     * @param dataJsonObj  当前jsonObject
     * @param key          指定key
     * @param defaultValue 默认值
     * @return 指定键对应的字符串，如果不存在或者指定key对应的属性是一个Json对象或者是Json数组返回默认值
     */
    public static String getString(JSONObject dataJsonObj, String key, String defaultValue) {
        String actualValue = getString(dataJsonObj, key);

        return actualValue == null ? defaultValue : actualValue;
    }
}

