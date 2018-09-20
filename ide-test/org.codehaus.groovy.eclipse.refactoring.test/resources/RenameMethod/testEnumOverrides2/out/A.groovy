package p;

import groovy.transform.CompileStatic;

@CompileStatic
enum A {

  ONE() {
    @Override
    String foo() {
      'bar'
    }
  },

  TWO() {
    @Override
    String foo() {
      'baz'
    }
  }

  String foo() {
  }
}
