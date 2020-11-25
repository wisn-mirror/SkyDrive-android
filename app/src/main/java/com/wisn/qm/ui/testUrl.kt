package com.wisn.qm.ui

import java.util.*

/**
 *
 * @Description:
 * @Author: Wisn
 * @CreateDate: 2020/11/21 下午5:25
 */
object testUrl {
    //预告片
    const val video1 = "http://vfx.mtime.cn/Video/2019/03/14/mp4/190314102306987969.mp4"
    const val video2 = "http://vfx.mtime.cn/Video/2019/03/12/mp4/190312143927981075.mp4"
    const val video3 = "http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4"
    const val video4 = "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4"
//    "sourceUrl": "https://static4.laiyifen.com/files/sns/image/97c81391d1e84509a9d566817ba8a1b9.mp4",
//    "imageSourceUrl": "https://static1.laiyifen.com/files/sns/image/f17020a0c3614316bf62c04e98dc3371.jpg?w=360&h=480&position=1",


    fun getVideoList(): List<VideoBean> {
        val videoList: MutableList<VideoBean> = ArrayList<VideoBean>()

        videoList.add(VideoBean("111",
                "https://static1.laiyifen.com/files/sns/image/597965bb421545438789a35e584f0153.jpg?w=480&h=276&position=1",
                "https://static5.laiyifen.com/files/sns/image/080efd38a0d54aa8aa00e8b5b18a9922.mp4"))

        videoList.add(VideoBean("222",
                "https://static4.laiyifen.com/files/sns/image/c445e1af94934f43b4b10146fc49013d.jpg?w=320&h=568&position=1",
                "https://static1.laiyifen.com/files/sns/image/137ee373bcf94ea6b67b9733ce23728a.mp4"))

        videoList.add(VideoBean("333",
                "https://static1.laiyifen.com/files/sns/image/f17020a0c3614316bf62c04e98dc3371.jpg?w=360&h=480&position=1",
                "https://static4.laiyifen.com/files/sns/image/97c81391d1e84509a9d566817ba8a1b9.mp4"))


        videoList.add(VideoBean("444",
                "https://static1.laiyifen.com/files/sns/image/fcf64d79d64248ca8888f11f67999586.jpg?w=480&h=276&position=1",
                "https://static1.laiyifen.com/files/sns/image/fd994f59c503448380210039f20fb11f.mp4"))




        videoList.add(VideoBean("555",
                "https://static1.laiyifen.com/files/sns/image/3f0f3a89b5c04e39bb727dd5fe4c8986.jpg?w=360&h=480&position=1",
                "https://static5.laiyifen.com/files/sns/image/cd77ed53e47142acb6237306d362c80a.mp4"))


        videoList.add(VideoBean("预告片1",
                "https://cms-bucket.nosdn.127.net/eb411c2810f04ffa8aaafc42052b233820180418095416.jpeg",
                "http://vfx.mtime.cn/Video/2019/02/04/mp4/190204084208765161.mp4"))
        videoList.add(VideoBean("预告片2",
                "https://cms-bucket.nosdn.127.net/cb37178af1584c1588f4a01e5ecf323120180418133127.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/21/mp4/190321153853126488.mp4"))
        videoList.add(VideoBean("预告片3",
                "https://cms-bucket.nosdn.127.net/eb411c2810f04ffa8aaafc42052b233820180418095416.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319222227698228.mp4"))
        videoList.add(VideoBean("预告片4",
                "https://cms-bucket.nosdn.127.net/cb37178af1584c1588f4a01e5ecf323120180418133127.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319212559089721.mp4"))
        videoList.add(VideoBean("预告片5",
                "https://cms-bucket.nosdn.127.net/eb411c2810f04ffa8aaafc42052b233820180418095416.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318231014076505.mp4"))
        videoList.add(VideoBean("预告片6",
                "https://cms-bucket.nosdn.127.net/cb37178af1584c1588f4a01e5ecf323120180418133127.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/18/mp4/190318214226685784.mp4"))
        videoList.add(VideoBean("预告片7",
                "https://cms-bucket.nosdn.127.net/eb411c2810f04ffa8aaafc42052b233820180418095416.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319104618910544.mp4"))
        videoList.add(VideoBean("预告片8",
                "https://cms-bucket.nosdn.127.net/cb37178af1584c1588f4a01e5ecf323120180418133127.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/19/mp4/190319125415785691.mp4"))
        videoList.add(VideoBean("预告片9",
                "https://cms-bucket.nosdn.127.net/eb411c2810f04ffa8aaafc42052b233820180418095416.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/17/mp4/190317150237409904.mp4"))
        videoList.add(VideoBean("预告片10",
                "https://cms-bucket.nosdn.127.net/cb37178af1584c1588f4a01e5ecf323120180418133127.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4"))
        videoList.add(VideoBean("预告片11",
                "https://cms-bucket.nosdn.127.net/eb411c2810f04ffa8aaafc42052b233820180418095416.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/14/mp4/190314102306987969.mp4"))
        videoList.add(VideoBean("预告片12",
                "https://cms-bucket.nosdn.127.net/cb37178af1584c1588f4a01e5ecf323120180418133127.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/13/mp4/190313094901111138.mp4"))
        videoList.add(VideoBean("预告片13",
                "https://cms-bucket.nosdn.127.net/eb411c2810f04ffa8aaafc42052b233820180418095416.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/12/mp4/190312143927981075.mp4"))
        videoList.add(VideoBean("预告片14",
                "https://cms-bucket.nosdn.127.net/cb37178af1584c1588f4a01e5ecf323120180418133127.jpeg",
                "http://vfx.mtime.cn/Video/2019/03/12/mp4/190312083533415853.mp4"))
        return videoList
    }
}

class VideoBean(var title: String, var thumb: String, var videoUrl: String) {

}
