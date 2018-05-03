import java.util.Random;

/**
 * 基于函数接口的AOP实现
 */
public class AspectJzm {
    public static AspectJzm define = new AspectJzm();

    private AspectJzm(){}

    /**
     * 记录所有的切面方法
     */
    private Delegate chain;

    /**
     * 将切面方法链接起来
     * @param apoMethod
     * @return
     */
    public AspectJzm combine(Delegate apoMethod){
        if(chain == null){
            chain = apoMethod;
        }else{
            Delegate exists = chain;
            chain = core -> {
                MethodProxy proxy = () -> exists.execute(core);
                return apoMethod.execute(proxy);
            };

            chain = new Delegate() {
                @Override
                public Object execute(MethodProxy core) {
                    return null;
                }
            }
        }
        return this;
    }

    /**
     * 具体的切面方法，打印日志
     * @return
     */
    public AspectJzm log(){
        return combine(core -> {
            System.out.println("方法开始...");
            Object result = core.execute();
            System.out.println("方法结束.");
            return result;
        });
    }

    /**
     * 执行具体的方法
     * @param method
     * @param <T>
     * @return
     */
    public <T> T doWork(MethodProxy method){
        if(chain == null){
            return (T)method.execute();
        }
        return (T)chain.execute(method);
    }


    public static void main(String[] args){
        int result = AspectJzm
                .define
                .log()
                .log()
                .doWork(AspectTest::get);
        System.out.println(result);

    }
}

/**
 * 切面方法接口
 */
interface Delegate{
    Object execute(MethodProxy core);
}

/**
 * 方法代理接口
 */
interface MethodProxy{
    Object execute();
}

/**
 * 测试类
 */
class AspectTest{
    public static int get(){
        int result = new Random().nextInt(10);
        System.out.println("正在执行方法add");
        return result;
    }
}
