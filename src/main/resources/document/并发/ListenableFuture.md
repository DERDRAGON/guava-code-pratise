- [Synchronizing threads](#synchronizing-threads)
  - [Monitor](#monitor)
  - [ListenableFuture接口](#listenablefuture%e6%8e%a5%e5%8f%a3)
    - [获得ListenableFuture接口](#%e8%8e%b7%e5%be%97listenablefuture%e6%8e%a5%e5%8f%a3)
  - [FutureCallback](#futurecallback)
    - [使用FutureCallback](#%e4%bd%bf%e7%94%a8futurecallback)
  - [SettableFuture](#settablefuture)
  - [AsyncFunction](#asyncfunction)
  - [FutureFallback](#futurefallback)
  - [Futures](#futures)
    - [异步转换](#%e5%bc%82%e6%ad%a5%e8%bd%ac%e6%8d%a2)
  - [RateLimiter](#ratelimiter)

# Synchronizing threads
com.google.util.concurrent中提供了更多的非常有用的特性，来帮助我们更简便的实现并发编程。

这个系列中，我们将包含以下内容：
- Monitor类作为一个互斥锁，确保串行访问我们代码中定义的区域，就像synchronized关键字一样，但是在语义上更容易理解，并包含一些有用的附加功能。
- ListenableFuture类，和Java中的一些监听类作用相似，在并发编程中， 我们可以通过注册一个回调函数，在Future完成后执行。
- FutureCallback类，能够帮助我们接收Future任务返回的结果，允许我们处理失败或成功的场景。
- SettableFuture、AsyncFunction和FutureFallback类，有用的实用程序类，我们可以在处理Future实例和Future做异步转换对象的时候使用。
- Futures类提供了许多有用的静态方法用于处理Future实例。
- RateLimiter类用于限制线程访问资源的频率，它很像信号量的限制，但实质是限制线程的数量，RateLimiter类限制基于时间的访问。

## Monitor
一个支持任意布尔条件的同步的抽象。Monitor类是作为ReentrantLock的一个替代，代码中使用 Monitor比使用ReentrantLock更不易出错，可读性也更强，并且也没有显著的性能损失，使用Monitor甚至有潜在的性能得到优化

> public abstract static class Guard：一个标识线程是否等待的布尔条件，Guard类总是与单一的Monitor相关联，Monitor可以在任意时间从任意占用Monitor的线程检查Guard，这样代码的编写将不在关心Guard是否被检查的频率。

> public abstract boolean isSatisfied()：Guard内部提供的抽象方法，isSatisfied()，当被关联的Monitor被占用时，Guard的此方法会被调用，该方法的实现必须取决于被关联Monitor保护的状态，并且状态不可修改。

> enter()：进入到当前Monitor，无限期阻塞。

> enterInterruptibly()：进入到当前Monitor，无限期阻塞，但可能会被打断。

> enter(long time, TimeUnit unit)：进入到当前Monitor，最多阻塞给定的时间，返回是否进入Monitor。

> enterInterruptibly(long time, TimeUnit unit)：进入到当前Monitor，最多阻塞给定的时间，但可能会被打断，返回是否进入Monitor。

> tryEnter()：如果可以的话立即进入Monitor，不阻塞，返回是否进入Monitor。

> enterWhen(Guard guard)：当Guard的isSatisfied()为true时，进入当前Monitor，无限期阻塞，但可能会被打断。

> enterWhenUninterruptibly(Guard guard)：当Guard的isSatisfied()为true时，进入当前Monitor，无限期阻塞。

> enterWhen(Guard guard, long time, TimeUnit unit)：当Guard的isSatisfied()为true时，进入当前Monitor，最多阻塞给定的时间，这个时间包括获取锁的时间和等待Guard satisfied的时间，但可能会被打断。

> enterWhenUninterruptibly(Guard guard, long time, TimeUnit unit)：当Guard的isSatisfied()为true时，进入当前Monitor，最多阻塞给定的时间，这个时间包括获取锁的时间和等待Guard satisfied的时间。

> enterIf(Guard guard)：如果Guard的isSatisfied()为true，进入当前Monitor，无限期的获得锁，不需要等待Guard satisfied。

> enterIfInterruptibly(Guard guard)：如果Guard的isSatisfied()为true，进入当前Monitor，无限期的获得锁，不需要等待Guard satisfied，但可能会被打断。

> enterIf(Guard guard, long time, TimeUnit unit)：如果Guard的isSatisfied()为true，进入当前Monitor，在给定的时间内持有锁，不需要等待Guard satisfied。

> enterIfInterruptibly(Guard guard, long time, TimeUnit unit)：如果Guard的isSatisfied()为true，进入当前Monitor，在给定的时间内持有锁，不需要等待Guard satisfied，但可能会被打断。

> tryEnterIf(Guard guard)：如果Guard的isSatisfied()为true并且可以的话立即进入Monitor，不等待获取锁，也不等待Guard satisfied。

> waitFor(Guard guard)：等待Guard satisfied，无限期等待，但可能会被打断，当一个线程当前占有Monitor时，该方法才可能被调用。

> waitForUninterruptibly(Guard guard)：等待Guard satisfied，无限期等待，当一个线程当前占有Monitor时，该方法才可能被调用。

> waitFor(Guard guard, long time, TimeUnit unit)：等待Guard satisfied，在给定的时间内等待，但可能会被打断，当一个线程当前占有Monitor时，该方法才可能被调用。

> waitForUninterruptibly(Guard guard, long time, TimeUnit unit)：等待Guard satisfied，在给定的时间内等待，当一个线程当前占有Monitor时，该方法才可能被调用。

> leave()：离开当前Monitor，当一个线程当前占有Monitor时，该方法才可能被调用。

> isFair()：判断当前Monitor是否使用一个公平的排序策略。

> isOccupied()：返回当前Monitor是否被任何线程占有，此方法适用于检测系统状态，不适用于同步控制。

> isOccupiedByCurrentThread()：返回当前线程是否占有当前Monitor。

> getOccupiedDepth()：返回当前线程进入Monitor的次数，如果房前线程不占有Monitor，返回0。

> getQueueLength()：返回一个估计的等待进入Monitor的线程数量，只是一个估算值，因为线程的数量在这个方法访问那不数据结构的时候可能会动态改变。此方法适用于检测系统状态，不适用于同步控制。

> getWaitQueueLength(Guard guard)：返回一个等待给定Guard satisfied的线程估计数量， 注意，因为超时和中断可能发生在任何时候，所以估计只作为一个等待线程的实际数目的上限。此方法适用于检测系统状态，不适用于同步控制。

> hasQueuedThreads()：返回是否有任何线程正在等待进入这个Monitor，注意，因为取消随时可能发生，所以返回true并不保证任何其他线程会进入这个Monitor。此方法设计用来检测系统状态。

> hasQueuedThread(Thread thread)：返回给定线程是否正在等待进入这个Monitor，注意，因为取消随时可能发生，所以返回true并不保证给定线程会进入这个Monitor。此方法设计用来检测系统状态。

> hasWaiters(Guard guard)：返回是否有任何线程正在等待给定Guard satisfied，注意，因为取消随时可能发生，所以返回true并不保证未来Guard变成satisfied时唤醒任意线程。此方法设计用来检测系统状态。 

## ListenableFuture接口
ListenableFuture接口并继承了JDK concurrent包下的Future 接口。配合Futures工具类，可以很方便的实现以下功能：
- 监听任务执行结果并执行回调方法。
- 提供方便的任务接口转换。
- 多线程并发执行取结果集合。
eg:
```
ExecutorService executor = Executors.newCachedThreadPool();
Future<Integer> future = executor.submit(new Callable<Integer>() {
    @Override
    public Integer call() throws Exception {
        //这里调用一些处理逻辑
        return 1 + 1;
    }
});
```
上面的例子中，我们提交了一个Callable对象到ExecutorService实例，ExecutorService实例会立即返回Future对象，但是这并不意味着线程任务已经完成，要获取线程执行的结果，我们需要调用Future.get方法，但是如果任务并未完成的话会造成线程阻塞。

ListenableFuture接口继承了Future接口进行了扩展，允许我们注册一个Callback函数，并在任务完成后执行。翻开ListenableFuture接口的源码，我们看到其中只定义了一个addListener方法，我们可以通过ListenableFuture.addListener，传入一个Runnable线程和ExecutorService对象，这个ExecutorService对象可以是提交原始任务的Executor实例，或者是完全的ExecutorService实例。

###  获得ListenableFuture接口
当一个Callable对象提交后，ExecutorService接口会返回一个Future对象，那么我们应该怎样获取到 ListenableFuture对象以便于我们设置Callback回调函数呢？Guava通过ListentingExecutorService接 口包装了ExecutorService对象
`ListeningExecutorService executorService = MoreExecutors.listeningDecorator(executor);`

这里我们使用到了MoreExecutors类，其中包含了大量的静态方法用于处理Executor, ExecutorService和ThreadPool实例，翻开源码，整理其中的公共方法，如下：
- getExitingExecutorService( ThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit)：将给定的ThreadPoolExecutor转换成ExecutorService实例，在程序完成时退出， 它是通过使用守护线程和添加一个关闭钩子来等待他们完成。
- getExitingScheduledExecutorService( ScheduledThreadPoolExecutor executor, long terminationTimeout, TimeUnit timeUnit)：将给定的ScheduledThreadPoolExecutor转换成ScheduledExecutorService实例，在程序完成时退出， 它是通过使用守护线程和添加一个关闭钩子来等待他们完成。
- addDelayedShutdownHook( ExecutorService service, long terminationTimeout, TimeUnit timeUnit)：添加一个关闭的钩子来等待给定的ExecutorService中的线程完成。
- getExitingExecutorService(ThreadPoolExecutor executor)：将给定的ThreadPoolExecutor转换成ExecutorService实例，在程序完成时退出， 它是通过使用守护线程和添加一个关闭钩子来等待他们完成。
- getExitingScheduledExecutorService( ScheduledThreadPoolExecutor executor)：将给定的ThreadPoolExecutor转换成ScheduledExecutorService实例，在程序完成时退出， 它是通过使用守护线程和添加一个关闭钩子来等待他们完成。
- sameThreadExecutor()：创建一个ExecutorService实例，运行线程中的每一个任务。
- listeningDecorator( ExecutorService delegate)：创建一个ExecutorService实例，通过线程提交或者唤醒其他线程提交ListenableFutureTask到给定的ExecutorService实例。
- listeningDecorator( ScheduledExecutorService delegate)：创建一个ScheduledExecutorService实例，通过线程提交或者唤醒其他线程提交ListenableFutureTask到给定的ExecutorService实例。
- platformThreadFactory()：返回一个默认的线程工厂用于创建新的线程。
- shutdownAndAwaitTermination( ExecutorService service, long timeout, TimeUnit unit)：逐渐关闭指定的ExecutorService，首先会禁用新的提交， 然后会取消现有的任务。 
```
int NUM_THREADS = 10;//10个线程
executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(NUM_THREADS));
ListenableFuture<String> listenableFuture =
        executorService.submit(new Callable<String>(){
            @Override
            public String call() throws Exception {
                return null;
            }
        });
listenableFuture.addListener(new Runnable() {
    @Override
    public void run() {
        //在Future任务完成之后运行的一些方法
        System.out.println("methodToRunOnFutureTaskCompletion");
    }
}, executorService);

首先，我们使用ExecutorService实例创建了一个固定大小的线程池，然后我们将Callable对象提 交给ListeningExecutorService获取到我们需要的ListenableFuture实例，最后我们注册了一个回调函数在任务完成后 执行，值得注意的有一点，如果任务完成的时候我们设置回调方法，它将立即执行。
不过，ListenableFuture. addListener有一个小的缺陷，我们没有办法接收返回的对象，这就导致在任务执行失败或成功的时候，我们不能执行其他的操作，不过Guava 提供了FutureCallback接口来弥补这个缺陷
```

## FutureCallback
Guava提供了 FutureCallback接口，FutureCallback接口提供了onSuccess 和onFailure 方法，onSuccess 方法以Future任务的执行结果作为参数，因此我们就可以在成功时候获取任务执行的结果，做进一步的处理了

### 使用FutureCallback
使用 FutureCallback非常简单，我们以类似的方式向ListenableFuture注册一个回调接口，我们不需要直接向 ListenableFuture添加FutureCallback回调函数，而是直接使用Futures.addCallback方法。Futures 类提供了一些有用的、用于处理Future实例的静态方法集合

eg:
```
class FutureCallbackImpl implements FutureCallback<String> {
    private StringBuilder builder = new StringBuilder();
 
    @Override
    public void onSuccess(String result) {
        builder.append(result).append(" successfully");
    }
 
    @Override
    public void onFailure(Throwable t) {
        builder.append(t.toString());
    }
 
    public String getCallbackResult() {
        return builder.toString();
    }
}
```
我们在onSuccess方法中捕获到了任务执行的结果，并将结果追加字符" successfully"，在异常的情况下，我们通过Throwable对象获取到了异常信息，来看下面的一个完整的应用例子
```
@Test
public void testFutureCallback() {
    // 创建一个线程缓冲池Service
    ExecutorService executor = Executors.newCachedThreadPool();
    //创建一个ListeningExecutorService实例
    ListeningExecutorService executorService =
            MoreExecutors.listeningDecorator(executor);
    //提交一个可监听的线程
    ListenableFuture<String> futureTask = executorService.submit
            (new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return "Task completed";
                }
            });
    FutureCallbackImpl callback = new FutureCallbackImpl();
    //线程结果处理回调函数
    Futures.addCallback(futureTask, callback, Executors.newSingleThreadExecutor());
    //如果callback中执行的是比较费时的操作，Guava建议使用以下方法。
//        Futures.addCallback(futureTask,callback,executorService);
    //处理后的线程执行结果："Task completed successfully"
    assertThat(callback.getCallbackResult(), is("Task completed successfully"));
}
创建了一个ListenableFuture接口和一个FutureCallback接口的实现，并且注册，在 ListenableFuture实例完成后会立即执行回调函数，并且能够准确的捕获到执行结果进行处理。通常情况下，我们不想从 FutureCallback实例访问结果，而是让FutureCallback异步处理结果。如果提供的FutureCallback实例 执行的是比较费时的操作，使用如下的Futures.addCallback方法可能比较好：

Futures.addCallback(futureTask,callback,executorService);

通过这个方法，FutureCallback操作将会执行在单独的线程，这个线程由传入的ExecutorService参数提供。否则的话，初始 ListenableFuture实例执行的线程将会执行FutureCallback操作，就像ThreadPoolExecutor、 CallerRunsPolicy执行者服务，即任务将在调用者的线程上运行。 
```
FutureCallback接口用于处理任务完成或失败后的结果，适用于在多线程并发编程中对Future结果的获取和处理，是对ListenableFuture接口很好的补充，弥补了ListenableFuture接口无法处理任务返回值的缺陷。

## SettableFuture
SettableFuture继承了AbstractFuture抽象 类，AbstractFuture抽象类实现了ListenableFuture接口，所以SettableFuture类也是 ListenableFuture接口的一种实现，源码相当的简单，其中只包含了三个方法，一个用于创建SettableFuture实例的静态 create()方法；set方法用于设置Future的值，返回是否设置成功，如果Future的值已经被设置或任务被取消，会返回 false；setException与set方法类似，用于设置Future返回特定的异常信息，返回exception是否设置成功。

SettableFuture类是ListenableFuture接口的一种实现，我们可以通过SettableFuture设置Future的返回 值，或者设置Future返回特定的异常信息，可以通过SettableFuture内部提供的静态方法create()创建一个 SettableFuture实例，下面是一个简单的例子：
```
SettableFuture sf = SettableFuture.create();
//设置成功后返回指定的信息
sf.set("SUCCESS");
//设置失败后返回特定的异常信息
sf.setException(new RuntimeException("Fails"));
```
通过create()方法，我们可以创建一个默认的ettableFuture实例，当我们需要为Future实例设置一个返 回值时，我们可以通过set方法，设置的值就是Future实例在执行成功后将要返回的值；另外，当我们想要设置一个异常导致Future执行失败，我们 可以通过调用setException方法，我们将给Future实例设置指定的异常返回。

当我们有一个方法返回Future实例时，SettableFuture会显得更有价值，但是已经有了Future的返回值，我们也不需要再去执行异步任 务获取返回值

## AsyncFunction
AsyncFuction和Function都需要接收一个input参数，不同的是AsyncFunction接口返回的是 ListenableFuture，当我们需要接收AsyncFunction转换后的结果时，我们需要调用 ListenableFuture.get()方法。

AsyncFunction接口常被用于当我们想要异步的执行转换而不造成线程阻塞时，尽管Future.get()方法会在任务没有完成时造成阻塞，但 是AsyncFunction接口并不被建议用来异步的执行转换，它常被用于返回Future实例，我们来看下面的代码示例：
```
public class AsyncFunctionTest {

    class AsyncFunctionSample implements
            AsyncFunction<Long, String> {
        private ConcurrentMap<Long, String> map = Maps.newConcurrentMap();
        private ListeningExecutorService listeningExecutorService;
        //这里简单的模拟一个service
        private Map<Long,String> service = new HashMap<Long, String>(){
            {
                put(1L,"retrieved");
            }
        };

        @Override
        public ListenableFuture<String> apply(final Long input) throws
                Exception {
            if (map.containsKey(input)) {
                SettableFuture<String> listenableFuture = SettableFuture.
                        create();
                listenableFuture.set(map.get(input));
                return listenableFuture;
            } else {
                return listeningExecutorService.submit(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        //service中通过input获取retrieved
                        String retrieved = service.get(input);
                        map.putIfAbsent(input, retrieved);
                        return retrieved;
                    }
                });
            }
        }
    }
}
```
例子是对AsyncFunction接口的一个简单实现，内部包含了ConcurrentHashMap的实例，当我们调用apply方法的时候， 我们会首先在我们的map中查询value值，传入的input对象充当了一个关键的key，如果我们在map中找到了相应的value值，我们使用 SettableFuture对象创建了一个Future对象，并且设置返回值等于从map中获取到的value值；否则的话，我们通过向 ExecutorService提交Callable返回Future对象，同样的，在相应的map中，为相应的key设置获取到的value值。

## FutureFallback
提供一个Future的备用来替代之前失败的Future；在Future实例失败后常被用来作为Future的备份或者默认的值。

FutureFallback接口只提供了一个方法： ListenableFuture<V> create(Throwable t)，通过接收一个Throwable 实例，我们可以决定是尝试恢复失败的Future，返回默认的Future，或者传播这个异常。
```
class FutureFallbackImpl implements FutureFallback<String> {
    @Override
    public ListenableFuture<String> create(Throwable t) throws
            Exception {
        if (t instanceof FileNotFoundException) {
            SettableFuture<String> settableFuture =
                    SettableFuture.create();
            settableFuture.set("Not Found");
            return settableFuture;
        }
        throw new Exception(t);
    }
}
```
假设我们试图异步的检索文件的名称，但是没有找到，不过没有关系，我们首先判断了异常的类型，当捕获到找不到文件的异常 时，我们通过SettableFuture构造了一个Future对象，并且将其值设置为“Not Found”，然后将其返回，如果是其他的异常，我们可以继续传播此异常。

## Futures
Futures是Guava Concurrency提供的用于处理Future实例的工具类，其中提供了需要有用方便的方法，一些方法的使用也有特定的场景。

### 异步转换
Futures提供了transform方法，使我们能够简便的使用AsyncFunction接口：
```
@Test
public void testFuturesTransform() throws ExecutionException, InterruptedException {
    listenableFuture = executorService.submit(new Callable<String>() {
        @Override
        public String call() throws Exception {
            return "张三";
        }
    });
    AsyncFunction<String, Person> asyncFunction = new AsyncFunction<String, Person>() {
        @Override
        public ListenableFuture<Person> apply(String input) throws Exception {
            person.setName(input);
            return executorService.submit(new Callable<Person>() {
                @Override
                public Person call() throws Exception {
                    return person;
                }
            });
        }
    };
    ListenableFuture<Person> lf =
            Futures.transform(listenableFuture, asyncFunction);
    assertThat(lf.get().getName(), is("张三"));
}
```
Futures.transform方法返回了一个ListenableFuture实例，其结果是通过对从传递到方法中的ListenableFuture的执行结果进行一个异步的转换。

## RateLimiter
RateLimiter 类与Java api中的semaphore信号量比较类似，主要用于限制对资源并发访问的线程数，RateLimiter类限制线程访问的时间，这就意味着可以限制每 秒中线程访问资源的数量
eg: RateLimiter limiter = RateLimiter.create(4.0);

通过RateLimiter类提供的静态create方法，传入一个double类型的参数4.0，这就意味着每秒不超过4个任务被提 交。当我们要限制线程访问的时间时，我们需要用到RateLimiter类，我们可以使用和JDK信号量相同的方式进行操作
```
limiter.acquire();
executor.submit(new Callable<Object>() {
    @Override
    public Object call() throws Exception {
        return "Hello RateLimiter";
    }
});
```
我们调用了acquire方法，在获取允许访问资源的许可之前，线程会阻塞， 如果我们不希望当前线程阻塞，我们可以这样调用：
```
if(limiter.tryAcquire()){
    //有资源访问的许可
    System.out.println("someThing");
}else{
    //没有资源访问的许可
    System.out.println("anotherThing");
}
```
调用了tryAcquire方法，判断是否拥有资源访问的许可， 如果没有许可的话，将立即执行anotherThing部分的代码。tryAcquire方法有另外重载的方法，我们可以设置特定的超时时间

- create(double permitsPerSecond)：创建具有指定稳定吞吐量的RateLimiter类，传入允许每秒提交的任务数量。
- create(double permitsPerSecond, long warmupPeriod, TimeUnit unit)：创建具有指定稳定吞吐量的RateLimiter类，传入允许每秒提交的任务数量和准备阶段的时间，在这段时间RateLimiter会有个缓冲，直到达到它的最大速率（只要有饱和的足够的请求）
- setRate(double permitsPerSecond)：稳定的更新RateLimiter的速率，RateLimiter的构造方法中中设置permitsPerSecond参数，调用这个方法后，当前阻塞的线程不会被唤醒，因此它们不会观察到新的速率被设置。
- getRate()：返回RateLimiter被设置的稳定的速率值。
- acquire()：从这个ratelimiter获得一个许可，阻塞线程直到请求可以再授予许可。
- acquire(int permits)：获取传入数量的许可，阻塞线程直到请求可以再授予许可。
- tryAcquire(long timeout, TimeUnit unit)：判断是否可以在指定的时间内从ratelimiter获得一个许可，或者在超时期间内未获得许可的话，立即返回false。
- tryAcquire(int permits)：判断是否可以立即获取相应数量的许可。
- tryAcquire(）：判断是否可以立即获取许可。
- tryAcquire(int permits, long timeout, TimeUnit unit)：判断是否可以在超时时间内获取相应数量的许可。
