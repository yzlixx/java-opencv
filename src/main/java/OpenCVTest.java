import org.junit.Test;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.opencv.highgui.HighGui.imshow;
import static org.opencv.highgui.HighGui.waitKey;
import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;

public class OpenCVTest {
    /**
     * 测试OpenCV是否能运行：需要自行修改图片位置
     *
     * @throws Exception 测试是否成功
     */
    @Test
    public void testOpenCV() throws Exception {
        URL url = ClassLoader.getSystemResource("lib/opencv_java452.dll");
        System.load(url.getPath());
        //填你的图片地址
        Mat image = imread("C:\\Users\\lixiaoxuan\\Desktop\\test\\1.png", 1);
        if (image.empty()) {
            throw new Exception("image is empty!");
        }
        imshow("Original Image", image);


        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // 高斯滤波，降噪
        Imgproc.GaussianBlur(gray, gray, new Size(3, 3), 2, 2);

        // Canny边缘检测
        Imgproc.Canny(gray, gray, 50, 80, 3, false);

        // 膨胀，连接边缘
        Imgproc.dilate(gray, gray, new Mat(), new Point(-1, -1), 3, 1, new Scalar(1));
        imshow("grey Image", gray);
        imwrite("C:\\Users\\lixiaoxuan\\Desktop\\test\\greyImage.png", gray);
        Mat submat = image.submat(10, 100, 20, 90);
        Mat merge = new Mat();
        submat.copyTo(image);
        imshow("merge", image);
        waitKey();
    }


    @Test
    public void testMerge() {

        URL url = ClassLoader.getSystemResource("lib/opencv_java452.dll");
        System.load(url.getPath());
        Mat anima = Imgcodecs.imread("C:\\Users\\lixiaoxuan\\Desktop\\test\\1.jpg", Imgcodecs.IMREAD_UNCHANGED);  //获取原图
        Mat back = Imgcodecs.imread("C:\\Users\\lixiaoxuan\\Desktop\\test\\2.jpg", Imgcodecs.IMREAD_UNCHANGED);
        Mat mix = new Mat();
        Rect area = new Rect(0, 0, 1020, 500);
        Mat img1 = new Mat(anima, area);
//        imshow("image1",img1);
        Mat img2 = new Mat(back, area);
//        imshow("image2",img2);
        double alpha = 0.75;  //设置的人物原图的权重（透明度）
        double beta;
        beta = 1.0 - alpha;  //用来叠加的背景的权重

        Core.addWeighted(img1, alpha, img2, beta, 0, mix);
        imshow("merge image", mix);
        waitKey();
    }

    @Test
    public void splitTest() {

        URL url = ClassLoader.getSystemResource("lib/opencv_java452.dll");
        System.load(url.getPath());
        Mat anima = Imgcodecs.imread("C:\\Users\\lixiaoxuan\\Desktop\\test\\2.jpg", Imgcodecs.IMREAD_UNCHANGED);  //获取原图
        List<Mat> mv = new ArrayList<Mat>();
        Core.split(anima, mv);
        for(int i=0;i<mv.size();i++){
//            imshow(i+"jpg",mv.get(i));
            imwrite("C:\\Users\\lixiaoxuan\\Desktop\\test\\split"+i+".jpg", mv.get(i));
        }
//        waitKey();
    }

    @Test
    public void mergeTest() {

        URL url = ClassLoader.getSystemResource("lib/opencv_java452.dll");
        System.load(url.getPath());
        Mat dest =new Mat();
        List<Mat> mv = new ArrayList<Mat>();// 加载split()方法分离出来的彩色通道数据
        Mat s1 = Imgcodecs.imread("C:\\Users\\lixiaoxuan\\Desktop\\test\\split0.jpg", Imgcodecs.IMREAD_UNCHANGED);  //获取原图
        Mat s2 = Imgcodecs.imread("C:\\Users\\lixiaoxuan\\Desktop\\test\\split1.jpg", Imgcodecs.IMREAD_UNCHANGED);  //获取原图
        Mat s3 = Imgcodecs.imread("C:\\Users\\lixiaoxuan\\Desktop\\test\\split2.jpg", Imgcodecs.IMREAD_UNCHANGED);  //获取原图
        mv.add(s1);
        mv.add(s2);
        mv.add(s3);
        Core.merge(mv, dest);// 合并通道
        imshow("merge",dest);
        waitKey();
    }

    @Test
    public void trackBarTest() {
      imread("C:\\Users\\lixiaoxuan\\Desktop\\test\\1.jpg");
    }
}
