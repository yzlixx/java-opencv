import org.bytedeco.ffmpeg.avcodec.AVPacket;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.indexer.UByteBufferIndexer;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.opencv.opencv_core.CvMat;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_highgui.CvTrackbarCallback;
import org.junit.Test;

import java.net.URL;
import java.nio.IntBuffer;

import static org.bytedeco.opencv.global.opencv_highgui.imshow;
import static org.bytedeco.opencv.global.opencv_highgui.namedWindow;
import static org.bytedeco.opencv.global.opencv_highgui.waitKey;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;



/**
 * @author lixiaoxuan
 * @description: TODO
 * @date 2021/6/24 17:32
 */
public class JavaCVTest2 {


    Mat g_srcImage;
    Mat g_dstImage;
    //对比度值
    int g_nContrastValue;
    //亮度值
    int g_nBrightValue;

    @Test
    public void trackBarTest() {
        g_srcImage = imread("C:\\Users\\lixiaoxuan\\Desktop\\test\\2.jpg");
        g_dstImage = new Mat(g_srcImage.size(), g_srcImage.type());
        //设定对比度和亮度的初值
        g_nContrastValue = 80;
        g_nBrightValue = 80;

        MyTrackbarCallBack myTrackbarCallBack = new MyTrackbarCallBack();
        namedWindow("【效果图窗口】", 0);
//        cvCreateTrackbar("对比度", "【效果图窗口】", myTrackbarCallBack.getIntBuffer(), 100, myTrackbarCallBack);
//        cvCreateTrackbar("亮度", "【效果图窗口】", myTrackbarCallBack.getIntBuffer(), 100, myTrackbarCallBack);
        imshow("【效果图窗口】", g_srcImage);
        waitKey();
    }


    public class MyTrackbarCallBack extends CvTrackbarCallback {
        private IntBuffer intBuffer = IntBuffer.allocate(30);

        @Override
        public void call(int position) {
//            System.out.println(position);
            //三个for循环，执行运算 g_dstImage(i,j) =a*g_srcImage(i,j) + b
            float alpha = 1.3f; //调整对比度参数
            float bate = 40f; //调整亮度参数
            int channels = g_srcImage.channels();//获取图像通道数
            double[] pixel = new double[3];
            for (int i = 0, rlen = g_srcImage.rows(); i < rlen; i++) {
                for (int j = 0, clen = g_srcImage.cols(); j < clen; j++) {
                    if (channels == 3) {//1 图片为3通道即平常的(R,G,B)
                        pixel[0] = getMatElement(g_srcImage,i,j,0);
                        pixel[1] = getMatElement(g_srcImage,i,j,1);
                        pixel[2] = getMatElement(g_srcImage,i,j,2);
                        pixel[0] = pixel[0] * alpha + bate;//R
                        pixel[1] = pixel[1] * alpha + bate;//G
                        pixel[2] = pixel[2] * alpha + bate;//B
                    }
                }
            }
            imshow("【原始图窗口】", g_srcImage);
            imshow("【效果图窗口】", g_dstImage);
        }

        public int getMatElement(Mat img,int row,int col,int channel){
            //获取字节指针
            BytePointer bytePointer = img.ptr(row, col);
            int value = bytePointer.get(channel);
            if(value<0){
                value=value+256;
            }
            return value;
        }



        public IntBuffer getIntBuffer() {
            return intBuffer;
        }

        public void setIntBuffer(IntBuffer intBuffer) {
            this.intBuffer = intBuffer;
        }
    }



    @Test
    public void testStream() throws Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("C:\\Users\\lixiaoxuan\\Desktop\\video\\test1.mp4");
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("C:\\Users\\lixiaoxuan\\Desktop\\video\\test2.mp4",2);
        grabber.start();
        recorder.start(grabber.getFormatContext());
        for (;;){
            AVPacket pkt = grabber.grabPacket();
            recorder.recordPacket(pkt);
        }
    }

    //视频转码
    @Test
    public void testStream2() throws Exception {
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("C:\\Users\\lixiaoxuan\\Desktop\\video\\test1.mp4");
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder("C:\\Users\\lixiaoxuan\\Desktop\\video\\test3.flv",1024,980,2);
        grabber.start();
        recorder.start();
        recorder.setFormat("flv");
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        for(;;){
            Frame frame = grabber.grab();
            recorder.record(frame);
        }
    }

    //截屏、录屏
    @Test
    public void testScreen() throws Exception {
        //读取windows屏幕，eguid原创
        FFmpegFrameGrabber grabber = new FFmpegFrameGrabber("desktop");
        grabber.setFormat("gdigrab");// 基于windows的gdigrab的输入格式
        grabber.start();
        for(;;){
            //该操作完成了采集屏幕图像的操作，得到的是像素图像
            Frame frame=grabber.grab();
        }
    }
}
