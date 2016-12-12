package com.leantaas.operation.manager;

import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.function.BiFunction;

/**
 * This one stores instances of operations
 * Created by boweiliu on 12/11/16.
 */
public class OperationGuavaClassPathResolver extends OperationResolver {

  @SuppressWarnings("unchecked")
  public OperationGuavaClassPathResolver(String stringOpsPackage,
                                         String numberOpsPackage) {
    super();
    try {
      ClassPath stringClassPath = ClassPath.from(OperationResolver.class.getClassLoader());
      for (ClassPath.ClassInfo classInfo : stringClassPath.getTopLevelClasses(stringOpsPackage)) {
        stringOperationLookup.put(classInfo.getSimpleName().toLowerCase(),
            (BiFunction<String, String, String>) classInfo.load().newInstance());
      }

      ClassPath numberOpClassPath = ClassPath.from(OperationResolver.class.getClassLoader());
      for(ClassPath.ClassInfo classInfo : numberOpClassPath.getTopLevelClasses(numberOpsPackage)) {
        numberOperationLookup.put(classInfo.getSimpleName().toLowerCase(),
            (BiFunction<Number, Number, Number>) classInfo.load().newInstance());
      }
    } catch (IOException ioe) {
      throw new RuntimeException("ConcatOperation.class.getClassLoader() IOE");
    } catch (IllegalAccessException | InstantiationException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }
}
