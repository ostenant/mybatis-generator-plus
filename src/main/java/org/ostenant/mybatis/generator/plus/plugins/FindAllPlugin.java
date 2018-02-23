package org.ostenant.mybatis.generator.plus.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.ostenant.mybatis.generator.plus.constant.MapperXmlKey;
import org.ostenant.mybatis.generator.plus.constant.MapperXmlValue;
import org.ostenant.mybatis.generator.plus.constant.StatementIdValue;

import java.util.List;
import java.util.stream.Collectors;

public class FindAllPlugin extends PluginAdapter {

    private FullyQualifiedJavaType listJavaType = new FullyQualifiedJavaType(StatementIdValue.JAVA_UTIL_LIST);

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * Mapper.xml文档DOM生成树，可以把自己的Statement挂在DOM树上。
     * 添加findAll的SQL Statement
     *
     * @param document          SQLMapper.xml 文档树描述对象
     * @param introspectedTable 表描述对象
     * @return 是否生成
     */
    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement rootElement = document.getRootElement();
        // <select></select>
        XmlElement statement = new XmlElement(MapperXmlKey.ELEMENT_SELECT);
        // id="findAll"
        statement.getAttributes().add(0, new Attribute(MapperXmlKey.ATTRIBUTE_ID, StatementIdValue.STATEMENT_FIND_ALL));
        // resultMap="BaseResultMap"
        statement.getAttributes().add(new Attribute(MapperXmlKey.ATTRIBUTE_RESULT_MAP, MapperXmlValue.ATTRIBUTE_BASE_RESULT_MAP));

        TextElement select = new TextElement("select");
        XmlElement include = new XmlElement(MapperXmlKey.ELEMENT_INCLUDE);
        include.getAttributes().add(new Attribute(MapperXmlKey.ATTRIBUTE_REFID, MapperXmlValue.ATTRIBUTE_BASE_COLUMN_LIST));

        TextElement from = new TextElement("from " + introspectedTable.getTableConfiguration().getTableName());

        statement.addElement(select);
        statement.addElement(include);
        statement.addElement(from);

        rootElement.addElement(statement);
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }

    /**
     * Mapper.java接口生成树，可以把自己的方法挂接在此接口上
     * List<EntityType> findAll();
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

        interfaze.addImportedType(listJavaType);
        Method method = new Method();
        method.setName(StatementIdValue.STATEMENT_FIND_ALL);

        FullyQualifiedJavaType listEntityJavaType = new FullyQualifiedJavaType(listJavaType.getShortName());
        listEntityJavaType.addTypeArgument(entityJavaType);

        method.setReturnType(listEntityJavaType);
        method.setVisibility(JavaVisibility.DEFAULT);

        interfaze.addMethod(method);

        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }
}
