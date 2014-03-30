///*
// * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
// *
// * Copyright (c) 2013-2014 sagyf Yang. The Four Group.
// */
//
//package com.github.sog.controller.action;
//
//import com.google.common.collect.Lists;
//import com.github.sog.annotation.Param;
//import org.apache.commons.lang3.StringUtils;
//import org.objectweb.asm.ClassReader;
//import org.objectweb.asm.ClassVisitor;
//import org.objectweb.asm.Label;
//import org.objectweb.asm.MethodVisitor;
//import org.objectweb.asm.Opcodes;
//import org.objectweb.asm.Type;
//
//import java.io.IOException;
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//import java.util.Collections;
//import java.util.List;
//
///**
// * <p>
// * .
// * </p>
// *
// * @author sagyf yang
// * @version 1.0 2014-02-23 20:34
// * @since JDK 1.6
// */
//public final class ActionParam {
//
//    private final String paramName;
//
//    private final java.lang.reflect.Type paramType;
//
//    private final Param param;
//
//
//    private ActionParam(String paramName, java.lang.reflect.Type paramType, Param param) {
//        this.paramName = paramName;
//        this.paramType = paramType;
//        this.param = param;
//    }
//
//    public static List<ActionParam> me(final Method method, final Class<?>[] parameterTypes) {
//        int size = parameterTypes.length;
//        if (size == 0) {
//            return Collections.emptyList();
//        }
//        final String name = method.getDeclaringClass().getName();
//
//        final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
//
//        ClassReader classReader;
//        try {
//            classReader = new ClassReader(name);
//        } catch (IOException e) {
//            throw new RuntimeException("the controll action method is error!");
//        }
//        final List<ActionParam> actionParams = Lists.newArrayListWithCapacity(size);
//        classReader.accept(new ClassVisitor(Opcodes.ASM4) {
//            @Override
//            public MethodVisitor visitMethod(
//                    final int access,
//                    final String name, final String desc,
//                    final String signature, final String[] exceptions
//            ) {
//                if (access == Opcodes.ACC_PUBLIC) {
//                    // only plublic method
//                    final Type[] args = Type.getArgumentTypes(desc);
//                    // 方法名相同并且参数个数相同
//                    if (!name.equals(method.getName())
//                            || !sameType(args, parameterTypes)) {
//                        return super.visitMethod(access, name, desc, signature,
//                                exceptions);
//                    }
//                    MethodVisitor v = super.visitMethod(access, name, desc, signature, exceptions);
//                    return new MethodVisitor(Opcodes.ASM4, v) {
//                        @Override
//                        public void visitLocalVariable(String name, String desc
//                                , String signature
//                                , Label start, Label end, int index) {
//                            // 如果是静态方法，则第一就是参数
//                            // 如果不是静态方法，则第一个是"this"，然后才是方法的参数
//                            if (Modifier.isStatic(method.getModifiers()) || StringUtils.equals("this", name)) {
//                                return;
//                            }
//                            final Class<?> parameterType = parameterTypes[index - 1];
//                            final Annotation[] paramterAnnotation = parameterAnnotations[index-1];
//                            if (paramterAnnotation.length > 0) {
//
//                                Param param = null;
//                                for (Annotation annotation : paramterAnnotation) {
//                                    if (annotation instanceof Param) {
//                                        param = (Param) annotation;
//                                        break;
//                                    }
//                                }
//                                actionParams.add(new ActionParam(name, parameterType, param));
//                            } else {
//                                actionParams.add(new ActionParam(name, parameterType, null));
//                            }
//                            super.visitLocalVariable(name, desc, signature, start,
//                                    end, index);
//                        }
//
//                    };
//                } else {
//                    return super.visitMethod(access, name, desc, signature,
//                            exceptions);
//                }
//            }
//        }, 0);
//        return actionParams;
//    }
//
//    /**
//     * <p>
//     * 比较参数类型是否一致
//     * </p>
//     *
//     * @param types   asm的类型({@link Type})
//     * @param clazzes java 类型({@link Class})
//     * @return if true some.
//     */
//    private static boolean sameType(Type[] types, Class<?>[] clazzes) {
//        // 个数不同
//        if (types.length != clazzes.length) {
//            return false;
//        }
//
//        for (int i = 0; i < types.length; i++) {
//            if (!Type.getType(clazzes[i]).equals(types[i])) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    public String getParamName() {
//        return paramName;
//    }
//
//    public java.lang.reflect.Type getParamType() {
//        return paramType;
//    }
//
//    public Param getParam() {
//        return param;
//    }
//}
