package com.asm.demo;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.lang.reflect.Method;

public class Demo3 extends ClassLoader{

    public static void main(String[] args) throws Exception {

        /*
        生成的代码:
        public class Example {
            public Example() {
            }

            public static void main(String[] var0) {
                System.out.println("Hello World!");
            }
        }
         */

        // 创建一个ClassWriter来写示例类，这个类继承Object
        ClassWriter cw = new ClassWriter(0);
        /*
           * 第一个参数：JDK版本
           * 第二个参数：这个类的访问标记
           * 第三个参数：这个类的名字
           * 第四个参数：这个类的签名，当这个类没有继承或者实现一个接口的时候可以为空。
           * 第五个参数：当前类父类的名字 接口的父类是Object当该类是Object的时候为空
           * 第六个参数：接口的名字 可以为空
        */
        cw.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC, "Example", null, "java/lang/Object", null);

        /*
           * 创建一个写默认构造器的MethodWriter
           * 第一个参数：方法的访问标记
           * 第二个参数：方法名称
           * 第三个参数：方法的描述符号
           * 第四个参数：方法签名（当方法参数 返回类型 以及异常没有用到类属性的时候可以为空）
           * 第⑤个参数：异常名称
        */
        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);

        // 压入this变量
        mv.visitVarInsn(Opcodes.ALOAD, 0);
        // 执行父类构造器
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
        mv.visitInsn(Opcodes.RETURN);
        // 这段代码使用最多一个堆元素 和一个 局部变量
        mv.visitMaxs(1,1 );
        mv.visitEnd();

        // 创建一个main方法的MethodWriter
        mv = cw.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
        // 调用System类的PrintStream类的out
        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        // 压入"Hello World!" 常量
        mv.visitLdcInsn("Hello World!");

        // 执行定义在PrintStream中的println方法
        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
        mv.visitInsn(Opcodes.RETURN);
        // 使用两个堆和两个局部变量
        mv.visitMaxs(2, 2);

        // 获取Example类的字节码并且动态加载它。
        byte[] code = cw.toByteArray();
        FileOutputStream fos = new FileOutputStream("Example.class");
        fos.write(code);
        fos.close();

        //加载生成的.class文件
        Demo3 demo3 = new Demo3();
        Class<?> exampleClass = demo3.defineClass("Example", code, 0, code.length);
        Method method = exampleClass.getMethods()[0];
        method.invoke(null,new Object[] {null});

    }
}
