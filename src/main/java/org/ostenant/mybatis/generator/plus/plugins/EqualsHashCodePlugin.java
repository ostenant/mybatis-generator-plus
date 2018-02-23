package org.ostenant.mybatis.generator.plus.plugins;


import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.internal.util.JavaBeansUtil;

import java.util.Iterator;
import java.util.List;

public class EqualsHashCodePlugin extends PluginAdapter {
    private FullyQualifiedJavaType objects = new FullyQualifiedJavaType("java.util.Objects");


    public EqualsHashCodePlugin() {
    }

    public boolean validate(List<String> warnings) {
        return true;
    }

    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        List<IntrospectedColumn> columns;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            columns = introspectedTable.getNonBLOBColumns();
        } else {
            columns = introspectedTable.getAllColumns();
        }

        this.generateEquals(topLevelClass, columns, introspectedTable);
        this.generateHashCode(topLevelClass, columns, introspectedTable);
        return true;
    }

    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.generateEquals(topLevelClass, introspectedTable.getPrimaryKeyColumns(), introspectedTable);
        this.generateHashCode(topLevelClass, introspectedTable.getPrimaryKeyColumns(), introspectedTable);
        return true;
    }

    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        this.generateEquals(topLevelClass, introspectedTable.getAllColumns(), introspectedTable);
        this.generateHashCode(topLevelClass, introspectedTable.getAllColumns(), introspectedTable);
        return true;
    }

    private void generateEquals(TopLevelClass topLevelClass, List<IntrospectedColumn> introspectedColumns, IntrospectedTable introspectedTable) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getBooleanPrimitiveInstance());
        method.setName("equals");
        method.addParameter(new Parameter(FullyQualifiedJavaType.getObjectInstance(), "that"));
        if (introspectedTable.isJava5Targeted()) {
            method.addAnnotation("@Override");
        }

        this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        method.addBodyLine("if (this == that) {");
        method.addBodyLine("return true;");
        method.addBodyLine("}");
        method.addBodyLine("if (that == null) {");
        method.addBodyLine("return false;");
        method.addBodyLine("}");
        method.addBodyLine("if (getClass() != that.getClass()) {");
        method.addBodyLine("return false;");
        method.addBodyLine("}");
        StringBuilder sb = new StringBuilder();
        sb.append(topLevelClass.getType().getShortName());
        sb.append(" other = (");
        sb.append(topLevelClass.getType().getShortName());
        sb.append(") that;");
        method.addBodyLine(sb.toString());
        boolean first = true;

        for (Iterator<IntrospectedColumn> iter = introspectedColumns.iterator(); iter.hasNext(); method.addBodyLine(sb.toString())) {
            IntrospectedColumn introspectedColumn = iter.next();
            sb.setLength(0);
            if (first) {
                sb.append("return (");
                first = false;
            } else {
                OutputUtilities.javaIndent(sb, 1);
                sb.append("&& (");
            }

            String getterMethod = JavaBeansUtil.getGetterMethodName(introspectedColumn.getJavaProperty(), introspectedColumn.getFullyQualifiedJavaType());
            if (introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                sb.append("this.");
                sb.append(getterMethod);
                sb.append("() == ");
                sb.append("other.");
                sb.append(getterMethod);
                sb.append("())");
            } else {
                sb.append("this.");
                sb.append(getterMethod);
                sb.append("() == null ? other.");
                sb.append(getterMethod);
                sb.append("() == null : this.");
                sb.append(getterMethod);
                sb.append("().equals(other.");
                sb.append(getterMethod);
                sb.append("()))");
            }

            if (!iter.hasNext()) {
                sb.append(';');
            }
        }

        topLevelClass.addMethod(method);
    }

    private void generateHashCode(TopLevelClass topLevelClass, List<IntrospectedColumn> introspectedColumns, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType(this.objects);
        // Objects.hash(id, name, grade, classNumber, instituteId);

        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setName("hashCode");
        if (introspectedTable.isJava5Targeted()) {
            method.addAnnotation("@Override");
        }
        this.context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);
        Iterator<IntrospectedColumn> iterator = introspectedColumns.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("return Objects.hash(");
        while (iterator.hasNext()) {
            IntrospectedColumn introspectedColumn = iterator.next();
            String javaProperty = introspectedColumn.getJavaProperty();
            sb.append(javaProperty);
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(");");

        method.addBodyLine(sb.toString());
        topLevelClass.addMethod(method);
    }
}

