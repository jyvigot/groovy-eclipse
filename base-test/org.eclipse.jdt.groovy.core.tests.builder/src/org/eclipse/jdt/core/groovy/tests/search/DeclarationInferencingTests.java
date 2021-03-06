/*
 * Copyright 2009-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eclipse.jdt.core.groovy.tests.search;

import static org.junit.Assert.assertEquals;

import org.codehaus.groovy.ast.MethodNode;
import org.junit.Test;

/**
 * Tests that the inferred declaration is correct.
 */
public final class DeclarationInferencingTests extends InferencingTestSuite {

    private void assertKnown(String source, String target, String declaringType, String declarationName, DeclarationKind declarationKind) {
        int offset = source.lastIndexOf(target);
        assertDeclaration(source, offset, offset + target.length(), declaringType, declarationName, declarationKind);
    }

    private void assertUnknown(String source, String target) {
        int offset = source.lastIndexOf(target);
        assertUnknownConfidence(source, offset, offset + target.length());
    }

    //--------------------------------------------------------------------------

    @Test
    public void testCategoryMethod0() {
        String contents = "new Object().@with";
        assertUnknown(contents, "with");
    }

    @Test
    public void testCategoryMethod1() {
        String contents = "new Object().with {\n}";
        assertKnown(contents, "with", "org.codehaus.groovy.runtime.DefaultGroovyMethods", "with", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField1() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "}");
            //@formatter:on

        String contents = "new Other().xxx";
        assertKnown(contents, "xxx", "Other", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField1a() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "}");
            //@formatter:on

        String contents = "new Other().@xxx";
        assertKnown(contents, "xxx", "Other", "xxx", DeclarationKind.FIELD);
    }

