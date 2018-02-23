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

public class SaveBatchPlugin extends UseGeneratedKeysColumnPlugin {

    private FullyQualifiedJavaType listJavaType = new FullyQualifiedJavaType(StatementIdValue.JAVA_UTIL_LIST);


    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * Mapper.xml文档DOM生成树，可以把自己的Statement挂在DOM树上。
     * 添加saveBatch的SQL Statement
     *
     * @param document          SQLMapper.xml 文档树描述对象
     * @param introspectedTable 表描述对象
     * @return 是否生成
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement rootElement = document.getRootElement();
        // <insert></insert>
        XmlElement statement = new XmlElement(MapperXmlKey.ELEMENT_INSERT);
        // id="saveBatch"
        statement.getAttributes().add(0, new Attribute(MapperXmlKey.ATTRIBUTE_ID, StatementIdValue.STATEMENT_SAVE_BATCH));
        // parameterType="java.util.List"
        statement.getAttributes().add(new Attribute(MapperXmlKey.ATTRIBUTE_PARAMETER_TYPE, StatementIdValue.JAVA_UTIL_LIST));
        // 配置主键自动生成 - generateKey
        configUseGeneratedKey(statement, introspectedTable);

        TextElement insert = new TextElement("insert into");
        TextElement table = new TextElement(introspectedTable.getTableConfiguration().getTableName());

        List<IntrospectedColumn> tableColumns = introspectedTable.getAllColumns();

        TextElement columns = new TextElement(tableColumns.stream()
                .map(IntrospectedColumn::getActualColumnName)
                .collect(Collectors.joining(", ", "(", ")")));

        TextElement values = new TextElement("values");

        XmlElement foreach = new XmlElement(MapperXmlKey.ELEMENT_FOREACH);
        foreach.getAttributes().add(0, new Attribute(MapperXmlKey.ATTRIBUTE_COLLECTION, "list"));
        foreach.getAttributes().add(1, new Attribute(MapperXmlKey.ATTRIBUTE_ITEM, "item"));
        foreach.getAttributes().add(2, new Attribute(MapperXmlKey.ATTRIBUTE_SEPARATOR, ","));

        TextElement foreachContent = new TextElement(tableColumns.stream()
                .map(column -> {
                    StringBuilder builder = new StringBuilder();
                    builder.append("#{item.");
                    builder.append(column.getJavaProperty());
                    builder.append(",jdbcType=");
                    builder.append(column.getJdbcTypeName().toUpperCase());
                    builder.append("}");
                    return builder.toString();
                }).collect(Collectors.joining(", ", "(", ")")));
        foreach.addElement(0, foreachContent);

        statement.addElement(insert);
        statement.addElement(table);
        statement.addElement(columns);
        statement.addElement(values);
        statement.addElement(foreach);

        rootElement.addElement(statement);

        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    /**
     * Mapper.java接口生成树，可以把自己的方法挂接在此接口上
     * int saveBatch(List<EntityType> list);
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
        method.setName(StatementIdValue.STATEMENT_SAVE_BATCH);

        FullyQualifiedJavaType listEntityJavaType = new FullyQualifiedJavaType(listJavaType.getShortName());
        listEntityJavaType.addTypeArgument(entityJavaType);

        switch (primaryKeyJavaType.getFullyQualifiedName()) {
            case StatementIdValue.JAVA_LANG_INTEGER:
                method.setReturnType(new FullyQualifiedJavaType("int"));
                break;

            case StatementIdValue.JAVA_LANG_LONG:
                method.setReturnType(new FullyQualifiedJavaType("long"));
                break;

            case StatementIdValue.JAVA_LANG_SHORT:
                method.setReturnType(new FullyQualifiedJavaType("short"));
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


}
