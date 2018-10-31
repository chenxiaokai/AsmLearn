package com.asm.demo;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Demo2 {

    public static void main(String[] args) {
        try {

            /*
            生成的代码:
            package com.asm.demo.test;

            public class C {
                public static long timer;

                public C() {
                    timer -= System.currentTimeMillis();
                    super();
                    timer += System.currentTimeMillis();
                }

                public void m() throws InterruptedException {
                    timer -= System.currentTimeMillis();
                    Thread.sleep(100L);
                    timer += System.currentTimeMillis();
                }
            }
             */

            ClassReader cr = new ClassReader("com/asm/demo/test/C");
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

            ClassVisitor classVisitor = new AddTimeClassAdapter(Opcodes.ASM7, cw);

            //使给定的访问者访问 Java类的 ClassReader
            cr.accept(classVisitor, ClassReader.SKIP_DEBUG);

            byte[] data = cw.toByteArray();
            File file = new File("./src/C.class");
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(data);
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
