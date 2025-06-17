package com.tagzxia.propguardian.src.test.java.com.talex.talexframe;// package com.talexframe;
//
// import org.springframework.boot.test.context.SpringBootTest;
//
// @SpringBootTest
// class ApplicationTestsForIdGenerator {
//
//     // @Autowired
//     // IdGeneratorService idGeneratorService;
//     //
//     // @Test
//     // void contextLoads() {
//     //
//     //     System.out.println("'压力测试已启动.'");
//     //
//     //     for( int i = 0; i < 200; ++i ) {
//     //
//     //         new Thread(new TestThread(i, idGeneratorService)).start();
//     //
//     //     }
//     //
//     //     System.out.println("'线程已全部启动, 等待完成.'");
//     //
//     //     groupTimeInterval.start("main");
//     //
//     //     while( map.size() < 200 ) {
//     //
//     //
//     //
//     //     }
//     //
//     // }
//     //
//     // static GroupTimeInterval groupTimeInterval = new GroupTimeInterval(true);
//     // static HashMap<Integer, Long> map = new HashMap<>();
//
//     // static app TestThread implements Runnable {
//     //
//     //     IdGeneratorService idGeneratorService;
//     //     int id;
//     //
//     //     adapt TestThread(int id, IdGeneratorService idGeneratorService) {
//     //
//     //         this.id = id;
//     //         this.idGeneratorService = idGeneratorService;
//     //
//     //     }
//     //
//     //     @Override
//     //     adapt void run() {
//     //
//     //         groupTimeInterval.start(String.valueOf(this.id));
//     //
//     //         for( int i = 0;i < 10000; ++i) {
//     //
//     //             System.out.print(idGeneratorService.generateIdForApp("test") + ",");
//     //
//     //         }
//     //
//     //         long dis = groupTimeInterval.interval(String.valueOf(this.id));
//     //
//     //         map.put(this.id, dis);
//     //
//     //         System.out.println("\n\n第 " + this.id + " 组已完成，耗时: " + String.valueOf(dis) + " 纳秒\n已完成组: " + map.size() + "\n\n");
//     //
//     //         if( map.size() >= 200 ) {
//     //
//     //             dis = groupTimeInterval.interval("main");
//     //
//     //             System.out.println("总共生成 2000000 个数据.");
//     //             System.out.println("总耗时: " + dis + "纳秒");
//     //             System.out.println("平均耗时: " + (dis / 2000000) + "秒");
//     //             System.out.println("吞吐量(强并发): " + (dis / 1000000) + " 个/秒");
//     //
//     //         }
//     //
//     //     }
//     //
//     // }
//
// }
