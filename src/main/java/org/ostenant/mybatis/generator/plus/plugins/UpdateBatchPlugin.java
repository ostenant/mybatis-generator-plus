package org.ostenant.mybatis.generator.plus.plugins;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.ostenant.mybatis.generator.plus.constant.MapperXmlKey;
import org.ostenant.mybatis.generator.plus.constant.StatementIdValue;

import java.util.List;
import java.util.stream.Collectors;

public class UpdateBatchPlugin extends UseGeneratedKeysColumnPlugin {

    private FullyQualifiedJavaType listJavaType = new FullyQualifiedJavaType(StatementIdValue.JAVA_UTIL_LIST);


    @Override
    public boolean validate(List<String> warings) {
        return true;
    }

    /**
     * Mapper.xml文档DOM生成树，可以把自己的Statement挂在DOM树上。
     * 添加updateBatch的SQL Statement
     *
     * @param document          SQLMapper.xml 文档树描述对象
     * @param introspectedTable 表描述对象
     * @return 是否生成
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement rootElement = document.getRootElement();
        // <update></update>
        XmlElement statement = new XmlElement(MapperXmlKey.ELEMENT_UPDATE);
        // id="updateBatch"
        statement.getAttributes().add(0, new Attribute(MapperXmlKey.ATTRIBUTE_ID, StatementIdValue.STATEMENT_UPDATE_BATCH));
        // parameterType="java.util.List"
        statement.getAttributes().add(new Attribute(MapperXmlKey.ATTRIBUTE_PARAMETER_TYPE, StatementIdValue.JAVA_UTIL_LIST));

        TextElement update = new TextElement("update " + introspectedTable.getTableConfiguration().getTableName());

        XmlElement trimSet = new XmlElement(MapperXmlKey.ELEMENT_TRIM);
        trimSet.addAttribute(new Attribute(MapperXmlKey.ATTRIBUTE_PREFIX, "set"));
        trimSet.addAttribute(new Attribute(MapperXmlKey.ATTRIBUTE_SUFFIX_OVERRIDES, ","));

        String secondaryLevelIdEqualsResult = getEqualsResult(introspectedTable.getPrimaryKeyColumns().get(0), false);

        List<IntrospectedColumn> nonPrimaryKeyColumns = introspectedTable.getNonPrimaryKeyColumns();
        nonPrimaryKeyColumns.stream().map(column -> {
            XmlElement trim = new XmlElement(MapperXmlKey.ELEMENT_TRIM);

            trim.addAttribute(new Attribute(MapperXmlKey.ATTRIBUTE_PREFIX, column.getActualColumnName() + " = case"));
            trim.addAttribute(new Attribute(MapperXmlKey.ATTRIBUTE_SUFFIX, "end,"));

            XmlElement innerForeach = new XmlElement(MapperXmlKey.ELEMENT_FOREACH);
            innerForeach.getAttributes().add(0, new Attribute(MapperXmlKey.ATTRIBUTE_COLLECTION, "list"));
            innerForeach.getAttributes().add(1, new Attribute(MapperXmlKey.ATTRIBUTE_ITEM, "item"));
            innerForeach.getAttributes().add(2, new Attribute(MapperXmlKey.ATTRIBUTE_INDEX, "index"));

            XmlElement ifElement = new XmlElement(MapperXmlKey.ELEMENT_IF);
            ifElement.addAttribute(new Attribute(MapperXmlKey.ATTRIBUTE_TEST, "item." + column.getJavaProperty() + " != null"));

            String builder = "when " +
                    secondaryLevelIdEqualsResult +
                    " then " +
                    getEqualsResult(column, false);

            TextElement contentElement = new TextElement(builder);
            ifElement.addElement(contentElement);

            innerForeach.addElement(0, ifElement);
            trim.addElement(0, innerForeach);

            return trim;
        }).forEachOrdered(trimSet::addElement);

        TextElement where = new TextElement("where " + introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName() + " in");

        XmlElement outerForeach = new XmlElement(MapperXmlKey.ELEMENT_FOREACH);
        outerForeach.getAttributes().add(0, new Attribute(MapperXmlKey.ATTRIBUTE_COLLECTION, "list"));
        outerForeach.getAttributes().add(1, new Attribute(MapperXmlKey.ATTRIBUTE_ITEM, "item"));
        outerForeach.getAttributes().add(1, new Attribute(MapperXmlKey.ATTRIBUTE_OPEN, "("));
        outerForeach.getAttributes().add(2, new Attribute(MapperXmlKey.ATTRIBUTE_SEPARATOR, ", "));
        outerForeach.getAttributes().add(1, new Attribute(MapperXmlKey.ATTRIBUTE_CLOSE, ")"));
        outerForeach.addElement(new TextElement(secondaryLevelIdEqualsResult));

        statement.addElement(update);
        statement.addElement(trimSet);
        statement.addElement(where);
        statement.addElement(outerForeach);

        rootElement.addElement(statement);

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    /**
     * Mapper.java接口生成树，可以把自己的方法挂接在此接口上
     * int updateBatch(List<EntityType> list);
     *
     * @param interfaze         Mapper接口信息描述对象
     * @param topLevelClass     此数据库表对应的实体类描述对象
     * @param introspectedTable 表描述对象
     * @return 是否生成
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        FullyQualifiedJavaType entityJavaType = interfaze.getMethods()
                .stream()
                .filter(m -> m.getName().equals(StatementIdValue.STATEMENT_SELECT_BY_PRIMARY_KEY) ||
                        m.getName().equals(StatementIdValue.STATEMENT_FIND_BY_ID))
                .distinct()
                .map(Method::getReturnType)
                .collect(Collectors.toList())
                .get(0);

        FullyQualifiedJavaType primaryKeyJavaType = interfaze.getMethods()
                .stream()
                .filter(m -> m.getName().equals(StatementIdValue.STATEMENT_SELECT_BY_PRIMARY_KEY) ||
                        m.getName().equals(StatementIdValue.STATEMENT_FIND_BY_ID))
                .distinct()
                .map(Method::getParameters)
                .map(parameters -> parameters.get(0).getType())
                .collect(Collectors.toList())
                .get(0);


        interfaze.addImportedType(listJavaType);
        Method method = new Method();
        method.setName(StatementIdValue.STATEMENT_UPDATE_BATCH);

        FullyQualifiedJavaType listEntityJavaType = new FullyQualifiedJavaType(listJavaType.getShortName());
        listEntityJavaType.addTypeArgument(entityJavaType);

        switch (primaryKeyJavaType.getFullyQualifiedName()) {
            case StatementIdValue.JAVA_LANG_SHORT:
                method.setReturnType(new FullyQualifiedJavaType("short"));
                break;

            case StatementIdValue.JAVA_LANG_INTEGER:
                method.setReturnType(new FullyQualifiedJavaType("int"));
                break;

            case StatementIdValue.JAVA_LANG_LONG:
                method.setReturnType(new FullyQualifiedJavaType("long"));
                break;

            default:
                method.setReturnType(primaryKeyJavaType);
                break;
        }

        method.setVisibility(JavaVisibility.DEFAULT);
        method.addParameter(0, new Parameter(listEntityJavaType, "list"));

        interfaze.addMethod(method);

        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }


    /**
     * 获取格式为id = #{id,jdbcType=INTEGER}的条件语句
     *
     * @param column     目标列
     * @param isTopLevel 是否处于顶级(不是foreach的子项)
     * @return id = #{id,jdbcType=INTEGER}格式
     */
    private String getEqualsCondition(IntrospectedColumn column, boolean isTopLevel) {
        StringBuilder builder = new StringBuilder();
        builder.append(column.getActualColumnName());
        builder.append(" = ");
        if (isTopLevel) {
            builder.append("#{");
        } else {
            builder.append("#{item.");
        }
        builder.append(column.getJavaProperty());
        builder.append(",jdbcType=");
        builder.append(column.getJdbcTypeName());
        builder.append("}");

        return builder.toString();
    }

    /**
     * 获取格式为#{id,jdbcType=INTEGER}的结果语句
     *
     * @param column     目标列
     * @param isTopLevel 是否处于顶级(不是foreach的子项)
     * @return id = #{id,jdbcType=INTEGER}格式
     */
    private String getEqualsResult(IntrospectedColumn column, boolean isTopLevel) {
        StringBuilder builder = new StringBuilder();
        if (isTopLevel) {
            builder.append("#{");
        } else {
            builder.append("#{item.");
        }
        builder.append(column.getJavaProperty());
        builder.append(",jdbcType=");
        builder.append(column.getJdbcTypeName());
        builder.append("}");

        return builder.toString();
    }

}
