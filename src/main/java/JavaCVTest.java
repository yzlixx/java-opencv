import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.junit.Test;

import static org.bytedeco.opencv.global.opencv_core.addWeighted;
import static org.bytedeco.opencv.global.opencv_highgui.imshow;
import static org.bytedeco.opencv.global.opencv_highgui.waitKey;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;

/**
 * @author lixiaoxuan
 * @description: TODO
 * @date 2021/6/24 15:37
 */
public class JavaCVTest {

    @Test
    public void testMerge() {
        Mat anima = imread("C:\\Users\\lixiaoxuan\\Desktop\\test\\1.jpg");  //获取原图
        Mat back = imread("C:\\Users\\lixiaoxuan\\Desktop\\test\\2.jpg");
        Mat mix = new Mat();
        Rect area = new Rect(0, 0, 1020, 500);
        Mat img1 = new Mat(anima, area);
//        imshow("image1",img1);
        Mat img2 = new Mat(back, area);
//        imshow("image2",img2);
        double alpha = 0.75;  //设置的人物原图的权重（透明度）
        double beta;
        beta = 1.0 - alpha;  //用来叠加的背景的权重
        addWeighted(img1, alpha, img2, beta, 0, mix);
        imshow("test", mix);
        waitKey();
    }



}
