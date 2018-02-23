package org.ostenant.mybatis.generator.plus.plugins;


import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.PrimitiveTypeWrapper;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Arrays;
import java.util.List;

public class UseGeneratedKeysColumnPlugin extends PluginAdapter {

    protected static final String USE_GENERATED_KEYS_PROPERTY = "useGeneratedKeys";
    protected static final String KEY_COLUMN_PROPERTY = "keyColumn";
    protected static final String KEY_PROPERTY_PROPERTY = "keyProperty";
    protected static final String USE_GENERATED_KEYS = "useGeneratedKeys";

    protected static final List<String> GENERATED_PROPERTY = Arrays.asList(KEY_COLUMN_PROPERTY, KEY_PROPERTY_PROPERTY, USE_GENERATED_KEYS);


    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public final boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return this.configUseGeneratedKey(element, introspectedTable);
    }

    @Override
    public final boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return this.configUseGeneratedKey(element, introspectedTable);
    }

    protected boolean configUseGeneratedKey(XmlElement element, IntrospectedTable introspectedTable) {
        // 直接从Table节点中尝试去获取useGeneratedKeys属性
        String useGeneratedKeys = introspectedTable.getTableConfigurationProperty(USE_GENERATED_KEYS);

        // 如果为true 生成参数
        if (StringUtility.isTrue(useGeneratedKeys)) {
            // 1.要使用useGeneratedKeys只能有一个主键 2.并且主键的类型必须是数字类型Integer或Long或Short
            List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
            // 主键列数为1时进行生成处理
            if (primaryKeyColumns.size() == 1) {
                //得到这个唯一的主键列
                IntrospectedColumn keyColumn = primaryKeyColumns.get(0);
                //得到这个列映射成Java模型之后的属性对应的Java类型
                FullyQualifiedJavaType javaType = keyColumn.getFullyQualifiedJavaType();
                if (javaType.equals(PrimitiveTypeWrapper.getIntegerInstance())
                        || javaType.equals(PrimitiveTypeWrapper
                        .getLongInstance())
                        || javaType.equals(PrimitiveTypeWrapper
                        .getShortInstance())) {

                    if (element.getAttributes().stream()
                            .distinct()
                            .filter(attribute ->
                                    GENERATED_PROPERTY.contains(attribute.getName()))
                            .count() <= 0) {
                        element.addAttribute(new Attribute(USE_GENERATED_KEYS_PROPERTY, "true"));
                        //通过IntrospectedColumn的getActualColumnName得到列中的名称 用于生成keyColumn属性
                        element.addAttribute(new Attribute(KEY_COLUMN_PROPERTY, keyColumn
                                .getActualColumnName()));
                        //通过IntrospectedColumn的getJavaProperty方法得到key在Java对象中的属性名 用于生成keyProperty属性
                        element.addAttribute(new Attribute(KEY_PROPERTY_PROPERTY, keyColumn
                                .getJavaProperty()));
                    }
                }
            }
        }

        return true;
    }


}
