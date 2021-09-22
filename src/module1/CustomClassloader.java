package module1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CustomClassloader extends ClassLoader{
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            byte[] bytes = getXlassBytes(name);
            for(int i=0;i<bytes.length;i++) {
                bytes[i] = (byte) (255 - bytes[i]);
            }
            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception ex) {
            return super.findClass(name);
        }
    }

    private byte[] getXlassBytes(String name) throws IOException {
        InputStream is = this.getClass().getResourceAsStream(name + ".xlass");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int i = 0;
        while ((i = is.read(buffer)) != -1) {
            outputStream.write(buffer, 0, i);
        }
        return outputStream.toByteArray();
    }

    public static void main(String[] args) throws Exception {
        Object obj = new CustomClassloader().loadClass("Hello").getDeclaredConstructor().newInstance();
        Class clazz = obj.getClass();
        System.out.println(clazz.getName());
        Method[] methods = clazz.getMethods();
        for(Method method: methods) {
            System.out.println("method: " + method.getName());
        }
        Method method = clazz.getMethod("hello");
        System.out.println("hello params: " + method.getParameters().length);
        System.out.println("===============================");
        method.invoke(obj);
    }

}