    @Test
    public void testGetterAndField2() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "}");
            //@formatter:on

        String contents = "new Other().getXxx()";
        assertKnown(contents, "getXxx", "Other", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField2a() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "}");
            //@formatter:on

        String contents = "new Other().getXxx";
        assertUnknown(contents, "getXxx");
    }

    @Test
    public void testGetterAndField2b() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "}");
            //@formatter:on

        String contents = "new Other().@getXxx";
        assertUnknown(contents, "getXxx");
    }

    @Test
    public void testGetterAndField2c() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "}");
            //@formatter:on

        String contents = "new Other().&getXxx";
        assertKnown(contents, "getXxx", "Other", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField3() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "}");
            //@formatter:on

        String contents = "new Other().getXxx()";
        assertKnown(contents, "getXxx", "Other", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField3a() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "}");
            //@formatter:on

        String contents = "new Other().getXxx";
        assertUnknown(contents, "getXxx");
    }

    @Test
    public void testGetterAndField3b() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "}");
            //@formatter:on

        String contents = "new Other().@getXxx";
        assertUnknown(contents, "getXxx");
    }

    @Test
    public void testGetterAndField3c() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "}");
            //@formatter:on

        String contents = "new Other().&getXxx";
        assertKnown(contents, "getXxx", "Other", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField4() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String getXxx() { null }\n" +
            "}");
            //@formatter:on

        String contents = "new Other().xxx";
        assertKnown(contents, "xxx", "Other", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField4a() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String getXxx() { null }\n" +
            "}");
            //@formatter:on

        String contents = "new Other().@xxx";
        assertUnknown(contents, "xxx");
    }

    @Test
    public void testGetterAndField5() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "}");
            //@formatter:on

        String contents = "def o = new Other(); o.xxx";
        assertKnown(contents, "xxx", "Other", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField5a() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "}");
            //@formatter:on

        String contents = "def o = new Other(); o.@xxx";
        assertKnown(contents, "xxx", "Other", "xxx", DeclarationKind.FIELD);
    }

    @Test
    public void testGetterAndField6() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "  static Other instance() { new O() }\n" +
            "}");
            //@formatter:on

        String contents = "Other.instance().xxx";
        assertKnown(contents, "xxx", "Other", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField6a() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "  static Other instance() { new O() }\n" +
            "}");
            //@formatter:on

        String contents = "Other.instance().@xxx";
        assertKnown(contents, "xxx", "Other", "xxx", DeclarationKind.FIELD);
    }

    @Test
    public void testGetterAndField7() {
        String contents =
            //@formatter:off
            "class Other {\n" +
            "  private String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "  void setXxx(String xxx) { this.xxx = xxx }\n" +
            "  public Other(String xxx) { /**/this.xxx = xxx }\n" +
            "  private def method() { def xyz = xxx; this.xxx }\n" +
            "}";
            //@formatter:on

        int offset = contents.indexOf("xxx", contents.indexOf("getXxx"));
        assertDeclaration(contents, offset, offset + 3, "Other", "xxx", DeclarationKind.FIELD);

        offset = contents.indexOf("this.xxx") + 5;
        assertDeclaration(contents, offset, offset + 3, "Other", "xxx", DeclarationKind.FIELD);

        offset = contents.indexOf("/**/this.xxx") + 9;
        assertDeclaration(contents, offset, offset + 3, "Other", "xxx", DeclarationKind.FIELD);

        offset = contents.lastIndexOf("= xxx") + 2;
        assertDeclaration(contents, offset, offset + 3, "Other", "xxx", DeclarationKind.FIELD);

        offset = contents.lastIndexOf("this.xxx") + 5;
        assertDeclaration(contents, offset, offset + 3, "Other", "xxx", DeclarationKind.FIELD);
    }

    @Test
    public void testGetterAndField7a() {
        String contents =
            //@formatter:off
            "class Other {\n" +
            "  private String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "  void setXxx(String xxx) { this.@xxx = xxx }\n" +
            "  public Other(String xxx) { /**/this.@xxx = xxx }\n" +
            "  private def method() { def xyz = xxx; this.@xxx }\n" +
            "}";
            //@formatter:on

        int offset = contents.indexOf("this.@xxx") + 6;
        assertDeclaration(contents, offset, offset + 3, "Other", "xxx", DeclarationKind.FIELD);

        offset = contents.indexOf("/**/this.@xxx") + 10;
        assertDeclaration(contents, offset, offset + 3, "Other", "xxx", DeclarationKind.FIELD);

        offset = contents.lastIndexOf("this.@xxx") + 6;
        assertDeclaration(contents, offset, offset + 3, "Other", "xxx", DeclarationKind.FIELD);
    }

    @Test
    public void testGetterAndField8() {
        String contents =
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "  void meth() { def closure = { xxx; this.xxx } }\n" +
            "}";
            //@formatter:on

        int offset = contents.indexOf("xxx;");
        assertDeclaration(contents, offset, offset + 3, "Other", "getXxx", DeclarationKind.METHOD);

        offset = contents.lastIndexOf("xxx");
        assertDeclaration(contents, offset, offset + 3, "Other", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField8a() {
        String contents =
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "  void meth() { def closure = { this.@xxx } }\n" +
            "}";
            //@formatter:on

        assertKnown(contents, "xxx", "Other", "xxx", DeclarationKind.FIELD);
    }

    @Test
    public void testGetterAndField9() {
        String contents =
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "  int compareTo(Other that) { this.xxx <=> that.xxx }\n" +
            "}";
            //@formatter:on

        int offset = contents.indexOf("this.xxx") + 5;
        assertDeclaration(contents, offset, offset + 3, "Other", "xxx", DeclarationKind.PROPERTY);

        offset = contents.indexOf("that.xxx") + 5;
        assertDeclaration(contents, offset, offset + 3, "Other", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField9a() {
        String contents =
            //@formatter:off
            "class Other {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "  int compareTo(Other that) { this.@xxx <=> that.@xxx }\n" +
            "}";
            //@formatter:on

        int offset = contents.indexOf("this.@xxx") + 6;
        assertDeclaration(contents, offset, offset + 3, "Other", "xxx", DeclarationKind.FIELD);

        offset = contents.indexOf("that.@xxx") + 6;
        assertDeclaration(contents, offset, offset + 3, "Other", "xxx", DeclarationKind.FIELD);
    }

    @Test
    public void testGetterAndField10() {
        createUnit("Foo",
            //@formatter:off
            "class Foo {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "class Bar extends Foo {\n" +
            "  String yyy\n" +
            "  def meth() {\n" +
            "    yyy = xxx\n" +
            "    this.yyy = this.xxx\n" +
            "  }\n" +
            "}";
            //@formatter:on

        int offset = contents.indexOf("yyy =");
        assertDeclaration(contents, offset, offset + 3, "Bar", "yyy", DeclarationKind.FIELD);

        offset = contents.indexOf("xxx");
        assertDeclaration(contents, offset, offset + 3, "Foo", "getXxx", DeclarationKind.METHOD);

        offset = contents.lastIndexOf("yyy");
        assertDeclaration(contents, offset, offset + 3, "Bar", "yyy", DeclarationKind.PROPERTY);

        offset = contents.lastIndexOf("xxx");
        assertDeclaration(contents, offset, offset + 3, "Foo", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField10a() {
        createUnit("Foo",
            //@formatter:off
            "class Foo {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "class Bar extends Foo {\n" +
            "  String yyy\n" +
            "  def meth() {\n" +
            "    this.@yyy = this.@xxx\n" +
            "  }\n" +
            "}";
            //@formatter:on

        assertKnown(contents, "yyy", "Bar", "yyy", DeclarationKind.FIELD);

        assertUnknown(contents, "xxx");
    }

    @Test
    public void testGetterAndField10b() {
        createUnit("Foo",
            //@formatter:off
            "class Foo {\n" +
            "  protected String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "class Bar extends Foo {\n" +
            "  protected String yyy\n" +
            "  def meth() {\n" +
            "    this.@yyy = super.@xxx\n" +
            "  }\n" +
            "}";
            //@formatter:on

        assertKnown(contents, "yyy", "Bar", "yyy", DeclarationKind.FIELD);

        assertKnown(contents, "xxx", "Foo", "xxx", DeclarationKind.FIELD);
    }

    @Test
    public void testGetterAndField11() {
        String contents =
            //@formatter:off
            "class Foo {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "  def yyy = new Object() {\n" +
            "    def meth() { Foo.this.xxx }\n" +
            "  }\n" +
            "}";
            //@formatter:on

        assertKnown(contents, "xxx", "Foo", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField11a() {
        String contents =
            //@formatter:off
            "class Foo {\n" +
            "  String xxx\n" +
            "  String getXxx() { xxx }\n" +
            "  def yyy = new Object() {\n" +
            "    def meth() { Foo.this.@xxx }\n" +
            "  }\n" +
            "}";
            //@formatter:on

        assertKnown(contents, "xxx", "Foo", "xxx", DeclarationKind.FIELD);
    }

    @Test
    public void testGetterAndField12() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  private static String xxx\n" +
            "  static String getXxx() { xxx }\n" +
            "}");
            //@formatter:on

        String contents = "Other.xxx";

        assertKnown(contents, "xxx", "Other", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField12a() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  private static String xxx\n" +
            "  static String getXxx() { xxx }\n" +
            "}");
            //@formatter:on

        String contents = "Other.@xxx";

        assertKnown(contents, "xxx", "Other", "xxx", DeclarationKind.FIELD);
    }

    @Test
    public void testGetterAndField13() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  private String xxx\n" +
            "  static String getXxx() { null }\n" +
            "}");
            //@formatter:on

        String contents = "Other.xxx";

        assertKnown(contents, "xxx", "Other", "getXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testGetterAndField13a() {
        createUnit("Other",
            //@formatter:off
            "class Other {\n" +
            "  private String xxx\n" +
            "  static String getXxx() { null }\n" +
            "}");
            //@formatter:on

        String contents = "Other.@xxx";

        assertUnknown(contents, "xxx");
    }

    @Test
    public void testSetterAndField1() {
        String contents =
            //@formatter:off
            "class Foo {\n" +
            "  String xxx\n" +
            "  void setXxx(String xxx) { this.xxx = xxx }\n" +
            "  void meth() { def closure = { xxx = ''; this.xxx = '' } }\n" +
            "}";
            //@formatter:on

        int offset = contents.indexOf("xxx", contents.indexOf("meth"));
        assertDeclaration(contents, offset, offset + 3, "Foo", "setXxx", DeclarationKind.METHOD);

        offset = contents.lastIndexOf("xxx");
        assertDeclaration(contents, offset, offset + 3, "Foo", "setXxx", DeclarationKind.METHOD);
    }

    @Test
    public void testSetterAndField2() {
        createUnit("Foo",
            //@formatter:off
            "class Foo {\n" +
            "  String xxx\n" +
            "  void setXxx(String xxx) { this.xxx = xxx }\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "class Bar extends Foo {\n" +
            "  String yyy\n" +
            "  def meth() {\n" +
            "    xxx = yyy\n" +
            "    this.xxx = this.yyy\n" +
            "  }\n" +
            "}";
            //@formatter:on

        int offset = contents.indexOf("xxx");
        assertDeclaration(contents, offset, offset + 3, "Foo", "setXxx", DeclarationKind.METHOD);

        offset = contents.indexOf("yyy", contents.indexOf("meth"));
        assertDeclaration(contents, offset, offset + 3, "Bar", "yyy", DeclarationKind.FIELD);

        offset = contents.lastIndexOf("xxx");
        assertDeclaration(contents, offset, offset + 3, "Foo", "setXxx", DeclarationKind.METHOD);

        offset = contents.lastIndexOf("yyy");
        assertDeclaration(contents, offset, offset + 3, "Bar", "yyy", DeclarationKind.PROPERTY);
    }

    @Test
    public void testLocalAndFieldWithSameName() {
        String contents =
            //@formatter:off
            "class Foo {\n" +
            "  String xxx\n" +
            "  void meth() {\n" +
            "    def xxx\n" +
            "    xxx = ''\n" +
            "  }\n" +
            "}";
            //@formatter:on

        assertKnown(contents, "xxx", "Foo", "xxx", DeclarationKind.VARIABLE);
    }

    @Test
    public void testParamAndFieldWithSameName() {
        String contents =
            //@formatter:off
            "class Foo {\n" +
            "  String xxx\n" +
            "  void meth(def xxx) {\n" +
            "    xxx = ''\n" +
            "  }\n" +
            "}";
            //@formatter:on

        assertKnown(contents, "xxx", "Foo", "xxx", DeclarationKind.VARIABLE);
    }

    @Test
    public void testMethodAndFieldWithSameName1() {
        createUnit("A",
            //@formatter:off
            "class A {\n" +
            "  String field\n" +
            "  public A field(){}\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "A a = new A()\n" +
            "a.field('')";
            //@formatter:on

        assertKnown(contents, "field", "A", "field", DeclarationKind.PROPERTY);
    }

    @Test
    public void testMethodAndFieldWithSameName2() {
        createUnit("A",
            //@formatter:off
            "class A {\n" +
            "  String field\n" +
            "  public A field(){}\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "A a = new A()\n" +
            "a.field('').field";
            //@formatter:on

        assertUnknown(contents, "field");
    }

    @Test
    public void testMethodAndFieldWithSameName3() {
        createUnit("A",
            //@formatter:off
            "class A {\n" +
            "  String field\n" +
            "  public A field(){}\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "A a = new A()\n" +
            "a.field";
            //@formatter:on

        assertKnown(contents, "field", "A", "field", DeclarationKind.PROPERTY);
    }

    @Test
    public void testMethodAndFieldWithSameName4() {
        createUnit("A",
            //@formatter:off
            "class A {\n" +
            "  String field\n" +
            "  public A field(){}\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "A a = new A()\n" +
            "a.field.field";
            //@formatter:on

        assertUnknown(contents, "field");
    }

    @Test
    public void testMethodAndFieldWithSameName5() {
        createUnit("A",
            //@formatter:off
            "class A {\n" +
            "  String field\n" +
            "  public A field(){}\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "A a = new A()\n" +
            "a.getField()";
            //@formatter:on

        assertKnown(contents, "getField", "A", "getField", DeclarationKind.METHOD);
    }

    @Test
    public void testMethodAndFieldWithSameName6() {
        createUnit("A",
            //@formatter:off
            "class A {\n" +
            "  String field\n" +
            "  public A field(){}\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "A a = new A()\n" +
            "a.setField('')";
            //@formatter:on

        assertKnown(contents, "setField", "A", "setField", DeclarationKind.METHOD);
    }

    @Test
    public void testMethodAndFieldWithSameName7() {
        createUnit("A",
            //@formatter:off
            "class A {\n" +
            "  String field\n" +
            "  public A field(){}\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "A a = new A()\n" +
            "a.field = a.field";
            //@formatter:on

        assertKnown(contents, "field", "A", "field", DeclarationKind.PROPERTY);
    }

    @Test
    public void testMethodAndFieldWithSameName8() {
        createUnit("A",
            //@formatter:off
            "class A {\n" +
            "  String field\n" +
            "  public A field(){}\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "A a = new A()\n" +
            "a.field = a.field()";
            //@formatter:on

        assertKnown(contents, "field", "A", "field", DeclarationKind.METHOD);
    }

    @Test
    public void testMethodAndFieldWithSameName9() {
        createUnit("A",
            //@formatter:off
            "class A {\n" +
            "  String field\n" +
            "  public A field(){}\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "A a = new A()\n" +
            "a.field(a.field)";
            //@formatter:on

        assertKnown(contents, "field", "A", "field", DeclarationKind.PROPERTY);
    }

    @Test // https://github.com/groovy/groovy-eclipse/issues/967
    public void testJavaInterfaceWithDefaultMethod1() {
        createJavaUnit("Face",
            //@formatter:off
            "public interface Face {\n" +
            "  default void meth() {\n" +
            "  }\n" +
            "}");
            //@formatter:on

        String contents =
            //@formatter:off
            "class Impl implements Face {\n" +
            "}\n" +
            "new Impl().meth()";
            //@formatter:on

        assertKnown(contents, "meth", "Face", "meth", DeclarationKind.METHOD);
    }

    @Test // https://github.com/groovy/groovy-eclipse/issues/967
    public void testJavaInterfaceWithDefaultMethod2() {
        createJavaUnit("Face",
            //@formatter:off
            "public interface Face {\n" +
            "  default void meth() {\n" +
            "  }\n" +
            "}");
            //@formatter:on
        createJavaUnit("Impl",
            //@formatter:off
            "public class Impl implements Face {\n" +
            "}");
            //@formatter:on

        String contents = "new Impl().meth()";

        assertKnown(contents, "meth", "Face", "meth", DeclarationKind.METHOD);
    }

    @Test // https://github.com/groovy/groovy-eclipse/issues/1047
    public void testJavaInterfaceWithDefaultMethod3() {
        // Java 11 adds default method toArray(IntFunction) to the Collection interface
        boolean jdkCollectToArray3;
        try {
            java.util.Collection.class.getDeclaredMethod("toArray", java.util.function.IntFunction.class);
            jdkCollectToArray3 = true;
        } catch (Exception e) {
            jdkCollectToArray3 = false;
        }

        String contents =
            //@formatter:off
            "List<String> list = []\n" +
            "list.toArray { n ->\n" +
            "  new String[n]\n" +
            "}\n";
            //@formatter:on

        int offset = contents.indexOf("toArray");
        MethodNode method = assertDeclaration(contents, offset, offset + 7, "java.util.Collection<java.lang.String>", "toArray", DeclarationKind.METHOD);
        assertEquals(jdkCollectToArray3 ? "java.util.function.IntFunction<T[]>" : "T[]", printTypeName(method.getParameters()[0].getType()));
    }

    @Test // GRECLIPSE-1105
    public void testFluentInterfaceWithFieldNameConflicts() {
        createUnit("A",
            //@formatter:off
            "class A {\n" +
            "  String field, other\n" +
            "  String getField() { return this.field }\n" +
            "  A field(String f){this.field = f; return this;}\n" +
            "  A other(String x){this.other = x; return this;}\n" +
            "}");
            //@formatter:on

        // field('f') should be the fluent method, not field property or getField method
        String contents = "new A().field('f').other('x').toString()";

        assertKnown(contents, "other", "A", "other", DeclarationKind.METHOD);
    }
}
