package org.ostenant.mybatis.generator.plus.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.ostenant.mybatis.generator.plus.utils.CamelNameUtils;

import java.util.Iterator;
import java.util.List;

public class ToJsonPlugin extends PluginAdapter {
    private FullyQualifiedJavaType jsonObject = new FullyQualifiedJavaType("com.alibaba.fastjson.JSONObject");
    private FullyQualifiedJavaType jsonAttrGetter = new FullyQualifiedJavaType("org.ostenant.springboot.learning.examples.mybatis.utils.JSONUtils");

    public ToJsonPlugin() {
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.generateToJson(introspectedTable, introspectedTable.getAllColumns(), topLevelClass);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.generateToJson(introspectedTable, introspectedTable.getAllColumns(), topLevelClass);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.generateToJson(introspectedTable, introspectedTable.getAllColumns(), topLevelClass);
        return true;
    }

    private void generateToJson(IntrospectedTable introspectedTable, List<IntrospectedColumn> introspectedColumns, TopLevelClass topLevelClass) {
        String shortName = topLevelClass.getType().getShortName();

        String outerClassShortName = topLevelClass.getType().getShortName();
        String innerKeyClassShortName = outerClassShortName + "Key";

        topLevelClass.addImportedType(jsonObject);
        topLevelClass.addImportedType(jsonAttrGetter);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(jsonObject);
        method.setName("toJson");

        method.addBodyLine("JSONObject toJsonObj = new JSONObject();");

        StringBuilder sb = new StringBuilder();
        sb.append(shortName.substring(0, 1).toLowerCase()).append(shortName.substring(1));
        sb.setLength(0);


        for (Iterator iterator = introspectedColumns.iterator(); iterator.hasNext(); method.addBodyLine(sb.toString())) {
            IntrospectedColumn introspectedColumn = (IntrospectedColumn) iterator.next();
            sb.setLength(0);

            String javaProperty = introspectedColumn.getJavaProperty();
            String javaPropertyKey = CamelNameUtils.toUnderlineName(javaProperty);
            sb.append("toJsonObj").append(".")
                    .append("put(")
                    .append(innerKeyClassShortName)
                    .append(".")
                    .append(javaPropertyKey)
                    .append(", ")
                    .append(javaProperty)
                    .append(");");
        }

        sb.setLength(0);
        method.addBodyLine("return toJsonObj;");
        topLevelClass.addMethod(method);

    }

}
