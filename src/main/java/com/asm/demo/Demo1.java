package com.asm.demo;


import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * ASM 官网 https://asm.ow2.io/
 */
public class Demo1 {

    public static void main(String[] args) throws IOException {

        /*
         asm生成的.class文件

            package com.asm3;

            public interface Comparable extends Mesurable {
                int LESS = -1;
                int EQUAL = 0;
                int GREATER = 1;

                int compareTo(Object var1);
            }
         */

        //生成一个类只需要ClassWriter组件即可
        ClassWriter cw = new ClassWriter(0);

        cw.visit(Opcodes.V1_5, Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT+Opcodes.ACC_INTERFACE,
                "com/asm3/Comparable", null, "java/lang/Object", new String[] {"com/asm3/Mesurable"});

        //定义类的属性
        cw.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL+Opcodes.ACC_STATIC, "LESS", "I",null, new Integer(-1)).visitEnd();
        cw.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL+Opcodes.ACC_STATIC, "EQUAL", "I", null, new Integer(0)).visitEnd();
        cw.visitField(Opcodes.ACC_PUBLIC+Opcodes.ACC_FINAL+Opcodes.ACC_STATIC, "GREATER", "I", null, new Integer(1)).visitEnd();

        //定义类的方法
        cw.visitMethod(Opcodes.ACC_PUBLIC+Opcodes.ACC_ABSTRACT, "compareTo", "(Ljava/lang/Object;)I",null,null).visitEnd();

        //使cw类已经完成
        cw.visitEnd();

        //将cw转换成字节数组写到文件里面去
        byte[] data = cw.toByteArray();
        File file = new File("./src/Comparable.class");
        System.out.println("path = "+file.getAbsolutePath());
        FileOutputStream fout = new FileOutputStream(file);
        fout.write(data);
        fout.close();
    }
}
