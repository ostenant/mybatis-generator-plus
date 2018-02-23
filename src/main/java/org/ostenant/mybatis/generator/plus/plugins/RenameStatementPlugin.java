package org.ostenant.mybatis.generator.plus.plugins;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.ostenant.mybatis.generator.plus.constant.MapperXmlKey;
import org.ostenant.mybatis.generator.plus.constant.StatementIdValue;

import java.util.List;

public class RenameStatementPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    /**
     * 重命名SQLMapper.xml文件中的selectByPrimaryKey为selectById
     *
     * @param element           当前select元素
     * @param introspectedTable 当前数据库表的描述信息
     * @return 是否生成
     */
    @Override
    public boolean sqlMapSelectByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        removeIdAttributeFromStatement(element);
        Attribute newAttribute = new Attribute(MapperXmlKey.ATTRIBUTE_ID, StatementIdValue.STATEMENT_FIND_BY_ID);
        element.addAttribute(newAttribute);
        return super.sqlMapSelectByPrimaryKeyElementGenerated(element, introspectedTable);
    }

    /**
     * 重命名Mapper接口文件中的selectByPrimaryKey为selectById
     *
     * @param method            当前方法
     * @param interfaze         所属Mapper的java接口
     * @param introspectedTable 当前数据库表的描述信息
     * @return
     */
    @Override
    public boolean clientSelectByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        method.setName(StatementIdValue.STATEMENT_FIND_BY_ID);
        return super.clientSelectByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    /**
     * 重命名SQLMapper.xml文件中的deleteByPrimaryKey为deleteById
     *
     * @param element           当前delete元素
     * @param introspectedTable 当前数据库表的描述信息
     * @return 是否生成
     */
    @Override
    public boolean sqlMapDeleteByPrimaryKeyElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        removeIdAttributeFromStatement(element);
        Attribute newAttribute = new Attribute(MapperXmlKey.ATTRIBUTE_ID, StatementIdValue.STATEMENT_DELETE_BY_ID);
        element.addAttribute(newAttribute);
        return super.sqlMapDeleteByPrimaryKeyElementGenerated(element, introspectedTable);
    }

    /**
     * 重命名Mapper接口文件中的deleteByPrimaryKey为deleteById
     *
     * @param method            当前方法
     * @param interfaze         所属Mapper的java接口
     * @param introspectedTable 当前数据库表的描述信息
     * @return
     */
    @Override
    public boolean clientDeleteByPrimaryKeyMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        method.setName(StatementIdValue.STATEMENT_DELETE_BY_ID);
        return super.clientDeleteByPrimaryKeyMethodGenerated(method, interfaze, introspectedTable);
    }

    /**
     * 重命名SQLMapper.xml文件中的insert为save
     *
     * @param element           当前insert元素
     * @param introspectedTable 当前数据库表的描述信息
     * @return 是否生成
     */
    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        removeIdAttributeFromStatement(element);
        Attribute newAttribute = new Attribute(MapperXmlKey.ATTRIBUTE_ID, StatementIdValue.STATEMENT_SAVE);
        element.addAttribute(newAttribute);
        return super.sqlMapInsertElementGenerated(element, introspectedTable);
    }

    /**
     * 不生成SQLMapper.xml文件中的insertSelective节点
     *
     * @param element           当前insert元素
     * @param introspectedTable 当前数据库表的描述信息
     * @return 是否生成
     */
    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * 重命名Mapper接口文件中的insert为save
     *
     * @param method            当前方法
     * @param interfaze         所属Mapper的java接口
     * @param introspectedTable 当前数据库表的描述信息
     * @return
     */
    @Override
    public boolean clientInsertMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        method.setName(StatementIdValue.STATEMENT_SAVE);
        return super.clientInsertMethodGenerated(method, interfaze, introspectedTable);
    }

    /**
     * 不生成Mapper接口文件中的insertSelective方法
     *
     * @param method            当前方法
     * @param interfaze         所属Mapper的java接口
     * @param introspectedTable 当前数据库表的描述信息
     * @return
     */
    @Override
    public boolean clientInsertSelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * 重命名SQLMapper.xml文件中的updateByPrimaryKeySelective为update
     *
     * @param element           当前update元素
     * @param introspectedTable 当前数据库表的描述信息
     * @return 是否生成
     */
    @Override
    public boolean sqlMapUpdateByPrimaryKeySelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        removeIdAttributeFromStatement(element);
        Attribute newAttribute = new Attribute(MapperXmlKey.ATTRIBUTE_ID, StatementIdValue.STATEMENT_UPDATE);
        element.addAttribute(newAttribute);
        return super.sqlMapUpdateByPrimaryKeySelectiveElementGenerated(element, introspectedTable);
    }

    /**
     * 不生成SQLMapper.xml文件中的updateByPrimaryKeyWithBLOBs
     *
     * @param element           当前update元素
     * @param introspectedTable 当前数据库表的描述信息
     * @return 是否生成
     */
    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * 不生成SQLMapper.xml文件中的updateByPrimaryKeyWithoutBLOBs
     *
     * @param element           当前update元素
     * @param introspectedTable 当前数据库表的描述信息
     * @return 是否生成
     */
    @Override
    public boolean sqlMapUpdateByPrimaryKeyWithoutBLOBsElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * 重命名Mapper接口文件中的updateByPrimaryKeySelective为update
     *
     * @param method            当前方法
     * @param interfaze         所属Mapper的java接口
     * @param introspectedTable 当前数据库表的描述信息
     * @return
     */
    @Override
    public boolean clientUpdateByPrimaryKeySelectiveMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        method.setName(StatementIdValue.STATEMENT_UPDATE);
        return super.clientUpdateByPrimaryKeySelectiveMethodGenerated(method, interfaze, introspectedTable);
    }

    /**
     * 不生成Mapper接口文件中的updateByPrimaryKeyWithBLOBs方法
     *
     * @param method            当前方法
     * @param interfaze         所属Mapper的java接口
     * @param introspectedTable 当前数据库表的描述信息
     * @return
     */
    @Override
    public boolean clientUpdateByPrimaryKeyWithBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * 不生成Mapper接口文件中的insertSelective方法
     *
     * @param method            当前方法
     * @param interfaze         所属Mapper的java接口
     * @param introspectedTable 当前数据库表的描述信息
     * @return
     */
    @Override
    public boolean clientUpdateByPrimaryKeyWithoutBLOBsMethodGenerated(Method method, Interface interfaze, IntrospectedTable introspectedTable) {
        return false;
    }

    /**
     * 删除statement中旧的id
     *
     * @param element 当前statement元素
     */
    protected void removeIdAttributeFromStatement(XmlElement element) {
        List<Attribute> attributes = element.getAttributes();
        for (int i = 0; i < attributes.size(); i++) {
            Attribute attribute = attributes.get(i);
            if (StringUtils.equalsIgnoreCase(attribute.getName(), MapperXmlKey.ATTRIBUTE_ID)) {
                attributes.remove(attribute);
            }
        }
    }
}
