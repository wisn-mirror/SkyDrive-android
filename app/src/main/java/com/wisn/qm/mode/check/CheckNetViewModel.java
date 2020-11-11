package com.wisn.qm.mode.check;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.library.base.net.broadcast.BroadcastAll;
import com.library.base.net.broadcast.BroadcastGroup;
import com.library.base.net.broadcast.MessageCall;
import com.library.base.net.broadcast.UdpConfig;
import com.wisn.qm.App;
import com.wisn.qm.mode.db.AppDataBase;
import com.wisn.qm.mode.db.test.User;
import com.wisn.qm.mode.db.test.UserDao;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by Wisn on 2020/6/7 上午11:17.
 */
public class CheckNetViewModel extends ViewModel {
    private MutableLiveData<String> result;
    private  BroadcastGroup client;
    private  BroadcastAll client1;
    private  WifiManager.MulticastLock multicastLock;
    private static String TAG = "CheckNetViewModel";

    public CheckNetViewModel() {
        try {
            client = BroadcastGroup.getInstance();
            client.listenerMessage(UdpConfig.groupIp, UdpConfig.Group_ClientListenport, new MessageCall() {
                @Override
                public void callBackMessage(final String message, final String ip) {
                    System.out.println(" MESSAGE:" + message + " ip:" + ip);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
    //                        addressCallBack.call(hostAddress);
                            getResult().setValue("group MESSAGE:" + message + " ip:" + ip);
                        }
                    });
                }
            });
            client1 = BroadcastAll.getInstance();
            client1.listenerMessage(UdpConfig.All_ClientListenport, new MessageCall() {
                @Override
                public void callBackMessage(final String message, final String ip) {
                    System.out.println(" MESSAGE:" + message + " ip:" + ip);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
    //                        addressCallBack.call(hostAddress);
                            getResult().setValue("all MESSAGE:" + message + " ip:" + ip);
                        }
                    });
                }
            });
