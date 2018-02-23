package org.ostenant.mybatis.generator.plus.plugins;


import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

public class FieldCommentPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        this.generateComments(field, topLevelClass, introspectedColumn, introspectedTable, modelClassType);
        return true;
    }

    private void generateComments(Field field, TopLevelClass topLevelClass, IntrospectedColumn introspectedColumn, IntrospectedTable introspectedTable, ModelClassType modelClassType) {
        field.getJavaDocLines().add("/**");
        field.getJavaDocLines().add(" * " + introspectedColumn.getRemarks());
        field.getJavaDocLines().add(" */");
    }

}
