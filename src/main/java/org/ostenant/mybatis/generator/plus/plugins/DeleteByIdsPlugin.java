package org.ostenant.mybatis.generator.plus.plugins;


import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.ostenant.mybatis.generator.plus.constant.MapperXmlKey;
import org.ostenant.mybatis.generator.plus.constant.StatementIdValue;

import java.util.List;
import java.util.stream.Collectors;

public class DeleteByIdsPlugin extends PluginAdapter {

    private FullyQualifiedJavaType listJavaType = new FullyQualifiedJavaType(StatementIdValue.JAVA_UTIL_LIST);

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * Mapper.xml文档DOM生成树，可以把自己的Statement挂在DOM树上。
     * 添加deleteByIds的SQL Statement
     *
     * @param document          SQLMapper.xml 文档树描述对象
     * @param introspectedTable 表描述对象
     * @return 是否生成
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement rootElement = document.getRootElement();
        // <delete></delete>
        XmlElement statement = new XmlElement(MapperXmlKey.ELEMENT_DELETE);
        // id="deleteByIds"
        statement.getAttributes().add(0, new Attribute(MapperXmlKey.ATTRIBUTE_ID, StatementIdValue.STATEMENT_DELETE_BY_IDS));
        // parameterType="java.util.List"
        statement.getAttributes().add(new Attribute(MapperXmlKey.ATTRIBUTE_PARAMETER_TYPE, StatementIdValue.JAVA_UTIL_LIST));

        TextElement delete = new TextElement("delete from");
        TextElement student = new TextElement(introspectedTable.getTableConfiguration().getTableName());
        TextElement where = new TextElement("where");
        TextElement idIn = new TextElement("id in");

        XmlElement foreach = new XmlElement(MapperXmlKey.ELEMENT_FOREACH);
        foreach.getAttributes().add(0, new Attribute(MapperXmlKey.ATTRIBUTE_COLLECTION, "list"));
        foreach.getAttributes().add(1, new Attribute(MapperXmlKey.ATTRIBUTE_ITEM, "item"));
        foreach.getAttributes().add(2, new Attribute(MapperXmlKey.ATTRIBUTE_OPEN, "("));
        foreach.getAttributes().add(3, new Attribute(MapperXmlKey.ATTRIBUTE_SEPARATOR, ","));
        foreach.getAttributes().add(4, new Attribute(MapperXmlKey.ATTRIBUTE_CLOSE, ")"));

        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();

        TextElement foreachContent = new TextElement("#{item,jdbcType=" + primaryKeyColumns.get(0).getJdbcTypeName() + "}");
        foreach.addElement(0, foreachContent);

        statement.addElement(delete);
        statement.addElement(student);
        statement.addElement(where);
        statement.addElement(idIn);
        statement.addElement(foreach);

        rootElement.addElement(statement);
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }


    /**
     * Mapper.java接口生成树，可以把自己的方法挂接在此接口上
     * int deleteByIds(List<Integer> list);
     *
     * @param interfaze         Mapper接口信息描述对象
     * @param topLevelClass     此数据库表对应的实体类描述对象
     * @param introspectedTable 表描述对象
     * @return 是否生成
     */
    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
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
        method.setName(StatementIdValue.STATEMENT_DELETE_BY_IDS);

        FullyQualifiedJavaType listPrimaryKeyJavaType = new FullyQualifiedJavaType(listJavaType.getShortName());
        listPrimaryKeyJavaType.addTypeArgument(primaryKeyJavaType);

        switch (primaryKeyJavaType.getFullyQualifiedName()) {
            case StatementIdValue.JAVA_LANG_LONG:
                method.setReturnType(new FullyQualifiedJavaType("long"));
                break;

            case StatementIdValue.JAVA_LANG_INTEGER:
                method.setReturnType(new FullyQualifiedJavaType("int"));
                break;

            case StatementIdValue.JAVA_LANG_SHORT:
                method.setReturnType(new FullyQualifiedJavaType("short"));
                break;

            default:
                method.setReturnType(primaryKeyJavaType);
                break;
        }

        method.setVisibility(JavaVisibility.DEFAULT);
        method.addParameter(0, new Parameter(listPrimaryKeyJavaType, "list"));

        interfaze.addMethod(method);
        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }
}