//            WifiManager wifiManager = (WifiManager) App.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiManager wifiManager = (WifiManager) App.app.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            multicastLock = wifiManager.createMulticastLock("multicast.test");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MutableLiveData<String> getResult() {
        if (result == null) {
            result = new MutableLiveData<>();
        }
        return result;
    }

    public void checkNet() {
        multicastLock.acquire();
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.sendBackMessage(UdpConfig.groupIp, UdpConfig.Group_ServerportRecevie, "AABBC");

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                client1.sendBackMessage(UdpConfig.groupIp, UdpConfig.All_ServerportRecevie, "AABBC");
            }
        }).start();

    }

    int count = 0;
    int maxRetryCount = 6;

    public void TextRxjava() {
        Disposable subscribe = Observable.create(new ObservableOnSubscribe<User[]>() {
            @Override
            public void subscribe(ObservableEmitter<User[]> observableEmitter) throws Exception {
//                AppDataBase qmdatabase = Room.databaseBuilder(App.app, AppDataBase.class, "qmdatabase1").build();
                AppDataBase qmdatabase = Room.databaseBuilder(App.app, AppDataBase.class, "qmdatabase1").allowMainThreadQueries().build();
                UserDao userDao = qmdatabase.getUserDao();
//                userDao.insertUser(new User("wisn", "aa@qq.com"));
//                userDao.insertUser(new User("abc", "aa@qq.com"));
//                userDao.insertUser(new User("123", "aa@qq.com"));
//                userDao.deleteUser("123");
                userDao.updateUser(122 ,"wisn@outlook.com");
//                User[] allUser = userDao.getAllUser();
                User[] allUser = userDao.getUserByUsername("abc");
                observableEmitter.onNext(allUser);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<User[]>() {
            @Override
            public void accept(User[] allUser) throws Exception {
                for (User user : allUser) {
                    Log.d(TAG, "user:" + user);
                }
            }
        });

//        create1();
//        create2();
//        create3();
//        create4();
//        create5();
//        create6();
//        create7();
//        create8();
//        create9();
//        create10();
//        create11();
//        create12();
//        create13();
//        create14();
//        create15();

    }

    /**
     * repeatWhen  指定重试的条件，和 retryWhen 的区别是 repeatWhen 用于正常的重试，retryWhen用于重试
     */
    private void create15() {
        //repeat 无条件重试, 但必须要在 onComplete 之后才会接着 但在subscribe 中所有的重试都完成后，只会收到一次onComplete
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onComplete();
//                emitter.onError(new Throwable("异常"));
            }
        }).repeat(3).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "repeat 开始onSubscribe" + d);
            }

            @Override
            public void onNext(Integer s) {
                Log.d(TAG, "repeat onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "repeat onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "repeat onComplete");
            }
        });
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(2222);
                emitter.onNext(3333);
                emitter.onComplete();
            }
        }).repeatWhen(new Function<Observable<Object>, ObservableSource<?>>() {
            @Override
            public ObservableSource<?> apply(Observable<Object> objectObservable) throws Exception {
                // 此处有2种情况：
                // 1. 若新被观察者（Observable）返回1个Complete（） /  Error（）事件，则不重新订阅 & 发送原来的 Observable
                // 2. 若新被观察者（Observable）返回其余事件，则重新订阅 & 发送原来的 Observable

                return objectObservable.flatMap(new Function<Object, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Object o) throws Exception {
                        Log.d(TAG, "retry apply" + o.toString());
                        // 情况1：若新被观察者（Observable）返回1个Complete（） /  Error（）事件，则不重新订阅 & 发送原来的 Observable
//                        return Observable.empty();
                        // Observable.empty() = 发送Complete事件，但不会回调观察者的onComplete（）

                        // return Observable.error(new Throwable("不再重新订阅事件"));
                        // 返回Error事件 = 回调onError（）事件，并接收传过去的错误信息。

                        // 情况2：若新被观察者（Observable）返回其余事件，则重新订阅 & 发送原来的 Observable
                        // return Observable.just(1);
                        // 仅仅是作为1个触发重新订阅被观察者的通知，发送的是什么数据并不重要，只要不是Complete（） /  Error（）事件
                        if (count <= maxRetryCount) {
                            //重试3次
                            count++;
                            return Observable.just(100).delay(count, TimeUnit.SECONDS);
                        } else {
                            //重试结束
//                            Observable.empty() = 发送Complete事件，但不会回调观察者的onComplete（）
//                            return Observable.empty();
                            return Observable.error(new Throwable("重试结束"));
                        }
                    }
                });
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "repeat 开始onSubscribe" + d);
            }

            @Override
            public void onNext(Integer s) {
                Log.d(TAG, "repeat onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "repeat onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "repeat onComplete");
            }
        });

    }

    /**
     * retry 无限重试，指定重试条件，指定重试次数
     * retryWhen 指定条件重试，并且可以指定条件延时重试
     */
    private void create14() {
        //retry,无参数的时候，是无限的重试，同时可以控制是否重试
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onError(new Throwable("异常"));
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "retry 开始onSubscribe" + d);
            }

            @Override
            public void onNext(Integer s) {
                Log.d(TAG, "retry onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "retry onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "retry onComplete");
            }
        });
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onError(new Throwable("异常"));
            }
        }).retry(new Predicate<Throwable>() {
            @Override
            public boolean test(Throwable throwable) throws Exception {
                //判断异常是否要继续重试
                return true;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "retry 开始onSubscribe" + d);
            }

            @Override
            public void onNext(Integer s) {
                Log.d(TAG, "retry onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "retry onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "retry onComplete");
            }
        });

          /*  Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onError(new Throwable("异常"));
            }
        }).retry(new BiPredicate<Integer, Throwable>() {

            @Override
            public boolean test(Integer integer, Throwable throwable) throws Exception {
                Log.d(TAG, "retry 当前重试的次数 ：" + integer);

                return true;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "retry 开始onSubscribe" + d);
            }

            @Override
            public void onNext(Integer s) {
                Log.d(TAG, "retry onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "retry onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "retry onComplete");
            }
        });*/
       /* Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onError(new Throwable("异常"));
            }
        }).retry(3, new Predicate<Throwable>() {
            @Override
            public boolean test(Throwable throwable) throws Exception {

                return true;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "retry 开始onSubscribe" + d);
            }

            @Override
            public void onNext(Integer s) {
                Log.d(TAG, "retry onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "retry onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "retry onComplete");
            }
        });*/
        //retryUntil 出现错误后，判断是否需要重新发送数据 具体使用类似于retry（Predicate predicate），唯一区别：返回 true 则不重新发送数据事件\
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onError(new Throwable("异常"));
            }
        }).retryUntil(new BooleanSupplier() {
            @Override
            public boolean getAsBoolean() throws Exception {
                return true;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "retry 开始onSubscribe" + d);
            }

            @Override
            public void onNext(Integer s) {
                Log.d(TAG, "retry onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "retry onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "retry onComplete");
            }
        });


        count = 0;
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onError(new Throwable("异常"));
            }
        }).retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {

            @Override
            public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                    @Override
                    public ObservableSource<?> apply(Throwable throwable) throws Exception {
                        Log.d(TAG, "retry apply" + throwable.toString());

                        //返回有两种情况
                        // 1. 若 新的被观察者 Observable发送的事件 = Error事件，那么 原始Observable则不重新发送事件：
                        // 2. 若 新的被观察者 Observable发送的事件 = Next事件 ，那么原始的Observable则重新发送事件：
                        if (count <= maxRetryCount) {
                            //重试3次
                            count++;
                            return Observable.just(100).delay(count, TimeUnit.SECONDS);
                        } else {
                            //重试结束
                            return Observable.error(new Throwable("重试结束"));
                        }
                    }
                });

            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "retry 开始onSubscribe" + d);
            }

            @Override
            public void onNext(Integer s) {
                Log.d(TAG, "retry onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "retry onError" + e.toString());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "retry onComplete");
            }
        });

    }

    /**
     * onErrorResumeNext
     * onExceptionResumeNext
     */
    private void create13() {
        //onErrorResumeNext 处理的是Throwable，当遇到错误的时候会发送一个新的Observable，相当于交给了另外一个人
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onError(new Throwable("异常"));
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
            //当发生异常事件的时候，会出发这个被观察者
            @Override
            public ObservableSource<? extends Integer> apply(Throwable throwable) throws Exception {
                return Observable.just(199);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onErrorResumeNext 开始onSubscribe" + d);
            }

            @Override
            public void onNext(Integer s) {
                Log.d(TAG, "onErrorResumeNext onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "onErrorResumeNext onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onErrorResumeNext onComplete");
            }
        });
        //onExceptionResumeNext 处理的是Exception 当发生错误的时候给过程交给其他人
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onError(new Exception("异常"));
            }
        }).onExceptionResumeNext(new Observable<Integer>() {

            @Override
            protected void subscribeActual(Observer<? super Integer> observer) {
                observer.onNext(88);
                observer.onNext(99);
                observer.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onExceptionResumeNext 开始onSubscribe" + d);
            }

            @Override
            public void onNext(Integer s) {
                Log.d(TAG, "onExceptionResumeNext onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "onExceptionResumeNext onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onExceptionResumeNext onComplete");
            }
        });
    }

    /**
     * 链式调用的事件监听
     * onErrorReturn 可以在错误的时候返回指定值
     */
    private void create12() {
        // 链式调用的事件监听
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onNext(4);
                emitter.onError(new Throwable("异常"));
            }
        }).doOnEach(new Consumer<Notification<Integer>>() {
            // 1. 当Observable每发送1次数据事件就会调用1次
            @Override
            public void accept(Notification<Integer> integerNotification) throws Exception {
                Log.d(TAG, "doOnEach " + integerNotification.getValue());
            }
        }).doOnNext(new Consumer<Integer>() {
            // 2. 执行Next事件前调用

            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "doOnNext " + integer);
            }
        }).doAfterNext(new Consumer<Integer>() {
            // 3. 执行Next事件后调用

            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "doAfterNext " + integer);

            }
        }).doOnComplete(new Action() {
            // 4. 事件发送完成后调用

            @Override
            public void run() throws Exception {
                Log.d(TAG, "doOnComplete ");

            }
        }).doOnError(new Consumer<Throwable>() {
            //5. 发送错误事件的时候调用
            @Override
            public void accept(Throwable throwable) throws Exception {
                throwable.printStackTrace();
                Log.d(TAG, "doOnError ");

            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            //6.订阅的时候调用
            @Override
            public void accept(Disposable disposable) throws Exception {
                Log.d(TAG, "doOnSubscribe " + disposable);

            }
        }).doAfterTerminate(new Action() {
            //7. 无论正常发送完事件还是发生异常 最后都要调用
            @Override
            public void run() throws Exception {
                Log.d(TAG, "doAfterTerminate ");

            }
        }).doFinally(new Action() {
            //8 最后调用
            @Override
            public void run() throws Exception {
                Log.d(TAG, "doFinally ");

            }
        }).onErrorReturn(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(Throwable throwable) throws Exception {
                //指定错误事件后返回指定的
                Log.d(TAG, "onErrorReturn ");
                return 100000;
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onErrorReturn 开始onSubscribe" + d);
            }

            @Override
            public void onNext(Integer s) {
                Log.d(TAG, "onErrorReturn onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "onErrorReturn onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onErrorReturn onComplete");
            }
        });
    }

    /**
     * subscribeOn 和observeOn
     * delay
     */
    private void create11() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("test");
                SystemClock.sleep(10000);
                emitter.onNext("test");
            }
        })
                //subscribeOn 多次调用只会第一次生效
                .subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                //observeOn 每次都会生效
                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, " onNext" + s);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Observable.just(2, 3, 4, 5)
                .delay(3, TimeUnit.SECONDS)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "delay 开始onSubscribe" + d);
                    }

                    @Override
                    public void onNext(Integer s) {
                        Log.d(TAG, "delay onNext" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "delay onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "delay onComplete");
                    }
                });
    }

    /**
     * startWith 追加
     * count 统计次数
     */
    private void create10() {
        //startWith追加顺序为后调用的先追加
        Observable.just(1, 3, 5)//最后遍历
                .startWith(Observable.just(11, 12, 13))//第3出来
                .startWith(0)//第二出来
                .startWithArray(6, 7, 8, 9)//先出来
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer i) throws Exception {
                        Log.d(TAG, "startWith accept" + i);
                    }
                });
        // count 统计次数
        Observable.just(432, 1, 5, 1)
                .count()
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "count accept 统计次数" + aLong);
                    }
                });
    }

    /**
     * reduce 对数据的聚合
     * collect 对数据处理，聚合到一个新的数据结构中
     */
    private void create9() {
        //reduce数据聚合 类似阶乘计算结果
        Observable.just(2, 1, 5, 2, 55)
                .reduce(new BiFunction<Integer, Integer, Integer>() {
                    @Override
                    public Integer apply(Integer s, Integer b) throws Exception {
                        return s + b;
                    }
                }).subscribe(new Consumer<Integer>() {

            @Override
            public void accept(Integer s) throws Exception {
                Log.d(TAG, "reduce accept" + s);
            }
        });
        //collect 给数据整理到一个新的数据结构中
        Observable.just(1, 2, 4, 2, 3, 6)
                .collect(new Callable<ArrayList<Integer>>() {
                    @Override
                    public ArrayList<Integer> call() throws Exception {

                        return new ArrayList<>();
                    }
                }, new BiConsumer<ArrayList<Integer>, Integer>() {
                    @Override
                    public void accept(ArrayList<Integer> integers, Integer integer) throws Exception {
                        integers.add(integer * 100);
                    }
                }).subscribe(new Consumer<ArrayList<Integer>>() {
            @Override
            public void accept(ArrayList<Integer> integers) throws Exception {
                for (int i : integers) {
                    Log.d(TAG, "collect accept" + i);
                }
            }
        });
    }

    /**
     * zip 按照个数上合并，一对一
     * combineLatest  时间上，同一时间点上合并
     */
    private void create8() {
        //zip 最终合并的事件数量 = 多个被观察者（Observable）中数量最少的数量
        Observable<Integer> observable1 = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "observable1 onNext" + 1);
                emitter.onNext(1);
                SystemClock.sleep(1000);
                Log.d(TAG, "observable1 onNext" + 2);
                emitter.onNext(2);
                SystemClock.sleep(1000);
                Log.d(TAG, "observable1 onNext" + 3);
                emitter.onNext(3);
                SystemClock.sleep(1000);
            }
        }).subscribeOn(Schedulers.io());//指定在io
        Observable<String> observable2 = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.d(TAG, "observable2 onNextA");
                emitter.onNext("A");
                SystemClock.sleep(500);
                Log.d(TAG, "observable2 onNextB");
                emitter.onNext("B");
                SystemClock.sleep(600);
                Log.d(TAG, "observable2 onNextC");
                emitter.onNext("C");
                SystemClock.sleep(700);
                Log.d(TAG, "observable2 onNextD");
                emitter.onNext("D");
                SystemClock.sleep(700);
            }
        }).subscribeOn(Schedulers.newThread());//指定在新线程
        Observable.zip(observable1, observable2, new BiFunction<Integer, String, String>() {
            @Override
            public String apply(Integer integer, String s) throws Exception {

                return "合并" + integer + s;
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "zip 开始onSubscribe" + d);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "zip onNext" + s);
                //只会输出 1A 2B 3C
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "zip onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "zip onComplete");
            }
        });
        //combineLatest 是匹配最后几个，一一对应上 同样处理异常的combineLatestDelayError
        Observable.combineLatest(Observable.just(1, 2, 3, 4, 5, 6, 7, 8)
                , Observable.intervalRange(0, 3, 1, 1, TimeUnit.SECONDS)
                , new BiFunction<Integer, Long, Long>() {
                    @Override
                    public Long apply(Integer a, Long b) throws Exception {
                        return a + b;
                    }
                }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long i) throws Exception {
                Log.d(TAG, "combineLatest accept:" + i);
            }
        });
    }

    private void create7() {
        //concatArrayDelayError/mergeArrayDelayError 处理错误，执行完所有的被观察者，让错误最后回调  如果只用merge或concat 如果有错误会终止执行
        Observable.mergeArrayDelayError(
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(1);
                        emitter.onNext(2);
                        emitter.onError(new NullPointerException());
//                        emitter.onNext(3);
                    }
                }),
                Observable.just(1, 2, 4, 2, 3, 6)
        )/*.subscribe(new Consumer<Integer>(){

            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "concatArrayDelayError accept" + integer);
            }
        })*/
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "concatArrayDelayError 开始onSubscribe" + d);
                    }

                    @Override
                    public void onNext(Integer s) {
                        Log.d(TAG, "concatArrayDelayError onNext" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.d(TAG, "concatArrayDelayError onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "concatArrayDelayError onComplete");
                    }
                });
    }

    /**
     * 合并执行和并行执行
     * concat concatArray
     * merge  mergeArray
     */
    private void create6() {
        //concat 组合最多4个，超过了用concatArray  合并后是按发送顺序串行执行
        Observable.concat(
                Observable.just(1, 2),
                Observable.just(1, 2),
                Observable.just(1, 2),
                Observable.just(1, 2))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "concat accept" + integer);
                    }
                });
        //concatArray  组合超过4个 合并后是按发送顺序串行执行
        Observable.concatArray(
                Observable.just(1, 2),
                Observable.just(1, 2),
                Observable.just(1, 2),
                Observable.just(1, 2),
                Observable.just(1, 2),
                Observable.just(1, 2),
                Observable.just(1, 2))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "concatArray accept" + integer);
                    }
                });

        //merge 大于4个用mergeArray，按时间线并行执行
        Observable.mergeArray(
                Observable.intervalRange(1, 3, 1, 1, TimeUnit.SECONDS),
                Observable.intervalRange(5, 3, 1, 1, TimeUnit.SECONDS),
                Observable.intervalRange(10, 3, 1, 1, TimeUnit.SECONDS)
        ).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long integer) throws Exception {
                Log.d(TAG, "merge accept" + integer);
            }
        });
    }

    /**
     * 操作符
     * map
     * flatmap
     * contactmap
     * buffer
     */
    private void create5() {
        //map 每个事件将做一个处理，将int 转换string
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 5; i++) {
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }).map(new Function<Integer, String>() {

            @Override
            public String apply(Integer integer) throws Exception {
                return integer + " 转换成String";
            }
        }).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
                return s + " 二次处理";
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "11111onNext" + s);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        //flatMap 无序的将被观察者发送的整个事件序列进行变换
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List<String> data = new ArrayList<>();
                for (int i = 0; i < integer; i++) {
                    data.add(i + "处理过的");
                }
                return Observable.fromIterable(data);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "11111 accept" + s);
            }
        });
        // concatMap 有序的将被观察者发送的整个事件序列进行变换 的顺序 = 被观察者旧序列生产的顺序
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).concatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List<String> data = new ArrayList<>();
                for (int i = 0; i < integer; i++) {
                    data.add(i + "处理过的");
                }
                return Observable.fromIterable(data);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "11111 accept" + s);
            }
        });


        //buffer 排队，缓冲4个 每次取2个一起分批发送
        Observable.just(1, 2, 3, 4, 5, 6, 7)
                .buffer(4, 2)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Log.d(TAG, "buffer buffer" + integers.size());
                        for (int a : integers) {
                            Log.d(TAG, "buffer a" + a);
                        }
                    }
                });
    }

    /**
     * 基础的操作符
     * just
     * fromArray
     * defer
     * interval
     * intervalRange
     * range
     */
    private void create4() {
        //just 最多10个参数
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "11111开始onSubscribe" + d);
            }

            @Override
            public void onNext(Integer s) {
                Log.d(TAG, "11111onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "11111onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "11111onComplete");
            }
        });
        //fromArray 任意参数
        Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12).subscribe(new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "11111开始onSubscribe" + d);
            }

            @Override
            public void onNext(Integer s) {
                Log.d(TAG, "11111onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "11111onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "11111onComplete");
            }
        });
        //fromIterable 遍历集合
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 88; i < 100; i++) {
            list.add(i);
        }
        Observable.fromIterable(list).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "accept" + integer);
            }
        });


        //defer 延迟的，提前定义
        Observable<Integer> defer = Observable.defer(new Callable<ObservableSource<Integer>>() {
            @Override
            public ObservableSource<Integer> call() throws Exception {
                return Observable.just(3333);
            }
        });
        defer.subscribe(new Consumer<Integer>() {

            @Override
            public void accept(Integer o) throws Exception {
                Log.d(TAG, "Consumer" + o);
            }
        });

        //timer 延时2s 之后执行
        Observable.timer(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "timer 开始onSubscribe" + d);
                    }

                    @Override
                    public void onNext(Long s) {
                        Log.d(TAG, "timer onNext" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "timer onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "timer onComplete");
                    }
                });
        //interval 第一次延迟 4s 间隔 1 s执行，,相当于定时器
        Observable.interval(4, 1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "interval 开始onSubscribe" + d);
                    }

                    @Override
                    public void onNext(Long s) {
                        Log.d(TAG, "interval onNext" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "interval onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "interval onComplete");
                    }
                });

        //intervalRange 开始的index ，数量为3，延时4s 每1s 执行一次，和interval 区别为多了一个数量
        Observable.intervalRange(5, 3, 4, 1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {

                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "intervalRange accept" + aLong);
                    }
                });
        //指定范围，定时器 从10开始 数量为4的定时器，和intervalRange 区别是没有延时开始的指定

        Observable.range(10, 4).subscribe(new Consumer<Integer>() {

            @Override
            public void accept(Integer integer) throws Exception {
                Log.d(TAG, "range accept" + integer);
            }
        });
    }

    /**
     * Consumer 测试
     */
    private void create3() {
        Observable<String> stringObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    emitter.onNext("aaa" + i);
                    emitter.onNext("bbb" + i);
                    emitter.onNext("ccc" + i);
                }
                emitter.onComplete();
            }
        });
        Disposable subscribe = stringObservable.subscribe(new Consumer<String>() {
            int count = 1;

            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "Consumer" + s);
                count++;
                if (count > 10) {
//                    subscribe.dispose();
                }
            }
        });
    }

    /**
     * 链式调用的demo
     */
    private void create2() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("aaaa");
                emitter.onNext("bbb");
                emitter.onNext("cccc");
                emitter.onNext("ddd");
                emitter.onComplete();
            }
        }).subscribe(new Observer<String>() {
            Disposable d;

            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "11111开始onSubscribe" + d);
                this.d = d;
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "11111onNext" + s);
                if (s.equals("bbb")) {
                    //切断关联
                    d.dispose();
                    Log.d(TAG, "是否关联" + d.isDisposed());
                }
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "11111onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "11111onComplete");
            }
        });
    }

    /**
     * 创建的几种方式
     */
    private void create1() {
        //创建 被观察者的几种方式：
        Observable<String> stringObservable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("aaa");
                emitter.onNext("bbb");
                emitter.onComplete();
            }
        });
        Observable<String> just = Observable.just("aaa", "bbb");
        Observable<String> stringObservable1 = Observable.fromArray("aaa", "bbb");

        //创建观察者的几种方式：
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "11111开始onSubscribe" + d);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "11111onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "11111onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "11111onComplete");
            }
        };

        stringObservable.subscribe(observer);
        Subscriber<String> subscriber = new Subscriber<String>() {

            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "2222开始 onSubscribe" + s);
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "2222onNext" + s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "2222onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "2222onComplete");
            }
        };
    }


}
